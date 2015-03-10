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
package com.salsaw.msalsa.clustal;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.salsaw.msalsa.utils.OSChecker;

public enum ClustalType {
	CLUSTAL_W,
	CLUSTAL_O;
	
	// CONSTANTS
	private static final String CLUSTAL_W_PROCESS_NAME = "clustalw2";
	private static final String CLUSTAL_O_PROCESS_NAME = "clustalo";
	
	// GET / SET
	public static final File getClustalProcessFile(ClustalType clustalType)
	{	
		String processName;		
		switch (clustalType) {
		case CLUSTAL_W:
			processName = CLUSTAL_W_PROCESS_NAME;
			break;
			
		case CLUSTAL_O:
			processName = CLUSTAL_O_PROCESS_NAME;
			break;

		default:
			throw new UnsupportedOperationException("Not implemented");
			//break;
		}
		
		//Get file from resources folder
		ClassLoader classLoader = ClustalType.class.getClassLoader();
		
		String osFolder = getOpertiveSystemPath();
		Path processFilePath = Paths.get(osFolder, processName);		
		
		return new File(classLoader.getResource(processFilePath.toString()).getFile());
	}
	
	private static final String getOpertiveSystemPath()
	{
		if (OSChecker.isWindows() == true){
			return "win";
		}
		else{
			throw new UnsupportedOperationException("Not implemented");
		}				
	}
}
