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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;
import com.salsaw.msalsa.cli.App;
import com.salsaw.msalsa.cli.exceptions.SALSAClustalException;

public class ClustalWManager extends ClustalManager {
	// CONSTANTS	
	static final Logger logger = LogManager.getLogger(ClustalWManager.class);
	
	// flag settings
	private static final String NEIGHBOUR_JOINING_TREE = "TREE";
	/**
	 * use Kimura's correction
	 */
	private static final String KIMURA_CORRECTION = "KIMURA";
	/**
	 * do full multiple alignment
	 */
	private static final String EXECUTE_MULTIPLE_ALIGNMENT = "ALIGN";	

	// keys of options
	/**
	 * sequence alignment file name
	 */
	private static final String OUPUT_FILE  = "OUTFILE";
	
	/**
	 * Set output format
	 */
	private static final String OUPUT_FORMAT = "OUTPUT";
	
	private static final String INPUT_FILE = "INFILE";
	
	private static final String TREE_OUTPUT_FORMAT = "OUTPUTTREE";
	
	private static final String TREE_CLUSTERING = "CLUSTERING";

	// METHODS
	
	@Override
	protected String createParameterEqualsCommand(String key, String value) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ARGUMENTS_START_SYMBOL);
		stringBuilder.append(key);
		stringBuilder.append(ARGUMENTS_ASSING_SYMBOL);
		stringBuilder.append(value);
		return stringBuilder.toString();
	}	
	
	@Override
	protected String createBooleanParameterCommand(String value) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ARGUMENTS_START_SYMBOL);
		stringBuilder.append(value);
		return stringBuilder.toString();
	}
	
	@Override
	public List<String> generateClustalArguments(List<String> commands) {			
		// Set output format
		commands.add(createParameterEqualsCommand(OUPUT_FORMAT, super.getOputputFormat().toString()));
		commands.add(createBooleanParameterCommand(EXECUTE_MULTIPLE_ALIGNMENT));
		
		return commands;
	}	

	@Override
	public void callClustal(String clustalPath,
			ClustalFileMapper clustalFileMapper) throws IOException,
			InterruptedException, SALSAException {
		// Create the name of output files
		String inputFileName = FilenameUtils.getBaseName(clustalFileMapper.getInputFilePath());
		String inputFileFolderPath = FilenameUtils.getFullPath(clustalFileMapper.getInputFilePath());
		Path alignmentFilePath = Paths.get(inputFileFolderPath, inputFileName + "-aln.fasta");
		clustalFileMapper.setAlignmentFilePath(alignmentFilePath.toString());		
		
		// Get program path to execute
		List<String> clustalProcessCommands = new ArrayList<String>();
		clustalProcessCommands.add(escapePath(clustalPath));
		clustalProcessCommands.add(escapePath(clustalFileMapper.getInputFilePath()));
		// Set output file name			
		clustalProcessCommands.add(createParameterEqualsCommand(OUPUT_FILE, escapePath(clustalFileMapper.getAlignmentFilePath())));
		generateClustalArguments(clustalProcessCommands);

		callClustalWProcess(clustalProcessCommands, clustalFileMapper);
	}	
	
	private void generateTreeArguments(ClustalFileMapper clustalFileMapper, List<String> commands)
	{
		// Create tree starting from alignment produced
		commands.add(createParameterEqualsCommand(INPUT_FILE, escapePath(clustalFileMapper.getAlignmentFilePath())));
		commands.add(createBooleanParameterCommand(NEIGHBOUR_JOINING_TREE));		
		
		// Use default parameters
		commands.add(createParameterEqualsCommand(TREE_CLUSTERING, ClustalWClusteringMethod.NEIGHBOR_JOINING.toString()));
		commands.add(createParameterEqualsCommand(TREE_OUTPUT_FORMAT, ClustalWTreeOputputFormat.DISTANCE.toString()));
	}
	
	public final void generateTree(String clustalPath,
			ClustalFileMapper clustalFileMapper) throws IOException, SALSAException, InterruptedException{			
		// Get program path to execute
		List<String> clustalProcessCommands = new ArrayList<String>();
		clustalProcessCommands.add(escapePath(clustalPath));
		generateTreeArguments(clustalFileMapper, clustalProcessCommands);

		callClustalWProcess(clustalProcessCommands, clustalFileMapper);
	}

	private final void callClustalWProcess(List<String> clustalProcessCommands, ClustalFileMapper clustalFileMapper)
			throws IOException, SALSAException, InterruptedException {
		final Process process = Runtime.getRuntime().exec(composeProcessCall(clustalProcessCommands));

		try {
			try (InputStream is = process.getInputStream()) {
				try (InputStreamReader isr = new InputStreamReader(is)) {
					try (BufferedReader br = new BufferedReader(isr)) {
						String line;

						Pattern pattern = Pattern.compile("\\[(.*?)\\]");

						while ((line = br.readLine()) != null) {

							if (App.IS_DEBUG == true) {
								// Print clustal output only on debug
								logger.debug(line);
							}

							if (line.indexOf("Phylogenetic tree file created:") >= 0) {
								// Read the path of Phylogenetic tree file
								// created from output
								// http://stackoverflow.com/a/4006273
								Matcher matcher = pattern.matcher(line);
								if (matcher.find() == false) {
									throw new SALSAException("Unable to read the path of phylognetic tree file");
								}
								clustalFileMapper.setPhylogeneticTreeFile(matcher.group(1));
							}

							if (line.indexOf("Guide tree file created:") >= 0) {
								Matcher matcher = pattern.matcher(line);
								if (matcher.find() == false) {
									throw new SALSAException("Unable to read the path of guide tree file");
								}
								clustalFileMapper.setGuideTreeFilePath(matcher.group(1));
							}

							if (line.indexOf("Fasta-Alignment file created") >= 0) {
								Matcher matcher = pattern.matcher(line);
								if (matcher.find() == false) {
									throw new SALSAException("Unable to read the path of alignment file");
								}
								clustalFileMapper.setAlignmentFilePath(matcher.group(1));
							}

							if (line.indexOf("Distance matrix file created:") >= 0) {
								Matcher matcher = pattern.matcher(line);
								if (matcher.find() == false) {
									throw new SALSAException("Unable to read the path of distance matrix file");
								}
								clustalFileMapper.setDistanceMatrixFilePath(matcher.group(1));
							}
						}
					}
				}
			}
			
			manageClustalProcessError(process, ClustalType.CLUSTAL_W_NAME);

			// The buffers must be consumed before wait or process will freeze
			int exitValue  = process.waitFor();
			if (exitValue != 0){
				// The process has errors not read on error stream
				throw new SALSAClustalException(ClustalType.CLUSTAL_W_NAME + " call failed");
			}
		} finally {
			// Ensure to kill subprocesses
			process.destroy();
		}
	}
}
