/**
 * Copyright 2016 Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
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
package com.salsaw.msalsa.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet filter to manage all unhandled exceptions performing log
 * 
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
@WebFilter("/*")
public class LogFilter implements Filter{
	static final Logger logger = LogManager.getLogger(LogFilter.class);
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// see http://stackoverflow.com/questions/7410414/how-to-grab-uncaught-exceptions-in-a-java-servlet-web-application
	    try {
	        chain.doFilter(request, response);
	    } catch (Throwable e) {
	    	logger.error(e);
	        if (e instanceof IOException) {
	            throw (IOException) e;
	        } else if (e instanceof ServletException) {
	            throw (ServletException) e;
	        } else if (e instanceof RuntimeException) {
	            throw (RuntimeException) e;
	        } else {
	            //This should never be hit
	            throw new RuntimeException("Unexpected Exception", e);
	        }
	    }
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Do noting		
	}

	@Override
	public void destroy() {
		// Do noting		
	}
}
