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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;

public class ClustalWManager extends ClustalManager {
	// CONSTANTS

	// flag settings
	private static final String NEIGHBOUR_JOINING_TREE = "TREE";	
	/**
	 * do full multiple alignment
	 */
	private static final String EXECUTE_MULTIPLE_ALIGNMENT = "ALIGN";

	// keys of options
	private static final String OUPUT_KEY = "OUTPUT";

	// FIELDS
	private boolean calculatePhylogeneticTree;

	// GET/SET
	public void setCalculatePhylogeneticTree(boolean value) {
		this.calculatePhylogeneticTree = value;
	}

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
	protected String createBolleanParameterCommand(String value) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(ARGUMENTS_START_SYMBOL);
		stringBuilder.append(value);
		return stringBuilder.toString();
	}
	
	@Override
	public List<String> generateClustalArguments(List<String> commands) {			
		// Set output format
		commands.add(createParameterEqualsCommand(OUPUT_KEY, super.getOputputFormat().toString()));
				
		if (this.calculatePhylogeneticTree == true) {
			commands.add(createBolleanParameterCommand(NEIGHBOUR_JOINING_TREE));
		} else{
			commands.add(createBolleanParameterCommand(EXECUTE_MULTIPLE_ALIGNMENT));
		}

		return commands;
	}

	@Override
	public void callClustal(String clustalPath,
			ClustalFileMapper clustalFileMapper) throws IOException,
			InterruptedException, SALSAException {
		
		// Get program path to execute
		List<String> clustalProcessCommands = new ArrayList<String>();
		clustalProcessCommands.add(clustalPath);
		clustalProcessCommands.add(clustalFileMapper.getInputFilePath());
		generateClustalArguments(clustalProcessCommands);

		// http://www.rgagnon.com/javadetails/java-0014.html
		ProcessBuilder builder = new ProcessBuilder(clustalProcessCommands);
		builder.redirectErrorStream(true);
		System.out.println(builder.command());
		final Process process = builder.start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
			
		String line;

		Pattern pattern = Pattern.compile("\\[(.*?)\\]");

		while ((line = br.readLine()) != null) {
			System.out.println(line);

			if (line.indexOf("Phylogenetic tree file created:") >= 0) {
				// Read the path of Phylogenetic tree file created from output
				// http://stackoverflow.com/a/4006273
				Matcher matcher = pattern.matcher(line);
				if (matcher.find() == false) {
					throw new SALSAException("Unable to read the path of phylognetic tree file");
				}
				clustalFileMapper.setPhylogeneticTreeFile(matcher.group(1));
			}
			
		    if (line.indexOf("Guide tree file created:") >= 0){
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
		}

		process.waitFor();

		if (process.exitValue() != 0) {
			throw new SALSAException("Failed clustalW2 call");
		}		
	}
}
