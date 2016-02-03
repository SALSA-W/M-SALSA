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
 * Alphabet.cpp
 *
 *  Created on: 08/ago/2014
 *      Author: Alessandro Daniele
 */

#include "Alphabet.h"

Alphabet::Alphabet(string s, int n){
	alphabet = calculateAlphabetArray(s, n);
}

Alphabet::Alphabet(string type){
	if (type == "DNA"){
		alphabet = calculateAlphabetArray("ATCG", 4);
	}
	else if (type == "RNA"){
		alphabet = calculateAlphabetArray("AUCG", 4);
	}
	else if (type == "PROTEINS") {
		alphabet = calculateAlphabetArray("ARNDCQEGHILKMFPSTWYVBZX", 23);
	}
	else throw("Error: the specified type of alphabet is not supported. Supported types are: PROTEINS, DNA or RNA");
}

char* Alphabet::calculateAlphabetArray(string s, int n){
	if (n <= 0) throw("Error: number of characters in the alphabet should be greater than 0!");

	char* array = new char[n];

	numberOfCharacters=0;
	for (int i=0;i<s.length();i++){
		if (s[i] != ' ' && s[i] != '\r'){
			if (numberOfCharacters == n) {
				delete[] array;
				throw("Number of characters found in the string doesn't correspond to the specified number of characters.");
			}

			array[numberOfCharacters]=s[i];
			numberOfCharacters++;
		}
	}

	if (numberOfCharacters != n) {
		delete[] array;

		throw("Number of characters found in the string doesn't correspond to the specified number of characters.");
	}
	
	return array;
}

int Alphabet::dimension(){
	return numberOfCharacters;
}

int Alphabet::INDEL(){
	return numberOfCharacters;
}

int Alphabet::charToInt(char c){
	for (int i=0; i<numberOfCharacters; i++){
		if (alphabet[i]==c) return i;
	}

	if (c=='-')	return INDEL();
	else throw("Alphabet doesn't contain character \'"+string(&c)+"\'.");
}

char Alphabet::intToChar(int i){
	if (i==INDEL()) return '-';
	if (i<0 || i>(numberOfCharacters-1)) throw("Error while saving the alignment");
	return alphabet[i];
}

Alphabet::~Alphabet() {
	delete[] alphabet;
}

