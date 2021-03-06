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
package com.salsaw.msalsa.algorithm.enums;

/**
 * For documentation see @see <a href="https://www.ddbj.nig.ac.jp/search/help/clustalwhelp-e.html">clustal help</a>
 * For resource see @see <a href="https://github.com/noporpoise/seq-align/tree/master/scoring">scoring matix source</a>
 * 
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public enum EmbeddedScoringMatrix {
	NONE,
	// Proteins
	/**
	 * Gonnet -These matrices were derived using almost the same procedure as the Dayhoff one (above) but are much more up to date and are based on a far larger data set. They appear to be more sensitive than the Dayhoff series.
	 */
	Gonnet,
	/**
	 * BLOSUM - These matrices appear to be the best available for carrying out data base similarity (homology searches).
	 */
	BLOSUM30,
	BLOSUM45,
	BLOSUM50,
	BLOSUM62,
	BLOSUM80,
	PAM20,
	PAM60,
	PAM120,
	PAM250,
	PAM350,
	// DNA
 	/**
 	 * This is the default scoring matrix used by BESTFIT for the comparison of nucleic acid sequences. X's and N's are treated as matches to any IUB ambiguity symbol. All matches score 1.9; all mismatches for IUB symbols score 0.
 	 */
	IUB,
	/**
	 * Matches score 1.0 and mismatches score 0. All matches for IUB symbols also score 0. 
	 */
	DNAClustal,
	RNAClustal,
	;
	
    // converter that will be used later
    public static final EmbeddedScoringMatrix fromString(String matrixName) {
 
        for(EmbeddedScoringMatrix scoringMatrix : EmbeddedScoringMatrix.values()) {
            if(scoringMatrix.toString().equalsIgnoreCase(matrixName)) {
                return scoringMatrix;
            }
        }
 
        return null;
    }
}
