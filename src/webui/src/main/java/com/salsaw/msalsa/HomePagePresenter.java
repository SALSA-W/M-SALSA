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
package com.salsaw.msalsa;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;
import com.salsaw.msalsa.cli.SalsaAlgorithmExecutor;
import com.salsaw.msalsa.cli.SalsaParameters;

import com.salsaw.msalsa.config.ConfigurationManager;
import com.vaadin.navigator.Navigator;

public class HomePagePresenter implements IHomePageListener{
	// CONSTANTS
	protected static final String PROCESSED = "processed";
	
	// FIELDS
	private final HomePageView view;
	private final Navigator navigator;
	private final SalsaParameters salsaParameters;
	private final UUID idProccedRequest;	

	// CONSTRUCTOR
	public HomePagePresenter(HomePageView view, Navigator navigator, SalsaParameters salsaParameters, UUID idProccedRequest) {
		if (view == null){
			throw new IllegalArgumentException("view");
		}
		if (navigator == null){
			throw new IllegalArgumentException("navigator");
		}
		if (salsaParameters == null){
			throw new IllegalArgumentException("salsaParameters");
		}
		if (idProccedRequest == null){
			throw new IllegalArgumentException("idProccedRequest");
		}		
		
		this.view = view;
		this.navigator = navigator;
		this.salsaParameters = salsaParameters;
		this.idProccedRequest = idProccedRequest;
	         
		this.view.addListener(this);	
	}
	
	// GET / SET
	public UUID getIdProccedRequest(){
		return this.idProccedRequest;
	}

	@Override
	public void buttonClick(File file) throws SALSAException, IOException, InterruptedException {
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
    	salsaParameters.setInputFile(file.getAbsolutePath());
    	String inputFileName = FilenameUtils.getBaseName(file.getAbsolutePath());		    	
    	salsaParameters.setOutputFile(
    			Paths.get(
    			ConfigurationManager.getInstance().getServerConfiguration().getTemporaryFilePath(),
    			getIdProccedRequest().toString(),
    			inputFileName + SalsaAlgorithmExecutor.SALSA_ALIGMENT_SUFFIX).toString());
    	  		    		
		SalsaAlgorithmExecutor.callClustal(salsaParameters);
		
		navigator.navigateTo(PROCESSED + "/" + getIdProccedRequest().toString()); 
	}	
}
