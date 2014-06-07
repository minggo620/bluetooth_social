package edu.minggo.chat.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import edu.minggo.chat.R;
import edu.minggo.chat.adapters.EmaiListAdapter;
import edu.minggo.chat.control.BluetoothChatInterface;
import edu.minggo.chat.control.BluetoothChatService;
import edu.minggo.chat.database.DataBaseOperator;
import edu.minggo.chat.database.MyProviderMetaData.UserTableMetaData;


/**
 * 更新自己的信息
 * @author minggo
 * @created 2013-2-6上午02:41:55
 */
public class ModifyMyInformationActivity extends Activity implements BluetoothChatInterface{
	public final static int MODIFY_NAME = 0; //更新姓名
	public final static int MODIFY_MOTTO = 9;//更新个性签名
	public final static int MODIFY_EMAIL = 5;//更新邮箱地址
	public final static int MODIFY_AGE = 1;//更新年龄
	public final static int MODIFY_PHONE = 3;//更新电话
	public final static int MODIFY_PROVINCE = 4;//更新地区
	public final static int MODIFY_LOGINNAME = 6;//更新登录名
	public final static int MODIFY_HOBBY = 7;//更新爱好
	public final static int MODIFY_INTRODUCE = 8;//更新自我介绍
	private Button backButton;
	private Button saveBttuon;
	private TextView title;
	private TextView mTextNum;
	private EditText editContent;
	private TextView contentTips;
	private String contentItem;
	private String content;
	private int kind;
	private View view;
	private View wordDelete;
	private ContentValues values;
	private ListView emails;
	private EmaiListAdapter emaiListAdapter;
	private Intent it;//用做setResult中用
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.modify_myinfo);
		
		backButton = (Button)findViewById(R.id.modify_exit_button);
		saveBttuon = (Button)findViewById(R.id.modify_save_button);
		title = (TextView)findViewById(R.id.modify_title_text);
		
		contentTips = (TextView)findViewById(R.id.modify_tips);
		view  = (View)findViewById(R.id.longContent);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("data");
		contentItem =  bundle.getString("contentItem");
		content =  bundle.getString("content");
		kind =  bundle.getInt("kind");
		title.setText(getResources().getString(R.string.modify_title)+contentItem);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BluetoothChatService.allActivity.remove(this);
				finish();
			}
		});
		init();
		
		editContent.setText(content);
		
		it = new Intent();
		
	}
	public class EmailItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			TextView emailText = (TextView)view.findViewById(R.id.email_item_text);
			editContent.setText(emailText.getText().toString());
			emails.setVisibility(View.GONE);
		}
		
	}
	@Override
	public void init() {
		values =new ContentValues();
		switch (kind) {
		case MODIFY_HOBBY:
			editContent = (EditText)findViewById(R.id.modify_content);
			editContent.setVisibility(View.VISIBLE);
			contentTips.setText(R.string.modify_hobby_tips);
			saveBttuon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					content = editContent.getText().toString();
					values.put(UserTableMetaData.USER_HOBBY, content);
					boolean flag = DataBaseOperator.updateData(ModifyMyInformationActivity.this.getApplicationContext(), UserTableMetaData.USER_LOGINNAME+"=?", new String[]{BluetoothChatService.nowuser.getLoginname()}, values);
					if(flag){
						
						Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_LONG).show();
						setResult(Activity.RESULT_OK,it.putExtra("hobby", content));
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		case MODIFY_LOGINNAME:
			editContent = (EditText)findViewById(R.id.modify_content);
			editContent.setVisibility(View.VISIBLE);
			contentTips.setText(R.string.modify_lognname_tips);
			saveBttuon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					content = editContent.getText().toString();
					values.put(UserTableMetaData.USER_LOGINNAME, content);
					boolean flag = DataBaseOperator.updateData(ModifyMyInformationActivity.this.getApplicationContext(), UserTableMetaData.USER_LOGINNAME+"=?", new String[]{BluetoothChatService.nowuser.getLoginname()}, values);
					if(flag){
						Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_LONG).show();
						setResult(Activity.RESULT_OK,it.putExtra("loginname", content));
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		case MODIFY_PROVINCE:
			editContent = (EditText)findViewById(R.id.modify_content);
			editContent.setVisibility(View.VISIBLE);
			contentTips.setText(R.string.modify_province_tips);
			saveBttuon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					content = editContent.getText().toString();
					values.put(UserTableMetaData.USER_PROVINCE, content);
					boolean flag = DataBaseOperator.updateData(ModifyMyInformationActivity.this.getApplicationContext(), UserTableMetaData.USER_LOGINNAME+"=?", new String[]{BluetoothChatService.nowuser.getLoginname()}, values);
					if(flag){
						Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_LONG).show();
						setResult(Activity.RESULT_OK,it.putExtra("province", content));
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		case MODIFY_PHONE:
			editContent = (EditText)findViewById(R.id.modify_content);
			editContent.setVisibility(View.VISIBLE);
			editContent.setInputType(InputType.TYPE_CLASS_PHONE);
			contentTips.setText(R.string.modify_phone_tips);
			saveBttuon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					content = editContent.getText().toString();
					values.put(UserTableMetaData.USER_TELEPHONE, content);
					boolean flag = DataBaseOperator.updateData(ModifyMyInformationActivity.this.getApplicationContext(), UserTableMetaData.USER_LOGINNAME+"=?", new String[]{BluetoothChatService.nowuser.getLoginname()}, values);
					if(flag){
						setResult(Activity.RESULT_OK,it.putExtra("tel", content));
						Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_LONG).show();
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		case MODIFY_AGE:
			editContent = (EditText)findViewById(R.id.modify_content);
			editContent.setVisibility(View.VISIBLE);
			editContent.setInputType(InputType.TYPE_CLASS_NUMBER);
			contentTips.setText(R.string.modify_age_tips);
			saveBttuon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					content = editContent.getText().toString();
					values.put(UserTableMetaData.USER_AGE, content);
					boolean flag = DataBaseOperator.updateData(ModifyMyInformationActivity.this.getApplicationContext(), UserTableMetaData.USER_LOGINNAME+"=?", new String[]{BluetoothChatService.nowuser.getLoginname()}, values);
					if(flag){
						Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_LONG).show();
						setResult(Activity.RESULT_OK,it.putExtra("age", content));
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		case MODIFY_EMAIL:
			editContent = (EditText)findViewById(R.id.modify_content);
			emails = (ListView)findViewById(R.id.email_auto_text);
			emaiListAdapter = new EmaiListAdapter(getApplicationContext());
			emails.setAdapter(emaiListAdapter);
			emails.setOnItemClickListener(new EmailItemListener());
			editContent.setVisibility(View.VISIBLE);
			contentTips.setText(R.string.modify_email_tips);
			editContent.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					System.out.println(s+"===="+start+"===="+before+"===="+count);
					if(BluetoothChatService.nowuser.getEmail()!=null&&!BluetoothChatService.nowuser.getEmail().contains(s)){
						emails.setVisibility(View.VISIBLE);
						for(int i = 0;i<emaiListAdapter.subEmailStr.length;i++){
							if(!s.toString().contains("@"))
							emaiListAdapter.subEmailStr[i]=s+emaiListAdapter.subname[i];
							else
							emaiListAdapter.subEmailStr[i]=s.toString().substring(0, s.toString().indexOf("@"))+emaiListAdapter.subname[i];
						}
					}else{
						emails.setVisibility(View.GONE);
					}
					emaiListAdapter.notifyDataSetChanged();
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				@Override
				public void afterTextChanged(Editable s) {
					
				}
			});
			
			saveBttuon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					content = editContent.getText().toString();
					values.put(UserTableMetaData.USER_EMAIL, content);
					boolean flag = DataBaseOperator.updateData(ModifyMyInformationActivity.this.getApplicationContext(), UserTableMetaData.USER_LOGINNAME+"=?", new String[]{BluetoothChatService.nowuser.getLoginname()}, values);
					if(flag){
						Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_LONG).show();
						setResult(Activity.RESULT_OK,it.putExtra("email", content));
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		case MODIFY_NAME://更新用户名
			editContent = (EditText)findViewById(R.id.modify_content);
			editContent.setVisibility(View.VISIBLE);
			contentTips.setText(R.string.modify_name_tips);
			
			saveBttuon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					content = editContent.getText().toString();
					values.put(UserTableMetaData.USER_NAME, content);
					boolean flag = DataBaseOperator.updateData(ModifyMyInformationActivity.this.getApplicationContext(), UserTableMetaData.USER_LOGINNAME+"=?", new String[]{BluetoothChatService.nowuser.getLoginname()}, values);
					if(flag){
						Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_LONG).show();
						it.putExtra("name", content);
						setResult(Activity.RESULT_OK, it);
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		case MODIFY_INTRODUCE:
			view.setVisibility(View.VISIBLE);
			editContent = (EditText)findViewById(R.id.modify_long_content);
			wordDelete = (View)findViewById(R.id.text_limit_unit);
			mTextNum = (TextView) this.findViewById(R.id.tv_text_limit);
			
			contentTips.setText(R.string.modify_introduce_tips);
			
			wordDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					editContent.setText("");
				}
			});
			
			editContent.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					String mText = editContent.getText().toString();
	                int len = mText.length();
	                if (len <= 20) {
	                    len = 20 - len;
	                    mTextNum.setTextColor(R.color.text_num_gray);
	                    if (!saveBttuon.isEnabled()){
	                    	saveBttuon.setVisibility(View.VISIBLE);
	                    	saveBttuon.setEnabled(true);
	                    }
	                } else {
	                    len = len - 20;

	                    mTextNum.setTextColor(Color.RED);
	                    if (saveBttuon.isEnabled()){
	                    	saveBttuon.setVisibility(View.GONE);
	                    	saveBttuon.setEnabled(false);
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
			
			saveBttuon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					content = editContent.getText().toString();
					values.put(UserTableMetaData.USER_INTRODUCE, content);
					boolean flag = DataBaseOperator.updateData(ModifyMyInformationActivity.this.getApplicationContext(), UserTableMetaData.USER_LOGINNAME+"=?", new String[]{BluetoothChatService.nowuser.getLoginname()}, values);
					if(flag){
						Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_LONG).show();
						setResult(Activity.RESULT_OK, it.putExtra("introduce", content));
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		case MODIFY_MOTTO:
			view.setVisibility(View.VISIBLE);
			editContent = (EditText)findViewById(R.id.modify_long_content);
			wordDelete = (View)findViewById(R.id.text_limit_unit);
			mTextNum = (TextView) this.findViewById(R.id.tv_text_limit);
			
			contentTips.setText(R.string.modify_motto_tips);
			
			wordDelete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					editContent.setText("");
				}
			});
			
			editContent.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					String mText = editContent.getText().toString();
	                int len = mText.length();
	                if (len <= 20) {
	                    len = 20 - len;
	                    mTextNum.setTextColor(R.color.text_num_gray);
	                    if (!saveBttuon.isEnabled()){
	                    	saveBttuon.setVisibility(View.VISIBLE);
	                    	saveBttuon.setEnabled(true);
	                    }
	                } else {
	                    len = len - 20;

	                    mTextNum.setTextColor(Color.RED);
	                    if (saveBttuon.isEnabled()){
	                    	saveBttuon.setVisibility(View.GONE);
	                    	saveBttuon.setEnabled(false);
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
			
			saveBttuon.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					content = editContent.getText().toString();
					values.put(UserTableMetaData.USER_MOTTO, content);
					boolean flag = DataBaseOperator.updateData(ModifyMyInformationActivity.this.getApplicationContext(), UserTableMetaData.USER_LOGINNAME+"=?", new String[]{BluetoothChatService.nowuser.getLoginname()}, values);
					if(flag){
						setResult(Activity.RESULT_OK, it.putExtra("motto", content));
						Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_LONG).show();
						finish();
					}else{
						Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		}
		
	}
	public void initButtonListen(){
	}
	public void deal(){
	}
	@Override
	protected void onResume() {
		super.onResume();
		init();
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void refresh(Object... param) {
	}	
	
}
