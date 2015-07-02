package com.salsaw.msalsa.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.salsaw.msalsa.cli.SalsaAlgorithmExecutor;
import com.salsaw.msalsa.config.ConfigurationManager;

/**
 * Servlet implementation class AligmentResultServlet
 */
@WebServlet("/AligmentResultServlet")
public class AlignmentResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private String msalsaAligmentFilePath;
	private String msalsaPhylogeneticTreeFilePath;	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlignmentResultServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		UUID idRequest = AlignmentStatusServlet.readAndValidateProcessId(request, response);
		if (idRequest == null){
			// The input data are invalid
			return;
		}
		
		initSalsaData(idRequest.toString());
		
		String newickTree = getPhylogeneticTreeFileContent(this.msalsaPhylogeneticTreeFilePath);
		request.setAttribute("newickTree", newickTree);
		
		String aligmentFileContent =  new String(Files.readAllBytes(Paths.get(this.msalsaAligmentFilePath)));
		request.setAttribute("aligmentFileContent", aligmentFileContent);
		
		// Redirect the request to index
		RequestDispatcher requestDispatcher =
			    request.getRequestDispatcher("results.jsp");
		requestDispatcher.forward(request, response);
	}

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
		    	// Search SALSA aligmnet and tree files
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
}
