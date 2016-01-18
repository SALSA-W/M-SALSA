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
package com.salsaw.msalsa.algorithm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class DistanceMatrix {
	
	int numberOfSequences;
	float[] distMatrix;
	String[] names;

	public DistanceMatrix(final InputStream distanceMatrixStream) {				
		try(Scanner scanner = new Scanner(distanceMatrixStream))
		{
			// First line contains the number of sequences
			this.numberOfSequences = scanner.nextInt();
			this.distMatrix = new float[numberOfSequences*numberOfSequences];
			this.names = new String[numberOfSequences];
								
			// Read the values for the matrix value (the matrix is alphabet x alphabet)
			for (int i=0; i<this.numberOfSequences;i++){
				// Read name
				names[i] = scanner.next();
				for (int j=0; j<this.numberOfSequences; j++){
					// Read value of distance matrix
					distMatrix[i*numberOfSequences + j] = scanner.nextInt();
				}
			}
		}			
	}
	
	float similarity(String name1, String name2) throws SALSAException {
		Integer index1 = null, index2 = null;

		for (int i = 0; i < numberOfSequences; i++) {
			if (this.names[i].equals(name1) || (">" + this.names[i]).equals(name1)){
				index1 = i;
			}
			if (this.names[i] == name2 || (">" + this.names[i]).equals(name2)){
				index2 = i;
			}
		}
		
		if (index1 == null ||
			index2 == null){
			throw new SALSAException("Unable to find similarity indexes");
		}			

		return 1 - distMatrix[index1 * numberOfSequences + index2];
	}

	SubstitutionMatrix createSubstitutionMatrix(final String matrixSerie, float GEP) throws SALSAException, IOException {
		float pid = 0.0f;

		for (int i = 0; i < numberOfSequences; i++) {
			for (int j = i + 1; j < numberOfSequences; j++) {
				pid += distMatrix[i*numberOfSequences + j];
			}
		}

		pid /= numberOfSequences * (numberOfSequences - 1) / 2;
		return SubstitutionMatrix.getSubstitutionMatrix(matrixSerie, pid, GEP, null);
	}	
}
