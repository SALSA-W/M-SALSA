package com.salsaw.msalsa;

import com.salsaw.msalsa.algorithm.TerminalGAPsStrategy;
import com.salsaw.msalsa.cli.SalsaParameters;
import com.salsaw.msalsa.cli.ScoringMatrix;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class SalsaParametersForm extends CustomComponent {	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
    
	public SalsaParametersForm(BeanItem<SalsaParameters> salsaParametersBeanItem) {		
        FormLayout formLayout = new FormLayout();
        scoringMatrixField.setNullSelectionAllowed(false);
        for(ScoringMatrix scoringMatrix : ScoringMatrix.values()) {
        	scoringMatrixField.addItem(scoringMatrix);
        }
        scoringMatrixField.setImmediate(true);
        formLayout.addComponent(scoringMatrixField);
        minIterationsField.setImmediate(true);       
        formLayout.addComponent(minIterationsField);        
        formLayout.addComponent(GOPField);
        formLayout.addComponent(GEPField);
        formLayout.addComponent(probabilityOfSplitField);
        formLayout.addComponent(gammaField);
        formLayout.addComponent(terminalGAPsStrategyField);
        terminalGAPsStrategyField.setNullSelectionAllowed(false);
        for(TerminalGAPsStrategy terminalGAPsStrategy : TerminalGAPsStrategy.values()) {
        	terminalGAPsStrategyField.addItem(terminalGAPsStrategy);
        }
        
        // Data binding based on attributes
        FieldGroup binder = new FieldGroup(salsaParametersBeanItem);
        binder.bindMemberFields(this);
		
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

