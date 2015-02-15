package com.salsaw.salsa.cli;

public class ClustalParameters {

	// CONSTANTS
	private static final String ARGUMENTS_START_SYMBOL = "-";
	private static final String ARGUMENTS_ASSING_SYMBOL = "=";

	// FLAG SETTINGS
	private static final String NEIGHBOUR_JOINING_TREE = "TREE";

	// OPTIONS KEYS
	private static final String OUPUT_KEY = "OUTPUT";

	// FIELDS
	private boolean calculatePhylogeneticTree;
	private ClustalOputputFormat oputputFormat = ClustalOputputFormat.FASTA;

	// GET/SET
	public void setCalculatePhylogeneticTree(boolean value) {
		this.calculatePhylogeneticTree = value;
	}

	// METHODS
	public String generateClustalArguments() {
		StringBuilder clustalCallBuilder = new StringBuilder();

		// Set output format
		appendParameter(clustalCallBuilder, OUPUT_KEY, oputputFormat.toString());

		if (this.calculatePhylogeneticTree == true) {
			appendBolleanParameter(clustalCallBuilder, NEIGHBOUR_JOINING_TREE);
		}

		return clustalCallBuilder.toString();
	}

	private StringBuilder appendBolleanParameter(StringBuilder stringBuilder,
			String value) {
		stringBuilder.append(ARGUMENTS_START_SYMBOL);
		stringBuilder.append(value);
		stringBuilder.append(" ");
		return stringBuilder;
	}

	private StringBuilder appendParameter(StringBuilder stringBuilder,
			String key, String value) {
		stringBuilder.append(ARGUMENTS_START_SYMBOL);
		stringBuilder.append(key);
		stringBuilder.append(ARGUMENTS_ASSING_SYMBOL);
		stringBuilder.append(value);
		stringBuilder.append(" ");
		return stringBuilder;
	}
}
