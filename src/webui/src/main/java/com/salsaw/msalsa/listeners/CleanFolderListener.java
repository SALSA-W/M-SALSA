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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.salsaw.msalsa.config.ConfigurationManager;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
// http://www.journaldev.com/1945/servlet-listener-example-servletcontextlistener-httpsessionlistener-and-servletrequestlistener
@WebListener
public class CleanFolderListener implements ServletContextListener {
	private static final int NUMBER_CLEAN_TREADS = 1;

	static final Logger logger = LogManager.getLogger(CleanFolderListener.class);
	private final ScheduledExecutorService executor;

	public CleanFolderListener() throws IOException {
		// http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/
		this.executor = Executors.newScheduledThreadPool(NUMBER_CLEAN_TREADS);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		int jobDaysValidity = ConfigurationManager.getInstance().getServerConfiguration().getCleanDaysValidityJob();

		Runnable cleanTask = () -> {
			// Clean the folder older then the validity time limit
			try {
				Instant validityDayLimit = Instant.now().minus(jobDaysValidity, ChronoUnit.DAYS);

				// http://www.adam-bien.com/roller/abien/entry/java_7_deleting_recursively_a
				Path workDirectory = Paths
						.get(ConfigurationManager.getInstance().getServerConfiguration().getTemporaryFilePath());

				Files.walkFileTree(workDirectory, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
						if (Files.getLastModifiedTime(dir).toInstant().isBefore(validityDayLimit)) {
							return FileVisitResult.CONTINUE;
						} else {
							return FileVisitResult.SKIP_SUBTREE;
						}
					}

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						// Not delete root folder
						if (workDirectory.equals(dir) == false){
							Files.delete(dir);
							logger.info("Delete folder {}", dir);
						}

						return FileVisitResult.CONTINUE;
					}
				});

			} catch (Exception e) {
				logger.error("error during clean task", e);
				e.printStackTrace();
			}
		};

		this.executor.scheduleWithFixedDelay(cleanTask, 0, jobDaysValidity, TimeUnit.DAYS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		this.executor.shutdownNow();
	}

}
