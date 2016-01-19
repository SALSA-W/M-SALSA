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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;

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
	
	private final static HashMap<String, SubstitutionMatrix> substitutionMatrixCache = new HashMap<String, SubstitutionMatrix>();

	// CONSTRUCTOR

	// GET/SET
	public final Alphabet getAlphabet() {
		return this.alphabet;
	}	
	

	public SubstitutionMatrix(InputStream scoringMatrixStream, Alphabet alphabet, float gep)
			throws SALSAException, IOException {
		this.GEP = gep;		
		
		try(Scanner scanner = new Scanner(scoringMatrixStream))
		{
			String line = scanner.nextLine();
			
			// Skip all headers comment lines
			while(line.startsWith("#") == true)
			{
				line = scanner.nextLine();
			}
			
			// Read first line with the alphabet
			Alphabet recognizedAlphabet = new Alphabet(line);
			
			int alphabetLength = alphabet.getNumberOfCharacters();
			this.matrix = new int[alphabetLength * alphabetLength];
			
			this.alphabetLength = alphabetLength;
			
			if (alphabetLength != recognizedAlphabet.dimension()) {
				//The two alphabets are different
				throw new SALSAException("Error: substitution matrix file format is not supported");
			}
			else { //Find out if the order of characters is the same in the two alphabets
				for (int i = 0; i < alphabetLength; i++) {
					if (alphabet.intToChar(i) != recognizedAlphabet.intToChar(i))
						throw new SALSAException("Error: substitution matrix file format is not supported");
				}
			}
			
			// Read the values for the matrix value (the matrix is alphabet x alphabet)
			for (int i=0; i<alphabetLength;i++){
				for (int j=0; j<alphabetLength; j++){
					this.matrix[i*alphabetLength+j] = scanner.nextInt();
				}
			}
		}	
		
		this.alphabet = alphabet;
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
	
	public static final SubstitutionMatrix getSubstitutionMatrix(MatrixSerie matrixSerie, float pid, float GEP, Alphabet alphabet) throws SALSAException, IOException {
		if (alphabet == null){
			alphabet = new Alphabet(AlphabetType.PROTEINS);
		}
		
		switch (matrixSerie) {
		case BLOSUM:
			if (pid > 0.8) return loadEmbeddedMatirx("BLOSUM80", alphabet, GEP);
			if (pid > 0.6) return loadEmbeddedMatirx("BLOSUM62", alphabet, GEP);
			if (pid > 0.3) return loadEmbeddedMatirx("BLOSUM45", alphabet, GEP);
			return loadEmbeddedMatirx("BLOSUM30", alphabet, GEP);

		case PAM:
			if (pid > 0.8) return loadEmbeddedMatirx("PAM20", alphabet, GEP);
			if (pid > 0.6) return loadEmbeddedMatirx("PAM60", alphabet, GEP);
			if (pid > 0.4) return loadEmbeddedMatirx("PAM120", alphabet, GEP);
			return loadEmbeddedMatirx("PAM350", alphabet, GEP);

		default:
			//MatrixSerie is not BLOSUM and it is not PAM
			return null;
		}		
	}

	/**
	 * Load well-known matrix from embedded resources
	 * 
	 * @return
	 * @throws IOException 
	 * @throws SALSAException 
	 */
	private static final SubstitutionMatrix loadEmbeddedMatirx(String scoringMatrixName, Alphabet alphabet, float GEP)
			throws IOException, SALSAException {
		SubstitutionMatrix cachedSubstitutionMatrix = substitutionMatrixCache.getOrDefault(scoringMatrixName, null);

		if (cachedSubstitutionMatrix == null) {
			// Data isn't present in the cache - load from file system
			try (InputStream stream = App.class.getResourceAsStream("/matrix/" + scoringMatrixName)) {
				cachedSubstitutionMatrix = new SubstitutionMatrix(stream, alphabet, GEP);
				substitutionMatrixCache.put(scoringMatrixName, cachedSubstitutionMatrix);
			}
		}

		return cachedSubstitutionMatrix;
	}
}
