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
 * Node.h
 *
 *  Created on: 06/ago/2014
 *      Author: Alessandro Daniele
 */

#ifndef NODE_H_
#define NODE_H_

#include <iostream>

using namespace std;

class Node {
public:
	Node(string n, Node* l, Node* r, Node* p, double d);
	bool leaf();

	string getName();
	Node* getLeft();
	Node* getRight();
	Node* getParent();
	Node* getBrother();
	double getDistance();
	int getDescendentLeaves();

	void setName(string n);
	void setLeft(Node* l);
	void setRight(Node* r);
	void setParent(Node* p);
	void setDistance(double d);
	/* It returns the best candidate for the role of root.
	 * The best root is the one that minimize the difference between left and right means.
	 * Left[Right] mean is the mean of distances from left[right] leaves to the root.
	 * Left leaves are the leaves in the left sub tree, and right leaves the others.
	 *
	 * Variable totalNumberOfLeafs is the total amount of leaves in all the tree and
	 * parentLeftSum is the sum of distances between parent's left leaves and parent himself.*/
	Node* calculatePositionOfRoot(int totalNumberOfLeafs, double parentLeftSum=0);
	/* It generates a new node and changes the tree in order to let him become the new root.
	 * It has to be invoked on the node returned by valculatePositionOfRoot. */
	Node* addRoot();

	void printNode();

	virtual ~Node();
protected:
	/* Right now, newParent is a son of the current node. The method invert the current parent with newParent
	 * and do a recursive call.*/
	void invertNode(Node* newParent, double newDistance);
	/* Calculate the number of descendant leaves of the current node and all the descendant nodes.
	 * Called on the root of a tree, it updates the internal variable descendantLeaves on all the nodes of the tree.*/
	int calculateDescendantLeaves();

	string name;
	Node* left;
	Node* right;
	Node* parent;

	double distance;
	int descendantLeaves;
	//It represent the sum of distances between this node and its descendant leaves
	double distancesSum;
	double difference;
};

#endif /* NODE_H_ */
