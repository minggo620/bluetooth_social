package edu.minggo.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.minggo.chat.R;
import edu.minggo.chat.model.User;
/**
 * 个人详细信息
 * @author minggo
 * @created 2013-2-3下午12:43:12
 */
public class PersonalInforAcitivity extends Activity {
	
	private ImageView portraitiv;
	private TextView usernametv;
	private TextView provincetv;
	private TextView mottotv;
	private TextView telephonetv;
	private Button sendMessagebt;
	private Button backbt;
	private ImageButton menubt;
	private Drawable portrait;
	private int whichOitemSelected = 1;
	private int position = 1;
	private User user;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info_detail);   
        backbt = (Button) findViewById(R.id.infor_reback_btn);
        menubt = (ImageButton) findViewById(R.id.infor_bt_menu);
        portraitiv = (ImageView) findViewById(R.id.infor_iv_portrait);
        usernametv = (TextView)findViewById(R.id.infor_tv_username);
        provincetv = (TextView)findViewById(R.id.infor_tv_province);
        mottotv = (TextView)findViewById(R.id.infor_tv_motto);
        telephonetv = (TextView)findViewById(R.id.infor_tv_telephone);
        sendMessagebt = (Button)findViewById(R.id.infor_bt_sendmsg);
        
        initData();
        
    }
	void initData(){
		Intent it = getIntent();
        user = (User) it.getBundleExtra("user").get("user");
        position = it.getExtras().getInt("position");
		backbt.setOnClickListener(new MyOnclickListener());
		menubt.setOnClickListener(new MyOnclickListener());
		portraitiv.setOnClickListener(new MyOnclickListener());
		sendMessagebt.setOnClickListener(new MyOnclickListener());
		
		usernametv.setText(user.getUsername());
		provincetv.setText(user.getProvince());
		mottotv.setText(user.getMotto());
		telephonetv.setText(user.getTelephone());
		
		portrait = this.getResources().getDrawable(R.drawable.xiaohei);
		portraitiv.setImageDrawable(portrait);
	}
	
	/**
	 * @author minggo
	 * @created 2013-2-3下午01:13:17
	 */
	private class MyOnclickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v == backbt){
				finish();
			}else if(v==menubt){
				Intent it = new Intent(PersonalInforAcitivity.this, ExitFromPersonInfoActivity.class);
				PersonalInforAcitivity.this.startActivityForResult(it, whichOitemSelected);
			}else if(v==portraitiv){
				Intent it = new Intent(PersonalInforAcitivity.this, PersonalPortraitActivity.class);
				PersonalInforAcitivity.this.startActivity(it);
			}else if(v==sendMessagebt){
				Intent it = new Intent(PersonalInforAcitivity.this, ChattingActivity.class);
				PersonalInforAcitivity.this.startActivity(it);
			}
			
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode ==1){
			Intent it = new Intent();
			it.putExtra("userid", user.getUserid());
			it.putExtra("position", position);
			setResult(1,it);
			finish();
			Toast.makeText(PersonalInforAcitivity.this, "选择了删除", 3000).show();
		}else if(resultCode==2){
			
		}
	}
}
