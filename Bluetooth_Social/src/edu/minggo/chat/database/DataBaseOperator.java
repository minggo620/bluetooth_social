package edu.minggo.chat.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import edu.minggo.chat.control.BluetoothChatService;
import edu.minggo.chat.database.MyProviderMetaData.UserTableMetaData;
import edu.minggo.chat.model.MyPhoto;
import edu.minggo.chat.model.MyPhoto.MyPhotoTable;
import edu.minggo.chat.model.User;

public class DataBaseOperator {
	/**
	 * 插入用户信息
	 * @param context
	 * @param values 封装好的数据
	 * @return
	 */
	public static boolean insertData(Context context,ContentValues values){
		try {
			context.getContentResolver().insert(UserTableMetaData.CONTENT_URI, values);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * 查询用户信息
	 * @param context
	 * @param selection 查询的语句
	 * @param args 查询的参数
	 * @return userList 用户列表
	 */
	public static List<User> quryData(Context context,String selection,String[] args){
		List<User> userList = new ArrayList<User>();
		Cursor c = context.getContentResolver().query(MyProviderMetaData.UserTableMetaData.CONTENT_URI, null, selection, args, UserTableMetaData.USER_NAME);
		while(c.moveToNext()){
			User user = new User();
			user.setUsername(c.getString(c.getColumnIndex(UserTableMetaData.USER_NAME)));
			user.setAge(c.getString(c.getColumnIndex(UserTableMetaData.USER_AGE)));
			user.setEmail(c.getString(c.getColumnIndex(UserTableMetaData.USER_EMAIL)));
			user.setHobby(c.getString(c.getColumnIndex(UserTableMetaData.USER_HOBBY)));
			user.setIntroduce(c.getString(c.getColumnIndex(UserTableMetaData.USER_INTRODUCE)));
			user.setMotto(c.getString(c.getColumnIndex(UserTableMetaData.USER_MOTTO)));
			user.setPersonkind(c.getString(c.getColumnIndex(UserTableMetaData.USER_PERSONKIND)));
			user.setPhoto(BitmapFactory.decodeFile(c.getString(c.getColumnIndex(UserTableMetaData.USER_ICON))));
			user.setProvince(c.getString(c.getColumnIndex(UserTableMetaData.USER_PROVINCE)));
			user.setSex(c.getString(c.getColumnIndex(UserTableMetaData.USER_SEX)));
			user.setTelephone(c.getString(c.getColumnIndex(UserTableMetaData.USER_TELEPHONE)));
			user.setUserid(c.getLong(c.getColumnIndex(UserTableMetaData._ID)));
			user.setPassword(c.getString(c.getColumnIndex(UserTableMetaData.USER_PASSWORD)));
			user.setLoginname(c.getString(c.getColumnIndex(UserTableMetaData.USER_LOGINNAME)));
			user.setPicpath(c.getString(c.getColumnIndex(UserTableMetaData.USER_ICON)));
			userList.add(user);
		}
		c.close();
		return userList;
	}
	/**
	 * 查询数据库中的用户的相册信息
	 * @param context
	 * @param selection
	 * @param args
	 * @return 相册中的每个图片信息
	 */
	public static List<MyPhoto> quryPhotos(Context context){
		List<MyPhoto> photoList = new ArrayList<MyPhoto>();
		DatabaseHelper dbHelper = new DatabaseHelper(context,MyProviderMetaData.DATABASE_NAME);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(MyPhoto.MyPhotoTable.TABLE_NAME, null, MyPhoto.MyPhotoTable.USER_NAME+"=?", new String[]{BluetoothChatService.nowuser.getLoginname()}, 
				null, null,MyPhotoTable.PHOTO_TIME+" desc");
		while(cursor.moveToNext()){
			MyPhoto photo = new MyPhoto();
			photo.setTime(cursor.getString(cursor.getColumnIndex(MyPhoto.MyPhotoTable.PHOTO_TIME)));
			photo.setPhotoDes(cursor.getString(cursor.getColumnIndex(MyPhoto.MyPhotoTable.PHOTO_DESC)));
			photo.setPicPath(cursor.getString(cursor.getColumnIndex(MyPhoto.MyPhotoTable.PHOTO_PATH)));
			photo.setMyphoto(BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex(MyPhoto.MyPhotoTable.PHOTO_PATH))));
			photoList.add(photo);
		}
		db.close();
		cursor.close();
		return photoList;
	}
	/**
	 * 获取相册图片个数
	 * @param context
	 * @return
	 */
	public static int getSizePhotos(Context context){
		DatabaseHelper dbHelper = new DatabaseHelper(context,MyProviderMetaData.DATABASE_NAME);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(MyPhoto.MyPhotoTable.TABLE_NAME, null, MyPhoto.MyPhotoTable.USER_NAME+"=?", new String[]{BluetoothChatService.nowuser.getLoginname()}, 
				null, null,MyPhotoTable.PHOTO_TIME+" desc");
		return cursor.getCount();
	}
	/**
	 * 插入相册信息
	 * @param context
	 * @return
	 */
	public static boolean insertPhoto(Context context,MyPhoto myphoto){
		ContentValues values = new ContentValues();
		values.put(MyPhotoTable.USER_NAME, myphoto.getUsername());
		values.put(MyPhotoTable.PHOTO_TIME,myphoto.getTime());
		values.put(MyPhotoTable.PHOTO_PATH,myphoto.getPicPath());
		values.put(MyPhotoTable.PHOTO_DESC,myphoto.getPhotoDes());
	
		DatabaseHelper dbHelper = new DatabaseHelper(context,MyProviderMetaData.DATABASE_NAME);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		if(db.insert(MyPhoto.MyPhotoTable.TABLE_NAME, null, values)>0){
			db.close();
			return true;
		}else{
			db.close();
			return false;
		}
		
	}
	/**
	 * 删除图片
	 * @param context
	 * @param photo
	 * @return
	 */
	public static boolean deletePhoto(Context context,MyPhoto photo){
		DatabaseHelper dbHelper = new DatabaseHelper(context,MyProviderMetaData.DATABASE_NAME);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if(db.delete(MyPhoto.MyPhotoTable.TABLE_NAME, MyPhoto.MyPhotoTable.PHOTO_PATH+"=?", new String[]{photo.getPicPath()})>0){
			db.close();
			return true;
		}else{
			db.close();
			return true;
		}
		
		
	}
	/**
	 * 更新用户信息(只是写了更新ID)
	 * @param context
	 * @param selection
	 * @param args
	 * @param ID
	 * @param values
	 * @return
	 */
	public static boolean updateData(Context context,String selection,String[] args,long ID,ContentValues values){
		Uri uri = ContentUris.withAppendedId(UserTableMetaData.CONTENT_URI, ID);
		int count = context.getContentResolver().update(uri, values, selection,args );
		if(count>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 更新用户信息
	 * @param context
	 * @param selection
	 * @param args
	 * @param ID
	 * @param values
	 * @return
	 */
	public static boolean updateData(Context context,String selection,String[] args,ContentValues values){
		Uri uri = UserTableMetaData.CONTENT_URI;
		int count = context.getContentResolver().update(uri, values, selection,args );
		if(count>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 删除用户信息(只是写了删除ID)
	 * @param context
	 * @param ID
	 * @return
	 */
	public static boolean deleteData(Context context,long ID){
		Uri uri = ContentUris.withAppendedId(UserTableMetaData.CONTENT_URI, ID);
		int count = context.getContentResolver().delete(uri,null,null );;
		if(count>0){
			return true;
		}else{
			return false;
		}
	}
}
