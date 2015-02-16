package com.salsaw.salsa.cli;

import com.beust.jcommander.JCommander;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	SalsaParameters salsaParameters = new SalsaParameters();
    	JCommander commands=  new JCommander(salsaParameters);
    	
        try {
        	
            commands.parse(args);

            System.out.println(commands.getParsedCommand());
            
        } catch (Exception e) {
        	
            System.out.println(e.getMessage());
            commands.usage();
        }       
    }
}
