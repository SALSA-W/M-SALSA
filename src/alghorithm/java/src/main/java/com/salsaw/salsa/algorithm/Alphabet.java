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
 * It represents the alphabet.
 * 
 * It exposed methods for the conversion of characters to integer and vice versa
 * for pre-processing (see class {@link Alignment});
 * <p>
 * Note: An instance of this class is created from {@link SubstitutionMatrix}
 * based on the content of the substitution matrix.
 * </p>
 * 
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class Alphabet {
	// FIELDS
	private final int numberOfCharacters;
	private final char[] alphabet;

	// CONSTRUCTOR
	public Alphabet(String matrixInputLine, int alphabetLength)
			throws SALSAException {
		this.alphabet = new char[alphabetLength];

		int numberOfCharacters = 0;
		for (int i = 0; i < matrixInputLine.length(); i++) {

			// For short string charAt has higher performance
			// http://stackoverflow.com/a/11876086
			char symbol = matrixInputLine.charAt(i);
			if (symbol != ' ') {
				alphabet[numberOfCharacters] = symbol;
				numberOfCharacters++;
			}
		}

		this.numberOfCharacters = numberOfCharacters;

		if (numberOfCharacters != alphabetLength) {
			throw new SALSAException(
					"Number of characters in the first line of the substitution matrix doesn't correspond to the specified number of characters.");
		}
	}

	// METHODS
	/**
	 * Conversion of character in an integer
	 * <p>
	 * Note: Used for efficiency reasons: before starting the local search, each
	 * cell of the alignment are converted in an integer. Then, during the local
	 * search, the score is calculated only by accessing the substitution
	 * matrix. The converted characters are indeed the indexes of the
	 * substitution matrix. Without this conversion it would be necessary to
	 * calculate every time the score's position in the substitution matrix.
	 * </p>
	 * 
	 * @param c
	 * @return
	 * @throws SALSAException
	 */
	public final int charToInt(char c) throws SALSAException {
		for (int i = 0; i < this.numberOfCharacters; i++) {
			if (this.alphabet[i] == c) {
				return i;
			}
		}

		if (c == '-') {
			// INDEL
			return this.numberOfCharacters;
		} else {
			throw new SALSAException("Alphabet doesn't contain character \'"
					+ c + "\'.");
		}
	}

	/**
	 * Conversion of an integer in a character (to be used after the end of
	 * local search)
	 * <p>
	 * Note: Used for efficiency reasons: before starting the local search, each
	 * cell of the alignment are converted in an integer. Then, during the local
	 * search, the score is calculated only by accessing the substitution
	 * matrix. The converted characters are indeed the indexes of the
	 * substitution matrix. Without this conversion it would be necessary to
	 * calculate every time the score's position in the substitution matrix.
	 * </p>
	 * 
	 * @param i
	 * @return
	 * @throws SALSAException
	 */
	public final char intToChar(int i) throws SALSAException {
		// this.numberOfCharacters as INDEL
		if (i == this.numberOfCharacters) {
			return '-';
		}
		if (i < 0 || i > (this.numberOfCharacters - 1)) {
			throw new SALSAException("Error while saving the alignment");
		}

		return alphabet[i];
	}

	/**
	 * Dimension of the alphabet number of characters
	 * 
	 * @return
	 */
	public final int dimension() {
		return this.numberOfCharacters;
	}

	/**
	 * The representation of the INDEL in the integer form (see above)
	 * <p>
	 * Note: Indel is a molecular biology term for the insertion or the deletion
	 * of bases in the DNA of an organism
	 * </p>
	 * 
	 * @return
	 */
	public final int INDEL() {
		return this.numberOfCharacters;
	}
}
