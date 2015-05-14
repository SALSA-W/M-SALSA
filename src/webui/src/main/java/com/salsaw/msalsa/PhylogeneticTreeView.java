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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.salsaw.msalsa.clustal.ClustalFileMapper;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TextArea;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 */
public class PhylogeneticTreeView extends CustomComponent implements View {
	
	private GridLayout mainLayout;
	
	private Label title;
	
	private Label svgHTMLPhylogenticTree;
	
	private static final long serialVersionUID = 1L;
	
	public PhylogeneticTreeView(ClustalFileMapper clustalFileMapper) throws IOException {			
		initializeUiComponents();
		
		HorizontalLayout  buttonsLayout = new HorizontalLayout ();
		buttonsLayout.setSpacing(true);
		mainLayout.addComponent(buttonsLayout);
		mainLayout.setComponentAlignment(buttonsLayout,  Alignment.MIDDLE_CENTER);
		
		// Download alignment file
		Button aligmentButton = new Button("Download alignment");
		Resource resAlignment = new FileResource(new File(clustalFileMapper.getAlignmentFilePath()));
		FileDownloader fdAln = new FileDownloader(resAlignment);
		fdAln.extend(aligmentButton);
		buttonsLayout.addComponent(aligmentButton);
		
		// Download tree file
		Button downloadTreeButton = new Button("Download phylogentic tree");
		Resource resTree = new FileResource(new File(clustalFileMapper.getTreeFilePath()));
		FileDownloader fdTree = new FileDownloader(resTree);
		fdTree.extend(downloadTreeButton);
		buttonsLayout.addComponent(downloadTreeButton);
		
		TabSheet tabsheet = new TabSheet();
		mainLayout.addComponent(tabsheet);
		mainLayout.setComponentAlignment(tabsheet, Alignment.MIDDLE_CENTER);
		
		// Add tab with aligment content
		String aligmentFileContent =  new String(Files.readAllBytes(Paths.get(clustalFileMapper.getAlignmentFilePath())));
		TextArea aligmentFileTextArea = new TextArea("M-SALSA Aligment");
		aligmentFileTextArea.setWordwrap(false);
		aligmentFileTextArea.setValue(aligmentFileContent);
		aligmentFileTextArea.setWidth("100%");
		aligmentFileTextArea.setHeight("100%");
		tabsheet.addTab(aligmentFileTextArea, "Alignments");		
		
		// Add and center with HTML div
		svgHTMLPhylogenticTree = new Label("<div id='svgCanvas'></div>", ContentMode.HTML);
		svgHTMLPhylogenticTree.setWidth("-1px");
		svgHTMLPhylogenticTree.setHeight("-1px");		
		tabsheet.addTab(svgHTMLPhylogenticTree, "Phylogenetic Tree");	
		
		// Add JavaScript component to generate phylogentic tree
		String newickTree = getPhylogeneticTreeFileContent(clustalFileMapper);
		JsPhyloSVG jsPhyloSVG = new JsPhyloSVG(newickTree);
		mainLayout.addComponent(jsPhyloSVG);
		
		tabsheet.addSelectedTabChangeListener(new SelectedTabChangeListener(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (event.getTabSheet().getSelectedTab() == svgHTMLPhylogenticTree)
				{
					// Force the call of JavaScript when Phylogenetic Tree tab is selected
					jsPhyloSVG.markAsDirty();
				}				
			}});
		
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
		
		// title
		title = new Label();
		title.setImmediate(false);
		title.setWidth("-1px");
		title.setHeight("-1px");
		title.setValue("M-SALSA");
		mainLayout.addComponent(title);
		mainLayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);			
			
		return mainLayout;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
