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
					salsaParameters.getScoringMatrix(), 23,
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
