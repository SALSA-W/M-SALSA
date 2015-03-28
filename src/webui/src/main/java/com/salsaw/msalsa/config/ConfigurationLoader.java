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
package com.salsaw.msalsa.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.salsaw.msalsa.cli.App;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class ConfigurationLoader {
	// CONSTANTS
	private static final String CONFIGURATION_RESOURCE_FILE_PATH = "/config/config.properties";
	
	private static final String KEY_CLUSTALW_PATH =  "clustalw.path";
	private static final String KEY_CLUSTALO_PATH =  "clustalo.path";
	private static final String KEY_TEMP_PATH =  "temporary.folder.path";
	
	public ServerConfiguration ReadConfiguration(){		
		Properties prop = new Properties();
		
		try(InputStream input = App.class.getResourceAsStream(CONFIGURATION_RESOURCE_FILE_PATH)){
	 
			// load a properties file
			prop.load(input);
	 
			// get the property value and print it out
			return new ServerConfiguration(
					prop.getProperty(KEY_CLUSTALW_PATH),
					prop.getProperty(KEY_CLUSTALO_PATH),
					prop.getProperty(KEY_TEMP_PATH));			
		} catch (IOException e) {
			// TODO Better exception management
			e.printStackTrace();
		}
		
		return null;
	}
}