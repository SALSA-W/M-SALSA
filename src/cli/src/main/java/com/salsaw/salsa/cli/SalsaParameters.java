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

import com.beust.jcommander.Parameter;
import com.salsaw.salsa.algorithm.TerminalGAPsStrategy;

public class SalsaParameters {
	@Parameter(names = { "-inputFile" }, description = "The FASTA file to process", required = true)
	private String inputFile;

	@Parameter(names = { "-outputFile" }, description = "The FASTA produced after the process", required = true)
	private String outputFile;

	@Parameter(names = { "-phTreeFile" }, description = "The ph file that contains the phylogenetic tree", required = true)
	private String phylogeneticTreeFile;

	@Parameter(names = { "-GOP" }, description = "GAP Opening Penalty")
	private float GOP = 8;

	@Parameter(names = "-GEP", description = "GAP Extension Penalty")
	private float GEP = 5;

	@Parameter(names = "-gamma", description = "dimension of the range of positions for a GAP during an iteration")
	private int gamma = 30;

	@Parameter(names = "-scoringMatrix", description = "scoring matrix")
	private String scoringMatrix = "BLOSUM62";

	@Parameter(names = "-minIt", description = "minimum number of iterations")
	private int minIterations = 500;

	@Parameter(names = "-pSplit", description = "probability of split")
	private float probabilityOfSplit = 0.5f;

	@Parameter(names = "-terminal", description = "the strategy to be used to manage terminal GAPs.", converter = TerminalGAPsStrategyConverter.class)
	private TerminalGAPsStrategy terminalGAPsStrategy = TerminalGAPsStrategy.ONLY_GEP;

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
	public String getScoringMatrix() {
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

}
