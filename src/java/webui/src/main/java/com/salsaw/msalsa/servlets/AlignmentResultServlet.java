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

package com.salsaw.msalsa.servlets;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.salsaw.msalsa.datamodel.AlignmentResult;
import com.salsaw.msalsa.datamodel.AlignmentResultFileType;


/**
 * Servlet implementation class AlignmentResultServlet
 */
@WebServlet("/AlignmentResultServlet")
public class AlignmentResultServlet extends HttpServlet {

	// CONSTANTS	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2516441712372871271L;
	private static final int BUFSIZE = 4096;
	public static final String FILE_TYPE_DOWNLOAD_ATTRIBUTE = "fileToDownload";
	public static final String PHYLOGENETIC_TREE_DATA_AVAILABLE_ATTRIBUTE = "phylogeneticTreeDataAvailable";	
	static final Logger logger = LogManager.getLogger(AlignmentResultServlet.class);

	/**
	 * @throws IOException 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			UUID idRequest = AlignmentStatusServlet.readAndValidateProcessId(request, response);
			if (idRequest == null){
				// The input data are invalid
				return;
			}
				
			AlignmentResult alignmentResult = new AlignmentResult(idRequest);
			
			boolean phylogeneticTreeDataAvailable = false;
			if (alignmentResult.getPhylogeneticTreeFilePath() != null) {
				phylogeneticTreeDataAvailable = true;
				String newickTree = getPhylogeneticTreeFileContent(alignmentResult.getPhylogeneticTreeFilePath());
				request.setAttribute("newickTree", newickTree);
			}	
			request.setAttribute(PHYLOGENETIC_TREE_DATA_AVAILABLE_ATTRIBUTE, phylogeneticTreeDataAvailable);
			
			String aligmentFileContent =  new String(Files.readAllBytes(Paths.get(alignmentResult.getAligmentFilePath())));
			request.setAttribute("aligmentFileContent", aligmentFileContent);
					
			// Redirect the request to index and add info to request
			request.setAttribute(AlignmentStatusServlet.ID_PARAMETER, idRequest);
			RequestDispatcher requestDispatcher =
				    request.getRequestDispatcher("results.jsp");
			requestDispatcher.forward(request, response);
		} catch (Exception e) {
			logger.error(e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		} 		
	}	
	
	private String getPhylogeneticTreeFileContent(
			String phylogeneticTreeFilePath) throws IOException {
		
		List<String> lines = Files.readAllLines(
				Paths.get(phylogeneticTreeFilePath),
				StandardCharsets.UTF_8);		
		StringBuilder newickTreeBuilder = new StringBuilder();		
		for (String line : lines) {
			newickTreeBuilder.append(line.trim());
        }
		
		return newickTreeBuilder.toString();
	}	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {			
		// Download the requested file
		AlignmentResultFileType alignmentResultFileType = 
				AlignmentResultFileType.valueOf(request.getParameter(FILE_TYPE_DOWNLOAD_ATTRIBUTE));
		
		UUID idRequest = AlignmentStatusServlet.readAndValidateProcessId(request, response);
		if (idRequest == null){
			// The input data are invalid
			return;
		}
		
		AlignmentResult alignmentResult = new AlignmentResult(idRequest);
		
		String fileToDownloadPath = null;
		switch (alignmentResultFileType) {		
		case Alignment:
			fileToDownloadPath = alignmentResult.getAligmentFilePath();
			break;

		case PhylogeneticTree:
			fileToDownloadPath = alignmentResult.getPhylogeneticTreeFilePath();
			break;
		}
		
		Path fileToDownload = Paths.get(fileToDownloadPath);		
		doDownload(request, response, fileToDownloadPath, fileToDownload.getFileName().toString());
	}
	
	/**
	 * Sends a file to the ServletResponse output stream. Typically
	 * you want the browser to receive a different name than the 
	 * name the file has been saved in your local database, since 
	 * your local names need to be unique.
	 * @param req
	 *            The request
	 * @param resp
	 *            The response
	 * 
	 * @param filePath
	 *            The name of the file you want to download.
	 * 
	 * @param original_filename
	 *            The name the browser should receive.
	 * 
	 */
	// https://dzone.com/articles/example-file-download-servlet
	private void doDownload(HttpServletRequest req, HttpServletResponse resp,
			String filePath, String original_filename)
					throws IOException
	{
		File f = new File(filePath);
		int length = 0;

		try(ServletOutputStream op = resp.getOutputStream()){
			
			String mimetype = getServletConfig().getServletContext().getMimeType(filePath);

			// Set the response and go!
			resp.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
			resp.setContentLength((int) f.length());
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + original_filename + "\"");

			// Stream to the requester.
			byte[] bbuf = new byte[BUFSIZE];
			try(DataInputStream in = new DataInputStream(new FileInputStream(f))){
				while ((in != null) && ((length = in.read(bbuf)) != -1))
				{
					op.write(bbuf, 0, length);
				}
			}

			op.flush();
		}	
	}
}
