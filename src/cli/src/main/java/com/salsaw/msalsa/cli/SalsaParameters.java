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
import com.salsaw.msalsa.algorithm.AlphabetType;
import com.salsaw.msalsa.algorithm.EmbeddedScoringMatrix;
import com.salsaw.msalsa.algorithm.MatrixSerie;
import com.salsaw.msalsa.algorithm.TerminalGAPsStrategy;
import com.salsaw.msalsa.clustal.ClustalType;

@XmlRootElement
public class SalsaParameters implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Parameter(names = { "-inputFile" }, description = "The FASTA file to process", required = true)
	private String inputFile;

	@Parameter(names = { "-outputFile" }, description = "The FASTA produced after the process", required = true)
	private String outputFile;

	@Parameter(names = { "-phTreeFile" }, description = "The ph or dnd file that contains the phylogenetic tree", required = false)
	private String phylogeneticTreeFile;

	@Parameter(names = { "-GOP" }, description = "GAP Opening Penalty")
	private float GOP = 8;

	@Parameter(names = "-GEP", description = "GAP Extension Penalty")
	private float GEP = 5;

	@Parameter(names = "-gamma", description = "dimension of the range of positions for a GAP during an iteration")
	private int gamma = 30;

	@Parameter(names = "-scoringMatrixPath", description = "scoring matrix file path")
	private String scoringMatrixFilePath;

	@Parameter(names = "-minIt", description = "minimum number of iterations")
	private int minIterations = 1000;

	@Parameter(names = "-pSplit", description = "probability of split")
	private float probabilityOfSplit = 0.1f;
	
	@Parameter(names = "-terminal", description = "the strategy to be used to manage terminal GAPs. Possible options: BOTH_PENALTIES, ONLY_GEP" , converter = TerminalGAPsStrategyConverter.class)
	private TerminalGAPsStrategy terminalGAPsStrategy = TerminalGAPsStrategy.ONLY_GEP;
	
	@Parameter(names = "-matrixSerie", description = "matrix serie. Possible options: BLOSUM, PAM, BLOSUM62, GONNET", converter = MatrixSerieConverter.class)
	private MatrixSerie matrixSerie = MatrixSerie.NONE;
	
	// TODO - set all parameters
	@Parameter(names = "-scoringMatrix", description = "scoring matrix to use. Possible options: BLOSUM, PAM, BLOSUM62, GONNET")
	private EmbeddedScoringMatrix embeddedScoringMatrix = EmbeddedScoringMatrix.BLOSUM62;
	
	@Parameter(names = "-type", description = "type of sequences. Possible options: DNA, RNA, PROTEINS", converter = AlphabetTypeConverter.class)
	private AlphabetType alphabetType = AlphabetType.PROTEINS;
	
	@Parameter(names = "-distanceMatrix", description = "distance matrix file")
	private String distanceMatrix;
	
	@Parameter(names = "-clustalPath", description = "define path where clustal program is intalled")
	private String clustalPath;
		
	@Parameter(names = "-clustal", description = "define what version of clustal use (could be W or Omega). Possible options: CLUSTAL_W, CLUSTAL_O")
	private ClustalType clustalType = ClustalType.CLUSTAL_O;
	
	@Parameter(names = "-generatePhTree", description = "define if the phylogenetic neighbour-joining tree file must be generated")
	private boolean generatePhylogeneticTree = false;
	
	@Parameter(names = "-clustalWPath", description = "define path where clustalW program is intalled. Required for generate tree file")
	private String clustalWPath;

	// GET
	/**
	 * GAP Opening Penalty
	 * 
	 * @return
	 */
	public float getGOP() {
		return GOP;
	}

	/**
	 * GAP Extension Penalty
	 * 
	 * @return
	 */
	public float getGEP() {
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

	public float getProbabilityOfSplit() {
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
	
	public String getClustalPath(){
		return this.clustalPath;
	}
	
	public String getClustalWPath(){
		return this.clustalWPath;
	}
	
	public ClustalType getClustalType() {
		return this.clustalType;
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
	
	// SET
	public void setGOP(float gop) {
		this.GOP = gop;
	}

	public void setGEP(float gep) {
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

	public void setProbabilityOfSplit(float probabilityOfSplit) {
		this.probabilityOfSplit = probabilityOfSplit;
	}

	public void setMinIterations(int minIterations) {
		this.minIterations = minIterations;
	}
	
	@XmlTransient
	public void setClustalPath(String clustalPath){
		this.clustalPath = clustalPath;
	}
	
	@XmlTransient
	public void setClustalWPath(String clustalWPath){
		this.clustalWPath = clustalWPath;
	}		
	
	public void setClustalType(ClustalType clustalType) {
		this.clustalType = clustalType;
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
