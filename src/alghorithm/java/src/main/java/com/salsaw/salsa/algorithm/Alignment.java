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
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public final class Alignment {
	// FIELDS
	
	// CONSTRUCTOR
	Alignment(String fileName, String treeFileName, SubstitutionMatrix s, float g, TerminalGAPsStrategy tgs) {
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
	 * They move a GAP of one position and return the improvement in the WSP-Score due to this change
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
	 * Same as moveLeft and moveRight, but the improvement is not returned (not even calculated).
	 * Used to do the backtracking and restore the cell as before the movement of the GAP g.
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
	 * It returns the penalty of adding a GOP in the specified row (it depends on the weight of the row).
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
}
