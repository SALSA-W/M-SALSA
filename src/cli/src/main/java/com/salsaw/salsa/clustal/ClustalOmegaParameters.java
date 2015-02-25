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

public class ClustalOmegaParameters extends ClustalParameters {
	// CONSTANTS
	private static final String INPUT_FILE = "i";	
	private static final String OUTPUT_FILE = "o";
	/**
	 * MSA output file format (default: fasta)
	 */
	private static final String OUTPUT_FORMAT = "outfmt";
	private static final String GUIDE_TREE_OUTPUT_FILE = "guidetree-out";
	
	// FILEDS
	private ClustalFileMapper clustalFileMapper;
	private ClustalOmegaOputputFormat clustalOmegaOputputFormat = ClustalOmegaOputputFormat.FASTA;
	
	// GET / SET
	public void setClustalFileMapper(ClustalFileMapper clustalFileMapper){
		this.clustalFileMapper = clustalFileMapper;
	}
	
	// METHODS	
	@Override
	public List<String> generateClustalArguments(List<String> commands) {		
		if (clustalFileMapper == null){
			throw new IllegalArgumentException("clustalFileMapper");
		}
		
		commands.add(createParameterCommand(INPUT_FILE, this.clustalFileMapper.getInputFilePath()));
		commands.add(createParameterCommand(OUTPUT_FILE, this.clustalFileMapper.getAlignmentFilePath()));
		commands.add(createParameterCommand(GUIDE_TREE_OUTPUT_FILE, this.clustalFileMapper.getGuideTreeFilePath()));
		
		if (this.clustalOmegaOputputFormat != ClustalOmegaOputputFormat.FASTA){
			commands.add(createParameterCommand(OUTPUT_FORMAT, this.clustalOmegaOputputFormat.toString()));
		}
			
		return commands;
	}

}
