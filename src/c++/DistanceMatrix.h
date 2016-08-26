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
* DistanceMatrix.h
*
*  Created on: 27/nov/2015
*      Author: Alessandro Daniele
*/

#include <string>
#include "SubstitutionMatrix.h"

using namespace std;

#ifndef DISTANCE_MATRIX_H_
#define DISTANCE_MATRIX_H_

class DistanceMatrix {
public:
	explicit DistanceMatrix(const char* fileName);
	double similarity(string name1, string name2);

	//It calculates the average percentage of identity of the sequences and based on that it choose the correct substitution matrix
	SubstitutionMatrix* createSubstitutionMatrix(const char* type, double GEP);

	~DistanceMatrix();
private:
	int numberOfSequences;
	double* distMatrix;
	string* names;
};

#endif /* DISTANCE_MATRIX_H_ */
