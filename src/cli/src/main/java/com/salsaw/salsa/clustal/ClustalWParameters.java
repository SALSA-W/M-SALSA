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

import java.util.List;

public class ClustalWParameters extends ClustalParameters {
	// CONSTANTS

	// FLAG SETTINGS
	private static final String NEIGHBOUR_JOINING_TREE = "TREE";	
	/**
	 * do full multiple alignment
	 */
	private static final String EXECUTE_MULTIPLE_ALIGNMENT = "ALIGN";

	// OPTIONS KEYS
	private static final String OUPUT_KEY = "OUTPUT";

	// FIELDS
	private boolean calculatePhylogeneticTree;

	// GET/SET
	public void setCalculatePhylogeneticTree(boolean value) {
		this.calculatePhylogeneticTree = value;
	}

	// METHODS
	@Override
	public List<String> generateClustalArguments(List<String> commands) {			
		// Set output format
		commands.add(createParameterCommand(OUPUT_KEY, super.getOputputFormat().toString()));
				
		if (this.calculatePhylogeneticTree == true) {
			commands.add(createBolleanParameterCommand(NEIGHBOUR_JOINING_TREE));
		} else{
			commands.add(createBolleanParameterCommand(EXECUTE_MULTIPLE_ALIGNMENT));
		}

		return commands;
	}
}
