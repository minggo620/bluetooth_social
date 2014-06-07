package edu.minggo.chat.ui;

import java.io.File;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.minggo.chat.R;
import edu.minggo.chat.control.BluetoothChatInterface;
import edu.minggo.chat.control.BluetoothChatService;
import edu.minggo.chat.database.DataBaseOperator;
import edu.minggo.chat.database.MyProviderMetaData.UserTableMetaData;
import edu.minggo.chat.util.ClippingPicture;
import edu.minggo.chat.util.OptionAlert;
/**
 * 个人信息设定
 * @author minggo
 * @created 2013-2-3下午09:00:12
 */
public class PersonalInfoSettingAcitivity extends Activity implements BluetoothChatInterface {
	private static final int GET_PHOTO_FROM_LOCAL = 1;
	private static final int GET_PHOTO_FROM_CARMERA = 0;
	
	private static final int MODIFY_MYINFO_NAME = 101;//修改邮件
	private static final int MODIFY_MYINFO_AGE = 102;//修改年龄
	private static final int MODIFY_MYINFO_SEX = 103;//选择性别
	private static final int MODIFY_MYINFO_PHONE = 104;//修改电话
	private static final int MODIFY_MYINFO_PROVINCE = 105;//修改地区
	private static final int MODIFY_MYINFO_EMAIL = 106;//修改邮件
	private static final int MODIFY_MYINFO_LOGINNAME = 107;//修改登录名
	private static final int MODIFY_MYINFO_HOBBY = 108;//修改爱好
	private static final int MODIFY_MYINFO_INTRODUCE = 109;//修改自我介绍
	private static final int MODIFY_MYINFO_PERSONALITY = 110;//修改自我介绍
	
	private View v0;
	private View v1;
	private View v2;
	private View v3;
	private View v4;
	private View v5;
	private View v6;
	private View v7;
	private View v8;
	private View v9;
	private View v10;
	
