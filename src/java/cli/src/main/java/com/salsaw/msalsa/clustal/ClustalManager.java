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
package com.salsaw.msalsa.clustal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.salsaw.msalsa.algorithm.Constants;
import com.salsaw.msalsa.algorithm.exceptions.SALSAException;
import com.salsaw.msalsa.cli.exceptions.SALSAClustalException;

public abstract class ClustalManager {

	// CONSTANTS
	protected static final String ARGUMENTS_START_SYMBOL = "-";
	protected static final String ARGUMENTS_ASSING_SYMBOL = "=";

	// FIELDS
	private ClustalWOputputFormat oputputFormat = ClustalWOputputFormat.FASTA;	

	// GET/SET
	public final ClustalWOputputFormat getOputputFormat() {
		return this.oputputFormat;
	}
	
	public final void setOputputFormat(final ClustalWOputputFormat oputputFormat) {
		this.oputputFormat = oputputFormat;
	}

	// METHODS
	protected abstract List<String> generateClustalArguments(List<String> commands);
	
	public abstract void callClustal(String clustalPath, ClustalFileMapper clustalFileMapper) 
			throws IOException,	InterruptedException, SALSAException;

	protected abstract String createBooleanParameterCommand(String value);

	protected abstract String createParameterEqualsCommand(String key, String value);
	
	protected final String escapePath(String pathInput){
		// Add quotes to manage path with spaces 
		return '"' + pathInput + '"';
	}
	
	protected final String composeProcessCall(Iterable<String> clustalProcessCommands) {
		// Compose process call for Runtime.getRuntime().exec
		// avoid ProcessBuilder due to quotes escape issues
		StringBuilder processCall = new StringBuilder();
		for (String clustalProcessCommand : clustalProcessCommands) {
			processCall.append(clustalProcessCommand);
			processCall.append(" ");
		}

		return processCall.toString();
	}
	
	/**
	 * Check if process has content in error stream.
	 * Throw {@link SALSAClustalException} if errors are present.
	 * 
	 * @throws IOException 
	 * @throws SALSAException Contains the messages print from the error stream
	 */
	public static final void manageClustalProcessError(Process process, String clustalName) throws SALSAClustalException, IOException{		 	
		// Get the error stream of the process and print it
		StringBuilder errorMessage = new StringBuilder("Failed call to ");	
		errorMessage.append(clustalName);
		errorMessage.append(Constants.NEW_LINE);
		
		boolean errorsArePresent = false;
		try (InputStream errorStream = process.getErrorStream()) {
			try (InputStreamReader isError = new InputStreamReader(errorStream)) {
				try (BufferedReader brError = new BufferedReader(isError)) {
					String errorLine;
					while ((errorLine = brError.readLine()) != null) {
						errorMessage.append(errorLine).append(Constants.NEW_LINE);
						errorsArePresent = true;
					}
				}
			}
		}
		
		if (errorsArePresent){
			throw new SALSAClustalException(errorMessage.toString());
		}
	}
	
	/**
	 * Factory method
	 * 
	 * @throws SALSAException 
	 */
	public static final ClustalManager createClustalManager(ClustalType clustalType) throws SALSAException{		
		switch (clustalType) {
		case CLUSTAL_W:
			return new ClustalWManager();
			
		case CLUSTAL_O:
			return new ClustalOmegaManager();
		}
		
		throw new SALSAException("Unable to create a clustal manager of type " + clustalType.toString());		
	}
}
