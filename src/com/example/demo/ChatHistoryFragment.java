package com.example.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChatHistoryFragment extends Fragment{
	
	//所有的会话列表
	private List<EMConversation> conversationList = new ArrayList<EMConversation>();
	
	ChatHistoryAdapter adapter ;
	ListView chatHistoryListView;
	View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		view = inflater.inflate(R.layout.fragment_conversation_history, container, false);
		return view;
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		conversationList.addAll(loadConversationWithRecentChat());
	
		chatHistoryListView = (ListView) view.findViewById(R.id.list);
		
		adapter = new ChatHistoryAdapter(getActivity(), conversationList);
		chatHistoryListView.setAdapter(adapter);
		
		
		chatHistoryListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EMConversation conversation = (EMConversation) adapter.getItem(position);
				Intent intent = new Intent(getActivity(), Conversation.class);
				intent.putExtra("userName", conversation.getUserName());
				startActivity(intent);
			}
			
		});
		
		
		int count = 0;
		for(int i=0; i<conversationList.size(); i++){
			count += conversationList.get(i).getUnreadMsgCount();
		}
		Log.v("count total", count+"");
		
		
	}
	
	
	/**
	 * 获取所有会话
	 * @return
	 */
	private Collection<? extends EMConversation> loadConversationWithRecentChat() {
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		
		List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
		synchronized(conversations){
			for(EMConversation conversation : conversations.values()){
				if(conversation.getAllMessages().size() != 0){
					sortList.add(new Pair<Long, EMConversation>
						(conversation.getLastMessage().getMsgTime(), conversation)
					);
				}
			}
		}
		
		try{
			sortConversationByLastChatTime(sortList);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		List<EMConversation> list = new ArrayList<EMConversation>();
		for(Pair<Long, EMConversation> sortItem : sortList){
			list.add(sortItem.second);
		}
		
		return list;
	}
	
	
	/**
	 * 根据最后一条消息的时间排序
	 * @param sortList
	 */
	private void sortConversationByLastChatTime(
			List<Pair<Long, EMConversation>> sortList) {
		Collections.sort(sortList, new Comparator<Pair<Long, EMConversation>>(){

			@Override
			public int compare(Pair<Long, EMConversation> con1,
					Pair<Long, EMConversation> con2) {
				if(con1.first == con2.first){
					return 0;
				}else if(con2.first > con1.first){
					return 1;
				}else{
					return -1;	
				}
			}
		});
	}

	public void refresh(){
		conversationList.clear();
		conversationList.addAll(loadConversationWithRecentChat());
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

}







