package com.salsaw.msalsa.algorithm.enums;

public enum AlphabetType {
	DNA("DNA"),
	RNA("RNA"),
	PROTEINS("PROTEINS"),
    ;

    private final String text;

    AlphabetType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
    
    public static final AlphabetType fromString(String alphabetTypeString) {   	 
        for(AlphabetType alphabetType : AlphabetType.values()) {
            if(alphabetType.toString().equalsIgnoreCase(alphabetTypeString)) {
                return alphabetType;
            }
        }
        
        return null;
    }
}
