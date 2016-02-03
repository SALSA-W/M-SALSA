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
 * Alignment.cpp
 *
 *  Created on: 08/ago/2014
 *      Author: Alessandro Daniele
 */

#include "Alignment.h"
#include <string>

Alignment::Alignment(const char* fileName, const char* treeFileName, SubstitutionMatrix* s, float g, terminalGAPsStrategy tgs): substitution(s), GOP(g), terminal(tgs) {
	alphabet=s->getAlphabet();
	vector<string>* seq=read(fileName);
	alignMatrix=new int[numberOfSequences*length];
	GAPS=new vector<GAP*>();

	createWeights(treeFileName);
	preprocessing(seq);
	createCounters();

	delete seq;
}

Alignment::Alignment(const char * fileName, const char * treeFileName, const char* matrixSerie, float gep, float gop, terminalGAPsStrategy tgs): GOP(gop), terminal(tgs) {
	alphabet = new Alphabet("PROTEINS");
	vector<string>* seq = read(fileName);
	alignMatrix = new int[numberOfSequences*length];
	GAPS = new vector<GAP*>();

	createWeights(treeFileName);
	preprocessing(seq);
	createCounters();

	createSubstitutionMatrix(matrixSerie, gep);
}

terminalGAPsStrategy Alignment::getTerminalGAPStrategy(){
	return terminal;
}

int* Alignment::convert(string s){
	int* sequenceOfNumbers=new int[length];

	for (int c=0; c<length; c++) sequenceOfNumbers[c]=alphabet->charToInt(s[c]);

	return sequenceOfNumbers;
}

void Alignment::preprocessing(vector<string>* seq){
	//If it is NULL, there are no GAPs opened
	GAP* g=NULL;
	GAP* previous;

	string currentSequence;
	int* convertedSequence;

	for (int r=0; r<numberOfSequences; r++){
		previous=NULL;
		currentSequence=(*seq)[r];
		convertedSequence=convert(currentSequence);

		for (int c=0; c<length; c++){
			align(r,c)=convertedSequence[c];
			if (convertedSequence[c]==alphabet->INDEL()){
				if (g==NULL){
					g=new GAP(r,c,length,previous,NULL);
					if (previous!=NULL) previous->setNext(g);
				}
				else g->extend();
			}
			else if (g!=NULL){ //A GAP has been found and it is finished
				GAPS->push_back(g);
				previous=g;
				g=NULL;
			}
		}
		if (g!=NULL){ //A GAP has been found and it is finished
			GAPS->push_back(g);
			g=NULL;
		}

		delete convertedSequence;
	}
}

void Alignment::createWeights(const char* fileName){
	//cout<<"Reading the guide tree inside "<<fileName<<" ..."<<endl;
	Tree* t=new Tree(fileName, numberOfSequences);
	//cout<<"Done!"<<endl;
	//t->printTree();
	t->changeRoot();
	//t->printTree();

	//cout<<endl<<endl;
	weights=new float[numberOfSequences];
	string* names=getNames();
	weightsSUM=t->generateWeights(names,weights);

	delete[] names;
	delete t;
}

void Alignment::createCounters(){
	countersMatrix=new float[(alphabet->dimension()+1)*length];

	for (int i=0; i<(alphabet->dimension()+1)*length; i++) countersMatrix[i]=0.0f;

	int character;
	for (int c=0; c<length; c++){
		for (int r=0; r<numberOfSequences; r++){
			character=align(r,c);
			counters(character,c)+=weights[r];
		}
	}
}

float Alignment::pairwise(int r1, int r2, int numberOfGAPSr1, int numberOfGAPSr2){
	float value=0;
	int alpha, beta;
	for (int c=0; c<length; c++){
		alpha=align(r1,c);
		beta=align(r2,c);
		value+=substitution->score(alpha,beta);
	}

	value-=GOP*(numberOfGAPSr1+numberOfGAPSr2);

	return value;
}

float Alignment::WSP(){
	float objval=0.0f;
	int* numberOfGAPS=new int[numberOfSequences];
	GAP* g;

	for (int row=0; row<numberOfSequences;row++) numberOfGAPS[row]=0;
	for (int i=0; i<GAPS->size(); i++){
		g=(*GAPS)[i];

		if (terminal==BOTH_PENALTIES || !g->terminalGAP()) numberOfGAPS[g->getRow()]++;
	}

	for (int r1=0; r1<numberOfSequences-1;r1++){
		for (int r2=r1+1; r2<numberOfSequences;r2++){
			objval+=weights[r1]*weights[r2]*pairwise(r1,r2,numberOfGAPS[r1],numberOfGAPS[r2]);
		}
	}

	delete[] numberOfGAPS;
	return objval;
}

float Alignment::getIdentityScore(int firstRow, int secondRow){
	if (firstRow < 0 || firstRow >= numberOfSequences || secondRow < 0 || secondRow >= numberOfSequences)
		throw("Error: the two rows are not inside the correct range");

	int count = 0;
	int lengthFirstRow = 0;
	int lengthSecondRow = 0;

	for (int i = 0; i < length; i++){
		if (align(firstRow, i) != alphabet->INDEL()) lengthFirstRow++;
		if (align(secondRow, i) != alphabet->INDEL()) lengthSecondRow++;

		if (align(firstRow, i) != alphabet->INDEL() && align(firstRow, i) == align(secondRow, i)) count++;
	}

	if (lengthFirstRow < lengthSecondRow) return count / ((float) lengthFirstRow);
	else return count / ((float) lengthSecondRow);
}

