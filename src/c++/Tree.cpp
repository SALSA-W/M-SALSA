/**
* Copyright 2015 Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

/*
 * Tree.cpp
 *
 *  Created on: 06/ago/2014
 *      Author: Alessandro Daniele
 */

#include "Tree.h"

Tree::Tree(const char* fileName, int numberOfSequences){
	insertedSequences=0;
	file.open(fileName);

	if(!file.is_open()) throw("ERROR: file "+string(fileName)+" can't be open.");

	leaves=new Node*[numberOfSequences];
	root=createNode(NULL);

	file.close();
}

string Tree::readName(){
	char c;
	string s="";

	file>>c;
	while (c!=':'){
		s+=c;
		file>>c;
	}
	file.seekg(-1,ios::cur);
	return s;
}

Node* Tree::createNode(Node* parent){
	file.setf(ios::skipws);
	Node* current=new Node("",NULL,NULL,parent,0);
	char c;
	float distance;

	file>>c;
	if (c!='('){ //Leaf
		string s = "";
		s += c;

		file >> c;
		while (c != ':') {
			s += c;
			file >> c;
		}

		current->setName(s);

		if (c==':'){
			file>>distance;
			current->setDistance(distance);
		}
		else throw("Error: missing distance in a leaf.");

		//next character should be a ',' or a ')'

		leaves[insertedSequences]=current;
		insertedSequences++;
	}
	else{ //Internal node
		current->setLeft(createNode(current));

		file>>c;
		if (c==','){
			current->setRight(createNode(current));

			file>>c;
			if (c==','){ //It is the root and there are three sons
				if (parent!=NULL) throw("More than two sons for an internal node (not root).");

				Node* newNode=new Node("ARTIFICIAL",current,NULL,NULL,0);
				current->setParent(newNode);

				newNode->setRight(createNode(newNode));
				current=newNode;
				file>>c;
			}
		}
		file>>c;
		if (c==':'){
			file>>distance;
			current->setDistance(distance);
		}
		else file.seekg(-1,ios::cur);
	}
	return current;
}

void Tree::changeRoot(){
	//Find best root
	Node* bestNode=root->calculatePositionOfRoot(insertedSequences);

	if (bestNode!=NULL) root=bestNode->addRoot();
}

float Tree::leafWeight(Node* leaf){
	Node* current=leaf;
	float w=0.0;

	while (current!=NULL){
		w+=current->getDistance()/current->getDescendentLeaves();

		current=current->getParent();
	}

	return w;
}

float Tree::generateWeights(string* names, float* weights){
	float WSUM=0.0;
	int index;

	for (int i=0;i<insertedSequences;i++){
		//Calculate sequence's index in the alignment
		index=-1;
		for (int j=0; j<insertedSequences;j++)
			if (names[j]==(leaves[i]->getName())) index=j;

		if (index==-1) throw("Error: the alignment and the tree have different sequences.");

		//Calculate the weight of the i-th leaf
		weights[index]=leafWeight(leaves[i]);
		WSUM+=weights[index];
	}

	return WSUM;
}

void Tree::printTree(){
	root->printNode();
	cout<<endl<<endl;
}

Tree::~Tree(){
	delete root;
	delete[] leaves;
}

