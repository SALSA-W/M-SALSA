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
package com.salsaw.msalsa.cli;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.salsaw.msalsa.algorithm.exceptions.SALSAException;
import com.salsaw.msalsa.cli.exceptions.SALSAParameterException;

/**
 * Hello world!
 *
 */
public class App {
	
    private static final Logger logger = LogManager.getLogger(App.class);
    
	public static void main(String[] args) {
		SalsaParameters salsaParameters = new SalsaParameters();
		JCommander commands = new JCommander(salsaParameters);

		try {
			commands.parse(args);
			
			SalsaAlgorithmExecutor.callClustal(salsaParameters);
			
			System.out.println("Aligned completed successfully");			
			System.exit(ExitCode.Success.ordinal());
		} catch(ParameterException pe){
			commands.usage();
			System.exit(ExitCode.ParametersError.ordinal());
		} catch(SALSAParameterException pe){
			System.err.println(pe.getMessage());
			commands.usage();
			System.exit(ExitCode.ParametersError.ordinal());
		}
		catch(SALSAException se){
			logger.error("SALSAException during msalsa execution", se);
			System.exit(ExitCode.InternalSalsaError.ordinal());
		}
		catch (Exception e) {
			logger.error("Exception during msalsa execution", e);
			System.exit(ExitCode.GenericException.ordinal());
		}
	}
}
