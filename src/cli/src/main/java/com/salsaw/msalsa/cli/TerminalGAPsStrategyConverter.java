package com.salsaw.msalsa.cli;

import java.util.EnumSet;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import com.salsaw.msalsa.algorithm.TerminalGAPsStrategy;

public class TerminalGAPsStrategyConverter implements IStringConverter<TerminalGAPsStrategy> {
	 
    @Override
    public TerminalGAPsStrategy convert(String value) {
    	TerminalGAPsStrategy convertedValue = TerminalGAPsStrategy.fromString(value);
 
        if(convertedValue == null) {
            throw new ParameterException(composeInvalidStringError(TerminalGAPsStrategy.class, value));
        }
        return convertedValue;
    }
    
    public static <E extends Enum<E>> String composeInvalidStringError(Class<E> enumClass, String value){
    	StringBuilder errorMessage = new StringBuilder();
    	errorMessage.append("Value ");
    	errorMessage.append(value);
    	errorMessage.append(" can not be converted to TerminalGAPsStrategy.");
    	errorMessage.append(" Available values are: ");
    	for(E en : EnumSet.allOf(enumClass)){
            errorMessage.append(en.toString());
            errorMessage.append(" ");
        }
    	
    	return errorMessage.toString();
    }
}
