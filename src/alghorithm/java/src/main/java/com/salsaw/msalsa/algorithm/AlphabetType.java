package com.salsaw.msalsa.algorithm;

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
}
