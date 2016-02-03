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
* DistanceMatrix.cpp
*
*  Created on: 27/nov/2015
*      Author: Alessandro Daniele
*/

#include "DistanceMatrix.h"
#include "SubstitutionMatrix.h"
#include <fstream>

using namespace std;

DistanceMatrix::DistanceMatrix(const char * fileName) {
	ifstream file;
	file.open(fileName);

	if (!file.is_open()) throw("ERROR: file " + string(fileName) + " can't be open.");

	file.setf(ios::skipws);
	file >> numberOfSequences;
	distMatrix = new float[numberOfSequences*numberOfSequences];
	names = new string[numberOfSequences];
	char c;

	for (int i = 0; i < numberOfSequences; i++) {
		file.setf(ios::skipws);

		do {
			file >> c;
			names[i] += c;
		} while (file.peek() != ' ');

		file.setf(ios::skipws);

		for (int j = 0; j < numberOfSequences; j++) {
			file >> distMatrix[i*numberOfSequences + j];
		}
	}
}

float DistanceMatrix::similarity(string name1, string name2) {
	int index1, index2;

	for (int i = 0; i < numberOfSequences; i++) {
		if (names[i] == name1 || ">" + names[i] == name1) index1 = i;
		if (names[i] == name2 || ">" + names[i] == name2) index2 = i;
	}

	return 1.0 - distMatrix[index1*numberOfSequences + index2];
}

SubstitutionMatrix* DistanceMatrix::createSubstitutionMatrix(const char* matrixSerie, float GEP) {
	float pid = 0.0;

	for (int i = 0; i < numberOfSequences; i++) {
		for (int j = i + 1; j < numberOfSequences; j++) {
			pid += 1.0 - distMatrix[i*numberOfSequences + j];
		}
	}

	pid /= numberOfSequences * (numberOfSequences - 1) / 2;
	return SubstitutionMatrix::getSubstitutionMatrix(matrixSerie, pid, GEP);
}

DistanceMatrix::~DistanceMatrix() {
	delete[] distMatrix;
	delete[] names;
}
