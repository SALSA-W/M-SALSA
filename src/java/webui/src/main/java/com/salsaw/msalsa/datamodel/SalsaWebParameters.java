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

import com.salsaw.msalsa.cli.SalsaParameters;
import com.salsaw.msalsa.clustal.ClustalType;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class SalsaWebParameters extends SalsaParameters {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String recipientEmail;
	private String salsaParametersFile;
	private String userJobTitle;
	private String[] uniProtIds;
	private String manualInputSequence;	
	/**
	 * define what version of Clustal must be uses for perform the pre-alignment.
	 */
	private ClustalType clustalType = ClustalType.CLUSTAL_O;
	
	public String getRecipientEmail() {
		return this.recipientEmail;
	}
	
	public String getSalsaParametersFile() {
		return this.salsaParametersFile;
	}
	
	public String[] getUniProtIds() {
		return this.uniProtIds;
	}
	
	/**
	 * Used as e-mail subject
	 * 
	 * @return
	 */
	public String getUserJobTitle() {
		return this.userJobTitle;
	}
	
	public String getManualInputSequence() {
		return this.manualInputSequence;
	}
	
	public ClustalType getClustalType() {
		return this.clustalType;
	}	
	
	public void setSalsaParametersFile(String salsaParametersFile) {
		this.salsaParametersFile = salsaParametersFile;
	}		
	
	public void setUserJobTitle(String userJobTitle) {
		this.userJobTitle = userJobTitle;
	}	
	
	public void setUniProtIds(String[] uniProtIds) {
		this.uniProtIds = uniProtIds;
	}
	
	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}
	
	public void setManualInputSequence(String manualInputSequence) {
		this.manualInputSequence = manualInputSequence;
	}
	
	public void setClustalType(ClustalType clustalType) {
		this.clustalType = clustalType;
	}
}
