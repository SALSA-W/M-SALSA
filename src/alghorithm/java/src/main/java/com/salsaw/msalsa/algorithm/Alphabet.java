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
package com.salsaw.msalsa.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;

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
	private static final char[] AlphabetDNA = new char[] {'A', 'T', 'C', 'G'};
	private static final char[] AlphabetRNA = new char[] {'A', 'U', 'C', 'G'};
	private static final char[] AlphabetPROTEINS = new char[] {'A', 'R', 'N', 'D', 'C','Q','E','G','H','I','L','K','M','F','P','S','T','W','Y','V','B','Z','X'};

	// CONSTRUCTOR
	
	public Alphabet(String matrixInputLine) throws SALSAException{
		this.alphabet = calculateAlphabetArray(matrixInputLine);
		this.numberOfCharacters = this.alphabet.length;
	}
	
	public Alphabet(AlphabetType type) throws SALSAException {
		switch (type) {
		case DNA:
			this.alphabet = AlphabetDNA;
			break;
		case PROTEINS:
			this.alphabet = AlphabetPROTEINS;
			break;
		case RNA:
			this.alphabet = AlphabetRNA;
			break;
		default:
			throw new SALSAException(
					"Error: the specified type of alphabet is not supported. Supported types are: PROTEINS, DNA or RNA");
		}

		this.numberOfCharacters = this.alphabet.length;
	}
	
	private static final char[] calculateAlphabetArray(String matrixInputLine) throws SALSAException{
		matrixInputLine = matrixInputLine.trim();

		List<Character> alphabetsSymbols = new ArrayList<Character>();		
		
		// Use scanner to avoid probles due to wrong spaces number
		try(Scanner scanner = new Scanner(matrixInputLine))
		{
			while(scanner.hasNext() == true){
				// For short string charAt has higher performance
				// http://stackoverflow.com/a/11876086
				alphabetsSymbols.add(scanner.next().charAt(0));
			}
		}
		
		if (alphabetsSymbols.size() <= 0){
			throw new SALSAException("Error: number of characters in the alphabet should be greater than 0!");
		}
		
		char[] alphabet = new char[alphabetsSymbols.size()];
		for (int i = 0; i < alphabetsSymbols.size(); i++) {
			alphabet[i] = alphabetsSymbols.get(i).charValue();
		}
		
		return alphabet;	
	}

	// GET/SET

	public final int getNumberOfCharacters() {
		return this.numberOfCharacters;
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
			throw new SALSAException("Alphabet doesn't contain character \'" + c + "\'.");
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
