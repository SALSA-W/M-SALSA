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

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 */
public class PhylogeneticTreeView extends CustomComponent implements View {
	
	private GridLayout mainLayout;
	
	private Label svgHTMLPhylogenticTree;
	
	private static final long serialVersionUID = 1L;
	
	public PhylogeneticTreeView() {			
		initializeUiComponents();
		
		// Add JavaScript component to generate phylogentic tree
		JsPhyloSVG jsPhyloSVG = new JsPhyloSVG("((1j46_A:0.34060,2lef_A:0.40639):0.05637,1k99_A:0.37389,1aab_:0.44092);");
		mainLayout.addComponent(jsPhyloSVG);
		
		setCompositionRoot(mainLayout);
	}
	
	private GridLayout initializeUiComponents(){
		mainLayout = new GridLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
				
		// Add and center with HTML div
		svgHTMLPhylogenticTree = new Label("<div id='svgCanvas'></div>", ContentMode.HTML);
		svgHTMLPhylogenticTree.setWidth("-1px");
		svgHTMLPhylogenticTree.setHeight("-1px");
		mainLayout.addComponent(svgHTMLPhylogenticTree);
		mainLayout.setComponentAlignment(svgHTMLPhylogenticTree,Alignment.MIDDLE_CENTER);
		
		return mainLayout;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
