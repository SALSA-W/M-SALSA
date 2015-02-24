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

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.salsaw.salsa.algorithm.exceptions.SALSAException;

/**
 * It represent the substitution matrix. The role is to return the value of
 * m(α;β) using method score.
 * 
 * <p>
 * Note: inside the matrix is represented by an array
 * </p>
 * 
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public final class SubstitutionMatrix {

	// FIELDS
	private final int alphabetLength;
	/**
	 * GAP extension penalty
	 */
	private final float GEP;

	private final Alphabet alphabet;

	private final int[] matrix;

	// CONSTRUCTOR

	// GET/SET
	public final Alphabet getAlphabet() {
		return this.alphabet;
	}

	public SubstitutionMatrix(InputStream scoringMatrixStream, float gep)
			throws SALSAException, IOException {
		this.GEP = gep;		
		
		try(Scanner scanner = new Scanner(scoringMatrixStream))
		{
			// Read first line with the alphabet
			String line = scanner.nextLine();
			this.alphabet = new Alphabet(line);
			
			int alphabetLength = this.alphabet.getNumberOfCharacters();
			this.matrix = new int[alphabetLength * alphabetLength];
			this.alphabetLength = alphabetLength;
			
			// Read the values for the matrix value (the matrix is alphabet x alphabet)
			for (int i=0; i<alphabetLength;i++){
				for (int j=0; j<alphabetLength; j++){
					this.matrix[i*alphabetLength+j] = scanner.nextInt();
				}
			}
		}		
	}

	/**
	 * Calculate the score between two characters (converted in integers)
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public final float score(int a, int b) {
		if (a == this.alphabet.INDEL()) {
			if (b == this.alphabet.INDEL()) {
				return 0;
			} else {
				return -GEP;
			}
		}
		if (b == this.alphabet.INDEL()) {
			return -GEP;
		}

		return matrix[a * alphabetLength + b];
	}

}
