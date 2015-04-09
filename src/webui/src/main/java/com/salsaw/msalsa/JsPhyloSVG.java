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

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

/**
 * {@link https://vaadin.com/blog/-/blogs/vaadin-7-loves-javascript-components} 
 * 
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 */
@JavaScript({
    /*
     * jsPhyloSVG
     */
    "vaadin://js/jsphylosvg-min.js",
    "vaadin://js/raphael-min.js",
    "vaadin://js/newick.js",
})
public class JsPhyloSVG extends AbstractJavaScriptComponent {

	private static final long serialVersionUID = 1L;

	public JsPhyloSVG() {
    	
		String newickTree = "((1j46_A:0.34060,2lef_A:0.40639):0.05637,1k99_A:0.37389,1aab_:0.44092);";
		this.getState().setNewickTree(newickTree);
    }
    
    @Override
    public JsPhyloSVGState getState() {
    	return (JsPhyloSVGState) super.getState();
    }
}
