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

/*
* main.cpp
*
*  Created on: 07/ago/2014
*      Author: Alessandro Daniele
*/

#include "DistanceMatrix.h"
#include "Node.h"
#include "Tree.h"
#include "Alignment.h"
#include "SubstitutionMatrix.h"
#include "LocalSearch.h"
#include <stdlib.h>
#include <string>

int main(int argc, char **argv) {
	try {
		//Mandatory parameters
		const char* inputFile = "";
		const char* outputFile = "";
		const char* phTreeFile = "";

		//Optional parameters
		int gamma = 30;
		int minIterations = 1000;
		float GOP = 8;
		float GEP = 5;
		float probabilityOfSplit = 0.1;
		const char* type = "PROTEINS";
		string subMatrix = "";
		string subMatrixPath = "";
		const char* distanceMatrix = "";
		const char* matrixSerie = "BLOSUM";
		terminalGAPsStrategy t = ONLY_GEP;

		for (int i = 1; i<argc; i += 2) {
			if (string(argv[i]) == "-inputFile") {
				inputFile = argv[i + 1];
				continue;
			}
			else if (string(argv[i]) == "-outputFile") {
				outputFile = argv[i + 1];
				continue;
			}
			else if (string(argv[i]) == "-phTreeFile") {
				phTreeFile = argv[i + 1];
				continue;
			}
			else if (string(argv[i]) == "-pSplit") {
				probabilityOfSplit = atof(argv[i + 1]);
				continue;
			}
			else if (string(argv[i]) == "-GOP") {
				GOP = atof(argv[i + 1]);
				continue;
			}
			else if (string(argv[i]) == "-GEP") {
				GEP = atof(argv[i + 1]);
				continue;
			}
			else if (string(argv[i]) == "-type") {
				type = argv[i + 1];
				continue;
			}
			else if (string(argv[i]) == "-scoringMatrix") {
				subMatrix = string(argv[i + 1]);
				continue;
			}
			else if (string(argv[i]) == "-scoringMatrixPath") {
				subMatrixPath = string(argv[i + 1]);
				continue;
			}
			else if (string(argv[i]) == "-distanceMatrix") {
				distanceMatrix = argv[i + 1];
				continue;
			}
			else if (string(argv[i]) == "-matrixSerie") {
				matrixSerie = argv[i + 1];
				continue;
			}
			else if (string(argv[i]) == "-gamma") {
				gamma = atoi(argv[i + 1]);
				continue;
			}
			else if (string(argv[i]) == "-minIt") {
				minIterations = atoi(argv[i + 1]);
				continue;
			}
			else if (string(argv[i]) == "-terminal") {
				if (string(argv[i + 1]) == "BOTH") t = BOTH_PENALTIES;
				else if (string(argv[i + 1]) != "ONLY_GEP") throw("Syntax error: " + string(argv[i + 1]) +
					"is not a valid value for terminal strategy. Valid values are: \n"
					"   1) ONLY_GEP: GOP=0 only for terminal GAPs (default) \n"
					"   2) BOTH: both opening and extension penalty for terminal GAPs \n");
				continue;
			}
			else throw("Syntax error: " + string(argv[i]) + " is not a valid option. Possible options are: \n"
				"  -inputFile:         The FASTA file to process \n"
				"  -outputFile:        The FASTA produced after the process \n"
				"  -phTreeFile:        The ph or dnd file that contains the phylogenetic tree \n"
				"  -GOP:               GAP Opening Penalty \n"
				"  -GEP:               GAP Extension Penalty \n"
				"  -gamma:             dimension of the range of positions for a GAP during an iteration \n"
				"  -type:              type of sequences. Possible options are DNA, RNA and PROTEINS (default PROTEINS) \n"
				"  -scoringMatrix:     scoring matrix \n"
				"  -scoringMatrixPath: scoring matrix file path. For more information on scoring matrix file format visit M-SALSA wiki at https://github.com/SALSA-W/M-SALSA/wiki \n"
				"  -matrixSerie:       matrix serie. Possible options: BLOSUM or PAM (default BLOSUM) \n"
				"  -distanceMatrix:    distance matrix file \n"
				"  -minIt:             minimum number of iterations \n"
				"  -pSplit:            probability of split \n"
				"  -terminal:          the strategy to be used to manage terminal GAPs. Possible values: \n"
				"                       1) ONLY_GEP: GOP=0 only for terminal GAPs (default) \n"
				"                       2) BOTH: both opening and extension penalty for terminal GAPs \n");
		}
		
		if (!strcmp(inputFile, "")) throw("Input file not specified");
		if (!strcmp(outputFile, "")) throw("Output file not specified");
		if (!strcmp(phTreeFile, "")) throw("phTreeFile option not specified");

		SubstitutionMatrix* matrix = NULL;

		if (subMatrix != "") {
			if (subMatrixPath != "") throw("Error: specified both scoring matrix and scoring matrix path");
			if (strcmp(distanceMatrix, "")) throw("Error: specified both scoring matrix and distance matrix");

			matrix = new SubstitutionMatrix(subMatrix, GEP);
		}
		else {
			if (strcmp(distanceMatrix, "")) { //If distance matrix is specified
				if (subMatrixPath != "") throw("Error: specified both scoring matrix path and distance matrix");
				if (strcmp(type, "PROTEINS")) throw("Error: distance matrix can not be used with sequence type " + string(type));

				DistanceMatrix* dm = new DistanceMatrix(distanceMatrix);
				matrix = dm->createSubstitutionMatrix(matrixSerie, GEP);
			}
			else {
				if (subMatrixPath != "") {
					matrix = new SubstitutionMatrix(subMatrixPath.c_str(), new Alphabet(type), GEP);
				}
				else { //Nothing is specified. Default matrix
					if (!strcmp(type, "RNA")) throw("Error: no default matrix for RNA sequences");
					if (!strcmp(type, "DNA")) subMatrix = "IUB";
					if (!strcmp(type, "PROTEINS")) subMatrix = "BLOSUM62";

					matrix = new SubstitutionMatrix(subMatrix, GEP);
				}
			}
		}

		Alignment* a = new Alignment(inputFile, phTreeFile, matrix, GOP, t);
		LocalSearch* l = new LocalSearch(a, gamma, minIterations, probabilityOfSplit);

		cout << "Initial value of WSP-Score: " << a->WSP() << endl;
		cout << "Starting optimization..." << endl << endl;
		a=l->execute();
		cout << "Job completed!" << endl << "New value of WSP-Score: " << a->WSP() << endl;

		a->save(outputFile);
		delete l;
		delete a;
	}
	catch (const char* s) {
		cout << s << endl;
	}
	catch (string& s) {
		cout << s << endl;
	}
}
