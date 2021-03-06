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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.beust.jcommander.Parameter;
import com.salsaw.msalsa.algorithm.enums.AlphabetType;
import com.salsaw.msalsa.algorithm.enums.EmbeddedScoringMatrix;
import com.salsaw.msalsa.algorithm.enums.MatrixSerie;
import com.salsaw.msalsa.algorithm.enums.TerminalGAPsStrategy;

@XmlRootElement
public class SalsaParameters implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// Commands
	public static final String CLUSTAL_W_PATH = "-clustalWPath";
	public static final String CLUSTAL_O_PATH = "-clustalOPath";
	
	// Documentation of commands
	public static final String INPUT_FILE__DOCS = "path of a file containing the initial alignment. The file must be in FASTA format";
	public static final String OUTPUT_FILE_DOCS = "path of the output file. This will be in FASTA format";
	public static final String PH_TREE_FILE_DOCS = "file containing the guide tree, used by M-SALSA in order to generate correct weigths for the WSP-Score. The file must be in Newicks format. The typical exstensions are: dnd, ph. Necessary only for a pre-aligned input sequence.";
	public static final String GOP_DOCS = "GAP Opening Penalty";
	public static final String GEP_DOCS = "GAP Extension Penalty";
	public static final String GAMMA_DOCS = "dimension of the range of positions for a GAP during an iteration";
	public static final String MIN_ITERATIONS_DOCS = "minimum number of iterations";
	public static final String PROBABILITY_OF_SPLIT_DOCS = "probability of split";
	public static final String TERMINAL_GAPS_STRATEGY_DOCS = "the strategy to be used to manage terminal GAPs";
	public static final String MATRIX_SERIE_DOCS = "matrix serie to use. The specific scoring matrix will be set base on distance matrix data";
	public static final String GENERATE_PHYLOGENETIC_TREE_DOCS = "define if the phylogenetic neighbour-joining tree file must be generated. Requires ClustalW2 path defined. Could be set using  "+ CLUSTAL_W_PATH +" or using ClustalW2 for the alignment";
	public static final String INPUT_TYPE_DOCS = "type of sequences in input file";
	public static final String SCORING_MATRIX_DOCS = "distance matrix file";
	public static final String EMBEDDED_SCORING_MATRIX_DOCS = "provided scoring matrix to use";

	@Parameter(names = { "-inputFile" }, description = INPUT_FILE__DOCS, required = true)
	private String inputFile;

	@Parameter(names = { "-outputFile" }, description = OUTPUT_FILE_DOCS, required = true)
	private String outputFile;

	@Parameter(names = { "-phTreeFile" }, description = PH_TREE_FILE_DOCS, required = false)
	private String phylogeneticTreeFile;

	@Parameter(names = { "-GOP" }, description = GOP_DOCS)
	private double GOP = 8;

	@Parameter(names = "-GEP", description = GEP_DOCS)
	private double GEP = 5;

	@Parameter(names = "-gamma", description = GAMMA_DOCS)
	private int gamma = 30;

	@Parameter(names = "-scoringMatrixPath", description = "scoring matrix file path. For more information on scoring matrix file format visit M-SALSA wiki at https://github.com/SALSA-W/M-SALSA/wiki")
	private String scoringMatrixFilePath;

	@Parameter(names = "-minIt", description = MIN_ITERATIONS_DOCS)
	private int minIterations = 1000;

	@Parameter(names = "-pSplit", description = PROBABILITY_OF_SPLIT_DOCS)
	private double probabilityOfSplit = 0.1f;
	
	@Parameter(names = "-terminal", description = TERMINAL_GAPS_STRATEGY_DOCS)
	private TerminalGAPsStrategy terminalGAPsStrategy = TerminalGAPsStrategy.ONLY_GEP;
	
	@Parameter(names = "-matrixSerie", description = MATRIX_SERIE_DOCS)
	private MatrixSerie matrixSerie = MatrixSerie.NONE;
	
	@Parameter(names = "-scoringMatrix", description = EMBEDDED_SCORING_MATRIX_DOCS)
	private EmbeddedScoringMatrix embeddedScoringMatrix = EmbeddedScoringMatrix.BLOSUM62;
	
	@Parameter(names = "-type", description = INPUT_TYPE_DOCS)
	private AlphabetType alphabetType = AlphabetType.PROTEINS;
	
	@Parameter(names = "-distanceMatrix", description = SCORING_MATRIX_DOCS)
	private String distanceMatrix;
	
	@Parameter(names = CLUSTAL_O_PATH, description = "define path where clustalOmega is intalled. Use to perform the pre-alignment.")	
	private String clustalOmegaPath;
		
	@Parameter(names = "-generatePhTree", description = GENERATE_PHYLOGENETIC_TREE_DOCS)
	private boolean generatePhylogeneticTree = false;
	
	@Parameter(names = CLUSTAL_W_PATH, description = "define path where clustalW2 is intalled. Use to perform the pre-alignment only if " + CLUSTAL_O_PATH + " not set. Required for generate tree file")
	private String clustalWPath;
	
	@Parameter(names = "-help", description="write the documentation of all commands" ,help = true)
	private boolean help;

	// GET
	/**
	 * GAP Opening Penalty
	 * 
	 * @return
	 */
	public double getGOP() {
		return GOP;
	}

	/**
	 * GAP Extension Penalty
	 * 
	 * @return
	 */
	public double getGEP() {
		return GEP;
	}

	/**
	 * Dimension of the range of positions for a GAP during an iteration
	 * 
	 * @return
	 */
	public int getGamma() {
		return gamma;
	}

	/**
	 * Scoring matrix file path
	 * 
	 * @return
	 */
	public String getScoringMatrixFilePath() {
		return scoringMatrixFilePath;
	}
	
	public EmbeddedScoringMatrix getEmbeddedScoringMatrix(){
		return this.embeddedScoringMatrix;
	}

	public String getInputFile() {
		return inputFile;
	}

	public String getOutputFile() {
		return outputFile;
	}

	public String getPhylogeneticTreeFile() {
		return phylogeneticTreeFile;
	}

	public TerminalGAPsStrategy getTerminalGAPsStrategy() {
		return terminalGAPsStrategy;
	}

	public double getProbabilityOfSplit() {
		return probabilityOfSplit;
	}

	/**
	 * Minimum number of iterations
	 * 
	 * @return
	 */
	public int getMinIterations() {
		return minIterations;
	}
	
	public String getClustalOmegaPath(){
		return this.clustalOmegaPath;
	}
	
	public String getClustalWPath(){
		return this.clustalWPath;
	}
	
	public boolean getGeneratePhylogeneticTree() {
		return this.generatePhylogeneticTree;
	}
	
	public MatrixSerie getMatrixSerie() {
		return this.matrixSerie;
	}
	
	public AlphabetType getAlphabetType() {
		return this.alphabetType;
	}
	
	public String getDistanceMatrix() {
		return this.distanceMatrix;
	}
	
	public boolean getHelp(){
		return this.help;
	}
	
	// SET
	public void setGOP(double gop) {
		this.GOP = gop;
	}

	public void setGEP(double gep) {
		this.GEP = gep;
	}

	public void setGamma(int gamma) {
		this.gamma = gamma;
	}

	public void setScoringMatrixFilePath(String scoringMatrixFilePath) {
		this.scoringMatrixFilePath = scoringMatrixFilePath;
	}
	
	public void setEmbeddedScoringMatrix(EmbeddedScoringMatrix embeddedScoringMatrix){
		this.embeddedScoringMatrix = embeddedScoringMatrix;
	}

	@XmlTransient
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	@XmlTransient
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	@XmlTransient
	public void setPhylogeneticTreeFile(String phylogeneticTreeFile) {
		this.phylogeneticTreeFile = phylogeneticTreeFile;
	}

	public void setTerminalGAPsStrategy(TerminalGAPsStrategy terminalGAPsStrategy) {
		this.terminalGAPsStrategy = terminalGAPsStrategy;
	}

	public void setProbabilityOfSplit(double probabilityOfSplit) {
		this.probabilityOfSplit = probabilityOfSplit;
	}

	public void setMinIterations(int minIterations) {
		this.minIterations = minIterations;
	}
	
	@XmlTransient
	public void setClustalOmegaPath(String clustalOmegaPath){
		this.clustalOmegaPath = clustalOmegaPath;
	}
	
	@XmlTransient
	public void setClustalWPath(String clustalWPath){
		this.clustalWPath = clustalWPath;
	}			
	
	public void setGeneratePhylogeneticTree(boolean generatePhylogeneticTree) {
		this.generatePhylogeneticTree = generatePhylogeneticTree;
	}
	
	public void setMatrixSerie(MatrixSerie matrixSerie) {
		this.matrixSerie = matrixSerie;
	}
	
	public void setAlphabetType(AlphabetType alphabetType) {
		this.alphabetType = alphabetType;
	}
	
	public void setDistanceMatrix(String distanceMatrix) {
		this.distanceMatrix = distanceMatrix;
	}
}
