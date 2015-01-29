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
package com.salsaw.salsa.algorithm;

import com.salsaw.salsa.algorithm.exceptions.SALSAException;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class Alphabet {
	// FILEDS
	private final int numberOfCharacters;
	private final char[] alphabet;	
	
	// CONSTRUCTOR
	public Alphabet(String matrixInputLine, int alphabetLength) throws SALSAException{		
		this.alphabet = new char[alphabetLength];

		int numberOfCharacters = 0;	
		for (int i=0; i < matrixInputLine.length(); i++){
			
			// For short string charAt has higher performance http://stackoverflow.com/a/11876086
			char symbol = matrixInputLine.charAt(i);
			if (symbol !=' '){
				alphabet[numberOfCharacters] = symbol;
				numberOfCharacters++;
			}
		}
		
		this.numberOfCharacters = numberOfCharacters;
		
		if (numberOfCharacters != alphabetLength){
			throw new SALSAException("Number of characters in the first line of the substitution matrix doesn't correspond to the specified number of characters.");
		}		
	}
	
	// METHODS
	/**
	 * Next two are used for efficiency reasons: before starting the local search,
	 * each cell of the alignment are converted in an integer. Then, during the local search,
	 * the score is calculated only by accessing the substitution matrix. The converted characters
	 * are indeed the indexes of the substitution matrix. Without this conversion it would be
	 * necessary to calculate every time the score's position in the substitution matrix.
	 **/
	//Conversion of character in an integer
	public int charToInt(char c){
		// TODO Report code from c
		return 0;
	}
	/**
	 * Conversion of an integer in a character (to be used after the end of local search)
	 * @param i
	 * @return
	 */
	public char intToChar(int i){
		// TODO Report code from c
		return 0;
	}

	/**
	 * Dimension of the alphabet number of characters
	 * @return
	 */
	public int dimension(){
		// TODO Report code from c
		return 0;
	}
	
	/**
	 * The representation of the INDEL in the integer form (see above)
	 * @return
	 */
	public int INDEL(){
		// TODO Report code from c
		return 0;
	}
}
