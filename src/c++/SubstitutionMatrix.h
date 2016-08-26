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
* SubstitutionMatrix.h
*
*  Created on: 08/ago/2014
*      Author: Alessandro Daniele
*/

#ifndef SUBSTITUTIONMATRIX_H_
#define SUBSTITUTIONMATRIX_H_

#include "Alphabet.h"
#include <fstream>

using namespace std;

class SubstitutionMatrix {
public:
	static SubstitutionMatrix* getSubstitutionMatrix(const char* matrixSerie, double pid, double GEP, Alphabet* a = NULL);

	SubstitutionMatrix(const char* file, Alphabet* a, double g);
	SubstitutionMatrix(string matrixName, double g);

	//Calculate the score between two characters (converted in integers)
	double score(int a, int b);
	Alphabet* getAlphabet();

	virtual ~SubstitutionMatrix();
private:
	bool embeddedMatrix;
	int alphabetLength;
	//GAP extension penalty
	double GEP;

	Alphabet* alphabet;
	int* matrix;
};

#endif /* SUBSTITUTIONMATRIX_H_ */
