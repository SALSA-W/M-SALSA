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

/**
 * Singleton class
 * 
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class ConfigurationManager {
	// FIELDS
	private static final ConfigurationManager instance;	
	private final ServerConfiguration serverConfiguration;
	
	// CONSTRUCTOR
	private ConfigurationManager(ServerConfiguration serverConfiguration){
		if (serverConfiguration == null){
			throw new IllegalArgumentException("serverConfiguration");
		}
		this.serverConfiguration = serverConfiguration;
	}
	
	// GET
	public ServerConfiguration getServerConfiguration() {
		return serverConfiguration;
	}

	public static ConfigurationManager getInstance() {
		return instance;
	}

	/* 
  	Static initialization
	Runtime initialization
	By default ThreadSafe
	*/
    static{    	
    	// Load configuration from files
    	ConfigurationLoader configurationLoader = new ConfigurationLoader();
    	ServerConfiguration serverConfiguration = configurationLoader.ReadConfiguration();
    	
    	instance = new ConfigurationManager(serverConfiguration);    			    	
    }
}
