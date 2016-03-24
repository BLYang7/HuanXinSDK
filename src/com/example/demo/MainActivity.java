package com.example.demo;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static final String UA = "blyang";
	public static final String PA = "bing";

	public static final String UB = "yangbingliang";
	public static final String PB = "bing";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		login(UA, PA);
		
	}
	
	private void login(String userName, String password){
		EMChatManager.getInstance().login(userName, password, new EMCallBack(){

			@Override
			public void onError(int arg0, String arg1) {
				
				Toast.makeText(MainActivity.this, "error", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onProgress(int arg0, String arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, "success", Toast.LENGTH_LONG).show();						
					}
				});
				
				//从本地DB加载到程序中
				EMChatManager.getInstance().loadAllConversations();
				startActivity(new Intent(MainActivity.this, Container.class));
				finish();
				
			}
			
		});
	}

}
