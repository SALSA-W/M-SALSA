package com.salsaw.msalsa.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import com.salsaw.msalsa.algorithm.exceptions.SALSAException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws URISyntaxException, FileNotFoundException, IOException, SALSAException
    {
        System.out.println( "Hello World!" );
        
        File testf = new File(App.class.getResource( "/test.ph" ).toURI());
        
        Tree tree = new Tree(testf.getAbsolutePath(), 4);
    }
}
