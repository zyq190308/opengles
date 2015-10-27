package com.example.triangle;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	private static MyApplication instance;
	public static MyApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
	}

}
