package edu.minggo.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import edu.minggo.chat.R;
import edu.minggo.chat.adapters.AboutLDialogAdapter;


public class AboutDialogActivity extends Activity {
	private TextView titleText;
	private Button confirm;
	private ListView aboutList;
	private AboutLDialogAdapter listAdapter;
	private String[] source ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置窗口tittle个性化
		
		WindowManager.LayoutParams lp=this.getWindow().getAttributes();   
        lp.dimAmount=0.7f;   
        this.getWindow().setAttributes(lp);   
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);  
        
        setContentView(R.layout.about_list);
		
		titleText = (TextView)findViewById(R.id.about_title_text);
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		titleText.setText(title);
		
		confirm = (Button)findViewById(R.id.about_confirm);
		aboutList = (ListView)findViewById(R.id.about_list_selection);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		if(title.equals("功能介绍")){
			source = new String[]{"发信息","发微薄","玩游戏","相册"};
			listAdapter = new AboutLDialogAdapter(getApplicationContext(), source);
		}else if(title.equals("检查新版本")){
			source = new String[]{"已是最新版"};
			listAdapter = new AboutLDialogAdapter(getApplicationContext(), source);
		}
		aboutList.setAdapter(listAdapter);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
		
	}
	
}
