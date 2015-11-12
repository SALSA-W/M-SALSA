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
package com.salsaw.msalsa.services;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.salsaw.msalsa.config.ConfigurationManager;
import com.salsaw.msalsa.datamodel.AlignmentRequest;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public final class AlignmentRequestManager {
	private static final AlignmentRequestManager instance= new AlignmentRequestManager();
	
	private final Map<UUID, AlignmentRequestExecutor> activeRequests = new ConcurrentHashMap<UUID, AlignmentRequestExecutor>();	
	
	private AlignmentRequestManager(){}
	
	public static AlignmentRequestManager getInstance(){
		return instance;
	}
	
	public final void startManageRequest(String webApplicationUri, AlignmentRequest alignmentRequest){		
		if (alignmentRequest == null){
			throw new IllegalArgumentException("salsaParameters");
		}
		if (webApplicationUri == null ||
			webApplicationUri.isEmpty() == true){
				throw new IllegalArgumentException("webApplicationUri");
		}		
		
		// Start the request process
		AlignmentRequestExecutor aligmentRequestExecutor = new AlignmentRequestExecutor(webApplicationUri, alignmentRequest);		
		this.activeRequests.put(alignmentRequest.getId(), aligmentRequestExecutor);

		aligmentRequestExecutor.startAsyncAlignment();
	}
	
	public final void endManageRequest(UUID alignmentRequestId){		
		this.activeRequests.remove(alignmentRequestId);
	}
	
	public final boolean RequestExists(UUID idRequest) {		
		if (activeRequests.containsKey(idRequest) == true){
			return true;
		}
		
		if (getServerAligmentFolder(idRequest).toFile().exists() == true){
			return true;
		}			
		
		// TODO Implement better checks
		return false;
	}

	public final boolean IsRequestCompleted(UUID idRequest) {
		if (activeRequests.containsKey(idRequest) == true){
			return false;
		}
		
		// TODO Implement better checks: look if expected files are present on server
		return true;
	}
	
	public final Path getServerAligmentFolder(UUID idRequest){
		// Load server configuration
		String tmpFolder = ConfigurationManager.getInstance().getServerConfiguration().getTemporaryFilePath();
		return Paths.get(tmpFolder, idRequest.toString());
	}
}
