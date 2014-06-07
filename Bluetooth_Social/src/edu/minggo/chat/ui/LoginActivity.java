package edu.minggo.chat.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.minggo.chat.R;
import edu.minggo.chat.adapters.LoginUserAdapter;
import edu.minggo.chat.control.BluetoothChatInterface;
import edu.minggo.chat.control.BluetoothChatService;
import edu.minggo.chat.database.DataBaseOperator;
import edu.minggo.chat.database.MyProviderMetaData.UserTableMetaData;
import edu.minggo.chat.model.Task;
import edu.minggo.chat.model.User;


public class LoginActivity extends Activity implements BluetoothChatInterface{

	public EditText etUser;
	public EditText etPass;
	public Button btLogin;
	public ImageView mywifeIcon;
	private Button rightButton;
	public static final int REFRESH_LOGIN = 1;
	private static int regist2login = 0;
	private ListView usersLogin;
	private LoginUserAdapter loginUserAdapter;
	private Button forgetpssbt;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.new_login);
		forgetpssbt = (Button) findViewById(R.id.forget_passwd);
		usersLogin = (ListView)findViewById(R.id.userlist);
		rightButton = (Button)findViewById(R.id.title_bt_right);
		rightButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(regist2login == 0){// 登录改成注册
					btLogin.setText(R.string.regist);
					rightButton.setText(R.string.login);
					regist2login = 1;
				}else{// 注册改成登录
					btLogin.setText(R.string.login);
					rightButton.setText(R.string.regist);
					regist2login = 0;
				}
			}
		});
		forgetpssbt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://user.qzone.qq.com/1053200192"); 
		    	Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
		    	startActivity(intent);
			}
		});
		etUser = (EditText) this.findViewById(R.id.user);
		etPass = (EditText) this.findViewById(R.id.password);
		btLogin = (Button) this.findViewById(R.id.loginButton);
		btLogin.setOnClickListener(new LoginButtonListener());
		//添加到Activity组件集合中
		BluetoothChatService.allActivity.add(this);
	}
	public class LoginButtonListener implements OnClickListener{

			public void onClick(View v) {
				String loginname = etUser.getText().toString();
				String password = etPass.getText().toString();
				if(!loginname.equals("")&&!password.equals("")&&regist2login == 0){ //登录
					
					Uri uri = UserTableMetaData.CONTENT_URI;
					Cursor c = getContentResolver().query(uri, null, UserTableMetaData.USER_LOGINNAME+"="+"?"+" and "+UserTableMetaData.USER_PERSONKIND+"="+"?", new String[]{loginname,"user"}, null);
					List<User> userList = DataBaseOperator.quryData(LoginActivity.this, UserTableMetaData.USER_LOGINNAME+"="+"?"+" and "+UserTableMetaData.USER_PERSONKIND+"="+"?", new String[]{loginname,"user"});
					if(c.moveToNext()){
						if(userList.get(0).getPassword().equals(password)){
							
							HashMap<String, Object> userinfo = new HashMap<String, Object>();
							userinfo.put("username", loginname);
							userinfo.put("password",password);
							userinfo.put("loginUser",userList.get(0));
							Task task  = new Task(Task.TASK_LOGIN_SUCCESS,userinfo);
							//添加到任务到Mainservice中
							BluetoothChatService.newTask(task);
							
							Intent intent = new Intent(LoginActivity.this,LoadingActivity.class);
							LoginActivity.this.startActivity(intent);
							
						}else{
							//progressDialog.cancel();//关闭进度条
							Toast.makeText(getApplicationContext(), "密码错误", 3000).show(); 
						}
						c.close();
					}else{
						//progressDialog.cancel();//关闭进度条
						Toast.makeText(getApplicationContext(), "用户名不存在", 3000).show(); 
					}
				}else if(!loginname.equals("")&&!password.equals("")&&regist2login == 1){//注册
					
					Uri uri = UserTableMetaData.CONTENT_URI;
					Cursor c = getContentResolver().query(uri, null, UserTableMetaData.USER_LOGINNAME+"="+"?"+" and "+UserTableMetaData.USER_PERSONKIND+"="+"?", new String[]{loginname,"user"}, null);
					
					if(!c.moveToNext()){
						ContentValues values = new ContentValues();
						values.put(UserTableMetaData.USER_LOGINNAME, loginname);
						values.put(UserTableMetaData.USER_PASSWORD, password);
						values.put(UserTableMetaData.USER_PERSONKIND, "user");
						uri = getContentResolver().insert(UserTableMetaData.CONTENT_URI, values);
						System.out.println("uri----->"+uri.toString() );
						Toast.makeText(getApplicationContext(), "成功注册"+loginname, Toast.LENGTH_LONG).show();
						init();
					}else{
						Toast.makeText(getApplicationContext(), "用户名已存在", 3000).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "输入不能为空", 3000).show();

				}
			}
		}
		
	//在前台的时候执行的方法
	@Override
	protected void onResume() {  
		super.onResume();
		init();
	}
	/**
	 * 这个系统最先调用
	 */
	@Override
	public void init() {
		Intent serviceintent = new Intent("edu.minggo.chat.control.BluetoothChatService");
		this.startService(serviceintent);
		Cursor c = getContentResolver().query(UserTableMetaData.CONTENT_URI, null, UserTableMetaData.USER_PERSONKIND+"=?", new String[]{"user"}, null);
		List<String> usernames = new ArrayList<String>();
		List<String> passwords = new ArrayList<String>();
		List<String> userids = new ArrayList<String>();
		while(c.moveToNext()){
			usernames.add(c.getString(c.getColumnIndex(UserTableMetaData.USER_LOGINNAME)));
			passwords.add(c.getString(c.getColumnIndex(UserTableMetaData.USER_PASSWORD)));
			userids.add(c.getString(c.getColumnIndex(UserTableMetaData._ID)));
		}
		if(usernames.size()>0){
			etUser.setText(usernames.get(0));
			etPass.setText(passwords.get(0));
		}else{
			Toast.makeText(getApplicationContext(), "请注册", 3000).show();
		}
		loginUserAdapter = new LoginUserAdapter(this, usernames,userids);
		
		usersLogin.setAdapter(loginUserAdapter);
		
		usersLogin.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				etUser.setText((String) ((TextView) view).getText());
				etPass.setText("");
			}
			
		});
		usersLogin.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Long useid = (long) Integer.parseInt(LoginUserAdapter.userid[position]);
				Uri uri = ContentUris.withAppendedId(UserTableMetaData.CONTENT_URI,useid); // 删除ID为1的记录
				BluetoothChatService.delete(LoginActivity.this, uri, useid.toString(),loginUserAdapter,position);
				return true;
			}
			
		});
		
	}
	
	@Override
	public void refresh(Object... param) {
		switch(((Integer)param[0]).intValue()){
		case -100 :
			//progressDialog.cancel();//关闭进度条
			Toast.makeText(this, "登录失败", 1000).show(); 
			break;
		case REFRESH_LOGIN :
			
			//progressDialog.cancel();
			
			//BluetoothChatService.allActivity.remove(this);
			//finish();
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			BluetoothChatService.promptExit(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
