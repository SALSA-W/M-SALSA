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

import javax.servlet.annotation.WebServlet;

import com.salsaw.msalsa.cli.SalsaParameters;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

/**
 *
 */
@Theme("mytheme")
@Widgetset("com.salsaw.msalsa.MyAppWidgetset")
public class MyUI extends UI {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Navigator navigator;
    protected static final String PROCESSED = "processed";

    @Override
    protected void init(VaadinRequest vaadinRequest) {    	    	   
    	// MVP form https://vaadin.com/book/vaadin7/-/page/advanced.architecture.html
    	
    	// Create a navigator to control the views
        navigator = new Navigator(this, this);
        
        // Create the model entity
		SalsaParameters salsaParameters = new SalsaParameters();
		salsaParameters.setGeneratePhylogeneticTree(true);
    	
    	// Create the model and the Vaadin view implementation    	
        final HomePageView homePageView = new HomePageView(salsaParameters);
        
        // The presenter binds the model and view together
        new HomePagePresenter(homePageView, navigator, salsaParameters);
                
        getPage().setTitle("M-SALSA");      
                
        navigator.addView("", homePageView);
        navigator.navigateTo("");
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
