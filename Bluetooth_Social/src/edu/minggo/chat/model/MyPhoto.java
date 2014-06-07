package edu.minggo.chat.model;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.provider.BaseColumns;

@SuppressWarnings("serial")
public class MyPhoto implements Serializable{
	private int id;
	private String time;
	private Bitmap myphoto;
	private String photoDes;
	private String username;
	private String picPath;
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhotoDes() {
		return photoDes;
	}
	public void setPhotoDes(String photoDes) {
		this.photoDes = photoDes;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Bitmap getMyphoto() {
		return myphoto;
	}
	public void setMyphoto(Bitmap myphoto) {
		this.myphoto = myphoto;
	}
	public static final class MyPhotoTable implements BaseColumns{
		public static final String TABLE_NAME = "myphoto";
		public static final String USER_NAME = "username";
		public static final String PHOTO_TIME = "time";
		public static final String PHOTO_PATH = "path";
		public static final String PHOTO_DESC = "description";
	}
	/*@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(time);
		dest.writeParcelable(myphoto, PARCELABLE_WRITE_RETURN_VALUE);
		dest.writeString(photoDes);
		dest.writeString(username);
		dest.writeString(picPath);
		
	}
	public static final Parcelable.Creator<MyPhoto> CREATOR = new Creator<MyPhoto>() {

		@Override
		public MyPhoto createFromParcel(Parcel source) {
			MyPhoto photo = new MyPhoto();
			photo.setId(source.readInt());
			photo.setTime(source.readString());
			photo.setMyphoto((Bitmap) source.readParcelable(ClassLoader.class.getClassLoader()));
			photo.setPhotoDes(source.readString());
			photo.setUsername(source.readString());
			photo.setPicPath(source.readString());
			return photo;
		}


		@Override
		public MyPhoto[] newArray(int size) {
			return new MyPhoto[size];
		}
		
	};*/
}
