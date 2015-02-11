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

import java.util.ArrayList;

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
	private final int numberOfGAPS;
	private final int gamma;
	private final int minIterations;
	private final float probabiltyOfSplit;

	private TerminalGAPsStrategy terminal;

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

	public final Alignment execute() {
		// TODO - report from c code
		return null;
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
	 */
	private final boolean move(int GAPPosition, boolean left) {
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
	 */
	private final boolean move(int GAPPosition, boolean left, boolean split) {
		// TODO - report from c code
		return false;
	}

	/**
	 * Like move() method, but it split the GAP before moving it
	 * 
	 * @param GAPPosition
	 * @param left
	 * @return
	 */
	private final boolean splitAndMove(int GAPPosition, boolean left) {
		// TODO - report from c code
		return false;
	}
}
