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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.salsaw.msalsa.services.AlignmentRequestManager;

/**
 * Servlet implementation class ProcessChecker
 * 
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 */
@WebServlet(description = "Check if the aligment proccess has been completed", urlPatterns = { "/ProcessChecker" })
public class ProcessCheckerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessCheckerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// For response code logic see http://www.restapitutorial.com/httpstatuscodes.html		
		String idRequest = AlignmentStatusServlet.readAndValidateProcessId(request, response);
		if (idRequest == null){
			// The input data are invalid
			return;
		}
			
		if (AlignmentRequestManager.getInstance().IsRequestCompleted(idRequest) == false){
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
		}else{
			response.setStatus(HttpServletResponse.SC_OK);
		}
	}
}
