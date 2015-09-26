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

public class ClustalFileMapper {
	// FIELDS
	private final String inputFilePath;
	private String alignmentFilePath;
	private String guideTreeFilePath;
	private String phylogeneticTreeFile;
	private String distanceMatrixFilePath;
	
	// CONSTRUCTOR
	public ClustalFileMapper(String inputFilePath){
		this.inputFilePath = inputFilePath;
	}
	
	// GET/ SET
	public String getInputFilePath(){
		return this.inputFilePath;
	}
	
	public String getGuideTreeFilePath(){
		return this.guideTreeFilePath;
	}
	
	public String getDistanceMatrixFilePath() {
		return this.distanceMatrixFilePath;
	}
	
	public String getPhylogeneticTreeFile(){
		return this.phylogeneticTreeFile;
	}
	
	public String getAlignmentFilePath(){
		return this.alignmentFilePath;
	}
	
	public void setGuideTreeFilePath(String guideTreeFilePath){
		this.guideTreeFilePath = guideTreeFilePath;
	}
	
	public void setDistanceMatrixFilePath(String distanceMatrixFilePath) {
		this.distanceMatrixFilePath = distanceMatrixFilePath;
	}	

	public void setPhylogeneticTreeFile(String phylogeneticTreeFile){
		this.phylogeneticTreeFile = phylogeneticTreeFile;
	}
	
	public void setAlignmentFilePath(String alignmentFilePath){
		this.alignmentFilePath = alignmentFilePath;
	}
	
	// METHODS
	public String getTreeFilePath(){
		// Return guide tree if phylogenetic tree isn't set
		if (this.phylogeneticTreeFile == null)
		{
			return this.guideTreeFilePath;
		}
		return this.phylogeneticTreeFile;
	}	
}
