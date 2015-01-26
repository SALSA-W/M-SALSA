package com.salsaw.salsa.algorithm;

public class GAP {
	// FIELDS
	private int row;
	private int begin;
	private int end;
	private int sequencesLength;
	
	private GAP previous;
	private GAP next;
	
	// CONSTRUCTOR
	public GAP(int row, int begin, int sequencesLength, GAP previous, GAP next)
	{
		this(row,begin, sequencesLength, previous, next, 1);
	}
	
	public GAP(int row, int begin, int sequencesLength, GAP previous, GAP next, int length)
	{
		
	}
}
