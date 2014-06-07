package edu.minggo.chat.ui;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import edu.minggo.chat.R;
/**
 * 环形进度条
 * @author minggo
 * @created 2013-1-28下午11:45:40
 */
public class LoadingActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.loading);
			
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				
				Intent intent = new Intent (LoadingActivity.this,MainTabActivity.class);			
				startActivity(intent);			
				LoadingActivity.this.finish();
				Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
			}
		}, 1000);
   }
}