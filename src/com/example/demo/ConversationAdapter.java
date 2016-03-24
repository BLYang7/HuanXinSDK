package com.example.demo;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ConversationAdapter extends BaseAdapter{
	
	private Context context;
	private EMConversation conversation;
	
	public ConversationAdapter(Context context, EMConversation conversation){
		this.context = context;
		this.conversation = conversation;
	}

	@Override
	public int getCount() {
		return conversation.getAllMessages().size();
	}

	@Override
	public Object getItem(int position) {
		return conversation.getAllMessages().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		EMMessage message = conversation.getAllMessages().get(position);
		TextMessageBody body = (TextMessageBody) message.getBody();
		Log.v("消息体数据", message.toString());
		
		
		
		//对于接收到的消息的处理
		if(message.direct == EMMessage.Direct.RECEIVE){
			if(message.getType() == EMMessage.Type.TXT){
				convertView = LayoutInflater.from(context)
						.inflate(R.layout.listview_item, parent, false);
			}
		}
		
		//对于发送消息的处理
		else{
			if(message.getType() == EMMessage.Type.TXT){
				convertView = LayoutInflater.from(context)
						.inflate(R.layout.listview_item1, parent, false);
			}
		}
		
		
		TextView content = (TextView)convertView.findViewById(R.id.tv_chatcontent);
		content.setText(body.getMessage());
		
		return convertView;
	}

	
}
