package com.salsaw.salsa.algorithm;

import com.salsaw.salsa.algorithm.exceptions.SALSAException;

public class GAP {
	// FIELDS
	private int row;
	private int begin;
	private int end;
	private int sequencesLength;
	
	private GAP previous;
	private GAP next;
	
	// CONSTRUCTOR
	public GAP(int row, int begin, int sequencesLength, GAP previous, GAP next) throws SALSAException
	{
		this(row,begin, sequencesLength, previous, next, 1);
	}
	
	public GAP(int row, int begin, int sequencesLength, GAP previous, GAP next, int length) throws SALSAException
	{
		this.end = begin + length - 1;

		if (end>sequencesLength-1){
			throw new SALSAException("Error: GAP exceed the end of the sequence");
		}
	}
}
