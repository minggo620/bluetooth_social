package edu.minggo.chat.adapters;

import edu.minggo.chat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EmaiListAdapter extends BaseAdapter {
	
	private Context context;
	public  String subname[] = new String[]{"@qq.com","@163.com","@yahoo.com","@sina.com"};
	public  String subEmailStr[] = new String[4];
	public EmaiListAdapter(Context context){
		this.context = context;
	}
	@Override
	public int getCount() {
		return subEmailStr.length;
	}

	@Override
	public Object getItem(int position) {
		return subEmailStr[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view  = LayoutInflater.from(context).inflate(R.layout.email_list_item, null);
		TextView email = (TextView)view.findViewById(R.id.email_item_text);
		email.setText(subEmailStr[position]);
		return view;
	}

}
