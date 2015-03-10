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
package com.salsaw.msalsa.cli;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.beust.jcommander.JCommander;
import com.salsaw.msalsa.algorithm.Alignment;
import com.salsaw.msalsa.algorithm.LocalSearch;
import com.salsaw.msalsa.algorithm.SubstitutionMatrix;
import com.salsaw.msalsa.algorithm.exceptions.SALSAException;
import com.salsaw.msalsa.clustal.ClustalFileMapper;
import com.salsaw.msalsa.clustal.ClustalManager;

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
				ClustalManager clustalManager = ClustalManager.CreateClustalManager(salsaParameters.getClustalType());		
				clustalManager.callClustal(salsaParameters.getClustalPath(), clustalFileMapper);
				alignmentFilePath = clustalFileMapper.getAlignmentFilePath();
				phylogeneticTreeFilePath = clustalFileMapper.getTreeFilePath();
			}
			
			SubstitutionMatrix matrix;
			String scoringMatrixName = salsaParameters.getScoringMatrix();
			
			if (scoringMatrixName == "BLOSUM50" ||
				scoringMatrixName == "BLOSUM62") {
				// Load well-known matrix from embedded resources
				try(InputStream stream = App.class.getResourceAsStream("/matrix/" + scoringMatrixName)) {
					matrix = new SubstitutionMatrix(stream, salsaParameters.getGEP());
				}
			}
			else {
				// Load user matrix from file
				try(InputStream stream = new FileInputStream(scoringMatrixName)) {
					matrix = new SubstitutionMatrix(stream, salsaParameters.getGEP());
				}
			}
			
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
	}
}