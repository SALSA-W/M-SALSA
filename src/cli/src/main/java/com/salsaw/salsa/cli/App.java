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
package com.salsaw.salsa.cli;

import com.beust.jcommander.JCommander;
import com.salsaw.salsa.algorithm.Alignment;
import com.salsaw.salsa.algorithm.LocalSearch;
import com.salsaw.salsa.algorithm.SubstitutionMatrix;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		SalsaParameters salsaParameters = new SalsaParameters();
		JCommander commands = new JCommander(salsaParameters);

		try {

			commands.parse(args);

			SubstitutionMatrix matrix = new SubstitutionMatrix(
					salsaParameters.getScoringMatrix(),
					salsaParameters.getGEP());

			Alignment a = new Alignment(salsaParameters.getInputFile(),
					salsaParameters.getPhylogeneticTreeFile(), matrix,
					salsaParameters.getGOP(),
					salsaParameters.getTerminalGAPsStrategy());

			LocalSearch l = new LocalSearch(a, salsaParameters.getGamma(),
					salsaParameters.getMinIterations(),
					salsaParameters.getProbabilityOfSplit());

			// cout<<"WSP-SCore prima: "<<a->WSP()<<endl;
			a = l.execute();
			// cout<<"WSP-SCore dopo: "<<a->WSP()<<endl;

			a.save(salsaParameters.getOutputFile());

		} catch (Exception e) {
			e.printStackTrace();
			commands.usage();
		}
	}
}
