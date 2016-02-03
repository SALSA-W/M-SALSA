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
 * Alphabet.h
 *
 *  Created on: 08/ago/2014
 *      Author: Alessandro Daniele
 */

#ifndef ALPHABET_H_
#define ALPHABET_H_

#include <iostream>
#include <cstring>

using namespace std;

class Alphabet{
public:
	Alphabet(string s, int n);
	//Type could be: PROTEINS, DNA or RNA
	explicit Alphabet(string type);

	/* Next two are used for efficiency reasons: before starting the local search,
	 * each cell of the alignment are converted in an integer. Then, during the local search,
	 * the score is calculated only by accessing the substitution matrix. The converted characters
	 * are indeed the indexes of the substitution matrix. Without this conversion it would be
	 * necessary to calculate every time the score's position in the substitution matrix.*/
	//Conversion of character in an integer
	int charToInt(char c);
	//Conversion of an integer in a character (to be used after the end of local search)
	char intToChar(int i);

	//Dimension of the alphabet (number of characters)
	int dimension();
	//The representation of the INDEL in the integer form (see above)
	int INDEL();

	virtual ~Alphabet();
protected:
	char* calculateAlphabetArray(string s, int n);
	int numberOfCharacters;
	char* alphabet;
};

#endif /* ALPHABET_H_ */
