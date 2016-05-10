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
import java.util.Map;
import java.util.Scanner;

import com.salsaw.msalsa.algorithm.enums.AlphabetType;
import com.salsaw.msalsa.algorithm.enums.EmbeddedScoringMatrix;
import com.salsaw.msalsa.algorithm.enums.MatrixSerie;
import com.salsaw.msalsa.algorithm.exceptions.SALSAException;
import java.util.EnumMap;

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
	// CONSTANTS 
	private static Alphabet ALPHABET_PROTEINS;
	private static Alphabet ALPHABET_DNA;
	private static Alphabet ALPHABET_RNA;

	// FIELDS
	private final int alphabetLength;
	/**
	 * GAP extension penalty
	 */
	private final float GEP;

	private final Alphabet alphabet;

	private final int[] matrix;
	
	private static final Map<EmbeddedScoringMatrix, SubstitutionMatrix> SUBSTITUTION_MATRIX_CACHE = new EnumMap<>(EmbeddedScoringMatrix.class);

	// CONSTRUCTOR
	
	// Static initialization
	static {
		try {
			ALPHABET_PROTEINS = new Alphabet(AlphabetType.PROTEINS);
			ALPHABET_DNA = new Alphabet(AlphabetType.DNA);
			ALPHABET_RNA = new Alphabet(AlphabetType.RNA);
		} catch (SALSAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public SubstitutionMatrix(SubstitutionMatrix substitutionMatrix, float gep)
			throws SALSAException, IOException {
		this.GEP = gep;
		this.matrix = substitutionMatrix.matrix;
		this.alphabet = substitutionMatrix.alphabet;
		this.alphabetLength = this.alphabet.dimension();
	}
	
	public SubstitutionMatrix(InputStream scoringMatrixStream, Alphabet expectedAlphabet, float gep)
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
			
			int alphabetLengthProcessing;
			if (expectedAlphabet != null){
				// Check if expected alphabet and one load from stream corresponds
				alphabetLengthProcessing = expectedAlphabet.getNumberOfCharacters();
				if (alphabetLengthProcessing != recognizedAlphabet.dimension()) {
					//The two alphabets are different
					throw new SALSAException("Error: substitution matrix file format is not supported");
				}
				else { //Find out if the order of characters is the same in the two alphabets
					for (int i = 0; i < alphabetLengthProcessing; i++) {
						if (expectedAlphabet.intToChar(i) != recognizedAlphabet.intToChar(i))
							throw new SALSAException("Error: substitution matrix file format is not supported");
					}
				}
				this.alphabet = expectedAlphabet;
			} else {
				alphabetLengthProcessing = recognizedAlphabet.getNumberOfCharacters();
				this.alphabet = recognizedAlphabet;
			}				
			
			this.matrix = new int[alphabetLengthProcessing * alphabetLengthProcessing];			
			this.alphabetLength = alphabetLengthProcessing;
			
			// Read the values for the matrix value (the matrix is alphabet x alphabet)
			for (int i=0; i<alphabetLengthProcessing;i++){
				for (int j=0; j<alphabetLengthProcessing; j++){
					this.matrix[i*alphabetLengthProcessing+j] = scanner.nextInt();
				}
			}
		}			
	}
	
	// GET/SET
	public final Alphabet getAlphabet() {
		return this.alphabet;
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
	
	public static final EmbeddedScoringMatrix getEmbeddedSubstitutionMatrix(MatrixSerie matrixSerie, float pid) throws SALSAException, IOException {		
		switch (matrixSerie) {
		case BLOSUM:
			if (pid > 0.8) return EmbeddedScoringMatrix.BLOSUM80;
			if (pid > 0.6) return EmbeddedScoringMatrix.BLOSUM62;
			if (pid > 0.3) return EmbeddedScoringMatrix.BLOSUM45;
			return EmbeddedScoringMatrix.BLOSUM62;

		case PAM:
			if (pid > 0.8) return EmbeddedScoringMatrix.PAM20;
			if (pid > 0.6) return EmbeddedScoringMatrix.PAM60;
			if (pid > 0.4) return EmbeddedScoringMatrix.PAM120;
			return EmbeddedScoringMatrix.PAM350;

		default:
			//MatrixSerie is not BLOSUM and it is not PAM
			throw new SALSAException("The " + matrixSerie + " isn't managed");
		}		
	}

	/**
	 * Load well-known matrix from embedded resources
	 * 
	 * @param scoringMatrix
	 * @param GEP
	 * @return
	 * @throws IOException 
	 * @throws SALSAException 
	 */
	public static final SubstitutionMatrix getSubstitutionMatrix(EmbeddedScoringMatrix scoringMatrix, float GEP)
			throws IOException, SALSAException {
		SubstitutionMatrix cachedSubstitutionMatrix = SUBSTITUTION_MATRIX_CACHE.getOrDefault(scoringMatrix, null);

		if (cachedSubstitutionMatrix == null) {
			Alphabet alphabet;
			switch (scoringMatrix) {
			case BLOSUM30:
			case BLOSUM45:
			case BLOSUM50:
			case BLOSUM62:
			case BLOSUM80:
			case PAM20:
			case PAM60:
			case PAM120:
			case PAM350:
				alphabet = ALPHABET_PROTEINS;
				break;
				
			case IUB:
			case DNAClustal:
				alphabet = ALPHABET_DNA;
				break;
				
			case RNAClustal:
				alphabet = ALPHABET_RNA;
				break;
								
			default:
				// Gonnet - BLOSUM50 - PAM250
				alphabet = null;
				break;
			}		
			
			// Data isn't present in the cache - load from file system
			try (InputStream stream = App.class.getResourceAsStream("/matrix/" + scoringMatrix.toString())) {
				cachedSubstitutionMatrix = new SubstitutionMatrix(stream, alphabet, GEP);
				SUBSTITUTION_MATRIX_CACHE.put(scoringMatrix, cachedSubstitutionMatrix);
			}
		}
		
		if (cachedSubstitutionMatrix.GEP != GEP){
			// TODO - evaluate if set GEP as alignment property and pass to score method of SubstitutionMatrix
			cachedSubstitutionMatrix = new SubstitutionMatrix(cachedSubstitutionMatrix, GEP);
		}

		return cachedSubstitutionMatrix;
	}
}
