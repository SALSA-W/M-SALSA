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
* SubstitutionMatrix.cpp
*
*  Created on: 08/ago/2014
*      Author: Alessandro Daniele
*/

#include "SubstitutionMatrix.h"
#include <string>

#ifndef DATA_DIR
	#define DATA_DIR ""
#endif

SubstitutionMatrix * SubstitutionMatrix::getSubstitutionMatrix(const char* matrixSerie, float pid, float GEP, Alphabet* a) {
	if (a == NULL) a = new Alphabet("PROTEINS");
	
	if (!strcmp(matrixSerie, "BLOSUM")) {
		if (pid > 0.8) return new SubstitutionMatrix((string(DATA_DIR) + "/BLOSUM80").c_str(), a, GEP);
		if (pid > 0.6) return new SubstitutionMatrix((string(DATA_DIR) + "/BLOSUM62").c_str(), a, GEP);
		if (pid > 0.3) return new SubstitutionMatrix((string(DATA_DIR) + "/BLOSUM45").c_str(), a, GEP);
		return new SubstitutionMatrix((string(DATA_DIR) + "/BLOSUM30").c_str(), a, GEP);
	}
	else if (!strcmp(matrixSerie, "PAM")) {
		if (pid > 0.8) return new SubstitutionMatrix((string(DATA_DIR) + "/PAM20").c_str(), a, GEP);
		if (pid > 0.6) return new SubstitutionMatrix((string(DATA_DIR) + "/PAM60").c_str(), a, GEP);
		if (pid > 0.4) return new SubstitutionMatrix((string(DATA_DIR) + "/PAM120").c_str(), a, GEP);
		return new SubstitutionMatrix((string(DATA_DIR) + "/PAM350").c_str(), a, GEP);
	}

	//MatrixSerie is not BLOSUM and it is not PAM
	return NULL;
}

SubstitutionMatrix::SubstitutionMatrix(const char* fileName, Alphabet* a, float g) : alphabet(a), GEP(g) {
	alphabetLength = alphabet->dimension();

	matrix = new int[alphabetLength*alphabetLength];

	ifstream file;
	file.open(fileName);
	if (!file.is_open()) throw("Error: file " + string(fileName) + " can't be open.");

	string line;
	getline(file, line);


	//The alphabet found in the substitution matrix file
	Alphabet* recognizedAlphabet = new Alphabet(line, alphabetLength);

	if (alphabetLength != recognizedAlphabet->dimension()) {
		//The two alphabets are different
		throw("Error: substitution matrix file format is not supported");
	}
	else { //Find out if the order of characters is the same in the two alphabets
		for (int i = 0; i < alphabetLength; i++) {
			if (alphabet->intToChar(i) != recognizedAlphabet->intToChar(i))
				throw("Error: substitution matrix file format is not supported");
		}
	}

	for (int i = 0; i<alphabetLength; i++) {
		for (int j = 0; j<alphabetLength; j++) {
			file >> matrix[i*alphabetLength + j];
		}
	}

	file.close();
}

float SubstitutionMatrix::score(int a, int b) {
	if (a == alphabet->INDEL()) {
		if (b == alphabet->INDEL()) return 0;
		else return -GEP;// *scalingFactor;
	}
	if (b == alphabet->INDEL()) return -GEP;// *scalingFactor;

	return matrix[a*alphabetLength + b];
}

Alphabet* SubstitutionMatrix::getAlphabet() {
	return alphabet;
}

SubstitutionMatrix::~SubstitutionMatrix() {
	delete[] matrix;
	delete alphabet;
}
