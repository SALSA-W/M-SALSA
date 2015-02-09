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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public final class Alignment {
	// FIELDS
	private int numberOfSequences;
	private int length;
	/**
	 * The alignment
	 */
	private int[] alignMatrix;
	private SubstitutionMatrix substitution;
	private Alphabet alphabet;
	/**
	 * Sequences name and properties (found in FASTA files)
	 */
	private ArrayList<String> properties;
	private float[] weights;
	private float weightsSUM;
	private ArrayList<GAP> GAPS;

	private float[] countersMatrix;

	/**
	 * GAP opening penalty
	 */
	private float GOP;

	private TerminalGAPsStrategy terminal;

	// CONSTRUCTORS
	Alignment(String fileName, String treeFileName, SubstitutionMatrix s,
			float g) {
		this(fileName, treeFileName, s, g, TerminalGAPsStrategy.ONLY_GEP);
	}

	Alignment(String fileName, String treeFileName, SubstitutionMatrix s,
			float g, TerminalGAPsStrategy tgs) {
	}

	// GET / SET
	public final int getNumberOfSequences() {
		// TODO Report code from c
		return 0;
	}

	public final int getLength() {
		// TODO Report code from c
		return 0;
	}

	public final ArrayList<GAP> getGAPS() {
		// TODO Report code from c
		return null;
	}

	// METHODS

	/**
	 * Calculate the WSP-score (in the classic way, without using the counters)
	 * 
	 * @return
	 */
	public final float WSP() {
		// TODO Report code from c
		return 0;
	}

	public final TerminalGAPsStrategy getTerminalGAPStrategy() {
		// TODO Report code from c
		return null;
	}

	/**
	 * They move a GAP of one position and return the improvement in the
	 * WSP-Score due to this change
	 * 
	 * @param g
	 * @return
	 */
	public final float moveLeft(GAP g) {
		// TODO Report code from c
		return 0;
	}

	public final float moveRight(GAP g) {
		// TODO Report code from c
		return 0;
	}

	/**
	 * Same as moveLeft and moveRight, but the improvement is not returned (not
	 * even calculated). Used to do the backtracking and restore the cell as
	 * before the movement of the GAP g.
	 * 
	 * @param g
	 */
	public final void goBackToLeft(GAP g) {
		// TODO Report code from c
	}

	public final void goBackToRight(GAP g) {
		// TODO Report code from c
	}

	/**
	 * It returns the penalty of adding a GOP in the specified row (it depends
	 * on the weight of the row).
	 * 
	 * @param row
	 * @return
	 */
	public final float getGOP(int row) {
		// TODO Report code from c
		return 0;
	}

	public final void save(String fileName) {
		// TODO Report code from c
	}

	// PRIVATE METHODS
	/**
	 * Given the file containing the guide tree, it generates the sequences'
	 * weights
	 * 
	 * @param fileName
	 */
	private final void createWeights(String fileName) {
		// TODO Report code from c
	}

	/**
	 * It reads sequences list (as a strings' vector) from a FASTA file
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException 
	 */
	private final ArrayList<String> readInputSequences(String filePath) throws IOException {
		ArrayList<String> sequences = new ArrayList<String>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(filePath))) {
			String line;
			StringBuffer contentBuffer = new StringBuffer();
			
			// https://github.com/joewandy/BioinfoApp/blob/master/src/com/joewandy/bioinfoapp/model/core/io/FastaReader.java
			
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}
				char firstChar = line.charAt(0);
				
				if (firstChar == '>') {
					if (contentBuffer.length() != 0) {
						// save the previous sequence read
						sequences.add(contentBuffer.toString());
					}
					
					// now can get the new id > ..
					//String sequenceId = line.substring(1).trim();
					
					// start a new content buffer
					contentBuffer = new StringBuffer();
					
				} else if (firstChar == ';') {
					// comment line, skip it
				} else {
					// carry on reading sequence content
					contentBuffer.append(line.trim());
				}
			}
			
			if (contentBuffer.length() != 0) {
				// save the last sequence
				sequences.add(contentBuffer.toString());
			}
		}

		return sequences;
	}

	/**
	 * Converts the character alignment in an integer alignment for efficiency
	 * reasons
	 * 
	 * @param seq
	 */
	private final void preprocessing(ArrayList<String> seq) {
		// TODO Report code from c
	}

	/**
	 * Used by pre-processing
	 * 
	 * @param s
	 * @return
	 */
	private final int[] convert(String s) {
		// TODO Report code from c
		return null;
	}

	private final void createCounters() {
		// TODO Report code from c
	}

	/**
	 * Returns the names of the sequences (extracted from the FASTA file
	 * removing the '>' character)
	 * 
	 * @return
	 */
	private final String[] getNames() {
		// TODO Report code from c
		return null;
	}

	/**
	 * Score of two sequences in the specified rows (used by WSP). It requires
	 * also the number of GAPS inside the rows
	 * 
	 * @param r1
	 * @param r2
	 * @param numberOfGAPSr1
	 * @param numberOfGAPSr2
	 * @return
	 */
	private final float pairwise(int r1, int r2, int numberOfGAPSr1,
			int numberOfGAPSr2) {
		// TODO Report code from c
		return 0;
	}

	/**
	 * It modify the character in position (row, column) and returns the
	 * improvement obtained by changing it.
	 * 
	 * @param row
	 * @param column
	 * @param newCharacter
	 * @return
	 */
	private final float changeCell(int row, int column, int newCharacter) {
		return 0;
	}

	/**
	 * Same as changeCell, but here the improvement is not calculated
	 * 
	 * @param row
	 * @param column
	 * @param newCharacter
	 */
	private final void restoreCell(int row, int column, int newCharacter) {
	}
}
