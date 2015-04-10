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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.salsaw.msalsa.clustal.ClustalFileMapper;
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
	
	public PhylogeneticTreeView(ClustalFileMapper clustalFileMapper) throws IOException {			
		initializeUiComponents();
						
		// Add JavaScript component to generate phylogentic tree
		JsPhyloSVG jsPhyloSVG = new JsPhyloSVG(getPhylogeneticTreeFileContent(clustalFileMapper));
		mainLayout.addComponent(jsPhyloSVG);
		
		setCompositionRoot(mainLayout);
	}
	
	private String getPhylogeneticTreeFileContent(
			ClustalFileMapper clustalFileMapper) throws IOException {
		
		List<String> lines = Files.readAllLines(
				Paths.get(clustalFileMapper.getPhylogeneticTreeFile()),
				StandardCharsets.UTF_8);		
		StringBuilder newickTreeBuilder = new StringBuilder();		
		for (String line : lines) {
			newickTreeBuilder.append(line.trim());
        }
		
		return newickTreeBuilder.toString();
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
