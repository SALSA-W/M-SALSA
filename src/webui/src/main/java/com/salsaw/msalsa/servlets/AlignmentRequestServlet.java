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

import com.salsaw.msalsa.datamodel.AlignmentRequest;
import com.salsaw.msalsa.datamodel.SalsaWebParameters;
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

	class EnumAwareConvertUtilsBean extends ConvertUtilsBean {
		// http://www.bitsandpix.com/entry/java-beanutils-enum-support-generic-enum-converter/
		private final EnumConverter enumConverter = new EnumConverter();

		public Converter lookup(Class clazz) {
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
			BeanUtilsBean beanUtils = new BeanUtilsBean(enumAwareConvertUtilsBean);

			beanUtils.populate(salsaWebParameters, request.getParameterMap());

			AlignmentRequest newRequest = new AlignmentRequest(salsaWebParameters);

			Part filePart = request.getPart("inputFile");
			String fileName = filePart.getSubmittedFileName();
			if (fileName.isEmpty())	{
				throw new ServletException("fileName empty");
			}
			
			try (InputStream inputAlignmentFileContet = filePart.getInputStream()) {

				File requestProcessFolder = AlignmentRequestManager.getInstance()
						.getServerAligmentFolder(newRequest.getId()).toFile();

				// if the directory does not exist, create it
				if (requestProcessFolder.exists() == false) {
					requestProcessFolder.mkdir();
				}

				// Open the file for writing.
				Path inputFilePath = Paths.get(requestProcessFolder.toString(), fileName);
				Files.copy(inputAlignmentFileContet, inputFilePath);
				salsaWebParameters.setInputFile(inputFilePath.toString());
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
