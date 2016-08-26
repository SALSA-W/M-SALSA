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
 * Tree.h
 *
 *  Created on: 06/ago/2014
 *      Author: Alessandro Daniele
 */

#ifndef TREE_H_
#define TREE_H_

#include "Node.h"
#include <fstream>

using namespace std;

class Tree {
public:
	//The parametrs are the name of the file containing the tree in the Newick notation and the number of sequences
	Tree(const char* fileName, int numberOfSequences);
	//Re-root the tree
	void changeRoot();

	/* Take as input the sequences' names and the weights array,
	 * it modifies the weights and returns the their sum.*/
	double generateWeights(string* names, double* weights);
	void printTree();

	virtual ~Tree();
protected:
	string readName();
	Node* createNode(Node* parent);
	double leafWeight(Node* leaf);

	Node* root;
	Node** leaves;
	int insertedSequences;
	ifstream file;
};

#endif /* TREE_H_ */
