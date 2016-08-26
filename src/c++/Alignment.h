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
 * Alignment.h
 *
 *  Created on: 08/ago/2014
 *      Author: Alessandro Daniele
 */
#ifndef ALIGNMENT_H_
#define ALIGNMENT_H_

#include <vector>
#include "DistanceMatrix.h"
#include "Tree.h"
#include "SubstitutionMatrix.h"
#include "GAP.h"

using namespace std;


/* A terminal GAP is a GAP in the beginning or in the end of a sequence. Usually, the penalties for a
 * terminal GAP is different from the others. Here there are two possibilities:
 * BOTH_PENALTIES: as other GAPs, a terminal GAP has both GOP and GEP
 * ONLY_GEP: there is no penalty for opening a terminal GAP, only for extending it*/
enum terminalGAPsStrategy {BOTH_PENALTIES, ONLY_GEP};

class Alignment{
public:
	//To be used when it is already available a substitution matrix
	Alignment(const char* fileName, const char* treeFileName, SubstitutionMatrix* s, double g, terminalGAPsStrategy tgs=ONLY_GEP);
	//To be used when the substitution matrix has to be decided according with the alignment's average percentage of identity (only with proteins)
	Alignment(const char* fileName, const char* treeFileName, const char* matrixSerie, double gep, double gop, terminalGAPsStrategy tgs = ONLY_GEP);

	int getNumberOfSequences();
	int getLength();
	vector<GAP*>* getGAPS();
	//Calculate the WSP-score (in the classic way, without using the counters)
	double WSP();

	terminalGAPsStrategy getTerminalGAPStrategy();

	//They move a GAP of one position and return the improvement in the WSP-Score due to this change
	double moveLeft(GAP* g);
	double moveRight(GAP* g);
	/* Same as moveLeft and moveRight, but the improvement is not returned (not even calculated).
	 * Used to do the backtracking and restore the cell as before the movement of the GAP g.*/
	void goBackToLeft(GAP* g);
	void goBackToRight(GAP* g);

	//It returns the penalty of adding a GOP in the specified row (it depends on the weight of the row).
	double getGOP(int row);

	void save(const char* fileName);
	virtual ~Alignment();
protected:
	//It calculates the mean of the identity scores of all couples of sequences in the alignment
	double getAverageIdentityScore();
	//Given the file containing the guide tree, it generates the sequences' weights
	void createWeights(const char* fileName);
	//It reads sequences list (as a strings' vector) from a FASTA file
	vector<string>* read(const char* file_name);
	//Converts the character alignment in an integer alignment for efficiency reasons
	void preprocessing(vector<string>* seq);
	//Used by preprocessing
	int* convert(string s);
	void createCounters();
	//Returns the names of the sequences (extracted from the FASTA file removing the '>' character)
	string* getNames();

	//It calculates the identity score of two sequences (the percentage of identical residues found in the pairwise alignment)
	double getIdentityScore(int firstRow, int secondRow);
	//It calculate the distance between two sequences
	double getPairwiseDistance(int firstRow, int secondRow);
	//It returns the best substitution matrix for this alignment (based on average identity score)
	void createSubstitutionMatrix(const char* matrixSerie, double GEP);

	/* Next two are used only to improve the readability of the code.
	 * They give a simpler access to the alignment and counters matrices.*/
	int& align(int row, int column);
	double& counters(int row, int column);

	//Score of two sequences in the specified rows (used by WSP). It requires also the number of GAPS inside the rows
	double pairwise(int r1, int r2, int numberOfGAPSr1, int numberOfGAPSr2);

	/* It modify the character in position (row, column) and returns
	 * the improvement obtained by changing it.*/
	double changeCell(int row, int column, int newCharacter);
	//Same as changeCell, but here the improvement is not calculated
	void restoreCell(int row, int column, int newCharacter);

	int numberOfSequences;
	int length;
	//The alignment
	int* alignMatrix;
	SubstitutionMatrix* substitution;
	Alphabet* alphabet;
	//Sequences name and properties (found in FASTA files)
	vector<string> properties;
	double* weights;
	double weightsSUM;
	vector<GAP*>* GAPS;

	double* countersMatrix;

	//GAP opening penalty
	double GOP;

	terminalGAPsStrategy terminal;
};

#endif /* ALIGNMENT_H_ */
