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
package com.salsaw.msalsa;

import java.io.File;

public class HomePagePresenter implements IHomePageListener{	
	private final HomePageView view;

	public HomePagePresenter(HomePageView  view) {
		if (view == null){
			throw new IllegalArgumentException("view");
		}		
		
		this.view  = view;
	         
		view.addListener(this);
	}

	@Override
	public void buttonClick(File file) {
		// TODO Auto-generated method stub
		
		if (file.exists()){
			file.delete();
        } 
	}
}
