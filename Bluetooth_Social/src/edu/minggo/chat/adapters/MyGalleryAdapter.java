package edu.minggo.chat.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.minggo.chat.R;
import edu.minggo.chat.model.MyPhoto;
import edu.minggo.chat.ui.MyGalleryDetailActivity;
/**
 *  我的相册的适配器
 * @author minggo
 * @created 2013-2-6下午12:52:04
 */
public class MyGalleryAdapter extends BaseAdapter {
	public static final int GET_PHOTO_FROM_LOCAL = 0;
	public static final int GET_PHOTO_FROM_CARMERA = 1;
	private String today;
	private Context context;
	public static List<MyPhoto> photos;
	private Intent it;
	private Map<Integer,View> map = new HashMap<Integer, View>();
	@SuppressWarnings("static-access")
	public MyGalleryAdapter(Context context,List<MyPhoto> photos){
		this.context = context;
		this.photos = photos;
		this.today=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	@Override
	public int getCount() {
		return photos.size();
	}
	@Override
	public Object getItem(int position) {
		return photos.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	public void refresh(){
		this.notifyDataSetChanged();
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = map.get(position);
		ViewHolder viewHolder;
		
		if(convertView==null){
			viewHolder = new ViewHolder();
			
			convertView = LayoutInflater.from(context).inflate(R.layout.mygallery_list_item, null);
			viewHolder.photoView = (ImageView)convertView.findViewById(R.id.myphoto_iv);
			viewHolder.dayView = (TextView)convertView.findViewById(R.id.myphoto_day);
			viewHolder.monthView = (TextView)convertView.findViewById(R.id.myphoto_mounth);
			viewHolder.descView = (TextView)convertView.findViewById(R.id.myphoto_feelings);
			System.out.println(position+"______________________________");
			
			
			map.put(position, convertView);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		if(!photos.isEmpty()){
			if(!today.substring(0, 8).equals(photos.get(position).getTime().substring(0, 8))){
				viewHolder.dayView.setText(photos.get(position).getTime().substring(6, 8));
				viewHolder.monthView.setText(photos.get(position).getTime().substring(4, 6)+"月");
			}else{
				viewHolder.dayView.setText("");
				viewHolder.monthView.setText("");
			}
			viewHolder.descView.setText(photos.get(position).getPhotoDes());
			if(photos.get(position).getMyphoto()!=null){
				viewHolder.photoView.setImageBitmap(photos.get(position).getMyphoto());
			}else{
				viewHolder.photoView.setImageResource(R.drawable.xiaohei);
			}
			viewHolder.photoView.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (position<photos.size()) {
						it = new Intent(context,MyGalleryDetailActivity.class);
						MyPhoto photo =  photos.get(position);
						photo.setMyphoto(null);
						if (photo.getMyphoto()==null) {
							System.out.println("ZZZZZZZZZZZZZZZZZz");
						}
						it.putExtra("bitmap", photos.get(position).getMyphoto());
						it.putExtra("MyPhoto", photo);
						it.putExtra("position",position);
						it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(it);
					}
					
				}
			});
		}	
		return convertView;
	}
	public class ViewHolder{
		ImageView photoView;
		TextView dayView;
		TextView monthView;
		TextView descView;
	}

}
