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
	if (argc<4) {
		cout << "Syntax error: not enought parameters. Correct syntax:" << endl;
		cout << "  ./M-SALSA input_file output_file guide_tree_file [OPTIONS]" << endl;
		cout << endl << "Possible options are: \n"
			"  -GOP:            GAP Opening Penalty \n"
			"  -GEP:            GAP Extension Penalty \n"
			"  -gamma:          dimension of the range of positions for a GAP during an iteration \n"
			"  -type:           type of sequences. Possible options are DNA, RNA and PROTEINS (default PROTEINS) \n"
			"  -scoringMatrix:  scoring matrix file\n"
			"  -matrixSerie:    matrix serie. Possible options: BLOSUM or PAM (default BLOSUM) \n"
			"  -distanceMatrix: distance matrix file \n"
			"  -minIt:          minimum number of iterations \n"
			"  -pSplit:         probability of split \n"
			"  -terminal:       the strategy to be used to manage terminal GAPs. Possible values: \n"
			"                   1) ONLY_GEP: GOP=0 only for terminal GAPs (default) \n"
			"                   2) BOTH: both opening and extension penalty for terminal GAPs \n" << endl;
	}
	else {
		try {
			int gamma = 30;
			int minIterations = 1000;
			float GOP = 8;
			float GEP = 5;
			float probabilityOfSplit = 0.1;
			const char* type = "PROTEINS";
			const char* subMatrix = "";
			const char* distanceMatrix = "";
			const char* matrixSerie = "BLOSUM";
			terminalGAPsStrategy t = ONLY_GEP;

			for (int i = 4; i<argc; i += 2) {
				if (string(argv[i]) == "-pSplit") {
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
					subMatrix = argv[i + 1];
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
					"  -GOP:            GAP Opening Penalty \n"
					"  -GEP:            GAP Extension Penalty \n"
					"  -gamma:          dimension of the range of positions for a GAP during an iteration \n"
					"  -type:           type of sequences. Possible options are DNA, RNA and PROTEINS (default PROTEINS) \n"
					"  -scoringMatrix:  scoring matrix file\n"
					"  -matrixSerie:    matrix serie. Possible options: BLOSUM or PAM (default BLOSUM) \n"
					"  -distanceMatrix: distance matrix file \n"
					"  -minIt:          minimum number of iterations \n"
					"  -pSplit:         probability of split \n"
					"  -terminal:       the strategy to be used to manage terminal GAPs. Possible values: \n"
					"                   1) ONLY_GEP: GOP=0 only for terminal GAPs (default) \n"
					"                   2) BOTH: both opening and extension penalty for terminal GAPs \n");
			}
			
			Alignment* a = NULL;
			SubstitutionMatrix* matrix = NULL;

			if (strcmp(distanceMatrix, "") && !strcmp(type, "PROTEINS")) { //If distance matrix is specified and we are dealing with proteins
				DistanceMatrix* dm = new DistanceMatrix(distanceMatrix);
				matrix = dm->createSubstitutionMatrix(matrixSerie, GEP);

				a = new Alignment(argv[1], argv[3], matrix, GOP, t);
			}
			else { //It is not possible to calculate the correct substitution matrix: we use the default value or the one specified by the user
				//Default value for substitution matrix
				if (!strcmp(type, "DNA") && !strcmp(subMatrix, "")) subMatrix = "IUB";
				if (!strcmp(type, "PROTEINS") && !strcmp(subMatrix, "")) subMatrix = "BLOSUM62";
				
				if (!strcmp(subMatrix, "")) throw("Substitution matrix is not specified and no default value exists for the specified type of sequences");
				
				matrix = new SubstitutionMatrix(subMatrix, new Alphabet(type), GEP);
				a = new Alignment(argv[1], argv[3], matrix, GOP, t);
			}
			
			LocalSearch* l = new LocalSearch(a, gamma, minIterations, probabilityOfSplit);

			cout << "Initial value of WSP-Score: " << a->WSP() << endl;
			cout << "Starting optimization..." << endl << endl;
			a=l->execute();
			cout << "Job completed!" << endl << "New value of WSP-Score: " << a->WSP() << endl;

			a->save(argv[2]);
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
}
