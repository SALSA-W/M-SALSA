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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.salsaw.msalsa.cli.App;
import com.salsaw.msalsa.services.EmailSender;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
class ConfigurationLoader {
	// CONSTANTS
	static final Logger logger = LogManager.getLogger(ConfigurationLoader.class);
	private static final String CONFIGURATION_RESOURCE_FILE_PATH = "/config/config.properties";
	
	private static final String KEY_CLUSTALW_PATH = "clustalw.path";
	private static final String KEY_CLUSTALO_PATH = "clustalo.path";
	private static final String KEY_TEMP_PATH = "temporary.folder.path";
	private static final String KEY_MAIL_USERNAME = "mail.username";
	private static final String KEY_MAIL_PASSWORD = "mail.password";
	private static final String KEY_CLEAN_DAYS_VALIDITY = "clean.validitydays";
	private static final String KEY_PUBLISHER = "author.publisher";
	private static final String KEY_GOOGLE_ANALYTICS_PROPERTY_ID = "google.analytics.propertyID";
	private static final String KEY_THREAD_POOL_MAX_NUMBER = "thread.pool.maxnumber";

	
	ServerConfiguration ReadConfiguration(){		
		Properties properties = new Properties();
		
		try(InputStream input = App.class.getResourceAsStream(CONFIGURATION_RESOURCE_FILE_PATH)){
	 
			// load a properties file
			properties.load(input);
	 
			// get the property value and print it out
			return new ServerConfiguration(
					properties.getProperty(KEY_CLUSTALW_PATH),
					properties.getProperty(KEY_CLUSTALO_PATH),
					properties.getProperty(KEY_TEMP_PATH),
					properties.getProperty(KEY_MAIL_USERNAME),
					properties.getProperty(KEY_MAIL_PASSWORD),
					properties.getProperty(EmailSender.PROP_MAIL_SMTP_AUTH),
					properties.getProperty(EmailSender.PROP_MAIL_SMTP_STARTTLS_ENABLE),
					properties.getProperty(EmailSender.PROP_MAIL_SMTP_HOST),
					properties.getProperty(EmailSender.PROP_MAIL_SMTP_PORT),					
					properties.getProperty(KEY_CLEAN_DAYS_VALIDITY),
					properties.getProperty(KEY_PUBLISHER),
					properties.getProperty(KEY_GOOGLE_ANALYTICS_PROPERTY_ID),
					properties.getProperty(KEY_THREAD_POOL_MAX_NUMBER)
					);
		} catch (IOException e) {
			logger.error(e);
		}
		
		return null;
	}
}
