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
package com.salsaw.msalsa;

import com.vaadin.shared.ui.JavaScriptComponentState;

/**
 * Shared state between client-side javascript and server-side vaadin.
 * State of JsPhyloSVG library.
 * 
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 */
public class JsPhyloSVGState extends JavaScriptComponentState {
	
	private static final long serialVersionUID = 1L;	
	public static final String DIV_ID = "svgCanvas";
	
	public final String divId = DIV_ID;
	/**
	 * The height in pixel based on the number of charters in the tree
	 */
	public int svgHeight;
	/**
	 * The width in pixel based on the number of charters in the tree
	 */	
	public int svgWidth;
	
	private String newickTree;
	
	public JsPhyloSVGState() {
	}

    public String getNewickTree() {
        return this.newickTree;
    }

    public void setNewickTree(String newickTree) {
        this.newickTree = newickTree;
        
        // Calculate height and width
        this.svgHeight = (int) (0.9173 * this.newickTree.length() + 494.82);
        this.svgWidth = (int) (1.8565 * this.newickTree.length() + 152.22);
    } 
}
