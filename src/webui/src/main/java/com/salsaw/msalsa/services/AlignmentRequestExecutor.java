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

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;
import com.salsaw.msalsa.cli.SalsaAlgorithmExecutor;
import com.salsaw.msalsa.config.ConfigurationManager;
import com.salsaw.msalsa.config.ServerConfiguration;
import com.salsaw.msalsa.datamodel.AlignmentRequest;
import com.salsaw.msalsa.datamodel.SalsaWebParameters;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class AlignmentRequestExecutor implements Runnable {

	static final Logger logger = LogManager.getLogger(AlignmentRequestExecutor.class);
	private final AlignmentRequest alignmentRequest;
	private final Thread thread;

	public AlignmentRequestExecutor(AlignmentRequest alignmentRequest) {
		if (alignmentRequest == null) {
			throw new IllegalArgumentException("salsaParameters");
		}

		this.alignmentRequest = alignmentRequest;
		this.thread = new Thread(this);
	}

	public void startAsyncAlignment() {
		this.thread.start();
	}

	@Override
	public void run() {
		SalsaWebParameters salsaWebParameters = this.alignmentRequest.getSalsaWebParameters();

		// Get path to correct Clustal process
		switch (salsaWebParameters.getClustalType()) {
		case CLUSTAL_W:
			salsaWebParameters.setClustalPath(
					ConfigurationManager.getInstance().getServerConfiguration().getClustalW().getAbsolutePath());
			break;

		case CLUSTAL_O:
			salsaWebParameters.setClustalPath(
					ConfigurationManager.getInstance().getServerConfiguration().getClustalO().getAbsolutePath());
			break;
		}
		salsaWebParameters.setClustalWPath(
				ConfigurationManager.getInstance().getServerConfiguration().getClustalW().getAbsolutePath());

		// Create M-SALSA output file name
		String inputFileName = FilenameUtils.getBaseName(salsaWebParameters.getInputFile());
		salsaWebParameters.setOutputFile(
				Paths.get(ConfigurationManager.getInstance().getServerConfiguration().getTemporaryFilePath(),
						this.alignmentRequest.getId().toString(),
						inputFileName + SalsaAlgorithmExecutor.SALSA_ALIGMENT_SUFFIX).toString());

		try {
			// Start alignment
			SalsaAlgorithmExecutor.callClustal(salsaWebParameters);
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

		String recipientEmail = salsaWebParameters.getRecipientEmail();
		if (recipientEmail != null && recipientEmail.isEmpty() == false) {

			try {
				this.sendResultMail(recipientEmail, this.alignmentRequest.getId().toString());
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				logger.error(recipientEmail, e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		AlignmentRequestManager.getInstance().endManageRequest(this.alignmentRequest.getId());
	}

	private void sendResultMail(String recipientEmail, String jobName) throws AddressException, MessagingException {

		ServerConfiguration serverConfiguration = ConfigurationManager.getInstance().getServerConfiguration();

		GmailSender sender = new GmailSender();
		sender.setSender(serverConfiguration.getMailUsername(), serverConfiguration.getMailPassword());

		sender.addRecipient(recipientEmail);
		sender.setSubject(String.format("Salsa job '%s' completed", jobName));
		sender.setBody(String.format("Hi! Your salsa job '%s' has been completed.", jobName));
		// sender.addAttachment("TestFile.txt");
		sender.send();
	}
}
