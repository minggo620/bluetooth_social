package edu.minggo.chat.adapters;

import edu.minggo.chat.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SexSelectListAdapter extends BaseAdapter {
	private Context context;
	private String lastSexSelect;
	private String sex[] = new String[]{"male","female"};
	public SexSelectListAdapter(Context context,String lastSexSelect){
		this.context = context;
		this.lastSexSelect = lastSexSelect;
	}
	@Override
	public int getCount() {
		return sex.length;
	}

	@Override
	public Object getItem(int position) {
		return sex[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view =  LayoutInflater.from(context).inflate(R.layout.sex_list_item, null);
		if(position==0){
			
			TextView sexStr = (TextView)view.findViewById(R.id.sex_tv);
			ImageView sexRadio = (ImageView)view.findViewById(R.id.sex_select);
			sexStr.setText(R.string.sex_male);
			if(lastSexSelect.equals("ÄÐ")){
				sexRadio.setImageResource(R.drawable.radio_checked);
			}else{
				sexRadio.setImageResource(R.drawable.radio_unchecked);
			}
		}else if(position==1){
			TextView sexStr = (TextView)view.findViewById(R.id.sex_tv);
			ImageView sexRadio = (ImageView)view.findViewById(R.id.sex_select);
			sexStr.setText(R.string.sex_female);
			if(lastSexSelect.equals("Å®")){
				sexRadio.setImageResource(R.drawable.radio_checked);
			}else{
				sexRadio.setImageResource(R.drawable.radio_unchecked);
			}
		}
		return view;
	}

}
