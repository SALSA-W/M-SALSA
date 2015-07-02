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
package com.salsaw.msalsa.services;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;
import com.salsaw.msalsa.cli.SalsaAlgorithmExecutor;
import com.salsaw.msalsa.cli.SalsaParameters;
import com.salsaw.msalsa.config.ConfigurationManager;
import com.salsaw.msalsa.datamodel.AlignmentRequest;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class AligmentRequestExecutor  implements Runnable {
	
	private final AlignmentRequest alignmentRequest;
	private final Thread thread;
	
	public AligmentRequestExecutor(AlignmentRequest alignmentRequest){
		if (alignmentRequest == null){
			throw new IllegalArgumentException("salsaParameters");
		}
		
		this.alignmentRequest = alignmentRequest;
		this.thread = new Thread(this);
	}
	
	public void startAsyncAlignment(){
		this.thread.start();
	}

	@Override
	public void run() {
		SalsaParameters salsaParameters = this.alignmentRequest.getSalsaParameters();
		
		// Get path to correct Clustal process
    	switch (salsaParameters.getClustalType()) {
		case CLUSTAL_W:
			salsaParameters.setClustalPath(ConfigurationManager.getInstance().getServerConfiguration().getClustalW().getAbsolutePath());
			break;

		case CLUSTAL_O:
			salsaParameters.setClustalPath(ConfigurationManager.getInstance().getServerConfiguration().getClustalO().getAbsolutePath());
			break;
		}
    	salsaParameters.setClustalWPath(ConfigurationManager.getInstance().getServerConfiguration().getClustalW().getAbsolutePath());
    	
    	// Create M-SALSA output file name
    	String inputFileName = FilenameUtils.getBaseName(salsaParameters.getInputFile());		    	
    	salsaParameters.setOutputFile(
    			Paths.get(
    			ConfigurationManager.getInstance().getServerConfiguration().getTemporaryFilePath(),
    			this.alignmentRequest.getId().toString(),
    			inputFileName + SalsaAlgorithmExecutor.SALSA_ALIGMENT_SUFFIX).toString());
    	  		    		
		try {
			// Start alignment
			SalsaAlgorithmExecutor.callClustal(salsaParameters);
		} catch (SALSAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AligmentRequestManager.getInstance().endManageRequest(this.alignmentRequest.getId());
	}

}
