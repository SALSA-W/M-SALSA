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
 * LocalSearch.h
 *
 *  Created on: 12/ago/2014
 *      Author: Alessandro Daniele
 */

#ifndef LOCALSEARCH_H_
#define LOCALSEARCH_H_

#include "Alignment.h"
#include <vector>

using namespace std;

class LocalSearch {
public:
	LocalSearch(Alignment* a, int g, int mi, float pos);
	Alignment* execute();
	int getNumberOfGAPs();

	virtual ~LocalSearch();
protected:
	/* GAPPosition is the index of the GAP to be moved (inside GAPS vector). The method returns TRUE if there were an improvement.
	 * Variable left tells the method the direction of the movement. split tells if there were a split and this information
	 * is used only to choose properly the new position of the GAP. splitAndMove will manage all the rest.*/
	bool move(int GAPPosition, bool left, bool split=false);
	//Like move() method, but it split the GAP before moving it
	bool splitAndMove(int GAPPosition, bool left);

	Alignment* align;
	vector<GAP*>* GAPS;
	int numberOfGAPS;
	int gamma;
	int minIterations;
	float probabiltyOfSplit;

	terminalGAPsStrategy terminal;
};

#endif /* LOCALSEARCH_H_ */
