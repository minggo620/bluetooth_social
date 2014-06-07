package edu.minggo.chat.adapters;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import edu.minggo.chat.R;
import edu.minggo.chat.model.User;
import edu.minggo.chat.util.PinyinComparator;
/**
 * MainTabActivity中的通讯录的信息列表的设配器
 * @author minggo
 * @created 2013-1-29下午12:45:39
 */
public class MainAddressAdapter extends BaseAdapter implements SectionIndexer{

	public static List<User> friendlist;
	public static String username[];
	public Context context;
	public MainAddressAdapter(Context contextParam,List<User> friendslist){
		friendlist = friendslist;
		context = contextParam;
		initData();
	}

	@SuppressWarnings("unchecked")
	public void initData(){
		username = new String[friendlist.size()];
		for(int i = 0;i<friendlist.size();i++){
			username[i] = friendlist.get(i).getUsername()+"."+i;
		}
		Arrays.sort(username, new PinyinComparator());
	}
	@Override
	public int getCount(){  
		return friendlist.size();
	}
	@Override
	public long getItemId(int index) {
		return friendlist.get(Integer.parseInt(username[index].substring(username[index].lastIndexOf(".")+1))).getUserid();//如果用户选定中间项
	}
	public void refresh(){
		initData();
		notifyDataSetChanged();
		
	}
	
	//删除数据
	public void deleItem(int positon){
		friendlist.remove(Integer.parseInt(username[positon].substring(username[positon].lastIndexOf(".")+1)));
		initData();
		notifyDataSetChanged();
	}
	@Override
	public Object getItem(int position) {
		return friendlist.get(Integer.parseInt(username[position].substring(username[position].lastIndexOf(".")+1)));
	}

	
	//定义一个静态类，用户获取和处理列表中每一个条目数据的更新 
	public static class ViewHolder{
		ImageView ivItemPortrait;  //头像 有默认值
		TextView tvItemName;//昵称
		TextView tvItemMotto;//个性签名
		TextView tvCatalog;//头字母
		
	}
	
		
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final String nickName = username[position].substring(0,username[position].lastIndexOf("."));
		ViewHolder viewHolder = null;
		if(convertView==null){
			convertView = LayoutInflater.from(context).inflate(R.layout.main_tab_address_listitem, null);
			viewHolder = new ViewHolder();
			viewHolder.tvCatalog = (TextView)convertView.findViewById(R.id.contactitem_catalog);
			viewHolder.ivItemPortrait = (ImageView)convertView.findViewById(R.id.head);
			viewHolder.tvItemName = (TextView)convertView.findViewById(R.id.username);
			viewHolder.tvItemMotto = (TextView)convertView.findViewById(R.id.motto);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		String catalog = converterToFirstSpell(nickName).substring(0, 1);
		if(position==0){
			viewHolder.tvCatalog.setVisibility(View.VISIBLE);
			viewHolder.tvCatalog.setText(catalog);
		}else{
			String lastCatalog = converterToFirstSpell(username[position-1]).substring(0, 1);
			if(catalog.equals(lastCatalog)){
				viewHolder.tvCatalog.setVisibility(View.GONE);
			}else{
				viewHolder.tvCatalog.setVisibility(View.VISIBLE);
				viewHolder.tvCatalog.setText(catalog);
			}
		}
		viewHolder.ivItemPortrait.setImageResource(R.drawable.xiaohei);
		viewHolder.tvItemName.setText(nickName);
		viewHolder.tvItemMotto.setText(friendlist.get(Integer.parseInt(username[position].substring(username[position].lastIndexOf(".")+1))).getMotto());
	 
		return convertView;
	}	

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < username.length; i++) {  
            String l = converterToFirstSpell(username[i]).substring(0, 1);  
            char firstChar = l.toUpperCase().charAt(0);  
            if (firstChar == section) {  
                return i;  
            }  
        } 
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}
		
	
	/**  
	 * 汉字转换位汉语拼音首字母，英文字符不变  
	 * @param chines 汉字  
	 * @return 拼音  
	 */     
	public static String converterToFirstSpell(String chines){             
	     String pinyinName = "";      
	     char[] nameChar = chines.toCharArray();      
	     HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();      
	     defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);      
	     defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);      
	    for (int i = 0; i < nameChar.length; i++) {      
	        if (nameChar[i] > 128) {      
	            try {      
	                 pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);      
	             } catch (BadHanyuPinyinOutputFormatCombination e) {      
	                 e.printStackTrace();      
	             }      
	         }else{      
	             pinyinName += nameChar[i];      
	         }      
	     }      
	    return pinyinName;      
	 }  
	
}
