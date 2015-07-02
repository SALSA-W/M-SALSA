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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.salsaw.msalsa.datamodel.AlignmentRequest;

/**
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public final class AligmentRequestManager {
	private static final AligmentRequestManager instance= new AligmentRequestManager();
	
	private final Map<UUID, AligmentRequestExecutor> activeRequests = new ConcurrentHashMap<UUID, AligmentRequestExecutor>();	
	
	private AligmentRequestManager(){}
	
	public static AligmentRequestManager getInstance(){
		return instance;
	}
	
	public final void startManageRequest(AlignmentRequest alignmentRequest){		
		if (alignmentRequest == null){
			throw new IllegalArgumentException("salsaParameters");
		}
		
		// Start the request process
		AligmentRequestExecutor aligmentRequestExecutor = new AligmentRequestExecutor(alignmentRequest);		
		this.activeRequests.put(alignmentRequest.getId(), aligmentRequestExecutor);

		aligmentRequestExecutor.startAsyncAlignment();
	}
	
	public final void endManageRequest(UUID alignmentRequestId){		
		this.activeRequests.remove(alignmentRequestId);
	}
	
	public final boolean RequestExists(String idRequest) {
		// TODO Auto-generated method stub
		return true;
	}

	public final boolean IsRequestCompleted(String idRequest) {
		// TODO Auto-generated method stub
		return true;
	}
}
