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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;
import com.salsaw.msalsa.cli.SalsaAlgorithmExecutor;
import com.salsaw.msalsa.config.ConfigurationManager;
import com.salsaw.msalsa.config.ServerConfiguration;
import com.salsaw.msalsa.datamodel.AlignmentRequest;
import com.salsaw.msalsa.datamodel.SalsaWebParameters;
import com.salsaw.msalsa.servlets.AlignmentResultServlet;
import com.salsaw.msalsa.servlets.AlignmentStatusServlet;
import com.salsaw.msalsa.utils.SalsaParametersXMLExporter;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class AlignmentRequestExecutor implements Runnable {

	static final String RESULT_ZIP_FILE_NAME = SalsaAlgorithmExecutor.M_SALSA_HEADER + "-results.zip";
	static final Logger logger = LogManager.getLogger(AlignmentRequestExecutor.class);
	private final AlignmentRequest alignmentRequest;
	private final Thread thread;

	public AlignmentRequestExecutor(AlignmentRequest alignmentRequest) {
		if (alignmentRequest == null) {
			throw new IllegalArgumentException("alignmentRequest");
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
		salsaWebParameters.setOutputFile(Paths.get(this.alignmentRequest.getAlignmentRequestPath().toString(),
				inputFileName + SalsaAlgorithmExecutor.SALSA_ALIGMENT_SUFFIX).toString());

		try {
			// Save parameters inside file
			SalsaParametersXMLExporter salsaParametersExporter = new SalsaParametersXMLExporter();
			salsaWebParameters
					.setSalsaParametersFile(Paths.get(this.alignmentRequest.getAlignmentRequestPath().toString(),
							SalsaParametersXMLExporter.FILE_NAME).toString());
			salsaParametersExporter.exportSalsaParameters(salsaWebParameters,
					salsaWebParameters.getSalsaParametersFile());

			// Start alignment
			SalsaAlgorithmExecutor.callClustal(salsaWebParameters);
		} catch (SALSAException | IOException | InterruptedException | JAXBException e) {
			logger.error(e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String recipientEmail = salsaWebParameters.getRecipientEmail();
		if (recipientEmail != null && recipientEmail.isEmpty() == false) {

			try {
				this.sendResultMail(salsaWebParameters, recipientEmail, this.alignmentRequest.getId().toString());
			} catch (MessagingException | IOException e) {
				logger.error(recipientEmail, e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		AlignmentRequestManager.getInstance().endManageRequest(this.alignmentRequest.getId());
	}

	private void sendResultMail(SalsaWebParameters salsaWebParameters, String recipientEmail, String jobName)
			throws AddressException, MessagingException, IOException {

		ServerConfiguration serverConfiguration = ConfigurationManager.getInstance().getServerConfiguration();

		GmailSender sender = new GmailSender();
		sender.setSender(serverConfiguration.getMailUsername(), serverConfiguration.getMailPassword());
		sender.addRecipient(recipientEmail);
		sender.setSubject(String.format("Salsa job '%s' completed", jobName));
		sender.setBody(composeMailMessage(jobName), "ISO-8859-1", "text/html");
		String resultsZipFile = composeZipResultFile(salsaWebParameters);
		sender.addAttachment(resultsZipFile);
		// sender.send();
		// Delete the file use only as attachment
		Files.delete(Paths.get(resultsZipFile));
	}
	
	private String composeMailMessage(String jobName) throws UnknownHostException, MalformedURLException{
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
		messageBuilder.append(
				"<address class=\"author\"><a rel=\"author\" href=\"https://www.linkedin.com/pub/alessandro-daniele/31/b1a/280\">Alessandro Daniele</a></address> ");
		messageBuilder.append(
				"<address class=\"author\"><a rel=\"author\" href=\"https://it.linkedin.com/pub/fabio-cesarato/4b/584/255/en\">Fabio Cesarato</a></address> ");
		messageBuilder.append(
				"<address class=\"author\"><a rel=\"author\" href=\"https://it.linkedin.com/pub/andrea-giraldin/30/452/121\">Andrea Giraldin</a></address>");
		messageBuilder.append("</p>");
		
		return messageBuilder.toString();
	}

	private String composeZipResultFile(SalsaWebParameters salsaWebParameters) throws IOException {
		String outputFolderPath = (new File(salsaWebParameters.getOutputFile())).getParent();
		String resultZipFilePath = Paths.get(outputFolderPath, RESULT_ZIP_FILE_NAME).toString();

		try (FileOutputStream fileOutStream = new FileOutputStream(resultZipFilePath)) {
			try (ZipOutputStream zipOutStream = new ZipOutputStream(fileOutStream)) {
				// Add all results files of SALSA
				List<String> filesToCompress = new ArrayList<String>();
				filesToCompress.add(salsaWebParameters.getOutputFile());
				filesToCompress.add(salsaWebParameters.getSalsaParametersFile());
				if (salsaWebParameters.getGeneratePhylogeneticTree() == true){
					filesToCompress.add(salsaWebParameters.getPhylogeneticTreeFile());
				}

				for (String file : filesToCompress) {
					// Get file name and add as zip entity
					Path filePath = Paths.get(file);
					ZipEntry zipEntry = new ZipEntry(filePath.getFileName().toString());
					zipOutStream.putNextEntry(zipEntry);

					// Write file into zip entity
					try (FileInputStream in = new FileInputStream(file)) {
						int len;
						byte[] buffer = new byte[1024];
						while ((len = in.read(buffer)) > 0) {
							zipOutStream.write(buffer, 0, len);
						}						
						in.close();
					}				
					zipOutStream.closeEntry();
				}				
				zipOutStream.close();
			}
		}

		return resultZipFilePath;
	}

	public static URL GetJobResultPath(String jobId) throws UnknownHostException, MalformedURLException {
		InetAddress ip = InetAddress.getLocalHost();

		// TODO - remove hard-coded params
		// Compose the alignment result job url
		return new URL("http", ip.getHostAddress(), 8080, "/" + AlignmentResultServlet.class.getSimpleName() + "?"
				+ AlignmentStatusServlet.ID_PARAMETER + "=" + jobId);
	}

}
