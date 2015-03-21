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

import com.beust.jcommander.Parameter;
import com.salsaw.msalsa.algorithm.TerminalGAPsStrategy;
import com.salsaw.msalsa.clustal.ClustalType;

public class SalsaParameters implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Parameter(names = { "-inputFile" }, description = "The FASTA file to process", required = true)
	private String inputFile;

	@Parameter(names = { "-outputFile" }, description = "The FASTA produced after the process", required = true)
	private String outputFile;

	@Parameter(names = { "-phTreeFile" }, description = "The ph file that contains the phylogenetic tree", required = false)
	private String phylogeneticTreeFile;

	@Parameter(names = { "-GOP" }, description = "GAP Opening Penalty")
	private float GOP = 8;

	@Parameter(names = "-GEP", description = "GAP Extension Penalty")
	private float GEP = 5;

	@Parameter(names = "-gamma", description = "dimension of the range of positions for a GAP during an iteration")
	private int gamma = 30;

	@Parameter(names = "-scoringMatrix", description = "scoring matrix")
	private ScoringMatrix scoringMatrix = ScoringMatrix.BLOSUM62;

	@Parameter(names = "-minIt", description = "minimum number of iterations")
	private int minIterations = 500;

	@Parameter(names = "-pSplit", description = "probability of split")
	private float probabilityOfSplit = 0.5f;

	@Parameter(names = "-terminal", description = "the strategy to be used to manage terminal GAPs.", converter = TerminalGAPsStrategyConverter.class)
	private TerminalGAPsStrategy terminalGAPsStrategy = TerminalGAPsStrategy.ONLY_GEP;
	
	@Parameter(names = "-clustalPath", description = "define path where clustal program is intalled")
	private String clustalPath;
	
	@Parameter(names = "-clustal", description = "define what version of clustal use (could be W or Omega)")
	private ClustalType clustalType = ClustalType.CLUSTAL_W;

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
	 * Scoring matrix
	 * 
	 * @return
	 */
	public ScoringMatrix getScoringMatrix() {
		return scoringMatrix;
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
	
	public ClustalType getClustalType() {
		return this.clustalType;
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

	public void setScoringMatrix(ScoringMatrix scoringMatrix) {
		this.scoringMatrix = scoringMatrix;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

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
	
	public void setClustalPath(String clustalPath){
		this.clustalPath = clustalPath;
	}	
	
	public void setClustalType(ClustalType clustalType) {
		this.clustalType = clustalType;
	}
}
