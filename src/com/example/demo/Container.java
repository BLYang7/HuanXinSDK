package com.example.demo;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Container extends Activity{
	
	LinearLayout fragmentContainer;
	TextView label;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.container);
		
		initView();
		
	}
	
	private void initView(){
		fragmentContainer = (LinearLayout) findViewById(R.id.fragment_container);
		label = (TextView) findViewById(R.id.unread_total_label);
		
		//创建修改实例
		Fragment newFragment = new ChatHistoryFragment();
		FragmentTransaction transaction =getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container,newFragment);
		transaction.addToBackStack(null);
		//提交修改
		transaction.commit();
	}
	
	
}









