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
#include "EmbeddedMatrices.h"
#include <string>

SubstitutionMatrix * SubstitutionMatrix::getSubstitutionMatrix(const char* matrixSerie, float pid, float GEP, Alphabet* a) {
	if (a == NULL) a = new Alphabet("PROTEINS");
	
	if (!strcmp(matrixSerie, "BLOSUM")) {
		if (pid > 0.8) return new SubstitutionMatrix("BLOSUM80", GEP);
		if (pid > 0.6) return new SubstitutionMatrix("BLOSUM62", GEP);
		if (pid > 0.3) return new SubstitutionMatrix("BLOSUM45", GEP);
		return new SubstitutionMatrix("BLOSUM30", GEP);
	}
	else if (!strcmp(matrixSerie, "PAM")) {
		if (pid > 0.8) return new SubstitutionMatrix("PAM20", GEP);
		if (pid > 0.6) return new SubstitutionMatrix("PAM60", GEP);
		if (pid > 0.4) return new SubstitutionMatrix("PAM120", GEP);
		return new SubstitutionMatrix("PAM350", GEP);
	}

	//MatrixSerie is not BLOSUM and it is not PAM
	return NULL;
}

SubstitutionMatrix::SubstitutionMatrix(const char* fileName, Alphabet* a, float g) : alphabet(a), GEP(g) {
	embeddedMatrix = false;
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

SubstitutionMatrix::SubstitutionMatrix(string matrixName, float g): GEP(g){
	embeddedMatrix = true;

	if (matrixName != "IUB") alphabet = new Alphabet("PROTEINS");
	else alphabet = new Alphabet("DNA");
	alphabetLength = alphabet->dimension();

	if (matrixName == "BLOSUM30") matrix = BLOSUM30;
	if (matrixName == "BLOSUM45") matrix = BLOSUM45;
	if (matrixName == "BLOSUM62") matrix = BLOSUM62;
	if (matrixName == "BLOSUM80") matrix = BLOSUM80;
	if (matrixName == "IUB") matrix = IUB;
	if (matrixName == "PAM20") matrix = PAM20;
	if (matrixName == "PAM60") matrix = PAM60;
	if (matrixName == "PAM120") matrix = PAM120;
	if (matrixName == "PAM350") matrix = PAM350;
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
	if (!embeddedMatrix) delete[] matrix; //If it is an embedded matrix it is not saved in the heap
	delete alphabet;
}
