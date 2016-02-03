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
 * GAP.cpp
 *
 *  Created on: 09/ago/2014
 *      Author: Alessandro Daniele
 */

#include "GAP.h"

GAP::GAP(int r, int b, int sl, GAP* p, GAP* n, int length): row(r), begin(b), sequencesLength(sl), previous(p), next(n) {
	end=b+length-1;

	if (end>sequencesLength-1) throw("Error: GAP exceed the end of the sequence");
}

void GAP::extend(){
	end++;

	if (end>sequencesLength-1) throw("Error: GAP exceed the end of the sequence");
}

void GAP::setNext(GAP* n){
	next=n;
}

bool GAP::terminalGAP(){
	return (begin==0 || end==sequencesLength-1);
}

bool GAP::nearPreviousGAP(){
	return (previous!=NULL && begin==(previous->end+1));
}

bool GAP::nearNextGAP(){
	return (next!=NULL && next->begin==(end+1));
}

bool GAP::nearAnotherGAP(){
	return nearPreviousGAP() || nearNextGAP();
}

int GAP::getBegin(){
	return begin;
}

int GAP::getRow(){
	return row;
}

int GAP::getLength(){
	return end-begin+1;
}

int GAP::getEnd(){
	return end;
}

void GAP::moveLeft(){
	begin--;
	end--;

	if (begin<0)
		throw("Error: border exceeded by a GAP.");
	if (previous!=NULL && begin<(previous->end+1))
		throw("Error: overlapping of two GAPs");
}

void GAP::moveRight(){
	begin++;
	end++;

	if (end>(sequencesLength-1))
		throw("Error: border exceeded by a GAP.");
	if (next!=NULL && end>(next->begin-1))
		throw("Error: overlapping of two GAPs");
}

void GAP::unify(){
	if (nearPreviousGAP()){
		previous->end=end;
		previous->next=next;
		if (next!=NULL) next->previous=previous;
	}
	else if (nearNextGAP()){
		next->begin=begin;
		next->previous=previous;
		if (previous!=NULL) previous->next=next;
	}
	else throw("Error while unify two GAPs: there is no GAP close to the current one.");
}

GAP* GAP::split(int column, bool leftNew){
	if (column<begin || column>=end) throw("Error while splitting a GAP: the specified point is not inside the GAP.");
	GAP* g;

	if (leftNew){
		g=new GAP(row,begin,sequencesLength,previous,this,column-begin+1);
		if (previous!=NULL) previous->next=g;
		previous=g;
		begin=column+1;
	}
	else{
		g=new GAP(row,column+1,sequencesLength,this,next,end-column);
		if (next!=NULL) next->previous=g;
		next=g;
		end=column;
	}

	return g;
}

GAP::~GAP() {
	//previous and next have not to be deleted, otherwise if there was a union more GAPs could be deleted
}

