package edu.minggo.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.minggo.chat.R;
import edu.minggo.chat.util.OptionAlert;
import edu.minggo.tencent.weibo.OAuth;
/**
 * 微博绑定操作
 * @author minggo
 * @created 2013-2-28下午02:36:45
 */
public class TencentWeiboBoundActivity extends Activity {
	
	private Button oauthbt;
	private Button backbt;
	private Button cancelbt;
	private SharedPreferences prefs;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tencent_weibo_bound);
       
		oauthbt = (Button) findViewById(R.id.btn_bound);
		cancelbt = (Button)findViewById(R.id.btn_cancel);
        backbt = (Button)findViewById(R.id.weibo_reback_btn);
        
        backbt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TencentWeiboBoundActivity.this);
			    String oauth_token = prefs.getString(OAuth.OAUTH_TOKEN, "");//从prefs中取出OAuth_Token，若无则赋空值
			    String oauth_token_secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
			    if (oauth_token!=null&&oauth_token_secret!=null&&!oauth_token_secret.equals("")&&!oauth_token.equals("")) {
					setResult(4);
				}else{
					setResult(5);
				}
				finish();
			}
		});
        //用户OAuth授权
        oauthbt.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	startActivity(new Intent().setClass(v.getContext(), PrepareRequestTokenActivity.class));
            }
        });
        
        cancelbt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				OptionAlert.showAlert(TencentWeiboBoundActivity.this, "对微博取消绑定", 
						new String[]{"取消绑定微博"},
						null, new OptionAlert.OnAlertSelectId(){
	
					@Override
					public void onClick(int whichButton) {						
						switch(whichButton){
						case 0:
							prefs = PreferenceManager.getDefaultSharedPreferences(TencentWeiboBoundActivity.this);
							Editor edit = prefs.edit();
							edit.putString(OAuth.OAUTH_TOKEN, "");
							edit.putString(OAuth.OAUTH_TOKEN_SECRET, "");
							edit.commit();
							setResult(5);
							Toast.makeText(TencentWeiboBoundActivity.this, "取消成功", 2000).show();
							break;
						default:
							break;
						}
					}
				});
				
			}
		});
	}
}