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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

import com.salsaw.msalsa.algorithm.Alignment;
import com.salsaw.msalsa.algorithm.Alphabet;
import com.salsaw.msalsa.algorithm.Constants;
import com.salsaw.msalsa.algorithm.DistanceMatrix;
import com.salsaw.msalsa.algorithm.LocalSearch;
import com.salsaw.msalsa.algorithm.SubstitutionMatrix;
import com.salsaw.msalsa.algorithm.enums.AlphabetType;
import com.salsaw.msalsa.algorithm.enums.EmbeddedScoringMatrix;
import com.salsaw.msalsa.algorithm.enums.MatrixSerie;
import com.salsaw.msalsa.algorithm.exceptions.SALSAException;
import com.salsaw.msalsa.cli.exceptions.SALSAParameterException;
import com.salsaw.msalsa.clustal.ClustalFileMapper;
import com.salsaw.msalsa.clustal.ClustalManager;
import com.salsaw.msalsa.clustal.ClustalType;
import com.salsaw.msalsa.clustal.ClustalWManager;
/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class SalsaAlgorithmExecutor {
	
	public static final String M_SALSA_HEADER = "MSALSA";
	private static final String SALSA_ALIGMENT_SUFFIX = "-" + M_SALSA_HEADER + "-aln";
	public static final String SALSA_ALIGMENT_FASTA_SUFFIX = SALSA_ALIGMENT_SUFFIX + Constants.FASTA_FILE_EXSTENSION;
	public static final String SALSA_ALIGMENT_CLUSTAL_SUFFIX = "-" + M_SALSA_HEADER + "-aln" + Constants.CLUSTAL_FILE_EXSTENSION;
	public static final String SALSA_TREE_SUFFIX = "-" + M_SALSA_HEADER + "-aln.ph";
	
	public static final String AUTHOR_ALESSANDRO_DANIELE = "Alessandro Daniele";
	public static final String AUTHOR_LINK_ALESSANDRO_DANIELE = "https://www.linkedin.com/pub/alessandro-daniele/31/b1a/280";
	public static final String AUTHOR_FABIO_CESARATO = "Fabio Cesarato";
	public static final String AUTHOR_LINK_FABIO_CESARATO = "https://it.linkedin.com/pub/fabio-cesarato/4b/584/255/en";
	public static final String AUTHOR_ANDREA_GIRALDIN = "Andrea Giraldin";
	public static final String AUTHOR_LINK_ANDREA_GIRALDIN = "https://it.linkedin.com/pub/andrea-giraldin/30/452/121";
	
	private static void validateParameters(SalsaParameters salsaParameters) throws SALSAException
	{
		// VALIDATION
		if (salsaParameters.getClustalWPath() == null && 
			salsaParameters.getClustalOmegaPath() == null &&
			salsaParameters.getPhylogeneticTreeFile() == null) {
			throw new SALSAParameterException("Required input missing: - clustal path ("
					+ SalsaParameters.CLUSTAL_W_PATH + " or " + SalsaParameters.CLUSTAL_O_PATH
					+ ") for calculate aligment on input file OR - input files required are aligment and phylogenetic tree");
		}	
		
		if (salsaParameters.getGeneratePhylogeneticTree() == true &&
			salsaParameters.getClustalWPath() == null){
				throw new SALSAParameterException("To calculate the phylogenetic tree the ClustalW path is required");
		}
		
		if (salsaParameters.getMatrixSerie() == MatrixSerie.NONE &&
			salsaParameters.getEmbeddedScoringMatrix() == EmbeddedScoringMatrix.NONE){
			throw new SALSAParameterException("A matrix serie or a scoring matrix must be set");
		}	
	}
	
	public static void callClustal(SalsaParameters salsaParameters) throws SALSAException, IOException, InterruptedException {
		validateParameters(salsaParameters);
		
		// PROCESS
		String phylogeneticTreeFilePath = salsaParameters.getPhylogeneticTreeFile();
		String alignmentFilePath = salsaParameters.getInputFile();
		String distanceMatrixFilePath = salsaParameters.getDistanceMatrix();
		
		ClustalFileMapper clustalFileMapper = null;
		
		if ((salsaParameters.getClustalWPath() != null || salsaParameters.getClustalOmegaPath() != null) &&
			 salsaParameters.getPhylogeneticTreeFile() == null) {
			// Find clustal type
			ClustalType clustalType;
			String clustalPath;
			// Evaluate first ClustalOmega because this is used to perform alignment also if ClustalW path is set
			if (salsaParameters.getClustalOmegaPath() != null) {
				clustalType = ClustalType.CLUSTAL_O;
				clustalPath = salsaParameters.getClustalOmegaPath();
				// Normalize input only for ClustalOmega
				salsaParameters.setInputFile(normalizeInputFile(Paths.get(salsaParameters.getInputFile())));
			} else if (salsaParameters.getClustalWPath() != null) {
				clustalType = ClustalType.CLUSTAL_W;
				clustalPath = salsaParameters.getClustalWPath();
			} else {
				throw new UnsupportedOperationException("Unable to find correct clustal");
			}

			// Use Clustal to generate initial alignment
			clustalFileMapper = new ClustalFileMapper(salsaParameters.getInputFile());

			ClustalManager clustalManager = ClustalManager.createClustalManager(clustalType);
			clustalManager.callClustal(clustalPath, clustalFileMapper);
			alignmentFilePath = clustalFileMapper.getAlignmentFilePath();
			phylogeneticTreeFilePath = clustalFileMapper.getTreeFilePath();
			distanceMatrixFilePath = clustalFileMapper.getDistanceMatrixFilePath();
		}
		else
		{
			// Start from existing alignments file
			clustalFileMapper = new ClustalFileMapper(null);
			clustalFileMapper.setAlignmentFilePath(alignmentFilePath);
			clustalFileMapper.setPhylogeneticTreeFile(phylogeneticTreeFilePath);
			clustalFileMapper.setDistanceMatrixFilePath(distanceMatrixFilePath);
		}
		
		SubstitutionMatrix matrix = null;
		if (salsaParameters.getScoringMatrixFilePath() != null){
			// Load user matrix from file
			try(InputStream stream = new FileInputStream(salsaParameters.getScoringMatrixFilePath())) {
				matrix = new SubstitutionMatrix(stream, new Alphabet(salsaParameters.getAlphabetType()), salsaParameters.getGEP());
			}
		}
		else{
			if (salsaParameters.getAlphabetType() != AlphabetType.PROTEINS &&
				salsaParameters.getEmbeddedScoringMatrix() == EmbeddedScoringMatrix.NONE){
				throw new SALSAException("Error: if the type of residues is different from "+ AlphabetType.PROTEINS.toString() +" it is required to specify the scoring matrix or provide one from file.");
			}
			
			if (distanceMatrixFilePath != null &&
				salsaParameters.getMatrixSerie() != MatrixSerie.NONE){
				try(InputStream stream = new FileInputStream(distanceMatrixFilePath)) {
					DistanceMatrix dm = new DistanceMatrix(stream);
					matrix = dm.createSubstitutionMatrix(salsaParameters.getMatrixSerie(), salsaParameters.getGEP());
				}				
			}
		}		
		
		Alignment alignment;		
		if (matrix != null) {
			alignment = new Alignment(alignmentFilePath, phylogeneticTreeFilePath, 
					matrix, salsaParameters.getGOP(), salsaParameters.getTerminalGAPsStrategy());
		} else {
			alignment = new Alignment(alignmentFilePath, phylogeneticTreeFilePath, 
					salsaParameters.getEmbeddedScoringMatrix(), salsaParameters.getGEP(), salsaParameters.getGOP(), salsaParameters.getTerminalGAPsStrategy());
		}			

		LocalSearch localSearch = new LocalSearch(alignment, salsaParameters.getGamma(),
				salsaParameters.getMinIterations(),
				salsaParameters.getProbabilityOfSplit());

		alignment = localSearch.execute();		
		alignment.save(salsaParameters.getOutputFile());					
		
		if (salsaParameters.getGeneratePhylogeneticTree() == true){
			// Generate phylogenetic tree using ClustalW from SALSA alignment
			clustalFileMapper.setAlignmentFilePath(salsaParameters.getOutputFile());
			ClustalWManager clustalWManager = new ClustalWManager();
			clustalWManager.generateTree(salsaParameters.getClustalWPath(), clustalFileMapper);
			salsaParameters.setPhylogeneticTreeFile(clustalFileMapper.getTreeFilePath());
		}			
	}
	
	/**
	 * Replace the characters in proteins name that could cause errors 
	 * 
	 * @param inputFilePath The input file that required the character replace
	 * @return The path to file with the replaced characters 
	 * @throws IOException
	 */
	public static final String normalizeInputFile(final Path inputFilePath) throws IOException{
		Charset charset = StandardCharsets.UTF_8;

		String content = new String(Files.readAllBytes(inputFilePath), charset);
					
		Boolean replaceChangesHeaders = false;
		String replacedContent = content
				.replaceAll(" ", "_")
				.replaceAll(":", "_")
				.replaceAll("\\.", "_");
		if (replacedContent.equals(content) == false){
			replaceChangesHeaders = true;
		}		
		
		// Check if file normalization is required
		if (replaceChangesHeaders == true){
			// Create the name of normalized input files
			String inputFileName = FilenameUtils.getBaseName(inputFilePath.toString());
			String inputFileExtension = FilenameUtils.getExtension(inputFilePath.toString());
			String inputFileFolderPath = FilenameUtils.getFullPath(inputFilePath.toString());			
			Path normalizedInputFilePath = Paths.get(inputFileFolderPath, inputFileName + "-normalized." + inputFileExtension);
				
			Files.write(normalizedInputFilePath, replacedContent.getBytes(charset));
			
			return normalizedInputFilePath.toString();
		} else{
			// The input file isn't changed
			return  inputFilePath.toString();
		}
	}
}
