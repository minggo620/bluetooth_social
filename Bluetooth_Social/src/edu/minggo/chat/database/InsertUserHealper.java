package edu.minggo.chat.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import edu.minggo.chat.database.MyProviderMetaData.UserTableMetaData;

public class InsertUserHealper {
	 /**
     * 昵称
     */
    private static String[] nicks = {"阿雅","北风","张山","李四","欧阳锋","郭靖","黄蓉","杨过","凤姐","芙蓉姐姐","移联网","樱木花道","风清扬","张三丰","梅超风"};
   
	public static void insertData(Context context){
		Uri uri = UserTableMetaData.CONTENT_URI;
		for(int i = 0 ; i<nicks.length; i++){
			ContentValues values = new ContentValues();
			values.put(UserTableMetaData.USER_NAME, nicks[i]);
			values.put(UserTableMetaData.USER_MOTTO, "世界是美好的！"+i);
			values.put(UserTableMetaData.USER_PERSONKIND, "friend");
			uri = context.getContentResolver().insert(UserTableMetaData.CONTENT_URI, values);
			System.out.println("uri----->"+uri.toString() );
		}
		Toast.makeText(context, "成功注册1-用户", Toast.LENGTH_LONG).show();
	}

}
