package edu.minggo.chat.adapters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.minggo.chat.R;
import edu.minggo.chat.model.User;
import edu.minggo.chat.util.PagingFriendList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * MainTab中的首页“蓝星”信息历史记录
 * @author minggo
 * @created 2013-1-30下午11:53:51
 */
public class MainLansiAdapter extends BaseAdapter {

	public static List<User> friendlist;
	public static boolean searchFlag = false;
	private boolean addflag = false;
	private View addressList;
	private Bitmap userIcon ;
	public Context context;
	private Map<Integer,View> map = new HashMap<Integer,View>();
	public MainLansiAdapter(Context contextParam,List<User> friendslist,View addressList){
		friendlist = friendslist;
		context = contextParam;
		this.addressList = addressList;
		initHeigh();
	}
	
	public void initHeigh(){
		View view =LayoutInflater.from(context).inflate(R.layout.main_tab_lansi_listitem, null);
		int layout_hiegh = view.findViewById(R.id.forHeigh0).getLayoutParams().height;
		addressList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, (friendlist.size()+1)*(layout_hiegh+2)));
	}
	@Override
	public int getCount(){  
		if(searchFlag==true)
			return friendlist.size();
		return friendlist.size()+1;
	}
	@Override
	public long getItemId(int index) {
		if(index<this.getCount()-1){
			return friendlist.get(index).getUserid();//如果用户选定中间项
		}else{
			return -1;
		}
	}
	//增加更多数据
	public void addMoreData(List<User> moreFriends){
		friendlist.addAll(moreFriends);
		this.notifyDataSetChanged();
		initHeigh();
		addflag = true;
	}
	//删除数据
	public void deleItem(int positon){
		friendlist.remove(positon);
		this.notifyDataSetChanged();
	}
	@Override
	public Object getItem(int position) {
		return friendlist.get(position);
	}
	
	public static class ViewHolder{
		ImageView ivItemPortrait;  //头像 有默认值
		TextView tvItemName;//昵称
		TextView tvItemLastMsg;//最近的消息记录
		TextView tvTail;//末尾查询提示
		TextView tvItemLasttime;//最近消息的时间
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = map.get(position);
		ViewHolder viewHolder;
		if(searchFlag==false){
			if (convertView==null||addflag==true) {
				viewHolder = new ViewHolder();
				if(position==this.getCount()-1){
					convertView = LayoutInflater.from(context).inflate(R.layout.list_moreitems, null);
					viewHolder.tvTail = (TextView)convertView.findViewById(R.id.textView);
				}else{
					convertView  = LayoutInflater.from(context).inflate(R.layout.main_tab_lansi_listitem, null);
					viewHolder.ivItemPortrait = (ImageView)convertView.findViewById(R.id.head);
					viewHolder.tvItemName = (TextView)convertView.findViewById(R.id.username);
					viewHolder.tvItemLastMsg = (TextView)convertView.findViewById(R.id.last_message);
					viewHolder.tvItemLastMsg = (TextView)convertView.findViewById(R.id.message_lasttime);
					
					Drawable d = context.getResources().getDrawable(R.drawable.xiaohei);
					BitmapDrawable bd = (BitmapDrawable) d;
					userIcon = bd.getBitmap();
				}
				map.put(position, convertView);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)convertView.getTag();
			}
			if(position<this.getCount()-1){
				viewHolder.tvItemName.setText(friendlist.get(position).getUsername());
				viewHolder.ivItemPortrait.setImageBitmap(userIcon);
				
			}else if(position==this.getCount()-1){
				if(PagingFriendList.allFriend.size()==position||PagingFriendList.allFriend.size()<7)
				   viewHolder.tvTail.setVisibility(View.GONE);
				else  viewHolder.tvTail.setText("更多");
			}
			return convertView;
		}else{
			View view = null ;
			if(!friendlist.isEmpty()){
				view  = LayoutInflater.from(context).inflate(R.layout.main_tab_address_listitem, null);
				Drawable d = context.getResources().getDrawable(R.drawable.xiaohei);
				BitmapDrawable bd = (BitmapDrawable) d;
				userIcon = bd.getBitmap();
				
				((TextView)view.findViewById(R.id.username)).setText(friendlist.get(position).getUsername());
				((ImageView) view.findViewById(R.id.head)).setImageBitmap(userIcon);
			    
			}
			return view;
		}
		
	}

}
