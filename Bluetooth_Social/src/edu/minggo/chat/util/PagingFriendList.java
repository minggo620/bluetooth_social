package edu.minggo.chat.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import edu.minggo.chat.database.MyProviderMetaData.UserTableMetaData;
import edu.minggo.chat.model.User;

public class PagingFriendList {
	//private int pageSize;
	//private int pageNow;
	private int listSize;
	private Context context;
	public static List<User> allFriend;

	@SuppressWarnings("static-access")
	public PagingFriendList(Context context) {
		this.allFriend = new ArrayList<User>();
		this.context = context;
	}
	/**
	 * 获取第几页的用户信息
	 * @param pageSize
	 * @param pageNow
	 * @return
	 */
	public List<User> getPagingNowFriend(int pageSize, int pageNow) {
		System.out.println("第几页===="+pageNow);
		Uri uri = UserTableMetaData.CONTENT_URI;
		Cursor c = context.getContentResolver().query(uri, null,UserTableMetaData.USER_PERSONKIND + "=" + "?",
				new String[] { "friend" }, UserTableMetaData.USER_NAME);
		while (c.moveToNext()) {
			User user = new User();
			user.setUsername(c.getString(c
					.getColumnIndex(UserTableMetaData.USER_NAME)));
			user.setMotto(c.getString(c
					.getColumnIndex(UserTableMetaData.USER_MOTTO)));
			user.setUserid(c.getLong(c
					.getColumnIndex(UserTableMetaData._ID)));
			allFriend.add(user);
		}
		listSize = allFriend.size();
		System.out.println("所有用户的数量---->"+listSize);
		List<User> returnList = new ArrayList<User>();
		
		if (listSize <= (pageSize*pageNow)) {
			for(int i = (pageSize * (pageNow-1));i<listSize;i++){
				User user = allFriend.get(i);
				returnList.add(user);
			}
		} else if ((pageSize *( pageNow )) < listSize) {
			for (int i = (pageSize * (pageNow-1)); i < (pageSize * pageNow); i++) {
				User user = allFriend.get(i);
				returnList.add(user);
			}
		}
		return returnList;
		
	}
	
}
