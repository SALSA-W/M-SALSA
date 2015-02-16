package com.salsaw.salsa.cli;

import com.beust.jcommander.Parameter;
import com.salsaw.salsa.algorithm.TerminalGAPsStrategy;

public class SalsaParameters {	 
	  @Parameter(names = { "-GOP" }, description = "GAP Opening Penalty")
	  private float GOP = 8;
	 
	  @Parameter(names = "-GEP", description = "GAP Extension Penalty")
	  private int GEP = 5;
	  
	  @Parameter(names = "-gamma", description = "dimension of the range of positions for a GAP during an iteration")
	  private int gamma = 30;	  
	 
	  @Parameter(names = "-scoringMatrix", description = "scoring matrix")
	  private String debug = "BLOSUM62";
	  
	  @Parameter(names = "-minIt", description = "minimum number of iterations")
	  private int minIterations = 500;	  
	  
	  @Parameter(names = "-pSplit", description = "probability of split")
	  private float probabilityOfSplit = 0.5f;
	  
	  @Parameter(names = "-terminal", description = "the strategy to be used to manage terminal GAPs.")
	  private TerminalGAPsStrategy terminalGAPsStrategy = TerminalGAPsStrategy.ONLY_GEP; 
}
