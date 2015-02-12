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
import java.util.Random;

import com.salsaw.salsa.algorithm.exceptions.SALSAException;

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
	public final Alignment execute() throws SALSAException {
		int lastImprovement=0;
		int iteration=0;
		int positionOfGAP;
		boolean left;
		float split;
		
		//Initialize seed
		Random random = new Random(System.currentTimeMillis());

		while(lastImprovement+minIterations>iteration){
						
			positionOfGAP=random.nextInt(this.numberOfGAPS);
			left=random.nextBoolean();
			split=random.nextFloat();					

			if (split<this.probabiltyOfSplit){
				if (splitAndMove(positionOfGAP, left)){
					lastImprovement=iteration;
				}
			}
			else if (move(positionOfGAP, left)){
				lastImprovement=iteration;
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
	 * @throws SALSAException 
	 */
	private final boolean splitAndMove(int GAPPosition, boolean left) throws SALSAException {
		GAP g= this.GAPS.get(GAPPosition);
		int length= g.getLength();

		if (length > 1){
			
			Random random = new Random();
			int positionOfSplit= g.getBegin() + random.nextInt(length-1);

			// newGAP will not be moved
			GAP newGAP = g.split(positionOfSplit, !left);
			boolean improvement=move(GAPPosition, left, true);
			if (improvement){
				this.GAPS.add(newGAP);
				this.numberOfGAPS++;
			}
			else{
				newGAP.unify();
			}

			return improvement;
		}
		else{ 
			//The GAP's length is one, therefore is not possible to split it
			return move(GAPPosition, left);
		}
	}
}
