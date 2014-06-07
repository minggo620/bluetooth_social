package edu.minggo.chat.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import edu.minggo.chat.R;

public class LoginUserAdapter extends BaseAdapter {
	
	private List<String> userList;
	private LayoutInflater mInflater;
	private List<String> userids;
	public static String userid[] ;
	public LoginUserAdapter(Context context,List<String> userlist,List<String> userids){
		this.mInflater = LayoutInflater.from(context);
		this.userList = userlist;
		this.userids = userids;
		userid = new String[userlist.size()];
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		userid[position] = userids.get(position);
		View view = mInflater.inflate(R.layout.login_use_list, null);
		TextView username = (TextView)view.findViewById(R.id.username);
		username.setText(userList.get(position));
		return view;
	}
	public void refresh(int position){
		userList.remove(position);
		userids.remove(position);
		
		notifyDataSetChanged();
	}
}
