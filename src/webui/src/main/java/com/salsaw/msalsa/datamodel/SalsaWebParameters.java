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
	private String emailSubject;
	private String salsaParametersFile;

	public String getRecipientEmail() {
		return this.recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}
	
	public String getSalsaParametersFile() {
		return this.salsaParametersFile;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
	
	public String getEmailSubject() {
		return this.emailSubject;
	}

	public void setSalsaParametersFile(String salsaParametersFile) {
		this.salsaParametersFile = salsaParametersFile;
	}	
}