float Alignment::getPairwiseDistance(int firstRow, int secondRow){
	return (1.0 - getIdentityScore(firstRow, secondRow)) * 100;
}

void Alignment::createSubstitutionMatrix(const char * matrixSerie, float GEP){
	float pid = getAverageIdentityScore();

	substitution = SubstitutionMatrix::getSubstitutionMatrix(matrixSerie, pid, GEP, alphabet);
}

float Alignment::getAverageIdentityScore(){
	float sum = 0.0;

	for (int i = 0; i < numberOfSequences - 1; i++){
		for (int j = i + 1; j < numberOfSequences; j++){
			sum += getIdentityScore(i, j);
		}
	}

	return 2 * sum / ((float) numberOfSequences * (numberOfSequences - 1));
}

float Alignment::changeCell(int row, int column, int newCharacter){
	int oldCharacter=align(row, column);
	counters(oldCharacter,column)-=weights[row];

	float delta=0.0f;
	for (int alpha=0; alpha<=alphabet->dimension();alpha++){
		delta+=counters(alpha,column)*(substitution->score(newCharacter,alpha)-substitution->score(oldCharacter,alpha));
	}
	delta*=weights[row];

	counters(newCharacter,column)+=weights[row];
	align(row,column)=newCharacter;

	return delta;
}

void Alignment::restoreCell(int row, int column, int newCharacter){
	int oldCharacter=align(row, column);
	counters(oldCharacter,column)-=weights[row];

	counters(newCharacter,column)+=weights[row];
	align(row,column)=newCharacter;
}

float Alignment::moveLeft(GAP* g){
	int leftColumn=g->getBegin()-1;
	int rightColumn=g->getEnd();
	int row=g->getRow();

	float delta=changeCell(row,rightColumn,align(row,leftColumn));
	delta+=changeCell(row,leftColumn,alphabet->INDEL());

	g->moveLeft();
	return delta;
}

float Alignment::moveRight(GAP* g){
	int leftColumn=g->getBegin();
	int rightColumn=g->getEnd()+1;
	int row=g->getRow();

	float delta=changeCell(row,leftColumn,align(row,rightColumn));
	delta+=changeCell(row,rightColumn,alphabet->INDEL());

	g->moveRight();
	return delta;
}

void Alignment::goBackToLeft(GAP* g/*OLD: , float delta*/){
	int leftColumn=g->getBegin()-1;
	int rightColumn=g->getEnd();
	int row=g->getRow();

	restoreCell(row,rightColumn,align(row,leftColumn));
	restoreCell(row,leftColumn,alphabet->INDEL());

	g->moveLeft();
}

void Alignment::goBackToRight(GAP* g/*OLD: , float delta*/){
	int leftColumn=g->getBegin();
	int rightColumn=g->getEnd()+1;
	int row=g->getRow();

	restoreCell(row,leftColumn,align(row,rightColumn));
	restoreCell(row,rightColumn,alphabet->INDEL());

	g->moveRight();
}

int& Alignment::align(int row, int column){
	return alignMatrix[row*length+column];
}

float& Alignment::counters(int character, int column){
	return countersMatrix[character*length+column];
}

int Alignment::getNumberOfSequences(){
	return numberOfSequences;
}

int Alignment::getLength(){
	return length;
}

string* Alignment::getNames(){
	string* names=new string[numberOfSequences];
	string p;
	for (int i=0;i<numberOfSequences;i++){
		p=properties[i];
		names[i]=p.substr(1,p.length());
	}

	return names;
}

vector<GAP*>* Alignment::getGAPS(){
	return GAPS;
}

float Alignment::getGOP(int row){
	return GOP*weights[row]*(weightsSUM-weights[row]);
}

vector<string>* Alignment::read(const char* fileName){
	ifstream file;
	file.open(fileName);

	vector<string>* seq=new vector<string>();

	if(!file.is_open()) throw("Error: file "+string(fileName)+" can't be open.");

	//number of sequences already inserted
	numberOfSequences=0;

	string previous_property="";
	string line;
	string sequence="";
	while (getline(file,line)){
		if (line[0]=='>'){
			if (sequence!=""){
				seq->push_back(sequence);
				sequence="";
				properties.push_back(previous_property);
				numberOfSequences++;
			}
			previous_property=line;
		}
		else{
			sequence.append(line);
		}
	}

	if (sequence!=""){
		seq->push_back(sequence);
		properties.push_back(previous_property);
		numberOfSequences++;
	}

	file.close();
	length=(*seq)[0].length();

	return seq;
}

void Alignment::save(const char* fileName){
	//cout<<"Saving Alignment ..."<<endl;
	ofstream file;
	file.open(fileName);

	for (int r=0; r<numberOfSequences; r++){
		file<<properties[r]<<endl;
		for (int c=0; c<length; c++){
			file<<alphabet->intToChar(align(r,c));
		}
		file<<endl;
	}

	file.close();
	//cout<<"Alignment saved in file "<<fileName<<"!"<<endl;
}

Alignment::~Alignment(){
	delete substitution;
	//alphabet has already been deleted together with the substitution matrix
	delete[] alignMatrix;
	delete[] countersMatrix;
	delete[] weights;
	for (unsigned int i=0;i<GAPS->size();i++) delete (*GAPS)[i];
	delete GAPS;
}
