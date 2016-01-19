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

import java.util.ArrayList;
import java.util.Random;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;

/**
 * Manage and perform the local search
 * 
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public final class LocalSearch {
	// FILEDS
	private final Alignment align;
	private final ArrayList<GAP> GAPS;
	private int numberOfGAPS;
	private final int gamma;
	private final int minIterations;
	private final float probabiltyOfSplit;

	private final TerminalGAPsStrategy terminal;

	// CONSTRUCTOR
	public LocalSearch(Alignment alignment, int gamma, int minIterations,
			float probabiltyOfSplit) {
		this.align = alignment;
		this.gamma = gamma;
		this.minIterations = minIterations;
		this.probabiltyOfSplit = probabiltyOfSplit;
		this.GAPS = alignment.getGAPS();
		this.numberOfGAPS = GAPS.size();
		this.terminal = align.getTerminalGAPStrategy();
	}

	// GET / SET
	public final int getNumberOfGAPs() {
		return this.gamma;
	}

	// PUBLIC METHODS
	public final Alignment execute() throws SALSAException {
		int lastImprovement = 0;
		int iteration = 0;
		int positionOfGAP;
		boolean left;
		float split;

		// Initialize seed
		Random random = new Random(System.currentTimeMillis());

		while (lastImprovement + minIterations > iteration) {

			positionOfGAP = random.nextInt(this.numberOfGAPS);
			left = random.nextBoolean();
			split = random.nextFloat();

			if (split < this.probabiltyOfSplit) {
				if (splitAndMove(positionOfGAP, left)) {
					lastImprovement = iteration;
				}
			} else if (move(positionOfGAP, left)) {
				lastImprovement = iteration;
			}

			iteration++;
		}

		return align;
	}

	// PRIVATE METHODS
	/**
	 * GAPPosition is the index of the GAP to be moved (inside GAPS vector). The
	 * method returns TRUE if there were an improvement. Variable left tells the
	 * method the direction of the movement. split tells if there were a split
	 * and this information is used only to choose properly the new position of
	 * the GAP. splitAndMove will manage all the rest.
	 * 
	 * @param GAPPosition
	 * @param left
	 * @return
	 * @throws SALSAException
	 */
	private final boolean move(int GAPPosition, boolean left)
			throws SALSAException {
		return move(GAPPosition, left, false);
	}

	/**
	 * GAPPosition is the index of the GAP to be moved (inside GAPS vector). The
	 * method returns TRUE if there were an improvement. Variable left tells the
	 * method the direction of the movement. split tells if there were a split
	 * and this information is used only to choose properly the new position of
	 * the GAP. splitAndMove will manage all the rest.
	 * 
	 * @param GAPPosition
	 * @param left
	 * @param split
	 * @return
	 * @throws SALSAException
	 */
	private final boolean move(int GAPPosition, boolean left, boolean split)
			throws SALSAException {
		GAP g = this.GAPS.get(GAPPosition);

		// g is terminal and it has to move in the wrong direction (that is,
		// outside the sequence)
		if ((left && (g.getBegin() == 0))
				|| (!left && (g.getEnd() == this.align.getLength() - 1))) {
			return false;
		}

		// Best delta found until now
		float deltaMax;

		// It indicates the iteration corresponding to the best delta found
		int bestIterator = 0;

		// Penalty of a GOP in the corresponding row
		float deltaGOP = this.align.getGOP(g.getRow());

		if (split) {
			deltaMax = deltaGOP;
		} else {
			if (this.terminal == TerminalGAPsStrategy.ONLY_GEP
					&& g.terminalGAP()) {
				deltaMax = deltaGOP;
			} else {
				deltaMax = 0.0f;
			}
		}

		// Improvement of the last move
		float delta;

		// Total improvement in current iteration
		float deltaSum = 0.0f;

		boolean finished = false;
		boolean improvement = false;

		// Number of movement done
		int iterator = 0;

		/*
		 * INVARIANT: If finished is false, it is correct to move the GAP of one
		 * position in the given direction because: 1) if the GAP is terminal,
		 * finished is true or the direction of movement is in the opposite way
		 * of the touching border 2) the GAP is not in contact with another GAP
		 * (in that case finished would have been set to true). The only case in
		 * which it is possible if there were a split (the GAP will move in the
		 * opposite direction). Moreover, bestIterator correspond to the best
		 * position tried so far.
		 */
		while (!finished && iterator < this.gamma) {
			// If g is not in the border
			iterator++;

			if (left)
				delta = this.align.moveLeft(g);
			else
				delta = this.align.moveRight(g);

			deltaSum += delta;
			if (g.nearAnotherGAP()) {
				finished = true;

				// The GAP is attached to another one, so a penalty for the GAP
				// opening should be removed
				if (deltaSum+deltaGOP-deltaMax > 0.00001f) {
					g.unify();

					// Delete g and remove it form GAPS vector
					this.GAPS.remove(g);
					g = null;

					this.numberOfGAPS--;

					deltaMax = deltaSum;
					bestIterator = iterator;
					improvement = true;
				}
			} else {
				// It is not near another GAP
				if (g.terminalGAP()) {
					if (this.terminal == TerminalGAPsStrategy.ONLY_GEP) {
						deltaSum += deltaGOP;
					}
					finished = true;
				}

				if (deltaSum-deltaMax > 0.00001f) {
					deltaMax = deltaSum;
					bestIterator = iterator;
					improvement = true;
				}
			}
		}

		// Here iterator is the number of movement done, bestIterator the right
		// amount of movement
		if (left)
			for (; iterator > bestIterator; iterator--) {
				align.goBackToRight(g);
			}
		else
			// Right
			for (; iterator > bestIterator; iterator--) {
				align.goBackToLeft(g);
			}

		return improvement;
	}

	/**
	 * Like move() method, but it split the GAP before moving it
	 * 
	 * @param GAPPosition
	 * @param left
	 * @return
	 * @throws SALSAException
	 */
	private final boolean splitAndMove(int GAPPosition, boolean left)
			throws SALSAException {
		GAP g = this.GAPS.get(GAPPosition);
		int length = g.getLength();

		if (length > 1) {

			Random random = new Random();
			int positionOfSplit = g.getBegin() + random.nextInt(length - 1);

			// newGAP will not be moved
			GAP newGAP = g.split(positionOfSplit, !left);
			boolean improvement = move(GAPPosition, left, true);
			if (improvement) {
				this.GAPS.add(newGAP);
				this.numberOfGAPS++;
			} else {
				newGAP.unify();
			}

			return improvement;
		} else {
			// The GAP's length is one, therefore is not possible to split it
			return move(GAPPosition, left);
		}
	}
}
