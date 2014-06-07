package edu.minggo.chat.adapters;

import edu.minggo.chat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AboutLDialogAdapter extends BaseAdapter {
	private Context context;
	private String source[];
	public AboutLDialogAdapter(Context context,String[] source){
		this.context = context;
		this.source = source;
	}
	@Override
	public int getCount() {
		return source.length;
	}

	@Override
	public Object getItem(int position) {
		return source[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view =  LayoutInflater.from(context).inflate(R.layout.about_list_item, null);
		TextView sexStr = (TextView)view.findViewById(R.id.about_tv);
		sexStr.setText(source[position]);
		return view;
	}

}
