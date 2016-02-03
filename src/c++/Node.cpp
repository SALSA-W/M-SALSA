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
 * Node.cpp
 *
 *  Created on: 06/ago/2014
 *      Author: Alessandro Daniele
 */

#include "Node.h"
#include <math.h>
#include <string>

Node::Node(string n, Node* l, Node* r, Node* p, float d):name(n),left(l),parent(p),distance(d){
	//In this way, descendentLeafs is correct
	setRight(r);
}

bool Node::leaf(){
	return (left==NULL && right==NULL);
}

string Node::getName(){
	return name;
}

Node* Node::getLeft(){
	return left;
}

Node* Node::getRight(){
	return right;
}

Node* Node::getParent(){
	return parent;
}

Node* Node::getBrother(){
	if (parent!=NULL){
		if (parent->left==this) return parent->right;
		else if (parent->right==this) return parent->left;
		else throw("Error: two node brothers have a different parent.");
	}
	return NULL;
}

float Node::getDistance(){
	return distance;
}

int Node::getDescendentLeaves(){
	return descendantLeaves;
}

void Node::setName(string n){
	name=n;
}

void Node::setLeft(Node* l){
	left=l;

	descendantLeaves=0;
	distancesSum=0;
	if (left!=NULL){
		descendantLeaves=left->descendantLeaves;
		distancesSum=left->distancesSum+left->distance*left->descendantLeaves;
	}
	if (right!=NULL){
		descendantLeaves+=right->descendantLeaves;
		distancesSum+=right->distancesSum+right->distance*right->descendantLeaves;
	}

	if (descendantLeaves==0) descendantLeaves=1; //The node is a leaf itself
}

void Node::setRight(Node* r){
	right=r;

	descendantLeaves=0;
	distancesSum=0;
	if (right!=NULL){
		descendantLeaves=right->descendantLeaves;
		distancesSum=right->distancesSum+right->distance*right->descendantLeaves;
	}
	if (left!=NULL){
		descendantLeaves+=left->descendantLeaves;
		distancesSum+=left->distancesSum+left->distance*left->descendantLeaves;
	}

	if (descendantLeaves==0) descendantLeaves=1; //The node is a leaf itself
}

void Node::setParent(Node* p){
	parent=p;
}

void Node::setDistance(float d){
	distance=d;
}

int Node::calculateDescendantLeaves(){
	descendantLeaves=0;
	if (left!=NULL) descendantLeaves=left->calculateDescendantLeaves();
	if (right!=NULL) descendantLeaves+=right->calculateDescendantLeaves();

	if (descendantLeaves==0) descendantLeaves=1; //The node is a leaf itself
	return descendantLeaves;
}

Node* Node::calculatePositionOfRoot(int totalNumberOfLeafs, float parentLeftSum){
	Node* brother=getBrother();
	int numberOfLeftLeaves=totalNumberOfLeafs-descendantLeaves;

	//Distances of brother's descendant leaves from parent
	float d=0;
	if (brother!=NULL) d=brother->distancesSum+brother->distance*brother->descendantLeaves;

	//Distances of all left leaves from parent
	d+=parentLeftSum;

	//Distances of all left leaves from the current node
	d+=distance*numberOfLeftLeaves;

	float leftMean=0.0f;
	if (numberOfLeftLeaves!=0) leftMean=d/numberOfLeftLeaves;

	if (left!=NULL){
		Node* rootLeft=left->calculatePositionOfRoot(totalNumberOfLeafs, d);
		if (rootLeft!=NULL) return rootLeft;
	}
	if (right!=NULL){
		Node* rootRight=right->calculatePositionOfRoot(totalNumberOfLeafs, d);
		if (rootRight!=NULL) return rootRight;
	}

	float currentDifference=leftMean-(distancesSum/descendantLeaves);
	if (currentDifference==0 || (currentDifference>0 && currentDifference<2*distance)){
		difference=currentDifference;
		return this;
	}

	return NULL;
}

void Node::invertNode(Node* newParent, float newDistance){
	Node* oldParent=parent;
	float oldDistance=distance;
	parent=newParent;
	distance=newDistance;

	if (left==newParent) left=oldParent;
	else if (right==newParent) right=oldParent;
	else throw("Error while trying to invert parent with son in the tree.");

	if (oldParent!=NULL) oldParent->invertNode(this, oldDistance);
}

Node* Node::addRoot(){
	if (parent!=NULL){
		float newDistance=difference/2;
		float newDistanceParent=distance-newDistance;

		Node* root=new Node("ROOT",parent, this, NULL, 0.0);
		if (parent->left==this) parent->left=root;
		else parent->right=root;
		parent=root;
		distance=newDistance;

		root->left->invertNode(root,newDistanceParent);
		root->calculateDescendantLeaves();

		return root;
	}
	else return this;
}

void Node::printNode(){
	if (leaf()) cout<<name<<":"<<distance;
	else{
		cout<<"I => "<<name<<" ("<<endl<<"  left: ";
		if (left!=NULL) left->printNode();
		else cout<<"NO LEFT";
		if (right!=NULL){
			cout<<','<<endl<<"  right: ";
			right->printNode();
		}
		else cout<<"NO RIGHT"<<endl;
		cout<<"):"<<distance;
	}
}

Node::~Node(){
	delete left;
	delete right;
	//Parent has not to be deleted because the destructor works in a recursive way.
}
