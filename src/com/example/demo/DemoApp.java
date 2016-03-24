package com.example.demo;

import com.easemob.chat.EMChat;

import android.app.Application;

public class DemoApp extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		EMChat.getInstance().init(this);
		
	}

}
