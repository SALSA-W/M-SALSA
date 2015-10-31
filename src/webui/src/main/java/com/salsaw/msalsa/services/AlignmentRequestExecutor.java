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
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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
import com.salsaw.msalsa.servlets.AlignmentResultServlet;
import com.salsaw.msalsa.servlets.AlignmentStatusServlet;

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
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		AlignmentRequestManager.getInstance().endManageRequest(this.alignmentRequest.getId());
	}

	private void sendResultMail(String recipientEmail, String jobName) throws AddressException, MessagingException, UnknownHostException, MalformedURLException {

		ServerConfiguration serverConfiguration = ConfigurationManager.getInstance().getServerConfiguration();

		GmailSender sender = new GmailSender();
		sender.setSender(serverConfiguration.getMailUsername(), serverConfiguration.getMailPassword());

		sender.addRecipient(recipientEmail);
		sender.setSubject(String.format("Salsa job '%s' completed", jobName));
		
		URL resultLink = GetJobResultPath(jobName);
		StringBuilder messageBuilder = new StringBuilder();		
		messageBuilder.append("<h1>");
		messageBuilder.append(SalsaAlgorithmExecutor.M_SALSA_HEADER);
		messageBuilder.append("</h1>");
		
		// Hello user :)
		messageBuilder.append("<p>Dear user</p>");
		messageBuilder.append("<p>Your salsa job ");
		messageBuilder.append(jobName);
		messageBuilder.append(" has been completed.</p>");
		
		// Download link
		messageBuilder.append("<p>Result is available at link: ");		
		messageBuilder.append("<a href=\"");
		messageBuilder.append(resultLink);
		messageBuilder.append("\">");
		messageBuilder.append(resultLink);
		messageBuilder.append("</a>");		
		messageBuilder.append("</p>");
		
		// Validity of link
		messageBuilder.append("<p>The result will be available for ");
		DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime todayDate = LocalDateTime.now();
		int validatyJobDays = ConfigurationManager.getInstance().getServerConfiguration().getCleanDaysValidityJob();
		messageBuilder.append(validatyJobDays);		
		messageBuilder.append(" days. The results will be delete form server ");
		messageBuilder.append(todayDate.plusDays(validatyJobDays).format(fmtDate));
		messageBuilder.append(".</p>");
		
		// Signature
		messageBuilder.append("<p>Best Regards</p>");
		messageBuilder.append("<p>");
		messageBuilder.append(SalsaAlgorithmExecutor.M_SALSA_HEADER);
		messageBuilder.append(" TEAM: ");
		messageBuilder.append("<address class=\"author\"><a rel=\"author\" href=\"https://www.linkedin.com/pub/alessandro-daniele/31/b1a/280\">Alessandro Daniele</a></address> ");
		messageBuilder.append("<address class=\"author\"><a rel=\"author\" href=\"https://it.linkedin.com/pub/fabio-cesarato/4b/584/255/en\">Fabio Cesarato</a></address> ");
		messageBuilder.append("<address class=\"author\"><a rel=\"author\" href=\"https://it.linkedin.com/pub/andrea-giraldin/30/452/121\">Andrea Giraldin</a></address>");
		messageBuilder.append("</p>");
		
		sender.setBody(messageBuilder.toString(), "ISO-8859-1", "text/html");
		// sender.addAttachment("TestFile.txt");
		//sender.send();
	}
	
	public static URL GetJobResultPath(String jobId) throws UnknownHostException, MalformedURLException{		
        InetAddress ip = InetAddress.getLocalHost();
        
        // TODO - remove hard-coded params
		// Compose the alignment result job url
		return new URL(
				"http",
				ip.getHostAddress(),				
				8080,
				"/" + AlignmentResultServlet.class.getSimpleName() + "?" 
				+ AlignmentStatusServlet.ID_PARAMETER + "=" + jobId);		
	}
	
	
}

