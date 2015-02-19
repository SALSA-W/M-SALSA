package com.salsaw.salsa.cli;

public enum ClustalErrorCode {
	/**
	 * bad command line option
	 */
	BAD_COMMAND_LINE_OPTION(1),
	/**
	 * cannot open sequence file
	 */
	CANNOT_OPEN_SEQUENCE_FILE(2),
	/**
	 * wrong format in sequence file
	 */
	WRONG_FORMAT_SEQUENCE_FILE(3),
	/**
	 * sequence file contains only 1 sequence (for multiple alignments)
	 */
	INPUT_ONLY_1_SEQUENCE(4);

	private final int code;

	private ClustalErrorCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
