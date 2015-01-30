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
public final class Tree {
	// FIELDS
	private final Node root;
	private final Node[] leaves;
	private final int insertedSequences;

	// METHODS
	/**
	 * The parameters are the name of the file containing the tree in the Newick
	 * notation and the number of sequences
	 * 
	 * @param fileName
	 * @param numberOfSequences
	 */
	public Tree(final String fileName, int numberOfSequences) {

		this.insertedSequences = 0;
		// file.open(fileName);

		// if(!file.is_open())
		// throw("ERROR: file "+string(fileName)+" can't be open.");

		this.leaves = new Node[numberOfSequences];

		this.root = createNode(null);

		// file.close();

		// TODO Report code from c
	}

	/**
	 * Re-root the tree
	 */
	public final void changeRoot() {
		// TODO Report code from c
	}

	/**
	 * Take as input the sequences' names and the weights array, it modifies the
	 * weights and returns the their sum.
	 * 
	 * @param names
	 * @param weights
	 * @return
	 * @throws SALSAException
	 */
	public final float generateWeights(String[] names, float[] weights)
			throws SALSAException {
		float weightsSum = 0.0f;
		int index;

		for (int i = 0; i < this.insertedSequences; i++) {

			// Calculate sequence's index in the alignment
			index = -1;
			for (int j = 0; j < this.insertedSequences && index == - 1; j++) {
				if (names[j] == (leaves[i].getName())) {
					index = j;
				}
			}

			if (index == -1) {
				throw new SALSAException(
						"Error: the alignment and the tree have different sequences.");
			}

			// Calculate the weight of the i-th leaf
			weights[index] = leafWeight(leaves[i]);
			weightsSum += weights[index];
		}

		return weightsSum;
	}

	public final void printTree() {
		// TODO Report code from c
	}

	private final String readName() {
		// TODO Report code from c
		return null;
	}

	private final Node createNode(Node parent) {
		// TODO Report code from c
		return null;
	}

	private final float leafWeight(Node leaf) {
		// TODO Report code from c
		return 0;
	}

}
