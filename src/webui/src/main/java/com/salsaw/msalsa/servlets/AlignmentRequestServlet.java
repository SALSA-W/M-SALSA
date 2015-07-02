package com.salsaw.msalsa.servlets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.beanutils.BeanUtils;

import com.salsaw.msalsa.cli.SalsaParameters;
import com.salsaw.msalsa.datamodel.AlignmentRequest;
import com.salsaw.msalsa.services.AlignmentRequestManager;

/**
 * Servlet implementation class AlignmentRequestServlet
 */
@WebServlet("/AlignmentRequestServlet")
@MultipartConfig
public class AlignmentRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlignmentRequestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		
		// TODO Auto-generated method stub
		SalsaParameters salsaParameters = new SalsaParameters();
		try {
			BeanUtils.populate (salsaParameters, request.getParameterMap());
			
			AlignmentRequest newRequest = new AlignmentRequest(salsaParameters);		
			
			Part filePart = request.getPart("inputFile");
			String fileName = filePart.getSubmittedFileName();
			try(InputStream inputAlignmentFileContet = filePart.getInputStream()){
			
				File requestProcessFolder= AlignmentRequestManager.getInstance()
						.getServerAligmentFolder(newRequest.getId()).toFile();

				// if the directory does not exist, create it
				if (requestProcessFolder.exists() == false) {					  
				    requestProcessFolder.mkdir();	
				}
	    		
	            // Open the file for writing.
	            Path inputFilePath = Paths.get(requestProcessFolder.toString(), fileName);	            	            
				Files.copy(inputAlignmentFileContet, inputFilePath);				
				salsaParameters.setInputFile(inputFilePath.toString());
			}						
						
			AlignmentRequestManager.getInstance().startManageRequest(newRequest);
			
			// Redirect the request to index			
			response.sendRedirect("loading.jsp?id=" + newRequest.getId());
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}

}
