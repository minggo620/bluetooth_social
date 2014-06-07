package edu.minggo.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.minggo.chat.R;
/**
 * 关于
 * @author minggo
 * @created 2013-2-4下午09:15:01
 */
public class AppAboutActivity extends Activity {
	private Button backButton;
	private View functionv;
	private View checkv;
	private View welcomev;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_about);
		
		functionv = findViewById(R.id.about_v_functions);
		checkv = findViewById(R.id.about_v_check);
		welcomev = findViewById(R.id.about_v_welcome);
		backButton = (Button)findViewById(R.id.about_head_backbt);
		
		functionv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppAboutActivity.this,AboutDialogActivity.class);
				intent.putExtra("title", "功能介绍");
				AppAboutActivity.this.startActivity(intent);
			}
		});
		checkv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppAboutActivity.this,AboutDialogActivity.class);
				intent.putExtra("title", "检查新版本");
				AppAboutActivity.this.startActivity(intent);
			}
		});
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		welcomev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppAboutActivity.this,WelcomeActivity.class);
				AppAboutActivity.this.startActivity(intent);
				
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
