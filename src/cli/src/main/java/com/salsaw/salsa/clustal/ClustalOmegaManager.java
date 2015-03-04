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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

import com.salsaw.salsa.algorithm.exceptions.SALSAException;

public class ClustalOmegaManager extends ClustalManager {
	// CONSTANTS
	
	// keys of options
	private static final String INPUT_FILE = "i";	
	private static final String OUTPUT_FILE = "o";

	/**
	 * MSA output file format (default: fasta)
	 */
	private static final String OUTPUT_FORMAT = "outfmt";
	private static final String GUIDE_TREE_OUTPUT_FILE = "guidetree-out";
	
	// flag settings
	/**
	 * over-writing of already existing files
	 */
	private static final String OVERWITE_OUTPUT_FILE = "force";
	
	// FILEDS
	private ClustalFileMapper clustalFileMapper;
	private ClustalOmegaOputputFormat clustalOmegaOputputFormat = ClustalOmegaOputputFormat.FASTA;
	
	// GET / SET
	public void setClustalFileMapper(ClustalFileMapper clustalFileMapper){
		this.clustalFileMapper = clustalFileMapper;
	}
	
	// METHODS
	@Override
	protected String createBolleanParameterCommand(String value) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ARGUMENTS_START_SYMBOL);
		stringBuilder.append(ARGUMENTS_START_SYMBOL);
		stringBuilder.append(value);
		return stringBuilder.toString();
	}
	
	@Override
	public List<String> generateClustalArguments(List<String> commands) {		
		if (clustalFileMapper == null){
			throw new IllegalArgumentException("clustalFileMapper");
		}
		
		commands.add(createParameterSpaceCommand(INPUT_FILE, this.clustalFileMapper.getInputFilePath()));
		commands.add(createParameterSpaceCommand(OUTPUT_FILE, this.clustalFileMapper.getAlignmentFilePath()));
		commands.add(createParameterEqualsCommand(GUIDE_TREE_OUTPUT_FILE, this.clustalFileMapper.getGuideTreeFilePath()));
		
		if (this.clustalOmegaOputputFormat != ClustalOmegaOputputFormat.FASTA){
			commands.add(createParameterEqualsCommand(OUTPUT_FORMAT, this.clustalOmegaOputputFormat.toString()));
		}
		
		commands.add(createBolleanParameterCommand(OVERWITE_OUTPUT_FILE));
			
			
		return commands;
	}

	@Override
	public void callClustal(String clustalPath,
			ClustalFileMapper clustalFileMapper) throws IOException,
			InterruptedException, SALSAException {
		// Get program path to execute
		List<String> clustalProcessCommands = new ArrayList<String>();
		clustalProcessCommands.add(clustalPath);
		
		// Create the name of output files
		String inputFileName = FilenameUtils.getBaseName(clustalFileMapper.getInputFilePath());
		String inputFileFolderPath = FilenameUtils.getFullPath(clustalFileMapper.getInputFilePath());			
		Path alignmentFilePath = Paths.get(inputFileFolderPath, inputFileName + "-aln.fasta");
		Path guideTreeFilePath = Paths.get(inputFileFolderPath, inputFileName + "-tree.dnd");
		
		// Set inside file mapper
		clustalFileMapper.setAlignmentFilePath(alignmentFilePath.toString());
		clustalFileMapper.setGuideTreeFilePath(guideTreeFilePath.toString());		
		clustalProcessCommands.add(clustalFileMapper.getInputFilePath());		
		setClustalFileMapper(clustalFileMapper);
		
		// Create clustal omega data
		generateClustalArguments(clustalProcessCommands);

		// http://www.rgagnon.com/javadetails/java-0014.html
		ProcessBuilder builder = new ProcessBuilder(clustalProcessCommands);
		final Process process = builder.start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
			
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
		process.waitFor();

		if (process.exitValue() != 0) {
			throw new SALSAException("Failed clustal omega call");
		}		
	}
}
