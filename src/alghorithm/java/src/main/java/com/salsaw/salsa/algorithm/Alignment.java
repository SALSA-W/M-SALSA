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
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.salsaw.salsa.algorithm.exceptions.SALSAException;

/**
 * 
 * class that represents the alignment.
 * <p>
 * Theoretical notes:
 * <ul>
 * <li>Provides methods to move the GAP.</li>
 * <li>It does not keep inside the scores of the individual columns
 * (X<small><sub>k</sub></small>) and the value of the objective function, thus
 * avoiding the calculations to keep these updated variables. In fact, for the
 * purposes of the operation of the algorithm, it is not necessary to know at
 * each iteration the value of the WSP-Score, but only calculate the
 * improvements obtained by moving the GAP (the δ).</li>
 * <li>Another optimization is the implementation of a preprocessor that
 * converts the characters of the alignment to integers values. There is a
 * bijective correspondence between the characters and the integers generated.
 * <p>
 * The pre-processing is used only for efficiency purposes: the integers
 * correspond to correct indices in the matrix of class SubstitutionMatrix. In
 * this manner, when asked the score of the pair (α, β), the method of
 * SubstitutionMatrix score should not seek the position in memory, but simply
 * make a direct access to the position [α; β] of matrix.
 * </p>
 * <p>
 * The save method is the inverse operation of the pre-processing, bringing the
 * characters to their classical representation.
 * </p>
 * </li>
 * <ul>
 * </p>
 * 
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
	private final int[] alignMatrix;
	private final SubstitutionMatrix substitution;
	private final Alphabet alphabet;
	/**
	 * Sequences name and properties (found in FASTA files)
	 */
	private String[] properties;
	private float[] weights;
	private float weightsSUM;
	private final ArrayList<GAP> GAPS;

	private float[] countersMatrix;

	/**
	 * GAP opening penalty
	 */
	private final float GOP;

	private final TerminalGAPsStrategy terminal;

	// CONSTRUCTORS
	Alignment(String inputFilePath, String treeFileName, SubstitutionMatrix s,
			float g) throws IOException, SALSAException {
		this(inputFilePath, treeFileName, s, g, TerminalGAPsStrategy.ONLY_GEP);
	}

	Alignment(String inputFilePath, String treeFileName, SubstitutionMatrix s,
			float g, TerminalGAPsStrategy tgs) throws IOException,
			SALSAException {
		this.substitution = s;
		this.GOP = g;
		this.terminal = tgs;
		this.alphabet = s.getAlphabet();
		ArrayList<String> sequences = readInputSequences(inputFilePath);
		this.alignMatrix = new int[this.numberOfSequences * this.length];
		this.GAPS = new ArrayList<GAP>();

		createWeights(treeFileName);
		preprocessing(sequences);
		createCounters();
	}

	// GET / SET
	public final int getNumberOfSequences() {
		return this.numberOfSequences;
	}

	public final int getLength() {
		return this.length;
	}

	public final ArrayList<GAP> getGAPS() {
		return this.GAPS;
	}

	public final TerminalGAPsStrategy getTerminalGAPStrategy() {
		return this.terminal;
	}

	// METHODS
	/**
	 * Calculate the WSP-score (in the classic way, without using the counters)
	 * 
	 * @return
	 */
	public final float WSP() {
		float objval = 0.0f;
		// Int array already initialize at 0
		int[] numberOfGAPS = new int[numberOfSequences];
		GAP g;

		for (int i = 0; i < this.GAPS.size(); i++) {
			g = this.GAPS.get(i);

			if (this.terminal == TerminalGAPsStrategy.BOTH_PENALTIES
					|| !g.terminalGAP()) {
				numberOfGAPS[g.getRow()]++;
			}
		}

		for (int r1 = 0; r1 < this.numberOfSequences - 1; r1++) {
			for (int r2 = r1 + 1; r2 < this.numberOfSequences; r2++) {
				objval += this.weights[r1] * this.weights[r2]
						* pairwise(r1, r2, numberOfGAPS[r1], numberOfGAPS[r2]);
			}
		}

		return objval;
	}

	/**
	 * They move a GAP of one position and return the improvement in the
	 * WSP-Score due to this change
	 * 
	 * @param g
	 * @return
	 * @throws SALSAException
	 */
	public final float moveLeft(GAP g) throws SALSAException {
		int leftColumn = g.getBegin() - 1;
		int rightColumn = g.getEnd();
		int row = g.getRow();

		float delta = changeCell(row, rightColumn, this.alignMatrix[row
				* this.length + leftColumn]);
		delta += changeCell(row, leftColumn, this.alphabet.INDEL());

		g.moveLeft();
		return delta;
	}

	public final float moveRight(GAP g) throws SALSAException {
		int leftColumn = g.getBegin();
		int rightColumn = g.getEnd() + 1;
		int row = g.getRow();

		float delta = changeCell(row, leftColumn, this.alignMatrix[row
				* this.length + rightColumn]);
		delta += changeCell(row, rightColumn, this.alphabet.INDEL());

		g.moveRight();
		return delta;
	}

	/**
	 * Same as moveLeft and moveRight, but the improvement is not returned (not
	 * even calculated). Used to do the backtracking and restore the cell as
	 * before the movement of the GAP g.
	 * 
	 * @param g
	 * @throws SALSAException
	 */
	public final void goBackToLeft(GAP g) throws SALSAException {
		int leftColumn = g.getBegin() - 1;
		int rightColumn = g.getEnd();
		int row = g.getRow();

		restoreCell(row, rightColumn, this.alignMatrix[row * this.length
				+ leftColumn]);
		restoreCell(row, leftColumn, this.alphabet.INDEL());

		g.moveLeft();
	}

	/**
	 * Same as moveLeft and moveRight, but the improvement is not returned (not
	 * even calculated). Used to do the backtracking and restore the cell as
	 * before the movement of the GAP g.
	 * 
	 * @param g
	 * @throws SALSAException
	 */
	public final void goBackToRight(GAP g) throws SALSAException {
		int leftColumn = g.getBegin();
		int rightColumn = g.getEnd() + 1;
		int row = g.getRow();

		restoreCell(row, leftColumn, this.alignMatrix[row * this.length
				+ rightColumn]);
		restoreCell(row, rightColumn, this.alphabet.INDEL());

		g.moveRight();
	}

	/**
	 * It returns the penalty of adding a GOP in the specified row (it depends
	 * on the weight of the row).
	 * 
	 * @param row
	 * @return
	 */
	public final float getGOP(int row) {
		return this.GOP * this.weights[row]
				* (this.weightsSUM - this.weights[row]);
	}

	public final void save(String destinationFileNamePath) throws IOException,
			SALSAException {

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(
				destinationFileNamePath))) {

			// Write FASTA file
			for (int r = 0; r < numberOfSequences; r++) {
				// Add header indicator
				bw.write(">");
				bw.write(properties[r]);
				bw.newLine();

				for (int c = 0; c < length; c++) {
					// align
					bw.write(alphabet.intToChar(this.alignMatrix[r
							* this.length + c]));
				}

				bw.newLine();
				bw.flush();
			}
		}
	}

	// PRIVATE METHODS
	/**
	 * Given the file containing the guide tree, it generates the sequences'
	 * weights
	 * 
	 * @param fileName
	 * @throws SALSAException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private final void createWeights(String fileName) throws SALSAException,
			FileNotFoundException, IOException {
		Tree t = new Tree(fileName, this.numberOfSequences);

		t.changeRoot();

		this.weights = new float[this.numberOfSequences];
		this.weightsSUM = t.generateWeights(this.properties, this.weights);
	}

	/**
	 * It reads sequences list (as a strings' vector) from a FASTA file
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	private final ArrayList<String> readInputSequences(String filePath)
			throws IOException {
		ArrayList<String> sequences = new ArrayList<String>();
		ArrayList<String> sequencesHeaders = new ArrayList<String>();

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
					sequencesHeaders.add(line.substring(1).trim());

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

		this.numberOfSequences = sequences.size();
		this.properties = sequencesHeaders.toArray(new String[sequencesHeaders
				.size()]);
		this.length = this.properties[0].length();

		return sequences;
	}

	/**
	 * Converts the character alignment in an integer alignment for efficiency
	 * reasons
	 * 
	 * @param seq
	 * @throws SALSAException
	 */
	private final void preprocessing(ArrayList<String> seq)
			throws SALSAException {
		// If it is NULL, there are no GAPs opened
		GAP g = null;
		GAP previous;

		String currentSequence;
		int[] convertedSequence;

		for (int row = 0; row < this.numberOfSequences; row++) {
			previous = null;
			currentSequence = seq.get(row);
			convertedSequence = convert(currentSequence);

			for (int column = 0; column < this.length; column++) {
				this.alignMatrix[row * this.length + column] = convertedSequence[column];
				if (convertedSequence[column] == this.alphabet.INDEL()) {
					if (g == null) {
						g = new GAP(row, column, this.length, previous, null);
						if (previous != null) {
							previous.setNext(g);
						}
					} else
						g.extend();
				} else if (g != null) {
					// A GAP has been found and it is finished
					this.GAPS.add(g);
					previous = g;
					g = null;
				}
			}
			if (g != null) {
				// A GAP has been found and it is finished
				this.GAPS.add(g);
				g = null;
			}
		}
	}

	/**
	 * Used by pre-processing
	 * 
	 * @param s
	 * @return
	 * @throws SALSAException
	 */
	private final int[] convert(String s) throws SALSAException {
		int[] sequenceOfNumbers = new int[this.length];

		for (int c = 0; c < this.length; c++) {
			sequenceOfNumbers[c] = this.alphabet.charToInt(s.charAt(c));
		}

		return sequenceOfNumbers;
	}

	private final void createCounters() {
		this.countersMatrix = new float[(this.alphabet.dimension() + 1)
				* this.length];

		// Java already initialize at 0f :
		// http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
		// Initialize at 0
		int character;
		for (int column = 0; column < length; column++) {
			for (int row = 0; row < numberOfSequences; row++) {
				// align method
				character = this.alignMatrix[row * this.length + column];
				// counters method
				this.countersMatrix[character * this.length + column] += weights[row];
			}
		}
	}

	/**
	 * Score of two sequences in the specified rows (used by WSP). It requires
	 * also the number of GAPS inside the rows
	 * 
	 * @param row1
	 * @param row2
	 * @param numberOfGAPSr1
	 * @param numberOfGAPSr2
	 * @return
	 */
	private final float pairwise(int row1, int row2, int numberOfGAPSr1,
			int numberOfGAPSr2) {
		float value = 0;
		int alpha, beta;
		for (int column = 0; column < this.length; column++) {
			alpha = this.alignMatrix[row1 * this.length + column];
			beta = this.alignMatrix[row2 * this.length + column];
			value += this.substitution.score(alpha, beta);
		}

		value -= this.GOP * (numberOfGAPSr1 + numberOfGAPSr2);

		return value;
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
		// align
		int oldCharacter = this.alignMatrix[row * this.length + column];
		// counters
		this.countersMatrix[oldCharacter * this.length + column] -= weights[row];

		float delta = 0.0f;
		for (int alpha = 0; alpha <= this.alphabet.dimension(); alpha++) {
			delta +=
			// counters
			this.countersMatrix[alpha * this.length + column]
					* (this.substitution.score(newCharacter, alpha) - this.substitution
							.score(oldCharacter, alpha));
		}
		delta *= weights[row];

		// counters
		this.countersMatrix[newCharacter * this.length + column] += weights[row];
		// align
		this.alignMatrix[row * this.length + column] = newCharacter;

		return delta;
	}

	/**
	 * Same as changeCell, but here the improvement is not calculated
	 * 
	 * @param row
	 * @param column
	 * @param newCharacter
	 */
	private final void restoreCell(int row, int column, int newCharacter) {
		// align
		int oldCharacter = this.alignMatrix[row * this.length + column];
		// counters
		this.countersMatrix[oldCharacter * this.length + column] -= weights[row];

		// counters
		this.countersMatrix[newCharacter * this.length + column] += weights[row];
		// align
		this.alignMatrix[row * this.length + column] = newCharacter;
	}
}
