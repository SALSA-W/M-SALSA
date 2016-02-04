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

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;
import com.salsaw.msalsa.cli.App;
import com.salsaw.msalsa.cli.exceptions.SALSAClustalException;

public class ClustalOmegaManager extends ClustalManager {
	// CONSTANTS
	static final Logger logger = LogManager.getLogger(ClustalOmegaManager.class);

	// keys of options
	private static final String INPUT_FILE = "infile";
	private static final String OUTPUT_FILE = "outfile";

	/**
	 * MSA output file format (default: fasta)
	 */
	private static final String OUTPUT_FORMAT = "outfmt";
	private static final String GUIDE_TREE_OUTPUT_FILE = "guidetree-out";
	private static final String DISTANCE_MATRIX_OUTPUT_FILE = "distmat-out";
	private static final String NUMBER_THREADS = "threads";

	// flag settings
	/**
	 * Verbose output (increases if given multiple times)
	 */
	private static final String VERBOSE = "verbose";
	/**
	 * over-writing of already existing files
	 */
	private static final String OVERWITE_OUTPUT_FILE = "force";
	/**
	 * Use full distance matrix for guide-tree calculation (slow; mBed is
	 * default)
	 */
	private static final String FULL_DISTANCE_MATRIX_CALCULATION = "full";

	// FILEDS
	private ClustalFileMapper clustalFileMapper;
	private ClustalOmegaOputputFormat clustalOmegaOputputFormat = ClustalOmegaOputputFormat.FASTA;

	// GET / SET
	public void setClustalFileMapper(ClustalFileMapper clustalFileMapper) {
		this.clustalFileMapper = clustalFileMapper;
	}

	// METHODS
	@Override
	protected String createBooleanParameterCommand(String value) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ARGUMENTS_START_SYMBOL);
		stringBuilder.append(ARGUMENTS_START_SYMBOL);
		stringBuilder.append(value);
		return stringBuilder.toString();
	}

	@Override
	protected String createParameterEqualsCommand(String key, String value) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ARGUMENTS_START_SYMBOL);
		stringBuilder.append(ARGUMENTS_START_SYMBOL);
		stringBuilder.append(key);
		stringBuilder.append(ARGUMENTS_ASSING_SYMBOL);
		stringBuilder.append(value);
		return stringBuilder.toString();
	}

	@Override
	public List<String> generateClustalArguments(List<String> commands) {
		if (clustalFileMapper == null) {
			throw new IllegalArgumentException("clustalFileMapper");
		}

		commands.add(createParameterEqualsCommand(INPUT_FILE, escapePath(this.clustalFileMapper.getInputFilePath())));
		commands.add(
				createParameterEqualsCommand(OUTPUT_FILE, escapePath(this.clustalFileMapper.getAlignmentFilePath())));

		// Set where tree file will be write
		commands.add(createParameterEqualsCommand(GUIDE_TREE_OUTPUT_FILE,
				escapePath(this.clustalFileMapper.getGuideTreeFilePath())));
		commands.add(createParameterEqualsCommand(DISTANCE_MATRIX_OUTPUT_FILE,
				escapePath(this.clustalFileMapper.getDistanceMatrixFilePath())));
		commands.add(createParameterEqualsCommand(OUTPUT_FORMAT, this.clustalOmegaOputputFormat.toString()));

		// Use all available threads
		commands.add(createParameterEqualsCommand(NUMBER_THREADS,
				String.valueOf(Runtime.getRuntime().availableProcessors())));

		commands.add(createBooleanParameterCommand(FULL_DISTANCE_MATRIX_CALCULATION));
		commands.add(createBooleanParameterCommand(VERBOSE));
		commands.add(createBooleanParameterCommand(OVERWITE_OUTPUT_FILE));

		return commands;
	}

	@Override
	public void callClustal(String clustalPath, ClustalFileMapper clustalFileMapper)
			throws IOException, InterruptedException, SALSAException {
		// Get program path to execute
		List<String> clustalProcessCommands = new ArrayList<String>();
		clustalProcessCommands.add(escapePath(clustalPath));

		// Create the name of output files
		String inputFileName = FilenameUtils.getBaseName(clustalFileMapper.getInputFilePath());
		String inputFileFolderPath = FilenameUtils.getFullPath(clustalFileMapper.getInputFilePath());
		Path alignmentFilePath = Paths.get(inputFileFolderPath, inputFileName + "-aln.fasta");
		Path guideTreeFilePath = Paths.get(inputFileFolderPath, inputFileName + "-tree.dnd");
		Path distanceMatrixFilePath = Paths.get(inputFileFolderPath, inputFileName + "-dist-mat.dst");

		// Set inside file mapper
		clustalFileMapper.setAlignmentFilePath(alignmentFilePath.toString());
		clustalFileMapper.setGuideTreeFilePath(guideTreeFilePath.toString());
		clustalFileMapper.setDistanceMatrixFilePath(distanceMatrixFilePath.toString());
		setClustalFileMapper(clustalFileMapper);

		// Create clustal omega data
		generateClustalArguments(clustalProcessCommands);

		final Process process = Runtime.getRuntime().exec(composeProcessCall(clustalProcessCommands));

		try {			
			try (InputStream is = process.getInputStream()) {
				try (InputStreamReader isr = new InputStreamReader(is)) {
					try (BufferedReader br = new BufferedReader(isr)) {
						String line;
						while ((line = br.readLine()) != null) {
							if (App.IS_DEBUG == true) {
								// Print clustal output only on debug
								logger.debug(line);
							}
						}
					}
				}
			}
			
			manageClustalProcessError(process, ClustalType.CLUSTAL_O_NAME);

			// The buffers must be consumed before wait or process will freeze
			int exitValue  = process.waitFor();
			if (exitValue != 0){
				// The process has errors not read on error stream
				throw new SALSAClustalException(ClustalType.CLUSTAL_O_NAME + " call failed");
			}

		} finally {
			// Ensure to kill subprocesses
			process.destroy();
		}
	}
}
