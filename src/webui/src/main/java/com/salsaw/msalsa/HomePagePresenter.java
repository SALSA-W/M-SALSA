package com.salsaw.msalsa;

import java.io.File;

public class HomePagePresenter implements IHomePageListener{	
	private final HomePageView view;

	public HomePagePresenter(HomePageView  view) {
		this.view  = view;
	         
		view.addListener(this);
	}

	@Override
	public void buttonClick(File file) {
		// TODO Auto-generated method stub
		
	}
}
