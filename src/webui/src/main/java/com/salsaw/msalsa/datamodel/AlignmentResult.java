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
package com.salsaw.msalsa.datamodel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import com.salsaw.msalsa.cli.SalsaAlgorithmExecutor;
import com.salsaw.msalsa.config.ConfigurationManager;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class AlignmentResult {
	// FIELDS
	private final UUID id;
	
	private String msalsaAligmentFilePath;
	private String msalsaPhylogeneticTreeFilePath; 
	
	// CONSTRUCTOR
	public AlignmentResult(UUID id) throws IllegalStateException, IOException
	{
		if (id == null){
			throw new IllegalArgumentException("id");
		}
		
		this.id = id;
		initSalsaData(id.toString());
	}
	
	// GET / SET
	public UUID getId() {
		return this.id;
	}
	
	public String getAligmentFilePath() {
		return this.msalsaAligmentFilePath;
	}
	
	public String getPhylogeneticTreeFilePath() {
		return this.msalsaPhylogeneticTreeFilePath;
	}
	
	/**
	 * Load result file path from the file system 
	 * 
	 * @param idProccedRequest
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	private void initSalsaData(String idProccedRequest) throws IOException, IllegalStateException{
		// Get the folder where the files are stored
		File processedRequestFolder = new File(Paths.get(
				ConfigurationManager.getInstance().getServerConfiguration().getTemporaryFilePath(),
				idProccedRequest).toString());
		File[] listOfFiles = processedRequestFolder.listFiles();
		
		String msalsaAligmentFilePath = null;
		String msalsaPhylogeneticTreeFilePath = null; 
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	// Search SALSA alignment and tree files
		        if (file.getName().endsWith(SalsaAlgorithmExecutor.SALSA_ALIGMENT_SUFFIX)){
		        	msalsaAligmentFilePath = file.getAbsolutePath();
		        	continue;
		        }
		        if (file.getName().endsWith(SalsaAlgorithmExecutor.SALSA_TREE_SUFFIX))
		        {
		        	msalsaPhylogeneticTreeFilePath = file.getAbsolutePath();
		        	continue;
		        }
		    }
		}
		
		if (msalsaAligmentFilePath == null){
			throw new IllegalStateException("Unable to find file " + SalsaAlgorithmExecutor.SALSA_ALIGMENT_SUFFIX + " for UUID " + idProccedRequest);
		}		
		if (msalsaPhylogeneticTreeFilePath == null){
			throw new IllegalStateException("Unable to find file " + SalsaAlgorithmExecutor.SALSA_TREE_SUFFIX + " for UUID " + idProccedRequest);
		}
		
		this.msalsaAligmentFilePath = msalsaAligmentFilePath;
		this.msalsaPhylogeneticTreeFilePath = msalsaPhylogeneticTreeFilePath;
	}
}
