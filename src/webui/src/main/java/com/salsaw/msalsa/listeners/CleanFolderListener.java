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
package com.salsaw.msalsa.listeners;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.salsaw.msalsa.config.ConfigurationManager;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
// http://www.journaldev.com/1945/servlet-listener-example-servletcontextlistener-httpsessionlistener-and-servletrequestlistener
@WebListener
public class CleanFolderListener implements ServletContextListener {
	
	private static final int NUMBER_CLEAN_TREADS = 1;
	private final ScheduledExecutorService executor;
	
	public CleanFolderListener(){
		// http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/
		this.executor =  Executors.newScheduledThreadPool(NUMBER_CLEAN_TREADS);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO - clean old directories in ConfigurationManager.getInstance().getServerConfiguration().getTemporaryFilePath()
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		this.executor.shutdownNow();		
	}

}
