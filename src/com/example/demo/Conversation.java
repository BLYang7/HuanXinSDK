package com.example.demo;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Conversation extends Activity{
	
	ListView listView;
	Button button;
	
	private EMConversation conversation;
	
	private ConversationAdapter adapter;
	
	NewMessageBroadcastReceiver msgReceiver; //广播监听器，用于接收消息
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);
		
		listView = (ListView) findViewById(R.id.conversation_list);
		button = (Button) findViewById(R.id.conversation_btn);
		
		conversation = EMChatManager.getInstance()
				.getConversation(getIntent().getExtras().get("userName").toString());
		
		adapter = new ConversationAdapter(Conversation.this, conversation);
		listView.setAdapter(adapter);
		listView.setSelection(listView.getCount() - 1 );
		
		
		
		registerBroadcast();  //注册广播接收消息
		EMChat.getInstance().setAppInited();  //注册完广播之后，一定要声明这一句
		
		sendMsg();  //发送文本消息
		
		
	}
	
	
	
	/**
	 * 注册一个接收消息的广播
	 */
	private void registerBroadcast(){
		//只有注册了广播才能接收到新消息，目前离线消息，在线消息都是走接收消息的广播（离线消息目前无法监听，在登录以后，接收消息广播会执行一次拿到所有的离线消息）
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);
		
//		this.unregisterReceiver(msgReceiver);
	}
	
	
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
		    // 注销广播
			abortBroadcast();
	 
			// 消息id（每条消息都会生成唯一的一个id，目前是SDK生成）
			String msgId = intent.getStringExtra("msgid");
			//发送方
			String username = intent.getStringExtra("from");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			
			if (!username.equals(username)) {
				// 消息不是发给当前会话，return
				return;
			}
			
			conversation.addMessage(message);
			adapter.notifyDataSetChanged();
			listView.setAdapter(adapter);
			listView.setSelection(listView.getCount() - 1);
			
		}
	}
	
	
	/**
	 * 发送文本消息
	 */
	private void sendMsg(){
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
				EMConversation conversation = EMChatManager.getInstance().getConversation(MainActivity.UB);
				//创建一条文本消息
				EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
				//设置消息body
				TextMessageBody txtBody = new TextMessageBody("呵呵呵");
				message.addBody(txtBody);
				//设置接收人
				message.setReceipt(MainActivity.UB);
				//把消息加入到此会话对象中
				conversation.addMessage(message);
				
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);
				listView.setSelection(listView.getCount() - 1);
				
				//发送消息
				EMChatManager.getInstance().sendMessage(message, new EMCallBack(){

					@Override
					public void onError(int arg0, String arg1) {
						
					}

					@Override
					public void onProgress(int arg0, String arg1) {
						
					}

					@Override
					public void onSuccess() {
						
					}});
				
				
			}
		});
		
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		/**
		 * 当前Activity销毁的时候，标记所有信息为已读
		 * 销毁广播
		 */
		conversation.markAllMessagesAsRead();
		this.unregisterReceiver(msgReceiver);
		
	}
	
	
}