	private TextView nametv;
	private TextView agetv;
	private TextView sextv;
	private TextView teltv;
	private TextView provincetv;
	private TextView emailtv;
	private TextView loginnametv;
	private TextView interesttv;
	private TextView introducetv;
	private TextView personalitytv;
	private Button backbt;
	private ImageView portraitiv;
	private ImageView mypicLarge;
	private ImageView delectImg;
	private AlphaAnimation alphaAnimation;
	private View framelayout;
	private String mypicPath;
	private ImageView submenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_my_info);
		v0 = findViewById(R.id.myinfo_portrait_v_setting);
		v1 = findViewById(R.id.myinfo_name_v_setting);
		v2 = findViewById(R.id.myinfo_age_v_setting);
		v3 = findViewById(R.id.myinfo_sex_v_setting);
		v4 = findViewById(R.id.myinfo_tel_v_setting);
		v5 = findViewById(R.id.myinfo_province_v_setting);
		v6 = findViewById(R.id.myinfo_email_v_setting);
		v7 = findViewById(R.id.myinfo_interest_v_setting);
		v8 = findViewById(R.id.myinfo_loginname_v_setting);
		v9 = findViewById(R.id.myinfo_introduce_v_setting);
		v10 = findViewById(R.id.myinfo_personality_v_setting);
		
		backbt = (Button) findViewById(R.id.myinfo_bt_back);
		portraitiv = (ImageView) findViewById(R.id.setting_iv_portrait);
		
		framelayout = (View)findViewById(R.id.flPic);
		submenu = (ImageView)findViewById(R.id.menu);
		delectImg = (ImageView)findViewById(R.id.ivDelPic);
		
		nametv = (TextView) findViewById(R.id.my_infor_name);
		agetv=(TextView)findViewById(R.id.my_infor_age);
		sextv=(TextView) findViewById(R.id.my_infor_sex);
		teltv=(TextView) findViewById(R.id.my_infor_tel);
		provincetv=(TextView)findViewById(R.id.my_infor_province);
		emailtv=(TextView) findViewById(R.id.my_infor_email);
		loginnametv=(TextView)findViewById(R.id.my_infor_loginname);
		interesttv=(TextView)findViewById(R.id.my_infor_interesting);
		introducetv=(TextView) findViewById(R.id.my_infor_introduced);
		personalitytv=(TextView)findViewById(R.id.my_infor_personallity);
		
		if(BluetoothChatService.nowuser.getUsername()!=null)
			nametv.setText(BluetoothChatService.nowuser.getUsername());
		if(BluetoothChatService.nowuser.getAge()!=null)
			agetv.setText(BluetoothChatService.nowuser.getAge());
		if(BluetoothChatService.nowuser.getSex()!=null)
			sextv.setText(BluetoothChatService.nowuser.getSex());
		if(BluetoothChatService.nowuser.getTelephone()!=null)
			teltv.setText(BluetoothChatService.nowuser.getTelephone());
		if(BluetoothChatService.nowuser.getProvince()!=null)
			provincetv.setText(BluetoothChatService.nowuser.getProvince());
		if(BluetoothChatService.nowuser.getEmail()!=null)
			emailtv.setText(BluetoothChatService.nowuser.getEmail());
		if(BluetoothChatService.nowuser.getLoginname()!=null)
			loginnametv.setText(BluetoothChatService.nowuser.getLoginname());
		if(BluetoothChatService.nowuser.getHobby()!=null)
			interesttv.setText(BluetoothChatService.nowuser.getHobby());
		if(BluetoothChatService.nowuser.getIntroduce()!=null)
			introducetv.setText(BluetoothChatService.nowuser.getIntroduce());
		if(BluetoothChatService.nowuser.getMotto()!=null)
			personalitytv.setText(BluetoothChatService.nowuser.getMotto());
		
		portraitiv.setImageBitmap(BluetoothChatService.nowuser.getPhoto());
		
		backbt.setOnClickListener(new PersonalSettingListener());
		v0.setOnClickListener(new PersonalSettingListener());
		v1.setOnClickListener(new PersonalSettingListener());
		v2.setOnClickListener(new PersonalSettingListener());
		v3.setOnClickListener(new PersonalSettingListener());
		v4.setOnClickListener(new PersonalSettingListener());
		v5.setOnClickListener(new PersonalSettingListener());
		v6.setOnClickListener(new PersonalSettingListener());
		v7.setOnClickListener(new PersonalSettingListener());
		v8.setOnClickListener(new PersonalSettingListener());
		v9.setOnClickListener(new PersonalSettingListener());
		v10.setOnClickListener(new PersonalSettingListener());
		delectImg.setOnClickListener(new PersonalSettingListener());
		portraitiv.setOnClickListener(new PersonalSettingListener());
		
		
	}
	/**
	 * 个人信息的监听器
	 * @author minggo
	 * @created 2013-2-3下午10:18:47
	 */
	public class PersonalSettingListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v==v0){
				OptionAlert.showAlert(PersonalInfoSettingAcitivity.this, getString(R.string.set_myinfo), 
						PersonalInfoSettingAcitivity.this.getResources().getStringArray(R.array.set_myinfo_item),
						null, new OptionAlert.OnAlertSelectId(){
	
					@Override
					public void onClick(int whichButton) {						
						switch(whichButton){
						
						case GET_PHOTO_FROM_LOCAL:
							Intent intent = new Intent();
							intent.setType("image/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);
							startActivityForResult(Intent.createChooser(intent, "Select Picture"),GET_PHOTO_FROM_LOCAL);
							break;
						case GET_PHOTO_FROM_CARMERA:
                            Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(it,GET_PHOTO_FROM_CARMERA);     
							break;
						default:
							break;
						}
						
					}
				});
			}else if(v==v1){
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("contentItem", "名字");
				String name="未填写";
				if(BluetoothChatService.nowuser.getUsername()!=null)
					name = BluetoothChatService.nowuser.getUsername();
				bundle.putString("content", name);
				bundle.putInt("kind", ModifyMyInformationActivity.MODIFY_NAME);
				intent.setClass(PersonalInfoSettingAcitivity.this,ModifyMyInformationActivity.class);
				intent.putExtra("data", bundle);
				startActivityForResult(intent, MODIFY_MYINFO_NAME);
			}else if(v==v2){
				Intent intent = new Intent(PersonalInfoSettingAcitivity.this,ModifyMyInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("contentItem", "年龄");
				String age = "未填写";
				if(BluetoothChatService.nowuser.getAge()!=null)
					age=BluetoothChatService.nowuser.getAge();
				bundle.putString("content",age);
				bundle.putInt("kind", ModifyMyInformationActivity.MODIFY_AGE);
				intent.putExtra("data", bundle);
				startActivityForResult(intent, MODIFY_MYINFO_AGE);
			}else if(v==v3){
				Intent intent = new Intent(getApplicationContext(), SexSelectActivity.class);
				if(BluetoothChatService.nowuser.getSex()==null){
					intent.putExtra("sex", "男");
				}else{
					intent.putExtra("sex", BluetoothChatService.nowuser.getSex());
				}
				startActivityForResult(intent, MODIFY_MYINFO_SEX);
			}else if(v==v4){
				Intent intent = new Intent(PersonalInfoSettingAcitivity.this,ModifyMyInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("contentItem", "电话");
				String tel = "未填写";
				if(BluetoothChatService.nowuser.getTelephone()!=null)
					tel = BluetoothChatService.nowuser.getTelephone();
				bundle.putString("content",tel);
				bundle.putInt("kind", ModifyMyInformationActivity.MODIFY_PHONE);
				intent.putExtra("data", bundle);
				startActivityForResult(intent, MODIFY_MYINFO_PHONE);
			}else if(v==v5){
				Intent intent = new Intent(PersonalInfoSettingAcitivity.this,ModifyMyInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("contentItem", "地区");
				String province = "未填写";
				if(BluetoothChatService.nowuser.getProvince()!=null)
					province=BluetoothChatService.nowuser.getProvince();
				bundle.putString("content",province);
				bundle.putInt("kind", ModifyMyInformationActivity.MODIFY_PROVINCE);
				intent.putExtra("data", bundle);
				startActivityForResult(intent, MODIFY_MYINFO_PROVINCE);
			}else if(v==v6){
				Intent intent = new Intent(PersonalInfoSettingAcitivity.this,ModifyMyInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("contentItem", "Email");
				String email = "未填写";
				if(BluetoothChatService.nowuser.getEmail()!=null)
					email=BluetoothChatService.nowuser.getEmail();
				bundle.putString("content",email);
				bundle.putInt("kind", ModifyMyInformationActivity.MODIFY_EMAIL);
				intent.putExtra("data", bundle);
				startActivityForResult(intent, MODIFY_MYINFO_EMAIL);
			}else if(v==v7){
				Intent intent = new Intent(PersonalInfoSettingAcitivity.this,ModifyMyInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("contentItem", "兴趣爱好");
				String hobby = "未填写";
				if(BluetoothChatService.nowuser.getHobby()!=null)
					hobby = BluetoothChatService.nowuser.getHobby();
				bundle.putString("content",hobby);
				bundle.putInt("kind", ModifyMyInformationActivity.MODIFY_HOBBY);
				intent.putExtra("data", bundle);
				startActivityForResult(intent, MODIFY_MYINFO_HOBBY);
			}else if(v==v8){
				Intent intent = new Intent(PersonalInfoSettingAcitivity.this,ModifyMyInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("contentItem", "登录名");
				String loginname = "未填写";
				if(BluetoothChatService.nowuser.getLoginname()!=null)
					loginname=BluetoothChatService.nowuser.getLoginname();
				bundle.putString("content",loginname);
				bundle.putInt("kind", ModifyMyInformationActivity.MODIFY_LOGINNAME);
				intent.putExtra("data", bundle);
				startActivityForResult(intent, MODIFY_MYINFO_LOGINNAME);
			}else if(v==v9){
				Intent intent = new Intent(PersonalInfoSettingAcitivity.this,ModifyMyInformationActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("contentItem", "自我介绍");
				String introduce = "未填写";
				if(BluetoothChatService.nowuser.getIntroduce()!=null)
					introduce = BluetoothChatService.nowuser.getIntroduce();
				bundle.putString("content",introduce);
				bundle.putInt("kind", ModifyMyInformationActivity.MODIFY_INTRODUCE);
				intent.putExtra("data", bundle);
				startActivityForResult(intent, MODIFY_MYINFO_INTRODUCE);
			}else if(v==v10){
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("contentItem", "个性签名");
				String motto = "未填写";
				if(BluetoothChatService.nowuser.getMotto()!=null)
					motto = BluetoothChatService.nowuser.getMotto();
				bundle.putString("content", motto);
				bundle.putInt("kind", ModifyMyInformationActivity.MODIFY_MOTTO);
				intent.setClass(PersonalInfoSettingAcitivity.this,ModifyMyInformationActivity.class);
				intent.putExtra("data", bundle);
				PersonalInfoSettingAcitivity.this.startActivityForResult(intent, MODIFY_MYINFO_PERSONALITY);
			}else if(v==portraitiv){
				submenu.setImageResource(R.drawable.menu_pessed);
				framelayout.setVisibility(View.VISIBLE);
				alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
				framelayout.setAnimation(alphaAnimation);
				alphaAnimation.setDuration(2000);
				
				mypicLarge = (ImageView)framelayout.findViewById(R.id.ivImage);
				if(BluetoothChatService.nowuser.getPhoto()!=null){
					mypicLarge.setImageBitmap(BluetoothChatService.nowuser.getPhoto());
				}else{
					mypicLarge.setImageResource(R.drawable.mywife);
				}
			}else if(delectImg==v){
				framelayout.setVisibility(View.GONE);
				submenu.setImageResource(R.drawable.mm_submenu_normal);
			}else if(v==backbt){
				finish();
			}
			
		}
		
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case MODIFY_MYINFO_INTRODUCE:
			if (resultCode == Activity.RESULT_OK) {
				String introduce = data.getStringExtra("introduce");
				introducetv.setText(introduce);
				BluetoothChatService.nowuser.setIntroduce(introduce);
			}
			break;
		case MODIFY_MYINFO_LOGINNAME:
			if (resultCode == Activity.RESULT_OK) {
				String loginname = data.getStringExtra("loginname");
				loginnametv.setText(loginname);
				BluetoothChatService.nowuser.setLoginname(loginname);
			}
			break;
		case MODIFY_MYINFO_HOBBY:
			if (resultCode == Activity.RESULT_OK) {
				String hobby = data.getStringExtra("hobby");
				interesttv.setText(hobby);
				BluetoothChatService.nowuser.setHobby(hobby);
			}
			break;
		case MODIFY_MYINFO_EMAIL:
			if (resultCode == Activity.RESULT_OK) {
				String email = data.getStringExtra("email");
				emailtv.setText(email);
				BluetoothChatService.nowuser.setEmail(email);
			}
			break;
		case MODIFY_MYINFO_PROVINCE:
			if (resultCode == Activity.RESULT_OK) {
				String province = data.getStringExtra("province");
				provincetv.setText(province);
				BluetoothChatService.nowuser.setProvince(province);
			}
			break;
		case MODIFY_MYINFO_PHONE:
			if (resultCode == Activity.RESULT_OK) {
				String tel = data.getStringExtra("tel");
				teltv.setText(tel);
				BluetoothChatService.nowuser.setTelephone(tel);
			}
			break;
		case MODIFY_MYINFO_AGE:
			if (resultCode == Activity.RESULT_OK) {
				String age = data.getStringExtra("age");
				agetv.setText(age);
				BluetoothChatService.nowuser.setAge(age);
			}
			break;
		case MODIFY_MYINFO_PERSONALITY:
			if (resultCode == Activity.RESULT_OK) {
				String motto = data.getStringExtra("motto");
				personalitytv.setText(motto);
				BluetoothChatService.nowuser.setMotto(motto);
			}
			break;
		case MODIFY_MYINFO_NAME:
			if (resultCode == Activity.RESULT_OK) {
				String name = data.getStringExtra("name");
				nametv.setText(name);
				BluetoothChatService.nowuser.setUsername(name);
			}
			break;
		case MODIFY_MYINFO_SEX:
			if (resultCode == Activity.RESULT_OK) {
				String sex = data.getStringExtra("sexSelect");
				sextv.setText(sex);
				BluetoothChatService.nowuser.setSex(sex);
				ContentValues values =new ContentValues();
				values.put(UserTableMetaData.USER_SEX, sex);
			    DataBaseOperator.updateData(PersonalInfoSettingAcitivity.this.getApplicationContext()
			    		, UserTableMetaData.USER_LOGINNAME+"=?"+" and "+UserTableMetaData.USER_PERSONKIND+"='user'"
			    		, new String[]{BluetoothChatService.nowuser.getLoginname()},values);
			}
			break;
		case GET_PHOTO_FROM_LOCAL:
			if (resultCode == Activity.RESULT_OK) {
				if (data!=null&&data.getData()!=null) {
					Bitmap bitmap = null;
					Uri uri = data.getData();
					portraitiv.setImageURI(uri);
					Cursor cursor = getContentResolver().query(uri, null, null, null, null);
				    cursor.moveToFirst();
				    mypicPath = cursor.getString(1); //图片文件路径
				    if (new File(mypicPath).length()>1024*512) {
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inJustDecodeBounds = false;
						options.inSampleSize = 6;
						bitmap = BitmapFactory.decodeFile(mypicPath,options);
					}else{
						bitmap = BitmapFactory.decodeFile(mypicPath);
					}
				    BluetoothChatService.nowuser.setPhoto(bitmap);
				    ContentValues values =new ContentValues();
					values.put(UserTableMetaData.USER_ICON, ClippingPicture.saveUserPortrait(bitmap));
				    DataBaseOperator.updateData(PersonalInfoSettingAcitivity.this.getApplicationContext()
				    		, UserTableMetaData.USER_LOGINNAME+"=?"+" and "+UserTableMetaData.USER_PERSONKIND+"='user'"
				    		, new String[]{BluetoothChatService.nowuser.getLoginname()},values);
				    Intent it = new Intent();
				    it.putExtra("bitmap", bitmap);
				    setResult(2, it);
				}
			}
			break;
		case GET_PHOTO_FROM_CARMERA:
			if (resultCode == Activity.RESULT_OK&&data!=null&&data.getExtras()!=null) {
				Bundle extras = data.getExtras();
				Bitmap bmp = (Bitmap)extras.get("data");
				portraitiv.setImageBitmap(bmp);
				bmp = ClippingPicture.Resize(bmp);
				BluetoothChatService.nowuser.setPhoto(bmp);
				ContentValues values =new ContentValues();
				values.put(UserTableMetaData.USER_ICON, ClippingPicture.saveUserPortrait(bmp));
			    DataBaseOperator.updateData(PersonalInfoSettingAcitivity.this.getApplicationContext()
			    		, UserTableMetaData.USER_LOGINNAME+"=?"+" and "+UserTableMetaData.USER_PERSONKIND+"='user'"
			    		, new String[]{BluetoothChatService.nowuser.getLoginname()},values);
			    Intent it = new Intent();
			    it.putExtra("bitmap", bmp);
			    setResult(2, it);
			}
			break;
		}
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... param) {
		// TODO Auto-generated method stub

	}

}
