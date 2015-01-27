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

public final class GAP {
	// FIELDS
	private final int row;
	private final int begin;
	private int end;
	private final int sequencesLength;

	private final GAP previous;
	private GAP next;

	// CONSTRUCTOR
	public GAP(int row, int begin, int sequencesLength, GAP previous, GAP next)
			throws SALSAException {
		this(row, begin, sequencesLength, previous, next, 1);
	}

	public GAP(int row, int begin, int sequencesLength, GAP previous, GAP next,
			int length) throws SALSAException {
		this.row = row;
		this.begin = begin;
		this.sequencesLength = sequencesLength;
		this.end = begin + length - 1;

		this.previous = previous;
		this.next = next;

		if (end > sequencesLength - 1) {
			throw new SALSAException(
					"Error: GAP exceed the end of the sequence");
		}
	}

	// GET / SET
	public int getRow() {
		return this.row;
	}

	public int getBegin() {
		return this.begin;
	}

	public int getLength() {
		return this.end - this.begin + 1;
	}

	public int getEnd() {
		return this.end;
	}

	public void setNext(GAP next) {
		this.next = next;
	}

	// METHODS
	/**
	 * Increase the length of the GAP of one element (if it is possible,
	 * otherwise it throws an exception)
	 * 
	 * @throws SALSAException
	 */
	public void extend() throws SALSAException {
		this.end++;

		if (end > sequencesLength - 1) {
			throw new SALSAException(
					"Error: GAP exceed the end of the sequence");
		}
	}

	/**
	 * True if the GAP is terminal, that is it is in the beginning or in the end
	 * of a sequence
	 * 
	 * @return
	 */
	public boolean terminalGAP() {
		// TODO Report code from c
		return false;
	}

	/**
	 * Next three methods are used to see if the current GAP is attached to
	 * another one. If that is true, it could be necessary to unify the two GAPS
	 * in order to maintain the consistency with the alignment (two connected
	 * GAPS are considered as one).
	 * 
	 * @return
	 * */
	public boolean nearPreviousGAP() {
		// TODO Report code from c
		return false;
	}

	public boolean nearNextGAP() {
		// TODO Report code from c
		return false;
	}

	public boolean nearAnotherGAP() {
		// TODO Report code from c
		return false;
	}

	/**
	 * Next two methods move the GAP of one position.
	 */
	public void moveLeft() {
		// TODO Report code from c
	}

	public void moveRight() {
		// TODO Report code from c
	}

	/**
	 * If there is a GAP g attached to this GAP, the method unifies them,
	 * otherwise it throws an exception.
	 *
	 * The unified gap is stored inside g. Therefore, the GAP g is modified, not
	 * the current one. Current GAP should be deleted by the caller of unify().
	 */
	public void unify() {
		// TODO Report code from c
	}

	/**
	 * The method divide the GAP in two using the indel in position column as
	 * separator. In particular, the first created GAP ends in position column,
	 * the other one begin in the next position. Therefore, column should be
	 * inside the GAP. The method modifies the current GAP and creates another
	 * one that is returned. Variable leftNew specifies if the new GAP is the
	 * one on the left.
	 * 
	 * @return
	 * */
	public GAP split(int column, boolean leftNew) {
		// TODO Report code from c
		return null;
	}
}
