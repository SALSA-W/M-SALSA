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

import com.salsaw.msalsa.algorithm.Alignment;
import com.salsaw.msalsa.algorithm.LocalSearch;
import com.salsaw.msalsa.algorithm.SubstitutionMatrix;
import com.salsaw.msalsa.algorithm.exceptions.SALSAException;
import com.salsaw.msalsa.clustal.ClustalFileMapper;
import com.salsaw.msalsa.clustal.ClustalManager;
import com.salsaw.msalsa.clustal.ClustalWManager;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class SalsaAlgorithmExecutor {
	
	private static void validateParameters(SalsaParameters salsaParameters) throws SALSAException
	{
		// VALIDATION
		if (salsaParameters.getClustalPath() == null &&
			salsaParameters.getPhylogeneticTreeFile() == null){
			throw new SALSAException("Required input missing: - clustal path for calculate aligment on input file OR - input files required are aligment and phylogenetic tree");
		}		
		
		if (salsaParameters.getGeneratePhylogeneticTree() == true &&
			salsaParameters.getClustalWPath() == null){
				throw new SALSAException("To calculate the phylogenetic tree the ClustalW path is required");
		}
	}
	
	public static void callClustal(SalsaParameters salsaParameters) throws SALSAException, IOException, InterruptedException {
		validateParameters(salsaParameters);
		
		// PROCESS
		String phylogeneticTreeFilePath = salsaParameters.getPhylogeneticTreeFile();
		String alignmentFilePath = salsaParameters.getInputFile();
		
		ClustalFileMapper clustalFileMapper = null;
		
		if (salsaParameters.getClustalPath() != null &&
			salsaParameters.getPhylogeneticTreeFile() == null) {			
			// Use Clustal to generetate initial aligment
			clustalFileMapper = new ClustalFileMapper(salsaParameters.getInputFile());
			ClustalManager clustalManager = ClustalManager.CreateClustalManager(salsaParameters.getClustalType());		
			clustalManager.callClustal(salsaParameters.getClustalPath(), clustalFileMapper);
			alignmentFilePath = clustalFileMapper.getAlignmentFilePath();
			phylogeneticTreeFilePath = clustalFileMapper.getTreeFilePath();
		}
		else
		{
			// Start from existing alignments file
			clustalFileMapper = new ClustalFileMapper(null);
			clustalFileMapper.setAlignmentFilePath(alignmentFilePath);
			clustalFileMapper.setPhylogeneticTreeFile(phylogeneticTreeFilePath);
		}
		
		SubstitutionMatrix matrix;
		String scoringMatrixName = salsaParameters.getScoringMatrix().toString();
		
		if (salsaParameters.getScoringMatrix() == ScoringMatrix.BLOSUM50 ||
			salsaParameters.getScoringMatrix() == ScoringMatrix.BLOSUM62) {
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
			
		
		if (salsaParameters.getGeneratePhylogeneticTree() == true){
			// Generate phylogenetic tree using ClustalW from SALSA aligment
			clustalFileMapper.setAlignmentFilePath(salsaParameters.getOutputFile());
			ClustalWManager clustalWManager = new ClustalWManager();
			clustalWManager.generateTree(salsaParameters.getClustalWPath(), clustalFileMapper);
		}			
	}
}
