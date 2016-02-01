/**
 * Copyright 2016 Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public final class AlphabetOptimizer {
	
	// FIELDS
	private final int[] alignMatrix;
	private Alphabet alphabet;
	
	
	// CONSTRUCTORS
	public AlphabetOptimizer(final int[] alignMatrix, final Alphabet alphabet) {
		this.alignMatrix = alignMatrix;
		this.alphabet = alphabet;
	}	
	
	// GET / SET
	public final int[] getAlignMatrix() {
		return this.alignMatrix;
	}
	
	public final Alphabet getOptimizedAlphabet() {
		return this.alphabet;
	}
	
	// METHODS	
	/**
	 * Return the alphabet with only the used characters.
	 * Decrease the size of the alphabet using only the necessary characters decrease the complexity of algorithm that depends from alphabet size 
	 * 
	 * @return
	 * @throws SALSAException
	 */
	public final boolean tryOptimize() throws SALSAException {		
		// Try to optimize alphabet size	
		Set<Integer> usedAlphabetChars = new  HashSet<>();
		for (int alignElement : this.alignMatrix){
			usedAlphabetChars.add(alignElement);			
		}
		
		if (usedAlphabetChars.size() -1 != this.alphabet.getNumberOfCharacters()) {
			
			// Create a new alphabet that contains only necessary symbols
			char[] optimizedAlphabetArray = new char[usedAlphabetChars.size() -1];

			// Get characters from int
			int i = 0;
			int gapPosition = this.alphabet.charToInt(Alphabet.GAP_SYMBOL);
			for (Integer val : usedAlphabetChars) {
				if (val != gapPosition){
					optimizedAlphabetArray[i++] = this.alphabet.intToChar(val);
				}
			}
			
			Alphabet optimizedAlphabet = new Alphabet(optimizedAlphabetArray);

			// Create map to convert matrix from old alphabet to new one
			Map<Integer, Integer> oldToNewConversionMap = new HashMap<>();
			oldToNewConversionMap.put(gapPosition, optimizedAlphabet.charToInt(Alphabet.GAP_SYMBOL));
			for (char optimizedAlphabetSymbol : optimizedAlphabetArray) {
				int oldAlphabetIndex = this.alphabet.charToInt(optimizedAlphabetSymbol);
				int optimizedAlphabetindex = optimizedAlphabet.charToInt(optimizedAlphabetSymbol);
				if (oldAlphabetIndex != optimizedAlphabetindex){
					oldToNewConversionMap.put(oldAlphabetIndex, optimizedAlphabetindex);				
				}
			}
			
			// Use smaller alphabet instead of bigger one
			this.alphabet = optimizedAlphabet;
			
			// Update alignment matrix with new indexes
			for (int j = 0; j< this.alignMatrix.length; j++){
				if (oldToNewConversionMap.containsKey(alignMatrix[j]) == true){
					this.alignMatrix[j] = oldToNewConversionMap.get(alignMatrix[j]);	
				}
			}			
			
			return true;
		} else {
			return false;
		}
	}
}
