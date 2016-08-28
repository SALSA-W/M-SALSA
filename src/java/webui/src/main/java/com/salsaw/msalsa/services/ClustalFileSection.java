package com.salsaw.msalsa.services;

public class ClustalFileSection {
	private String[] sequencesHeaders;
	private String[] sequences;
	
	public ClustalFileSection(String[] sequencesHeaders, String[] sequences) throws IllegalArgumentException{
		if (sequencesHeaders == null){
			throw new IllegalArgumentException("sequencesHeaders");
		}
		if (sequences == null){
			throw new IllegalArgumentException("sequences");
		}
		
		this.sequencesHeaders = sequencesHeaders;
		this.sequences = sequences;
	}
	
	public String[] getSequencesHeaders() {
		return sequencesHeaders;
	}
	
	public String[] getSequences() {
		return sequences;
	}
}
