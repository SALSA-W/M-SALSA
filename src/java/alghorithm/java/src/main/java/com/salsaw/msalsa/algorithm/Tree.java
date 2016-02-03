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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public final class Tree {
	// FIELDS
	private Node root;
	private final Node[] leaves;
	private int insertedSequences;

	// CONSTRUCTOR
	/**
	 * The parameters are the name of the file containing the tree in the Newick
	 * notation and the number of sequences
	 * 
	 * @see <a
	 *      href="spec.html#http://en.wikipedia.org/wiki/Newick_format">Newick</a>
	 * 
	 * @param fileName
	 * @param numberOfSequences
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws SALSAException
	 */
	public Tree(final String fileName, int numberOfSequences)
			throws FileNotFoundException, IOException, SALSAException {
		this.insertedSequences = 0;
		this.leaves = new Node[numberOfSequences];

		try (PushbackReader buffer = new PushbackReader(
				new FileReader(fileName))) {
			this.root = createNode(buffer, null);
		}
	}

	// METHODS
	/**
	 * Re-root the tree
	 * 
	 * @throws SALSAException
	 */
	public final void changeRoot() throws SALSAException {
		// Find best root
		Node bestNode = this.root
				.calculatePositionOfRoot(this.insertedSequences);

		if (bestNode != null) {
			this.root = bestNode.addRoot();
		}
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
			for (int j = 0; j < this.insertedSequences && index == -1; j++) {
				if (names[j].equals(this.leaves[i].getName())) {
					index = j;
				}
			}

			if (index == -1) {
				throw new SALSAException(
						"Error: the alignment and the tree have different sequences.");
			}

			// Calculate the weight of the i-th leaf
			weights[index] = leafWeight(this.leaves[i]);
			weightsSum += weights[index];
		}

		return weightsSum;
	}

	public final void printTree() {
		// TODO Report code from c
	}

	private char ReadNext(PushbackReader reader) throws IOException {
		char c = (char) reader.read();

		while ( c == ' ' ||
				// Skip new line characters
				c == '\n' || c == '\r') {
			c = (char) reader.read();
		}

		return c;
	}

	private final Node createNode(PushbackReader reader, Node parent)
			throws IOException, SALSAException {
		Node current = new Node("", null, null, parent, 0);
		char c;

		c = ReadNext(reader);
		if (c != '(') {
			// Leaf

			// Example of leaf line: 2lef_A:0.40631,
			// Read name
			String name = "";
			do {							
				name += c;
				c = (char) reader.read();
			} while (c != ':');
			current.setName(name);

			ReadDistance(reader, current);

			// next character should be a ',' or a ')'
			leaves[insertedSequences] = current;
			insertedSequences++;
		} else {
			// Internal node
			current.setLeft(createNode(reader, current));

			c = ReadNext(reader);

			if (c == ',') {
				current.setRight(createNode(reader, current));

				c = ReadNext(reader);
				if (c == ',') {
					// It is the root and there are three sons
					if (parent != null) {
						throw new SALSAException(
								"More than two sons for an internal node (not root).");
					}

					Node newNode = new Node("ARTIFICIAL", current, null, null,
							0);
					current.setParent(newNode);

					newNode.setRight(createNode(reader, newNode));
					current = newNode;
				}
			}

			c = ReadNext(reader);
			if (c == ':') {
				ReadDistance(reader, current);
			} else {
				// pushes the character back into the buffer
				reader.unread((int) c);
			}
		}

		return current;
	}

	private void ReadDistance(PushbackReader reader, Node current)
			throws IOException {
		// Read distance
		String distanceValue = "";
		char c = ReadNext(reader);
		do {
			distanceValue += c;
			c = ReadNext(reader);
		} while (c != ',' && c != ')');
		current.setDistance(Float.valueOf(distanceValue));

		// pushes the character back into the buffer
		reader.unread((int) c);
	}

	private final float leafWeight(Node leaf) {
		Node current = leaf;
		float weight = 0.0f;

		// Cycling until the root has been found (parent = null)
		while (current != null) {
			weight += current.getDistance() / current.getDescendentLeaves();

			current = current.getParent();
		}

		return weight;
	}
}
