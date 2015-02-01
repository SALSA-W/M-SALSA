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
public final class Node {

	// FIELDS
	private String name;
	private Node left;
	private Node right;
	private Node parent;

	private float distance;
	private int descendantLeaves;

	/**
	 * It represent the sum of distances between this node and its descendant
	 * leaves
	 */
	private float distancesSum;
	private float difference;

	// CONSTRUCTORS
	public Node(String name, Node left, Node right, Node parent, float distance) {

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
	
	public final Node getBrother() throws SALSAException{
		if (this.parent!=null){
			if (this.parent.left==this){
				return this.parent.right;
			}
			else if (this.parent.right==this){
				return this.parent.left;
			}
			else{
				throw new SALSAException("Error: two node brothers have a different parent.");
			}
		}
		return null;
	}
	

	public final float getDistance() {
		return this.distance;
	}

	public final int getDescendentLeaves() {
		return this.descendantLeaves;
	}

	public final void setName(String name) {
		this.name = name;
	}
	
	public final  void setParent(Node parent){
		this.parent=parent;
	}

	public final  void setDistance(float distance){
		this.distance=distance;
	}

	public final void setLeft(Node left) {
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

	public final void setRight(Node right) {
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

	public final Node calculatePositionOfRoot(int insertedSequences) {
		// TODO Auto-generated method stub
		return null;
	}

	public final Node addRoot() throws SALSAException {
		if (this.parent!=null){
			float newDistance=this.difference/2;
			float newDistanceParent=this.distance-newDistance;

			Node root= new Node("ROOT",parent, this, null, 0.0f);
			if (this.parent.left==this){
				this.parent.left=root;
			}
			else{
				this.parent.right=root;
			}
			this.parent=root;
			this.distance=newDistance;

			root.left.invertNode(root,newDistanceParent);
			root.calculateDescendantLeaves();

			return root;
		}
		else{
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
	private final void invertNode(Node newParent, float newDistance)
			throws SALSAException {
		Node oldParent = this.parent;
		float oldDistance = this.distance;
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
	private final int calculateDescendantLeaves() {
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
