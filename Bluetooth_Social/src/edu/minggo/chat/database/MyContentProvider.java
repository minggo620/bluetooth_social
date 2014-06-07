package edu.minggo.chat.database;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import edu.minggo.chat.database.MyProviderMetaData.UserTableMetaData;

public class MyContentProvider extends ContentProvider {
	public static final UriMatcher uriMatcher;
	public static final int USER_COLLECTION = 1;
	public static final int USER_SINGLE = 2;
	
	public DatabaseHelper dh;
	/**
	 * uri的规则配置
	 */
	static{
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		//当符合查询users目录下所有的user，匹配放回值为INCREASING_USER_COLLECTION
		uriMatcher.addURI(MyProviderMetaData.AUTHORITY, "users", USER_COLLECTION);
		//当符合查询users目录下某个的user，匹配放回值为INCREASING_USER_SINGLE
		uriMatcher.addURI(MyProviderMetaData.AUTHORITY, "users/#", USER_SINGLE);
		
	}
	/**
	 * 是一个回调函数，当contentprovider创建的时间就会执行
	 */
	@Override
	public boolean onCreate() {
		dh = new DatabaseHelper(getContext(), MyProviderMetaData.DATABASE_NAME);
		System.out.println("--->onCreate()");
		return true;
	}
	/**
	 * 对数据字段设置别名
	 */
	public static HashMap<String, String> userProjectMap;
	static{
		userProjectMap = new HashMap<String, String>();
		userProjectMap.put(UserTableMetaData._ID, UserTableMetaData._ID);
		userProjectMap.put(UserTableMetaData.USER_NAME, UserTableMetaData.USER_NAME);
		userProjectMap.put(UserTableMetaData.USER_PASSWORD, UserTableMetaData.USER_PASSWORD);
		userProjectMap.put(UserTableMetaData.USER_SEX, UserTableMetaData.USER_SEX);
		userProjectMap.put(UserTableMetaData.USER_AGE, UserTableMetaData.USER_AGE);
		userProjectMap.put(UserTableMetaData.USER_PROVINCE, UserTableMetaData.USER_PROVINCE);
		userProjectMap.put(UserTableMetaData.USER_EMAIL, UserTableMetaData.USER_EMAIL);
		userProjectMap.put(UserTableMetaData.USER_LOGINNAME, UserTableMetaData.USER_LOGINNAME);
		userProjectMap.put(UserTableMetaData.USER_TELEPHONE, UserTableMetaData.USER_TELEPHONE);
		userProjectMap.put(UserTableMetaData.USER_ICON, UserTableMetaData.USER_ICON);
		userProjectMap.put(UserTableMetaData.USER_HOBBY, UserTableMetaData.USER_HOBBY);
		
		userProjectMap.put(UserTableMetaData.USER_MOTTO, UserTableMetaData.USER_MOTTO);
		userProjectMap.put(UserTableMetaData.USER_INTRODUCE, UserTableMetaData.USER_INTRODUCE);
		userProjectMap.put(UserTableMetaData.USER_PERSONKIND, UserTableMetaData.USER_PERSONKIND);
		
	}
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(UserTableMetaData.TABLE_NAME);
		switch (uriMatcher.match(uri)) {
		
		case USER_COLLECTION:
			qb.setTables(UserTableMetaData.TABLE_NAME);
			qb.setProjectionMap(userProjectMap);
			break;
		case USER_SINGLE:
			qb.setTables(UserTableMetaData.TABLE_NAME);
			qb.setProjectionMap(userProjectMap);
			//content://minggo.contentprovider.MyContentProvider/users/1
			//uri.getPathSegments()是得到/users/并首尾去掉"/"
			qb.appendWhere(UserTableMetaData._ID+"="+uri.getPathSegments().get(1));
		default:
			throw new SQLException("这是无效的URI"+uri);
		}
		String orderBy;
		if(TextUtils.isEmpty(sortOrder)){
			orderBy = UserTableMetaData.DEFAULT_SORT_ORDER;
		}else{
			orderBy = sortOrder;
		}
		SQLiteDatabase db = dh.getWritableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		//通知监听器，操作查询
		c.setNotificationUri(getContext().getContentResolver(), uri);
		System.out.println("----->query(..)");
		return c;
	}
	/**
	 * 获取匹配uri返回数据类型，也就是定义的匹配类型中的INCREASING_USER_COLLECTION、
	 * 和INCREASING_USER_SINGLE，在做相应的处理
	 */
	@Override
	public String getType(Uri uri) {
		System.out.println("----->getType(Uri uri) ");
		switch(uriMatcher.match(uri)){
		case USER_COLLECTION:
			return MyProviderMetaData.UserTableMetaData.CONTENT_TYE;
		case USER_SINGLE:
			return MyProviderMetaData.UserTableMetaData.CONTENT_TYP_ITEM;
		default:
			throw new IllegalArgumentException("Unknow Uri"+uri);
		}
		
	}
	/**
	 * 插入数据返回的是Uri
	 * content://minggo.contentprovider.MyContentProvider/users/1
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		System.out.println("---->insert(Uri uri, ContentValues values)");
		SQLiteDatabase db = dh.getWritableDatabase();
		long rowId = db.insert(UserTableMetaData.TABLE_NAME, null, values);
		if(rowId>0){
			Uri insertUserUri = ContentUris.withAppendedId(UserTableMetaData.CONTENT_URI, rowId);
			//通知监听器，数据已改变
			getContext().getContentResolver().notifyChange(insertUserUri, null);
			return insertUserUri;
		}
		//throw new SQLException("Fail to insert row into"+uri);
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dh.getWritableDatabase();
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case USER_COLLECTION:
			count = db.delete(UserTableMetaData.TABLE_NAME, selection, selectionArgs);
			break;
		case USER_SINGLE:
			String noteId = uri.getPathSegments().get(1);
			count = db.delete(UserTableMetaData.TABLE_NAME, UserTableMetaData._ID +"="+ noteId
					+ (!TextUtils.isEmpty(selection) ?" AND ("+ selection +')' : ""), selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("错误的 URI "+ uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// 获得数据库实例
		SQLiteDatabase db = dh.getWritableDatabase();
		int count;
		switch (uriMatcher.match(uri)) {
		// 根据指定条件更新
		case USER_COLLECTION:
			count = db.update(UserTableMetaData.TABLE_NAME, values, selection,
					selectionArgs);
			break;
		// 根据指定条件和ID更新
		case USER_SINGLE:
			String noteId = uri.getPathSegments().get(1);
			count = db.update(UserTableMetaData.TABLE_NAME,values,UserTableMetaData._ID+ "="
					+ noteId+ (!TextUtils.isEmpty(selection) ? " AND ("+ selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("错误的 URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
		
	}
	
	
}
