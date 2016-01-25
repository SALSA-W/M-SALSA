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

/**
 * A terminal GAP is a GAP in the beginning or in the end of a sequence. Usually, the penalties for a
 * terminal GAP is different from the others.
 * 
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public enum TerminalGAPsStrategy {
	/**
	 * as other GAPs, a terminal GAP has both GOP and GEP
	 */
	BOTH_PENALTIES,
	/**
	 * there is no penalty for opening a terminal GAP, only for extending it
	 */
	ONLY_GEP;
	
    public static final TerminalGAPsStrategy fromString(String strategy) {
 
        for(TerminalGAPsStrategy terminalGAPsStrategy : TerminalGAPsStrategy.values()) {
            if(terminalGAPsStrategy.toString().equalsIgnoreCase(strategy)) {
                return terminalGAPsStrategy;
            }
        }
 
        return null;
    }
}
