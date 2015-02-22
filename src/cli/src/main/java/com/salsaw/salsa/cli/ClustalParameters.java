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
