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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.beust.jcommander.JCommander;
import com.salsaw.salsa.algorithm.Alignment;
import com.salsaw.salsa.algorithm.LocalSearch;
import com.salsaw.salsa.algorithm.SubstitutionMatrix;
import com.salsaw.salsa.algorithm.exceptions.SALSAException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		SalsaParameters salsaParameters = new SalsaParameters();
		JCommander commands = new JCommander(salsaParameters);

		try {
			commands.parse(args);

			String phylogeneticTreeFilePath = salsaParameters.getPhylogeneticTreeFile();
			String alignmentFilePath = salsaParameters.getInputFile();
			if (salsaParameters.getClustalPath() != null &&
				salsaParameters.getPhylogeneticTreeFile() == null) {
				ClustalFileMapper clustalFileMapper = new ClustalFileMapper(
						salsaParameters.getInputFile());
				callClustal(salsaParameters.getClustalPath(), clustalFileMapper);
				alignmentFilePath = clustalFileMapper.getAlignmentFilePath();
				phylogeneticTreeFilePath = clustalFileMapper.getTreeFilePath();
			}

			SubstitutionMatrix matrix = new SubstitutionMatrix(
					salsaParameters.getScoringMatrix(),
					salsaParameters.getGEP());

			Alignment alignment = new Alignment(alignmentFilePath,
					phylogeneticTreeFilePath, matrix, salsaParameters.getGOP(),
					salsaParameters.getTerminalGAPsStrategy());

			LocalSearch localSearch = new LocalSearch(alignment, salsaParameters.getGamma(),
					salsaParameters.getMinIterations(),
					salsaParameters.getProbabilityOfSplit());

			alignment = localSearch.execute();
			alignment.save(salsaParameters.getOutputFile());

		} catch (Exception e) {
			e.printStackTrace();
			commands.usage();
		}
	}

	public static void callClustal(String clustalPath,
			ClustalFileMapper clustalFileMapper) throws IOException,
			InterruptedException, SALSAException {

		// Create parameters
		ClustalParameters clustalParameters = new ClustalParameters();
		clustalParameters.setCalculatePhylogeneticTree(true);

		// Get program path to execute
		List<String> clustalProcessCommands = new ArrayList<String>();
		clustalProcessCommands.add(clustalPath);
		clustalProcessCommands.add(clustalFileMapper.getInputFilePath());
		clustalParameters.generateClustalArguments(clustalProcessCommands);

		// http://www.rgagnon.com/javadetails/java-0014.html
		ProcessBuilder builder = new ProcessBuilder(clustalProcessCommands);
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
			throw new SALSAException("Failed clustal call");
		}
	}
}
