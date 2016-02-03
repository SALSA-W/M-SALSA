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
 * GAP.h
 *
 *  Created on: 09/ago/2014
 *      Author: Alessandro Daniele
 */

#ifndef GAP_H_
#define GAP_H_

#include <cstddef>

using namespace std;

class GAP {
public:
	GAP(int r, int b, int sl, GAP* p, GAP* n, int length =1);
	//Increase the length of the GAP of one element (if it is possible, otherwise it throws an exception)
	void extend();
	int getRow();
	int getBegin();
	int getLength();
	int getEnd();

	//True if the GAP is terminal, that is it is in the beginning or in the end of a sequence
	bool terminalGAP();

	/* Next three methods are used to see if the current GAP is attached to another one.
	 * If that is true, it could be necessary to unify the two GAPS in order to maintain the
	 * consistency with the alignment (two connected GAPS are considered as one).*/
	bool nearPreviousGAP();
	bool nearNextGAP();
	bool nearAnotherGAP();

	//Next two methods move the GAP of one position.
	void moveLeft();
	void moveRight();

	void setNext(GAP* n);

	/* If there is a GAP g attached to this GAP, the method unifies them, otherwise it throws an exception.
	 *
	 * The unified gap is stored inside g. Therefore, the GAP g is modified, not the current one.
	 * Current GAP should be deleted by the caller of unify().*/
	void unify();
	/* The method divide the GAP in two using the indel in position column as separator.
	 * In particular, the first created GAP ends in position column, the other one begin
	 * in the next position. Therefore, column should be inside the GAP.
	 * The method modifies the current GAP and creates another one that is returned.
	 * Variable leftNew specifies if the new GAP is the one on the left.*/
	GAP* split(int column, bool leftNew);

	virtual ~GAP();
protected:
	int row;
	int begin;
	int end;
	int sequencesLength;

	GAP* previous;
	GAP* next;
};

#endif /* GAP_H_ */
