package com.salsaw.msalsa;

import com.salsaw.msalsa.algorithm.TerminalGAPsStrategy;
import com.salsaw.msalsa.cli.SalsaParameters;
import com.salsaw.msalsa.cli.ScoringMatrix;
import com.salsaw.msalsa.clustal.ClustalType;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class SalsaParametersForm extends CustomComponent {	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PropertyId("clustalType")
    ComboBox clustalTypeField = new ComboBox("Clustal");

	@PropertyId("GOP")
    TextField GOPField = new TextField("GOP");
    
    @PropertyId("GEP")
    TextField GEPField = new TextField("GEP");
    
    @PropertyId("gamma")
    TextField gammaField = new TextField("Gamma");
    
    @PropertyId("scoringMatrix")
    ComboBox scoringMatrixField = new ComboBox("Scoring Matrix");
    
    @PropertyId("minIterations")
    TextField minIterationsField = new TextField("Min Matrix");
    
    @PropertyId("probabilityOfSplit")
    TextField probabilityOfSplitField = new TextField("Probability Of Split");
    
    @PropertyId("terminalGAPsStrategy")
    ComboBox terminalGAPsStrategyField = new ComboBox("Terminal GAPs Strategy");
    
    @PropertyId("generatePhylogeneticTree")
    CheckBox generatePhylogeneticTree = new CheckBox("Generate Phylogenetic Tree");
    
	public SalsaParametersForm(BeanItem<SalsaParameters> salsaParametersBeanItem) {		
        FormLayout formLayout = new FormLayout();
        
        clustalTypeField.setNullSelectionAllowed(false);
        clustalTypeField.setImmediate(true);
        for(ClustalType clustalType : ClustalType.values()) {
        	clustalTypeField.addItem(clustalType);
        }
        formLayout.addComponent(clustalTypeField);
        
        formLayout.addComponent(generatePhylogeneticTree); 
        
        scoringMatrixField.setNullSelectionAllowed(false);
        scoringMatrixField.setImmediate(true);
        for(ScoringMatrix scoringMatrix : ScoringMatrix.values()) {
        	scoringMatrixField.addItem(scoringMatrix);
        }
        formLayout.addComponent(scoringMatrixField);
        
        minIterationsField.setImmediate(true);        
        formLayout.addComponent(minIterationsField);
        
        GOPField.setImmediate(true);
        formLayout.addComponent(GOPField);
        
        GEPField.setImmediate(true);
        formLayout.addComponent(GEPField);
        
        probabilityOfSplitField.setImmediate(true);
        formLayout.addComponent(probabilityOfSplitField);
        
        gammaField.setImmediate(true);
        formLayout.addComponent(gammaField);        
       
        
        terminalGAPsStrategyField.setNullSelectionAllowed(false);
        terminalGAPsStrategyField.setImmediate(true);
        for(TerminalGAPsStrategy terminalGAPsStrategy : TerminalGAPsStrategy.values()) {
        	terminalGAPsStrategyField.addItem(terminalGAPsStrategy);
        }
        formLayout.addComponent(terminalGAPsStrategyField);
        
        // Data binding based on attributes
        FieldGroup binder = new FieldGroup(salsaParametersBeanItem);
        binder.bindMemberFields(this);
        binder.setBuffered(false);
		
        formLayout.setSizeUndefined();
        
		Button button = new Button("Commit modifications");
		button.addClickListener(new Button.ClickListener() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void buttonClick(ClickEvent event) {
		    	try {
					binder.commit();
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
				
		formLayout.addComponent(button);
        
		setCompositionRoot(formLayout);
	}

}

