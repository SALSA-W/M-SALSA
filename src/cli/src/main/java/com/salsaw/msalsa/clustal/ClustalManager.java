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

import java.io.IOException;
import java.util.List;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;

public abstract class ClustalManager {

	// CONSTANTS
	protected static final String ARGUMENTS_START_SYMBOL = "-";
	protected static final String ARGUMENTS_ASSING_SYMBOL = "=";

	// FIELDS
	private ClustalWOputputFormat oputputFormat = ClustalWOputputFormat.FASTA;	

	// GET/SET
	public ClustalWOputputFormat getOputputFormat() {
		return this.oputputFormat;
	}
	
	public void setOputputFormat(ClustalWOputputFormat oputputFormat) {
		this.oputputFormat = oputputFormat;
	}

	// METHODS
	protected abstract List<String> generateClustalArguments(List<String> commands);
	
	public abstract void callClustal(String clustalPath, ClustalFileMapper clustalFileMapper) 
			throws IOException,	InterruptedException, SALSAException;

	protected abstract String createBolleanParameterCommand(String value);

	protected abstract String createParameterEqualsCommand(String key, String value);
	
	/**
	 * Factory method
	 * @throws SALSAException 
	 */
	public static ClustalManager CreateClustalManager(ClustalType clustalType) throws SALSAException{		
		switch (clustalType) {
		case CLUSTAL_W:
			return new ClustalWManager();
			
		case CLUSTAL_O:
			return new ClustalOmegaManager();
		}
		
		throw new SALSAException("Unable to create a clustal manager of type " + clustalType.toString());		
	}
}
