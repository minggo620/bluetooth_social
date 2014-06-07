package edu.minggo.chat.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class MyProviderMetaData {
	public static final String AUTHORITY = "edu.minggo.chat.database.MyContentProvider";
	//数据库的名字minggoProvider.db
	public static final String DATABASE_NAME = "minggoChat.db";
	//数据库的版本
	public static final int DATATBASE_VERSION = 1;
	//数据库中的一个表名 
	public static final String USER_TABLE_NAME = "users";
	/**
	 * 实现了BaseColumns这个接口后就已经有_ID这个属性了，就不需要
	 * 自己定义一个_ID的属性，否则必须要有该属性。
	 * @author Administrator
	 *
	 */
	public static final class UserTableMetaData implements BaseColumns{
		//表名
		public static final String TABLE_NAME = "users";
		//访问contentprovider的URI
		public static final Uri CONTENT_URI = 	Uri.parse("content://"+AUTHORITY+"/users");
		//该contentprovider的访问返回的书库类型（多项）
		public static final String CONTENT_TYE = "vnd.android.cursor.dir/vnd.myprovider.user";
		//该contentprovider的访问返回的书库类型（一项）
		public static final String CONTENT_TYP_ITEM = "vnd.android.cursor.item/vnd.mprovider.user";
		//列属性的名字
		public static final String USER_LOGINNAME = "loginname";
		public static final String USER_NAME = "name";
		public static final String USER_PASSWORD = "password";
		public static final String USER_TELEPHONE = "phone";
		public static final String USER_ICON = "icon";
		public static final String USER_EMAIL = "email";
		public static final String USER_SEX = "sex";
		public static final String USER_PROVINCE = "province";
		public static final String USER_AGE = "age";
		public static final String USER_HOBBY = "hobby";
		//新增
		public static final String USER_MOTTO = "motto";
		public static final String USER_INTRODUCE = "introduce";
		public static final String USER_PERSONKIND = "personkind";
		
		//采用的id递增的方式排列
		public static final String DEFAULT_SORT_ORDER = "_id desc";
	}
	
}
