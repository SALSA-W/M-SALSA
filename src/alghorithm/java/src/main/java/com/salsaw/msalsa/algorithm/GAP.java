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
 * Class that defines the GAP. Provides methods to move them, put them together and divide (split);
 * <p>
 * Note: methods and class are final for better performance
 * </p>
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public final class GAP {
	// FIELDS
	private final int row;
	private final int sequencesLength;
	private int begin;
	private int end;	

	private GAP previous;
	private GAP next;

	// CONSTRUCTOR
	public GAP(final int row, final int begin, final int sequencesLength, final GAP previous, final GAP next)
			throws SALSAException {
		this(row, begin, sequencesLength, previous, next, 1);
	}

	public GAP(final int row, final int begin, final int sequencesLength, final GAP previous, final GAP next,
			final int length) throws SALSAException {
		this.row = row;
		this.begin = begin;
		this.sequencesLength = sequencesLength;
		this.end = begin + length - 1;

		this.previous = previous;
		this.next = next;

		if (end > sequencesLength - 1) {
			throw new SALSAException("Error: GAP exceed the end of the sequence");
		}
	}

	// GET / SET
	public final int getRow() {
		return this.row;
	}

	public final int getBegin() {
		return this.begin;
	}

	public final int getLength() {
		return this.end - this.getBegin() + 1;
	}

	public final int getEnd() {
		return this.end;
	}

	public final void setNext(final GAP next) {
		this.next = next;
	}

	// METHODS
	/**
	 * Increase the length of the GAP of one element (if it is possible,
	 * otherwise it throws an exception)
	 * 
	 * @throws SALSAException
	 */
	public final void extend() throws SALSAException {
		this.end++;

		if (this.end > this.sequencesLength - 1) {
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
	public final boolean terminalGAP() {
		return (this.begin == 0 || this.end == this.sequencesLength - 1);
	}

	/**
	 * Next three methods are used to see if the current GAP is attached to
	 * another one. If that is true, it could be necessary to unify the two GAPS
	 * in order to maintain the consistency with the alignment (two connected
	 * GAPS are considered as one).
	 * 
	 * @return
	 * */
	public final boolean nearPreviousGAP() {
		return (this.previous != null && this.getBegin() == (this.previous
				.getEnd() + 1));
	}

	public final boolean nearNextGAP() {
		return (this.next != null && this.next.getBegin() == (this.end + 1));
	}

	public final boolean nearAnotherGAP() {
		return nearPreviousGAP() || nearNextGAP();
	}

	/**
	 * Next two methods move the GAP of one position.
	 * 
	 * @throws SALSAException
	 */
	public final void moveLeft() throws SALSAException {
		this.begin--;
		this.end--;

		if (this.begin < 0) {
			throw new SALSAException("Error: border exceeded by a GAP.");
		}
		if (this.previous != null && this.getBegin() < (this.previous.end + 1)) {
			throw new SALSAException("Error: overlapping of two GAPs");
		}
	}

	public final void moveRight() throws SALSAException {
		this.begin++;
		this.end++;

		if (this.end > (this.sequencesLength - 1)) {
			throw new SALSAException("Error: border exceeded by a GAP.");
		}
		if (this.next != null && this.end > (this.next.begin - 1)) {
			throw new SALSAException("Error: overlapping of two GAPs");
		}
	}

	/**
	 * If there is a GAP g attached to this GAP, the method unifies them,
	 * otherwise it throws an exception.
	 *
	 * The unified gap is stored inside g. Therefore, the GAP g is modified, not
	 * the current one. Current GAP should be deleted by the caller of unify().
	 * 
	 * @throws SALSAException
	 */
	public final void unify() throws SALSAException {
		if (nearPreviousGAP()) {
			this.previous.end = this.end;
			this.previous.next = this.next;
			if (this.next != null) {
				this.next.previous = this.previous;
			}
		} else if (nearNextGAP()) {
			this.next.begin = this.begin;
			this.next.previous = this.previous;
			if (this.previous != null) {
				this.previous.next = this.next;
			}
		} else {
			throw new SALSAException(
					"Error while unify two GAPs: there is no GAP close to the current one.");
		}
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
	 * @throws SALSAException
	 * */
	public final GAP split(int column, boolean leftNew) throws SALSAException {
		if (column < begin || column >= end) {
			throw new SALSAException(
					"Error while splitting a GAP: the specified point is not inside the GAP.");
		}
		GAP gap;

		if (leftNew) {
			gap = new GAP(this.row, this.begin, this.sequencesLength,
					this.previous, this, column - this.begin + 1);
			if (this.previous != null) {
				this.previous.next = gap;
			}
			this.previous = gap;
			this.begin = column + 1;
		} else {
			gap = new GAP(this.row, column + 1, this.sequencesLength, this,
					this.next, this.end - column);
			if (this.next != null) {
				this.next.previous = gap;
			}
			this.next = gap;
			this.end = column;
		}

		return gap;
	}
}
