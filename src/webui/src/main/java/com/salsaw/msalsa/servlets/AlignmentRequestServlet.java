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

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.StringConverter;

import com.salsaw.msalsa.datamodel.AlignmentRequest;
import com.salsaw.msalsa.datamodel.SalsaWebParameters;
import com.salsaw.msalsa.services.AlignmentRequestManager;
import com.salsaw.msalsa.utils.UniProtSequenceManager;

/**
 * Servlet implementation class AlignmentRequestServlet
 */
@WebServlet("/AlignmentRequestServlet")
@MultipartConfig
public class AlignmentRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class EnumAwareConvertUtilsBean extends ConvertUtilsBean {
		// http://www.bitsandpix.com/entry/java-beanutils-enum-support-generic-enum-converter/
		private final EnumConverter enumConverter = new EnumConverter();

		public Converter lookup(Class<?> clazz) {
			final Converter converter = super.lookup(clazz);
			// no specific converter for this class, so it's neither a String,
			// (which has a default converter),
			// nor any known object that has a custom converter for it. It might
			// be an enum !
			if (converter == null && clazz.isEnum()) {
				return enumConverter;
			} else {
				return converter;
			}
		}

		private class EnumConverter implements Converter {
			public Object convert(Class type, Object value) {
				return Enum.valueOf(type, (String) value);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SalsaWebParameters salsaWebParameters = new SalsaWebParameters();
		try {
			// http://www.programcreek.com/java-api-examples/index.php?api=org.apache.commons.beanutils.ConvertUtilsBean
			EnumAwareConvertUtilsBean enumAwareConvertUtilsBean = new EnumAwareConvertUtilsBean();
						
			
			// http://www.programcreek.com/java-api-examples/index.php?api=org.apache.commons.beanutils.converters.ArrayConverter
			// See https://commons.apache.org/proper/commons-beanutils/apidocs/org/apache/commons/beanutils/converters/ArrayConverter.html
		    // Construct an String Converter for an String array (i.e. String[]) using a StringConverter as the element converter.
		    // N.B. Uses the default comma (i.e. ",") as the delimiter between individual numbers
			ArrayConverter stringArrayConverter = new ArrayConverter(String[].class, new StringConverter());
			stringArrayConverter.setOnlyFirstToString(false);
			stringArrayConverter.setDelimiter(',');
			enumAwareConvertUtilsBean.register(stringArrayConverter, String[].class);
						
			BeanUtilsBean beanUtils = new BeanUtilsBean(enumAwareConvertUtilsBean);				
			beanUtils.populate(salsaWebParameters, request.getParameterMap());

			AlignmentRequest newRequest = new AlignmentRequest(salsaWebParameters);
			
			Part filePart = request.getPart("inputFile");
			String fileName = filePart.getSubmittedFileName();
			if (salsaWebParameters.getUniProtIds() == null &&
				fileName.isEmpty() == true){
				throw new ServletException("Missing input data");
			}					
			
			File requestProcessFolder = AlignmentRequestManager.getInstance()
					.getServerAligmentFolder(newRequest.getId()).toFile();
			
			// if the directory does not exist, create it
			if (requestProcessFolder.exists() == false) {
				requestProcessFolder.mkdir();
			}

			if (salsaWebParameters.getUniProtIds() != null && salsaWebParameters.getUniProtIds().length != 0) {
				// Load data from UniProt
				UniProtSequenceManager uniProtSequenceManager = new UniProtSequenceManager(
						requestProcessFolder.toString(), salsaWebParameters.getUniProtIds());
				uniProtSequenceManager.composeInputFromId();
				salsaWebParameters.setInputFile(uniProtSequenceManager.getGeneratedInputFile().toString());
			}
			else{
				// Load data from file
				try (InputStream inputAlignmentFileContet = filePart.getInputStream()) {			
					// Open the file for writing.
					Path inputFilePath = Paths.get(requestProcessFolder.toString(), fileName);
					Files.copy(inputAlignmentFileContet, inputFilePath);
					salsaWebParameters.setInputFile(inputFilePath.toString());
				}
			}
			
			String webApplicationUri = request.getRequestURL().toString().substring(0,
					request.getRequestURL().toString().indexOf(request.getServletPath()));

			AlignmentRequestManager.getInstance().startManageRequest(webApplicationUri, newRequest);

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
