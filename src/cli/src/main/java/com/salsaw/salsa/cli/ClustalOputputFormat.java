package com.salsaw.salsa.cli;

public enum ClustalOputputFormat {
	/**
	 * The default Clustal output format
	 */
	CLUSTAL("CLUSTAL"),
	GCG("GCG"),
	GDE("GDE"),
	PHYLIP("PHYLIP"),
	PIR("PIR"),
	NEXUS("NEXUS"),
	FASTA("FASTA"),
    ;

    private final String text;

    private ClustalOputputFormat(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
