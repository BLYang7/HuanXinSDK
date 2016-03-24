package com.example.demo;

import java.util.List;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatHistoryAdapter extends BaseAdapter{
	
	Context context;
	List<EMConversation> conversationList;
	private List<EMConversation> copyConversationList;

	public ChatHistoryAdapter(Context context, List<EMConversation> object){
		this.context = context;
		this.conversationList = object;
	}

	@Override
	public int getCount() {
		return conversationList.size();
	}

	@Override
	public Object getItem(int position) {
		return conversationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = LayoutInflater.from(context)
					.inflate(R.layout.row_chat_history, parent, false);
		}
		
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if(holder == null){
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
			holder.message = (TextView) convertView.findViewById(R.id.message);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
			holder.list_item_layout = (RelativeLayout) convertView.findViewById(R.id.list_item_layout);
			convertView.setTag(holder);
		}
		
		if(position % 2 == 0){
			holder.list_item_layout.setBackgroundColor(Color.GRAY);
		}else{
			holder.list_item_layout.setBackgroundColor(Color.GRAY);
		}
		
		EMConversation conversation = (EMConversation) getItem(position);
		
		//设置用户名的显示
		holder.name.setText(conversation.getUserName());
		
		//设置未读数目显示
		if(conversation.getUnreadMsgCount() > 0){
			
			holder.unreadLabel.setText(conversation.getUnreadMsgCount()+"");
			holder.unreadLabel.setVisibility(View.VISIBLE);
		}else{
			holder.unreadLabel.setVisibility(View.INVISIBLE);
		}
		
		
		//设置message
		if(conversation.getMsgCount() != 0){
			holder.message.setText(getMessage(conversation.getLastMessage()));
			
			//设置时间
			//这里可能需要进行时间格式化，不同的格式请自行设定
			holder.time.setText(conversation.getLastMessage().getMsgTime()+"");
		}
		
		/**
		 * 测试用例
		 */
		Log.v("count", conversation.getUnreadMsgCount()+"");
		
		return convertView;
	}

	
	private String getMessage(EMMessage message){
		
		String str = "";
		
		switch(message.getType()){
		
		//图片消息
		case IMAGE:{
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			str = "[picture]" + imageBody.getFileName();
			break;
		}
		
		case TXT:{
			TextMessageBody txtBody = (TextMessageBody) message.getBody();
			str = txtBody.getMessage();
			
			break;
		}
		
		}
		
		return str;
	}
	
	
	private class ViewHolder{
		/** 和谁的聊天记录 */
		TextView name;
		
		/** 消息未读数 */
		TextView unreadLabel;
		
		/** 最后一条消息的内容 */
		TextView message;
		
		/** 最后一条消息的时间 */
		TextView time;
		
		/** 用户头像 */
		ImageView avatar;
		
		/** 整个list中每一行总布局 */
		RelativeLayout list_item_layout;
	}
	
	
	
	
}







