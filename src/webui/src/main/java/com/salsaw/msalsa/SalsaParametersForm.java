package com.salsaw.msalsa;

import com.salsaw.msalsa.algorithm.TerminalGAPsStrategy;
import com.salsaw.msalsa.cli.SalsaParameters;
import com.salsaw.msalsa.cli.ScoringMatrix;
import com.salsaw.msalsa.clustal.ClustalType;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.declarative.Design;

@DesignRoot
public class SalsaParametersForm extends FormLayout {	

	private static final long serialVersionUID = 1L;
	
	@PropertyId("clustalType")
    ComboBox clustalTypeField;

	@PropertyId("GOP")
    TextField GOPField;
    
    @PropertyId("GEP")
    TextField GEPField;
    
    @PropertyId("gamma")
    TextField gammaField;
    
    @PropertyId("scoringMatrix")
    ComboBox scoringMatrixField;
    
    @PropertyId("minIterations")
    TextField minIterationsField;
    
    @PropertyId("probabilityOfSplit")
    TextField probabilityOfSplitField;
    
    @PropertyId("terminalGAPsStrategy")
    ComboBox terminalGAPsStrategyField;
    
    @PropertyId("generatePhylogeneticTree")
    CheckBox generatePhylogeneticTree;
    
	public SalsaParametersForm(BeanItem<SalsaParameters> salsaParametersBeanItem) {		
        Design.read(this);
        
        for(ClustalType clustalType : ClustalType.values()) {
        	clustalTypeField.addItem(clustalType);
        }
        
        for(ScoringMatrix scoringMatrix : ScoringMatrix.values()) {
        	scoringMatrixField.addItem(scoringMatrix);
        }
        
        for(TerminalGAPsStrategy terminalGAPsStrategy : TerminalGAPsStrategy.values()) {
        	terminalGAPsStrategyField.addItem(terminalGAPsStrategy);
        }
        
        // Data binding based on attributes
        FieldGroup binder = new FieldGroup(salsaParametersBeanItem);
        binder.bindMemberFields(this);
        binder.setBuffered(false);
	}
}

