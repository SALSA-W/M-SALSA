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
package com.salsaw.salsa.clustal;

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
