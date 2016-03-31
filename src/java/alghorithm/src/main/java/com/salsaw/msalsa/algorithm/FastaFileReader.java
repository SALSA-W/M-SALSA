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
package com.salsaw.msalsa.algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Read the content of a FASTA file
 * 
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class FastaFileReader {
	// FIELDS
	private final String filePath;
	private final Collection<String> sequencesHeaders = new ArrayList<>();
	private final ArrayList<String> sequences = new ArrayList<>();
	
	// CONSTRUCTOR
	public FastaFileReader(String filePath) throws IOException{
		this.filePath = filePath;
		readInputSequences();
	}	
	
	// GET / SET
	/**
	 * The description of the sequence
	 * 
	 * @return
	 */
	public Collection<String> getSequencesHeaders() {
		return sequencesHeaders;
	}
	
	/**
	 * The sequence content
	 * 
	 * @return
	 */
	public ArrayList<String> getSequences() {
		return sequences;
	}

	// METHODS
	/**
	 * Reads sequences list (as a strings' vector) from a FASTA file
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	private void readInputSequences()
			throws IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(this.filePath))) {
			String line;
			StringBuffer contentBuffer = new StringBuffer();

			// https://github.com/joewandy/BioinfoApp/blob/master/src/com/joewandy/bioinfoapp/model/core/io/FastaReader.java
			while ((line = in.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty()) {
					continue;
				}
				char firstChar = line.charAt(0);

				if (firstChar == '>') {
					if (contentBuffer.length() != 0) {
						// save the previous sequence read
						sequences.add(contentBuffer.toString());
					}

					// now can get the new id > ..
					sequencesHeaders.add(line.substring(1).trim());

					// start a new content buffer
					contentBuffer = new StringBuffer();

				} else if (firstChar == ';') {
					// comment line, skip it
				} else {
					// carry on reading sequence content
					contentBuffer.append(line.trim());
				}
			}

			if (contentBuffer.length() != 0) {
				// save the last sequence
				sequences.add(contentBuffer.toString());
			}
		}
	}
}
