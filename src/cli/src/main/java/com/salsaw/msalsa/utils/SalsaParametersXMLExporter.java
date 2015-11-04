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
package com.salsaw.msalsa.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.salsaw.msalsa.cli.SalsaParameters;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
// http://www.mkyong.com/java/jaxb-hello-world-example/
public class SalsaParametersXMLExporter {
	
	public static final String EXTENSION = ".xml";
	public static final String FILE_NAME = "salsa-config" + EXTENSION;

	/**
	 * Write the data passed to a file
	 * 
	 * @param salsaParameters
	 * @param yamlPath
	 * @throws IOException
	 * @throws JAXBException 
	 */
	public void exportSalsaParameters(SalsaParameters salsaParameters, String xmlFilePath) throws IOException, JAXBException {
		try (Writer writer = new BufferedWriter(new FileWriter(xmlFilePath, false))) {
			JAXBContext jaxbContext = JAXBContext.newInstance(SalsaParameters.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(salsaParameters, writer);
		}	
	}
	
	/**
	 * Load data from a file
	 * 
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws JAXBException 
	 */
	public SalsaParameters importSalsaParameters(String xmlFilePath) throws FileNotFoundException, IOException, JAXBException{
		try(InputStream input = new FileInputStream(new File(xmlFilePath))){		    
			JAXBContext jaxbContext = JAXBContext.newInstance(SalsaParameters.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (SalsaParameters) jaxbUnmarshaller.unmarshal(input);
		}
	}
}
