package edu.minggo.chat.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.minggo.chat.R;
import edu.minggo.tencent.weibo.Constants;
import edu.minggo.tencent.weibo.OAuth;
import edu.minggo.tencent.weibo.WeiBoClient;
/**
 * 发表微博
 * @author minggo
 * @created 2013-2-28上午02:38:44
 */
public class TencentWeiboActivity extends Activity {
	
	final String TAG = getClass().getName();
	private SharedPreferences prefs;
	private EditText weiboContent =  null;
	private Button sendButton = null;
	private String oauth_token = "";
	private String oauth_token_secret = "";
	private WeiBoClient weiBoClient =  null;
	private Button backbt;
	private TextView mTextNum;
	private View deletev;
	private View processv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tencent_weibo);
		
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        oauth_token = prefs.getString(OAuth.OAUTH_TOKEN, "");//从prefs中取出OAuth_Token，若无则赋空值
        oauth_token_secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
        weiBoClient = new WeiBoClient(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET, oauth_token, oauth_token_secret);
        
        weiboContent = (EditText) findViewById(R.id.weibo_long_content);
        sendButton = (Button)findViewById(R.id.sendButtonId);
        backbt = (Button)findViewById(R.id.weibo_reback_btn);
        mTextNum = (TextView) findViewById(R.id.tv_text_limit);
        deletev = findViewById(R.id.text_limit_unit);
        processv = findViewById(R.id.weibo_sharing_process_v);
        processv.setVisibility(View.GONE);
        
        deletev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				weiboContent.setText("");
			}
		});
        weiboContent.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String mText = weiboContent.getText().toString();
               int len = mText.length();
               if (len <= 120) {
                   len = 120 - len;
                   mTextNum.setTextColor(R.color.text_num_gray);
                   if (!sendButton.isEnabled()){
                	   sendButton.setVisibility(View.VISIBLE);
                	   sendButton.setEnabled(true);
                   }
               } else {
                   len = len - 120;

                   mTextNum.setTextColor(Color.RED);
                   if (sendButton.isEnabled()){
                	   sendButton.setVisibility(View.GONE);
                	   sendButton.setEnabled(false);
                   }
               }
               mTextNum.setText(String.valueOf(len));
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
        
        backbt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        //发送微博消息
        sendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				processv.setVisibility(View.VISIBLE);
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TencentWeiboActivity.this);
		        oauth_token = prefs.getString(OAuth.OAUTH_TOKEN, "");//从prefs中取出OAuth_Token，若无则赋空值
		        oauth_token_secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
				//File file = Environment.getExternalStorageDirectory();
	            //String sdPath = file.getAbsolutePath();
	            //String picPath = sdPath + "/" + "abc.jpg";
	            //System.out.println(picPath);
				//需要向腾讯微博客户端发送的数据
				final Map<String, String> map = new HashMap<String, String>();
				String content = weiboContent.getText().toString();
				if (!weiboContent.getText().equals("")&&weiboContent.getText()!=null){
					 
					if (!oauth_token.equals("")&&!oauth_token_secret.equals("")) {
						map.put("format", "json");//数据格式为json
						map.put("content", content);//发布微博的内容
						map.put("clientip", "61.180.78.42");
						//map.put("pic", picPath);
						//url编码
						final List<String> decodeNames =  new  ArrayList<String>();
						decodeNames.add("oauth_signature");
						//"http://open.t.qq.com/api/t/add_pic"
						final Handler hdl = new Handler(){
							@Override
							public void handleMessage(Message msg) {
								switch (msg.what) {
								case 0:
									processv.setVisibility(View.GONE);
									Toast.makeText(TencentWeiboActivity.this, "发表成功", Toast.LENGTH_LONG).show();
									weiboContent.setText("");
									break;
								}
							}
						};
						hdl.postDelayed(new Runnable() {
							@Override
							public void run() {
								String result = weiBoClient.doPost("http://open.t.qq.com/api/t/add", map, decodeNames);
								if (result!=null) {
									hdl.obtainMessage(0).sendToTarget();
								}
							}
						},1000);
					}else{
						Toast.makeText(TencentWeiboActivity.this, "微博没有绑定，请在设置中绑定！", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(TencentWeiboActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
       
	}
}