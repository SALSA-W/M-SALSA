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
 * LocalSearch.cpp
 *
 *  Created on: 12/ago/2014
 *      Author: Alessandro Daniele
 */

#include "LocalSearch.h"
#include <stdlib.h>
#include <time.h>

LocalSearch::LocalSearch(Alignment* a, int g, int mi, double pos): align(a), gamma(g), minIterations(mi), probabiltyOfSplit(pos){
	GAPS=a->getGAPS();
	numberOfGAPS=GAPS->size();
	terminal=align->getTerminalGAPStrategy();
}

bool LocalSearch::move(int GAPPosition, bool left, bool split){
	GAP* g=(*GAPS)[GAPPosition];
	//g is terminal and it has to move in the wrong direction (that is, outside the sequence)
	if ((left && (g->getBegin()==0)) || (!left && (g->getEnd()==align->getLength()-1))) return false;

	//Best delta found until now
	double deltaMax;
	//It indicates the iteration corresponding to the best delta found
	int bestIterator=0;
	//Penalty of a GOP in the corresponding row
	double deltaGOP=align->getGOP(g->getRow());

	if (split) deltaMax=deltaGOP;
	else{
		if (terminal==ONLY_GEP && g->terminalGAP()) deltaMax=deltaGOP;
		else deltaMax=0.0f;
	}

	//Improvement of the last move
	double delta;
	//Total improvement in current iteration
	double deltaSum=0.0f;

	bool finished=false;
	bool improvement=false;
	//Number of movement done
	int iterator=0;

	/* INVARIANT:
	 * If finished is false, it is correct to move the GAP of one position in the given direction because:
	 * 1) if the GAP is terminal, finished is true or
	 *    the direction of movement is in the opposite way of the touching border
	 * 2) the GAP is not in contact with another GAP (in that case finished would have been set to true).
	 *    The only case in which it is possible if there were a split (the GAP will move in the opposite direction).
	 * Moreover, bestIterator correspond to the best position tried so far.
	 * */
	while (!finished && iterator<gamma){//If g is not in the border
		iterator++;

		if (left) delta=align->moveLeft(g);
		else delta=align->moveRight(g);

		deltaSum+=delta;
		if (g->nearAnotherGAP()){
			finished=true;

			//The GAP is attached to another one, so a penalty for the GAP opening should be removed
			if (deltaSum+deltaGOP-deltaMax > 0.00001){
				g->unify();

				//Delete g and remove it form GAPS vector
				delete g;
				g=NULL;
				GAPS->erase(GAPS->begin()+GAPPosition);
				numberOfGAPS--;

				deltaMax=deltaSum;
				bestIterator=iterator;
				improvement=true;
			}
		}
		else{ //It is not near another GAP
			if (g->terminalGAP()){
				if (terminal==ONLY_GEP)	deltaSum+=deltaGOP;
				finished=true;
			}

			if (deltaSum-deltaMax > 0.00001){
				deltaMax=deltaSum;
				bestIterator=iterator;
				improvement=true;
			}
		}
	}

	//Here iterator is the number of movement done, bestIterator the right amount of movement
	if (left)
		for (; iterator>bestIterator; iterator--){
			align->goBackToRight(g);
		}
	else //Right
		for (; iterator>bestIterator; iterator--){
			align->goBackToLeft(g);
		}

	return improvement;
}

bool LocalSearch::splitAndMove(int GAPPosition, bool left){
	GAP* g=(*GAPS)[GAPPosition];
	int length=g->getLength();

	if (length>1){
		int positionOfSplit=g->getBegin()+rand()%(length-1);

		//newGAP will not be moved
		GAP* newGAP=g->split(positionOfSplit, !left);
		bool improvement=move(GAPPosition, left, true);
		if (improvement){
			GAPS->push_back(newGAP);
			numberOfGAPS++;
		}
		else{
			newGAP->unify();
			delete newGAP;
		}
		
		return improvement;
	}
	else{ //The GAP's length is one, therefore is not possible to split it
		return move(GAPPosition, left);
	}
}

int LocalSearch::getNumberOfGAPs(){
	return numberOfGAPS;
}

Alignment* LocalSearch::execute(){
	//Initialize seed
	srand(time(NULL));

	int lastImprovement=0;
	int iteration=0;
	int positionOfGAP;
	bool left;
	double split;

	while(lastImprovement+minIterations>iteration){
		positionOfGAP=rand()%numberOfGAPS;
		left=rand()%2;
		split=((double)rand())/RAND_MAX; //split is between 0 and one

		if (split<probabiltyOfSplit){
			if (splitAndMove(positionOfGAP, left)) lastImprovement=iteration;
		}
		else if (move(positionOfGAP, left)) lastImprovement=iteration;

		iteration++;
	}
	cout<<"Total number of iterations: "<<iteration<<endl;

	return align;
}

LocalSearch::~LocalSearch() {
	//GAPS and align should not be deleted: they exist outside the LocalSearch class
}

