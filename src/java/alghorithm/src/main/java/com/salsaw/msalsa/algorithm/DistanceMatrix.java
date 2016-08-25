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
import java.util.Locale;
import java.util.Scanner;

import com.salsaw.msalsa.algorithm.enums.MatrixSerie;
import com.salsaw.msalsa.algorithm.exceptions.SALSAException;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public final class DistanceMatrix {

	private final int numberOfSequences;
	private final double[] distMatrix;
	private final String[] names;

	public DistanceMatrix(final InputStream distanceMatrixStream) {
		try (Scanner scanner = new Scanner(distanceMatrixStream)) {
			// First line contains the number of sequences
			this.numberOfSequences = scanner.nextInt();
			this.distMatrix = new double[numberOfSequences * numberOfSequences];
			this.names = new String[numberOfSequences];

			// Read the values for the matrix value (the matrix is numberOfSequences x numberOfSequences)
			// Set US culture
			scanner.useLocale(Locale.US);
			for (int i = 0; i < this.numberOfSequences; i++) {
				// Read name
				names[i] = scanner.next();
				for (int j = 0; j < this.numberOfSequences; j++) {
					// Read value of distance matrix
					distMatrix[i * numberOfSequences + j] = scanner.nextFloat();
				}
			}
		}
	}

	/**
	 * It calculates the average percentage of identity of the sequences and based on that it choose the correct substitution matrix
	 * 
	 * @param matrixSerie
	 * @param GEP
	 * @return
	 * @throws SALSAException
	 * @throws IOException
	 */
	public final SubstitutionMatrix createSubstitutionMatrix(final MatrixSerie matrixSerie, double GEP)
			throws SALSAException, IOException {
		double pid = 0.0f;

		for (int i = 0; i < this.numberOfSequences; i++) {
			for (int j = i + 1; j < this.numberOfSequences; j++) {
				pid += 1.0f - this.distMatrix[i * this.numberOfSequences + j];
			}
		}

		pid /= this.numberOfSequences * (this.numberOfSequences - 1) / 2;
		return SubstitutionMatrix.getSubstitutionMatrix(SubstitutionMatrix.getEmbeddedSubstitutionMatrix(matrixSerie, pid), GEP);
	}
}
