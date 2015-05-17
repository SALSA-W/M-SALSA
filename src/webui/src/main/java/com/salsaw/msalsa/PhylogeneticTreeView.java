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
import com.salsaw.msalsa.cli.SalsaAlgorithmExecutor;
import com.salsaw.msalsa.config.ConfigurationManager;
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
	
	private String msalsaAligmentFilePath;
	private String msalsaPhylogeneticTreeFilePath;
	
	private static final long serialVersionUID = 1L;
	
	public PhylogeneticTreeView() throws IOException {			
		initializeUiComponents();
		setCompositionRoot(mainLayout);
	}

	private String getPhylogeneticTreeFileContent(
			String phylogeneticTreeFilePath) throws IOException {
		
		List<String> lines = Files.readAllLines(
				Paths.get(phylogeneticTreeFilePath),
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
		
	private void initializeUiComponentsAfterEnter() throws IOException{		
		HorizontalLayout  buttonsLayout = new HorizontalLayout ();
		buttonsLayout.setSpacing(true);
		mainLayout.addComponent(buttonsLayout);
		mainLayout.setComponentAlignment(buttonsLayout,  Alignment.MIDDLE_CENTER);
		
		// Download alignment file
		Button aligmentButton = new Button("Download alignment");
		Resource resAlignment = new FileResource(new File(this.msalsaAligmentFilePath));
		FileDownloader fdAln = new FileDownloader(resAlignment);
		fdAln.extend(aligmentButton);
		buttonsLayout.addComponent(aligmentButton);
		
		// Download tree file
		Button downloadTreeButton = new Button("Download phylogentic tree");
		Resource resTree = new FileResource(new File(this.msalsaPhylogeneticTreeFilePath));
		FileDownloader fdTree = new FileDownloader(resTree);
		fdTree.extend(downloadTreeButton);
		buttonsLayout.addComponent(downloadTreeButton);
		
		TabSheet tabsheet = new TabSheet();
		mainLayout.addComponent(tabsheet);
		mainLayout.setComponentAlignment(tabsheet, Alignment.MIDDLE_CENTER);
		
		// Add tab with aligment content
		String aligmentFileContent =  new String(Files.readAllBytes(Paths.get(this.msalsaAligmentFilePath)));
		TextArea aligmentFileTextArea = new TextArea("M-SALSA Aligment");
		aligmentFileTextArea.setWordwrap(false);
		aligmentFileTextArea.setValue(aligmentFileContent);
		aligmentFileTextArea.setWidth("100%");
		aligmentFileTextArea.setHeight("700px");
		tabsheet.addTab(aligmentFileTextArea, "Alignments");		
		
		// Add and center with HTML div
		svgHTMLPhylogenticTree = new Label("<div id='"+ JsPhyloSVGState.DIV_ID + "'></div>", ContentMode.HTML);
		svgHTMLPhylogenticTree.setWidth("-1px");
		svgHTMLPhylogenticTree.setHeight("-1px");		
		tabsheet.addTab(svgHTMLPhylogenticTree, "Phylogenetic Tree");	
		
		// Add JavaScript component to generate phylogentic tree
		String newickTree = getPhylogeneticTreeFileContent(this.msalsaPhylogeneticTreeFilePath);
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
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		try {
			initSalsaData(event.getParameters());
			initializeUiComponentsAfterEnter();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        		
	}

	private void initSalsaData(String idProccedRequest) throws IOException, IllegalStateException{
		// Get the folder where the files are stored
		File processedRequestFolder = new File(Paths.get(
				ConfigurationManager.getInstance().getServerConfiguration().getTemporaryFilePath(),
				idProccedRequest).toString());
		File[] listOfFiles = processedRequestFolder.listFiles();
		
		String msalsaAligmentFilePath = null;
		String msalsaPhylogeneticTreeFilePath = null; 
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	// Search SALSA aligmnet and tree files
		        if (file.getName().endsWith(SalsaAlgorithmExecutor.SALSA_ALIGMENT_SUFFIX)){
		        	msalsaAligmentFilePath = file.getAbsolutePath();
		        	continue;
		        }
		        if (file.getName().endsWith(SalsaAlgorithmExecutor.SALSA_TREE_SUFFIX))
		        {
		        	msalsaPhylogeneticTreeFilePath = file.getAbsolutePath();
		        	continue;
		        }
		    }
		}
		
		if (msalsaAligmentFilePath == null){
			throw new IllegalStateException("Unable to find file " + SalsaAlgorithmExecutor.SALSA_ALIGMENT_SUFFIX + " for UUID " + idProccedRequest);
		}		
		if (msalsaPhylogeneticTreeFilePath == null){
			throw new IllegalStateException("Unable to find file " + SalsaAlgorithmExecutor.SALSA_TREE_SUFFIX + " for UUID " + idProccedRequest);
		}
		
		this.msalsaAligmentFilePath = msalsaAligmentFilePath;
		this.msalsaPhylogeneticTreeFilePath = msalsaPhylogeneticTreeFilePath;
	}    
}
