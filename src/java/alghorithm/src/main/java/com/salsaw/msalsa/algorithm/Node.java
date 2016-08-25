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

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public final class Node {

	// FIELDS
	private String name;
	private Node left;
	private Node right;
	private Node parent;

	private double distance;
	private int descendantLeaves;

	/**
	 * It represent the sum of distances between this node and its descendant
	 * leaves
	 */
	private double distancesSum;
	private double difference;

	// CONSTRUCTORS
	public Node(final String name, final Node left, final Node right, final Node parent, final double distance) {

		this.name = name;
		this.left = left;
		this.right = right;
		this.parent = parent;

		this.distance = distance;

		// In this way, descendentLeafs is correct
		setRight(right);
	}

	// GET/SET
	public final String getName() {
		return this.name;
	}

	public final Node getLeft() {
		return this.left;
	}

	public final Node getRight() {
		return this.right;
	}

	public final Node getParent() {
		return this.parent;
	}

	public final Node getBrother() throws SALSAException {
		if (this.parent != null) {
			if (this.parent.left == this) {
				return this.parent.right;
			} else if (this.parent.right == this) {
				return this.parent.left;
			} else {
				throw new SALSAException(
						"Error: two node brothers have a different parent.");
			}
		}
		return null;
	}

	public final double getDistance() {
		return this.distance;
	}

	public final int getDescendentLeaves() {
		return this.descendantLeaves;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final void setParent(final Node parent) {
		this.parent = parent;
	}

	public final void setDistance(final double distance) {
		this.distance = distance;
	}

	public final void setLeft(final Node left) {
		this.left = left;

		this.descendantLeaves = 0;
		this.distancesSum = 0;
		if (this.left != null) {
			this.descendantLeaves = this.left.descendantLeaves;
			this.distancesSum = this.left.distancesSum + this.left.distance
					* this.left.descendantLeaves;
		}
		if (this.right != null) {
			this.descendantLeaves += this.right.descendantLeaves;
			this.distancesSum += this.right.distancesSum + this.right.distance
					* this.right.descendantLeaves;
		}

		if (this.descendantLeaves == 0) {
			// The node is a leaf itself
			this.descendantLeaves = 1;
		}
	}

	public final void setRight(final Node right) {
		this.right = right;

		this.descendantLeaves = 0;
		this.distancesSum = 0;
		if (this.right != null) {
			this.descendantLeaves = this.right.descendantLeaves;
			this.distancesSum = this.right.distancesSum + this.right.distance
					* this.right.descendantLeaves;
		}
		if (this.left != null) {
			this.descendantLeaves += this.left.descendantLeaves;
			this.distancesSum += this.left.distancesSum + this.left.distance
					* this.left.descendantLeaves;
		}

		if (this.descendantLeaves == 0) {
			// The node is a leaf itself
			this.descendantLeaves = 1;
		}
	}

	// METHODS
	public final boolean leaf() {
		return (left == null && right == null);
	}

	/**
	 * It returns the best candidate for the role of root. The best root is the
	 * one that minimize the difference between left and right means.
	 * Left[Right] mean is the mean of distances from left[right] leaves to the
	 * root. Left leaves are the leaves in the left sub tree, and right leaves
	 * the others.
	 *
	 * Variable totalNumberOfLeafs is the total amount of leaves in all the tree
	 * and parentLeftSum is the sum of distances between parent's left leaves
	 * and parent himself.
	 * 
	 * @param totalNumberOfLeafs
	 * @return
	 * @throws SALSAException
	 */
	public final Node calculatePositionOfRoot(final int totalNumberOfLeafs)
			throws SALSAException {
		return calculatePositionOfRoot(totalNumberOfLeafs, 0.0f);
	}

	/**
	 * It returns the best candidate for the role of root. The best root is the
	 * one that minimize the difference between left and right means.
	 * Left[Right] mean is the mean of distances from left[right] leaves to the
	 * root. Left leaves are the leaves in the left sub tree, and right leaves
	 * the others.
	 *
	 * Variable totalNumberOfLeafs is the total amount of leaves in all the tree
	 * and parentLeftSum is the sum of distances between parent's left leaves
	 * and parent himself.
	 * 
	 * @param totalNumberOfLeafs
	 * @param parentLeftSum
	 * @return
	 * @throws SALSAException
	 */
	public final Node calculatePositionOfRoot(final int totalNumberOfLeafs,
			final double parentLeftSum) throws SALSAException {
		Node brother = getBrother();
		int numberOfLeftLeaves = totalNumberOfLeafs - this.descendantLeaves;

		// Distances of brother's descendant leaves from parent
		double d = 0;
		if (brother != null) {
			d = brother.distancesSum + brother.distance
					* brother.descendantLeaves;
		}

		// Distances of all left leaves from parent
		d += parentLeftSum;

		// Distances of all left leaves from the current node
		d += this.distance * numberOfLeftLeaves;

		double leftMean = 0.0f;
		if (numberOfLeftLeaves != 0) {
			leftMean = d / numberOfLeftLeaves;
		}

		if (this.left != null) {
			Node rootLeft = this.left.calculatePositionOfRoot(
					totalNumberOfLeafs, d);
			if (rootLeft != null) {
				return rootLeft;
			}
		}
		if (this.right != null) {
			Node rootRight = this.right.calculatePositionOfRoot(
					totalNumberOfLeafs, d);
			if (rootRight != null) {
				return rootRight;
			}
		}

		double currentDifference = leftMean
				- (this.distancesSum / this.descendantLeaves);
		if (currentDifference == 0
				|| (currentDifference > 0 && currentDifference < 2 * this.distance)) {
			this.difference = currentDifference;
			return this;
		}

		return null;
	}

	/**
	 * It generates a new node and changes the tree in order to let him become
	 * the new root. It has to be invoked on the node returned by
	 * valculatePositionOfRoot.
	 * 
	 * @return
	 * @throws SALSAException
	 */
	public final Node addRoot() throws SALSAException {
		if (this.parent != null) {
			double newDistance = this.difference / 2;
			double newDistanceParent = this.distance - newDistance;

			Node root = new Node("ROOT", parent, this, null, 0.0f);
			if (this.parent.left == this) {
				this.parent.left = root;
			} else {
				this.parent.right = root;
			}
			this.parent = root;
			this.distance = newDistance;

			root.left.invertNode(root, newDistanceParent);
			root.calculateDescendantLeaves();

			return root;
		} else {
			return this;
		}
	}

	// PRIVATE METHODS
	/**
	 * Right now, newParent is a son of the current node. The method invert the
	 * current parent with newParent and do a recursive call.
	 * 
	 * @throws SALSAException
	 */
	private void invertNode(final Node newParent, final double newDistance)
			throws SALSAException {
		Node oldParent = this.parent;
		double oldDistance = this.distance;
		this.parent = newParent;
		this.distance = newDistance;

		if (this.left == newParent) {
			this.left = oldParent;
		} else if (this.right == newParent) {
			this.right = oldParent;
		} else {
			throw new SALSAException(
					"Error while trying to invert parent with son in the tree.");
		}

		if (oldParent != null) {
			// Recursive call on old parent
			oldParent.invertNode(this, oldDistance);
		}
	}

	/**
	 * Calculate the number of descendant leaves of the current node and all the
	 * descendant nodes. Called on the root of a tree, it updates the internal
	 * variable descendantLeaves on all the nodes of the tree.
	 * 
	 * @return
	 */
	private int calculateDescendantLeaves() {
		this.descendantLeaves = 0;
		if (this.left != null) {
			this.descendantLeaves = this.left.calculateDescendantLeaves();
		}
		if (this.right != null) {
			this.descendantLeaves += this.right.calculateDescendantLeaves();
		}

		if (this.descendantLeaves == 0) {
			// The node is a leaf itself
			this.descendantLeaves = 1;
		}
		return this.descendantLeaves;
	}

}
