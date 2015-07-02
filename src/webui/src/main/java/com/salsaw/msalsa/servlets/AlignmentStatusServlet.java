package com.salsaw.msalsa.servlets;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.salsaw.msalsa.services.AligmentRequestManager;

/**
 * Servlet implementation class AlignmentStatusServlet
 */
@WebServlet("/alignment-status")
public class AlignmentStatusServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String ID_PARAMETER = "id";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AlignmentStatusServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idRequest = readAndValidateProcessId(request, response);
		if (idRequest == null){
			// The input data are invalid
			return;
		}
		
		if (AligmentRequestManager.getInstance().IsRequestCompleted(idRequest) == false){
			// Redirect the request to loading
			RequestDispatcher requestDispatcher =
				request.getRequestDispatcher("/loading.jsp");
			requestDispatcher.forward(request, response);
		}else{
			
			String aligmentResultServlet = "/AligmentResultServlet?" + ID_PARAMETER + URLEncoder.encode(idRequest, "UTF-8");
			// Redirect the request to result servlet
			RequestDispatcher requestDispatcher =
				request.getRequestDispatcher(aligmentResultServlet);
			requestDispatcher.forward(request, response);
		}
	}

	public static String readAndValidateProcessId(HttpServletRequest request, HttpServletResponse response) throws IOException{
		// For response code logic see http://www.restapitutorial.com/httpstatuscodes.html
		
		// Read the request id
		String idRequest = request.getParameter(ID_PARAMETER);
		
		if (idRequest == null){
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
		
		if (AligmentRequestManager.getInstance().RequestExists(idRequest) == false){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		return idRequest;
	}
}
