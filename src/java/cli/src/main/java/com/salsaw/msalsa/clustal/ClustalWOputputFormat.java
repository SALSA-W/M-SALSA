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
package com.salsaw.msalsa.clustal;

public enum ClustalWOputputFormat {
	/**
	 * The default Clustal output format
	 */
	CLUSTAL("CLUSTAL"),
	GCG("GCG"),
	GDE("GDE"),
	PHYLIP("PHYLIP"),
	PIR("PIR"),
	NEXUS("NEXUS"),
	FASTA("FASTA"),
    ;

    private final String text;

    ClustalWOputputFormat(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
