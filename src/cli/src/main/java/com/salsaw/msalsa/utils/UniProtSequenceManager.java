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
package com.salsaw.msalsa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.salsaw.msalsa.algorithm.Constants;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class UniProtSequenceManager {
	
	private final String inputFileFolder;
	private final Set<String> uniProtIds;
	private Path generatedInputFile;
	
	public UniProtSequenceManager(String inputFileFolder, String[] uniProtIds){
		if (inputFileFolder == null) {
			throw new IllegalArgumentException("inputFileFolder");
		}
		if (uniProtIds == null) {
			throw new IllegalArgumentException("uniProtIds");
		}
		
		this.inputFileFolder = inputFileFolder;
		// Use hash set to ensure no duplicated values
		this.uniProtIds = new HashSet<String>();		
		for (String uniProtId : uniProtIds) {
			this.uniProtIds.add(uniProtId);
		}
	}
	
	public Path getGeneratedInputFile(){
		return this.generatedInputFile;
	}

	public void composeInputFromId() throws IOException  {

		// idea from http://grepcode.com/file_/repo1.maven.org/maven2/org.biojava/biojava-alignment/4.1.0/demo/DemoAlignProteins.java/?v=source
		
		StringBuilder inputFileBuilder = new StringBuilder();
		for (String uniProtId : this.uniProtIds) {
			URL uniprotFasta = new URL(String.format("http://www.uniprot.org/uniprot/%s.fasta", uniProtId));
			try (InputStream fastaSequenceInputStream = uniprotFasta.openStream()) {
				// see http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
				try(Scanner fastaSequenceScanner = new java.util.Scanner(fastaSequenceInputStream)){
					try(Scanner fastaSequenceDelimetedScanner = fastaSequenceScanner.useDelimiter("\\A")){
						while (fastaSequenceDelimetedScanner.hasNext()) {
							inputFileBuilder.append(fastaSequenceDelimetedScanner.next());
						}
					}
				}				
			}
		}
		
		// Compose file name from id and save
		String fileName = String.join("-", this.uniProtIds) + Constants.FASTA_FILE_EXSTENSION;
		this.generatedInputFile = Paths.get(this.inputFileFolder, fileName);
		FileUtils.writeStringToFile(this.generatedInputFile.toFile(), inputFileBuilder.toString());
	}

}
