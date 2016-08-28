package com.salsaw.msalsa.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClustalFileContentSplitter {
	private final List<ClustalFileSection> clustalFileSections;

	// CONSTRUCTOR
	public ClustalFileContentSplitter(String clustalFilePath) throws IOException {
		this.clustalFileSections = new ArrayList<>();
		readInputSequences(clustalFilePath);
	}

	// GET
	public List<ClustalFileSection> getClustalFileSections() {
		return this.clustalFileSections;
	}
	
	private void readInputSequences(String clustalFilePath) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(clustalFilePath))) {
			String line;
			int lineIndex = 0;

			ArrayList<String> sequencesHeaders = new ArrayList<>();
			ArrayList<String> sequences = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				// Skip first comment line
				if (lineIndex != 0) {
					if (line.trim().isEmpty() == true) {
						// Store previous section and start a new one
						updateFileSection(sequencesHeaders, sequences);
						sequencesHeaders = new ArrayList<>();
						sequences = new ArrayList<>();
					} else {
						int indexFirstSpace = line.indexOf(' ');
						String proteinName = line.substring(0, indexFirstSpace);
						String proteinSequence = line.substring(indexFirstSpace, line.length()).trim();
						sequencesHeaders.add(proteinName);
						sequences.add(proteinSequence);
					}
				}

				lineIndex++;
			}
			
			// Ensure to save also last section data
			updateFileSection(sequencesHeaders, sequences);
		}
	}
	
	private void updateFileSection(List<String> sequencesHeaders, List<String> sequences){
		if (sequencesHeaders.size() != 0) {
			ClustalFileSection clustalFileSection = new ClustalFileSection(
					sequencesHeaders.stream().toArray(String[]::new),
					sequences.stream().toArray(String[]::new));
			this.clustalFileSections.add(clustalFileSection);
		}
	}
	
}
