package edu.minggo.chat.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Instrumentation;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MapActivity;
import com.baidu.mobstat.StatService;

import edu.minggo.chat.R;
import edu.minggo.chat.adapters.ChatMessageAdapter;
import edu.minggo.chat.adapters.SmileyPagerAdapter;
import edu.minggo.chat.control.BluetoothChatService;
import edu.minggo.chat.model.MessageEntity;
import edu.minggo.chat.model.Smiley;
import edu.minggo.chat.model.Task;
import edu.minggo.chat.util.BaiMapGlobalApplication;
import edu.minggo.chat.util.ClippingPicture;
import edu.minggo.chat.util.ClippingSounds;
import edu.minggo.chat.util.DateUtil;
import edu.minggo.chat.util.OptionAlert;
import edu.minggo.chat.util.PlaySound;
import edu.minggo.chat.util.RecordButton;
import edu.minggo.chat.util.RecordButton.OnEventListener;
import edu.minggo.chat.util.VibratorUtil;
/**
 * 信息交互
 * @author minggo
 * @created 2013-2-7上午11:24:23
 */
public class ChattingActivity extends MapActivity implements OnClickListener{
	/********************这些主要是对于逻辑辅助******************/
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 11;//请求安全链接
	private static final int REQUEST_ENABLE_BT = 3; // 请求启动蓝牙
	private static final int DEVICE_OPERATION = 4;//对设备的操作连接或者可探测
	
	public static final int MESSAGE_STATE_CHANGE = 1; // 消息状态的改变
	public static final int MESSAGE_SEND_OK = 2; 	//	消息发送成功
	public static final int MESSAGE_SEND_FAILED = 3; 	//	消息发送成功
	public static final int MESSAGE_READ = 4;//读信息
	public static final int MESSAGE_WRITE = 6;//写信息
	public static final int MESSAGE_TOAST = 5; // Toast出链接情况
	public static final int MESSAGE_WRITE_BOUND_MAX = 7;//发送的文件大于1m
	public static final int STATE_CONNECT_THE_SAME = 8;//链接同样的设备
	public static final int STATE_LISTEN = 1; // 监听被连接状态
	public static final int STATE_CONNECTING = 2; // 初始化一个主动连接庄陶
	public static final int STATE_CONNECTED = 3; // 已经连接一个远程设备状态
	
	public static AssetManager assetManager;
	private static String requestDeviceAdd;
	private BaiMapGlobalApplication baiMapApiUtil;
	private LocationListener mLocationListener = null;
	private double baiDu_Lon;
	private double baiDu_Lat;
	
	/*****************这些参数主要是对于ui设置***********************/
	public static final int GET_PHOTO_FROM_LOCAL = 1;
	public static final int GET_PHOTO_FROM_CARMERA = 0;
	public static final int GET_PHOTO_FROM_CARMERA_RETURN = 10;
	private static ArrayList<MessageEntity> messageEntities = null;
	private int smileyNoFlag=0;
	private Button backbt;
	private ImageButton menubt;
	private Button voicebt;
	private Button keyboardbt;
	private Button addbt;
	private Button sendbt;
	private RecordButton voicesendbt;
	private EditText contentet;
	private TextView friendname;
	private View addMorev;
	private ImageView expressionbt;
	private Button picbt;
	private Button locationbt;
	private Button cardbt;
	private boolean addMoreFlag;
	private boolean smileyTabFlag;
	private View smileyTabv;
	private View smileyLeaderv;
	
	private ListView chatMeassagelv;
	
	private ImageView page0iv;
	private ImageView page1iv;
	private ImageView page2iv;
	private ImageView page3iv;
	private ImageView page4iv;
	
	private ViewPager smileyPager;
	private TextView stab1,stab2,stab3;
	private View view1,view2,view3,view4,view5,view10,view20,view30,view40,view50;
	private int currIndex = 0;// 当前页卡编号
	private int currleader = 0;
	private ArrayList<View> viewsQQSmiley = new ArrayList<View>();
	private ArrayList<View> viewsEmjiSmiley = new ArrayList<View>();
	private SmileyPagerAdapter smileyPagerAdapter;
	private GridView expressionView0,expressionView1,expressionView2,expressionView3,expressionView4;
	private GridView expressionView00,expressionView10,expressionView20,expressionView30,expressionView40;
	private List<Map<String, Object>> contentQQSmile = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> contentEmji = new ArrayList<Map<String, Object>>();
	public final KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_DEL);
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chatting);
		
		backbt = (Button) findViewById(R.id.btn_back);
		menubt = (ImageButton) findViewById(R.id.right_btn);
		friendname = (TextView) findViewById(R.id.chatting_tv_friendname);
		voicebt = (Button) findViewById(R.id.chat_bt_voice);
		keyboardbt = (Button) findViewById(R.id.chat_bt_keyboard);
		addbt = (Button) findViewById(R.id.chat_bt_add);
		sendbt = (Button) findViewById(R.id.chat_btn_send);
		voicesendbt = (RecordButton) findViewById(R.id.chat_bt_send_voice);
		contentet = (EditText) findViewById(R.id.chat_et_sendmessage);
		
		addMorev = findViewById(R.id.chatting_add_v_menu);
		expressionbt = (ImageView) addMorev.findViewById(R.id.chatting_bt_simly);
		picbt = (Button)addMorev.findViewById(R.id.chatting_bt_pic);
		locationbt = (Button)addMorev.findViewById(R.id.chatting_bt_location);
		cardbt = (Button)addMorev.findViewById(R.id.chatting_bt_card);
		
		smileyTabv = findViewById(R.id.chatting_smiley_v);
		smileyLeaderv = smileyTabv.findViewById(R.id.chatting_smiley_leader_v);
		
		chatMeassagelv = (ListView) findViewById(R.id.chatting_message_listview);
		messageEntities = new ArrayList<MessageEntity>();
		
		page0iv = (ImageView) smileyLeaderv.findViewById(R.id.page0);
		page1iv = (ImageView) smileyLeaderv.findViewById(R.id.page1);
		page2iv = (ImageView) smileyLeaderv.findViewById(R.id.page2);
		page3iv = (ImageView) smileyLeaderv.findViewById(R.id.page3);
		page4iv = (ImageView) smileyLeaderv.findViewById(R.id.page4);
		
		
		expressionbt.setOnClickListener(this);
		picbt.setOnClickListener(this);
		locationbt.setOnClickListener(this);
		cardbt.setOnClickListener(this);
		
		backbt.setOnClickListener(this);
		menubt.setOnClickListener(this);
		voicebt.setOnClickListener(this);
		keyboardbt.setOnClickListener(this);
		addbt.setOnClickListener(this);
		sendbt.setOnClickListener(this);
		voicesendbt.setOnEventListener(new VoiceListener());
		
		initSmaleyPager();
		assetManager = this.getResources().getAssets();
		BluetoothChatService.allActivity.add(this);
		
		
	}
	/**
	 * 实现自定义的录音按钮的开始录音和结束录音
	 * @author minggo
	 * @created 2013-2-23下午12:39:56
	 */
	public class VoiceListener implements OnEventListener{

		@Override
		public void onFinishedRecord(String audioPath, int time) {
			try {
				PlaySound.play("sound/qrcode_completed.mp3", assetManager);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mHandler.obtainMessage(MESSAGE_WRITE, 1, -1, time).sendToTarget();
			
		}

		@Override
		public void onStartRecord() {
			try {
				
				VibratorUtil.Vibrate(ChattingActivity.this, (long) 100);
				PlaySound.play("sound/qrcode_completed.mp3", assetManager);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (requestDeviceAdd!=null&&BluetoothChatService.connectDeviceAdd!=null&&BluetoothChatService.connectDeviceAdd.equals(requestDeviceAdd)) {
			BluetoothDevice device = BluetoothChatService.mAdapter.getRemoteDevice(requestDeviceAdd);
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("mHandler", mHandler);
			param.put("device", device);
			param.put("secure", true);
			Task task = new Task(Task.TASK_CONECT_DEVICE, param);
			BluetoothChatService.newTask(task);
		}
		if (!BluetoothChatService.mAdapter.isEnabled()) {
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			if (BluetoothChatService.serviceExist == null) {
				
				//setupChat(); // 开启通信服务
				/*Intent intent = new Intent();
				intent.setClass(ChattingActivity.this, BluetoothChatService.class);
				startService(intent);*/
			}
		}
	}
	/**
	 * 推出activity但不finish
	 * @param keyCode
	 */
	public static void simulateKey(final int keyCode){
		new Thread(){
			public void run(){
				try {
					Instrumentation isn = new Instrumentation();
					isn.sendKeyDownUpSync(keyCode);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * 初始化表情
	 */
	public void initSmaleyPager(){
		addMoreFlag=false;
		smileyPager = (ViewPager)smileyTabv.findViewById(R.id.chatting_expression_tabpager);
		smileyPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		stab1 = (TextView) smileyTabv.findViewById(R.id.chatting_qq_smiley_tab);
		stab2 = (TextView) smileyTabv.findViewById(R.id.chatting_char_smiley_tab);
		stab3 = (TextView) smileyTabv.findViewById(R.id.chatting_personal_smiley_tab);
		
		stab1.setOnClickListener(new MyOnClickListener(0));
		stab2.setOnClickListener(new MyOnClickListener(1));
		stab3.setOnClickListener(new MyOnClickListener(2));
		
        
        //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.chatting_qq_smiley1, null);
        view2 = mLi.inflate(R.layout.chatting_qq_smiley1, null);
        view3 = mLi.inflate(R.layout.chatting_qq_smiley1, null);
        view4 = mLi.inflate(R.layout.chatting_qq_smiley1, null);
        view5 = mLi.inflate(R.layout.chatting_qq_smiley1, null);
        
        view10 = mLi.inflate(R.layout.chatting_character_smiley, null);
        view20 = mLi.inflate(R.layout.chatting_character_smiley, null);
        view30 = mLi.inflate(R.layout.chatting_character_smiley, null);
        view40 = mLi.inflate(R.layout.chatting_character_smiley, null);
        view50 = mLi.inflate(R.layout.chatting_character_smiley, null);
        
       
        /*view6 = mLi.inflate(R.layout.chatting_personal_smiley, null);*/
        
        for (int i = 0; i < Smiley.QQSIconIds.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("IMAGE", Smiley.QQSIconIds[i]);
			contentQQSmile.add(map);
		}
        for (int i = 0; i < Smiley.EmjiIconIds.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("IMAGE", Smiley.EmjiIconIds[i]);
			contentEmji.add(map);
		}
        
        expressionView0 = (GridView) view1.findViewById(R.id.expression_gridview);
		expressionView1 = (GridView) view2.findViewById(R.id.expression_gridview);
		expressionView2 = (GridView) view3.findViewById(R.id.expression_gridview);
		expressionView3 = (GridView) view4.findViewById(R.id.expression_gridview);
		expressionView4 = (GridView) view5.findViewById(R.id.expression_gridview);
		
		expressionView00 = (GridView) view10.findViewById(R.id.expression_gridview_emji);
		expressionView10 = (GridView) view20.findViewById(R.id.expression_gridview_emji);
		expressionView20 = (GridView) view30.findViewById(R.id.expression_gridview_emji);
		expressionView30 = (GridView) view40.findViewById(R.id.expression_gridview_emji);
		expressionView40 = (GridView) view50.findViewById(R.id.expression_gridview_emji);
		
		SimpleAdapter adapter0 = new SimpleAdapter(this, contentQQSmile.subList(0, 24),
				R.layout.chatting_qq_smiley_context, new String[] { "IMAGE" },
				new int[] { R.id.chatting_qq_smiley_item });
		SimpleAdapter adapter1 = new SimpleAdapter(this, contentQQSmile.subList(24, 48),
				R.layout.chatting_qq_smiley_context, new String[] { "IMAGE" },
				new int[] { R.id.chatting_qq_smiley_item });
		SimpleAdapter adapter2 = new SimpleAdapter(this, contentQQSmile.subList(48, 72),
				R.layout.chatting_qq_smiley_context, new String[] { "IMAGE" },
				new int[] { R.id.chatting_qq_smiley_item });
		SimpleAdapter adapter3 = new SimpleAdapter(this, contentQQSmile.subList(72, 96),
				R.layout.chatting_qq_smiley_context, new String[] { "IMAGE" },
				new int[] { R.id.chatting_qq_smiley_item });
		SimpleAdapter adapter4 = new SimpleAdapter(this, contentQQSmile.subList(96, 110),
				R.layout.chatting_qq_smiley_context, new String[] { "IMAGE" },
				new int[] { R.id.chatting_qq_smiley_item });
		
		SimpleAdapter adapter00 = new SimpleAdapter(this, contentEmji.subList(0, 24),
				R.layout.chatting_qq_smiley_context, new String[] { "IMAGE" },
				new int[] { R.id.chatting_qq_smiley_item });
		SimpleAdapter adapter10 = new SimpleAdapter(this, contentEmji.subList(24, 48),
				R.layout.chatting_qq_smiley_context, new String[] { "IMAGE" },
				new int[] { R.id.chatting_qq_smiley_item });
		SimpleAdapter adapter20 = new SimpleAdapter(this, contentEmji.subList(48, 72),
				R.layout.chatting_qq_smiley_context, new String[] { "IMAGE" },
				new int[] { R.id.chatting_qq_smiley_item });
		SimpleAdapter adapter30 = new SimpleAdapter(this, contentEmji.subList(72, 96),
				R.layout.chatting_qq_smiley_context, new String[] { "IMAGE" },
				new int[] { R.id.chatting_qq_smiley_item });
		SimpleAdapter adapter40 = new SimpleAdapter(this, contentEmji.subList(96, 120),
				R.layout.chatting_qq_smiley_context, new String[] { "IMAGE" },
				new int[] { R.id.chatting_qq_smiley_item });
		
		expressionView0.setAdapter(adapter0);
		expressionView1.setAdapter(adapter1);
		expressionView2.setAdapter(adapter2);
		expressionView3.setAdapter(adapter3);
		expressionView4.setAdapter(adapter4);
		
		expressionView00.setAdapter(adapter00);
		expressionView10.setAdapter(adapter10);
		expressionView20.setAdapter(adapter20);
		expressionView30.setAdapter(adapter30);
		expressionView40.setAdapter(adapter40);
		
		expressionView0.setOnItemClickListener(new SmileyItemListener(0,Smiley.QQSIconIds));
		expressionView1.setOnItemClickListener(new SmileyItemListener(1,Smiley.QQSIconIds));
		expressionView2.setOnItemClickListener(new SmileyItemListener(2,Smiley.QQSIconIds));
		expressionView3.setOnItemClickListener(new SmileyItemListener(3,Smiley.QQSIconIds));
		expressionView4.setOnItemClickListener(new SmileyItemListener(4,Smiley.QQSIconIds));
        
		expressionView00.setOnItemClickListener(new SmileyItemListener(0,Smiley.EmjiIconIds));
		expressionView10.setOnItemClickListener(new SmileyItemListener(1,Smiley.EmjiIconIds));
		expressionView20.setOnItemClickListener(new SmileyItemListener(2,Smiley.EmjiIconIds));
		expressionView30.setOnItemClickListener(new SmileyItemListener(3,Smiley.EmjiIconIds));
		expressionView40.setOnItemClickListener(new SmileyItemListener(4,Smiley.EmjiIconIds));
		
        //每个页面的view数据
		viewsQQSmiley.add(view1);
		viewsQQSmiley.add(view2);
		viewsQQSmiley.add(view3);
		viewsQQSmiley.add(view4);
		viewsQQSmiley.add(view5);
        
		viewsEmjiSmiley.add(view10);
		viewsEmjiSmiley.add(view20);
		viewsEmjiSmiley.add(view30);
		viewsEmjiSmiley.add(view40);
		viewsEmjiSmiley.add(view50);
		
        smileyPagerAdapter = new SmileyPagerAdapter(viewsQQSmiley);
        smileyPager.setAdapter(smileyPagerAdapter);
	}
	/**
	 * 图片点击监听器
	 * @author minggo
	 * @created 2013-2-16下午09:26:34
	 */
	public class SmileyItemListener implements OnItemClickListener{
		private int i;
		private int data[];
		public SmileyItemListener(int i,int data[]){
			this.i=i;
			this.data = data;
		}
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if((position+(23*i))%23!=0&&(position+(24*(i)))!=(data.length-1)||position==0){
				if(smileyNoFlag==0)
					setFace(position +(24*i), data[position+(24*i)]);
				else if(smileyNoFlag==1)
					setFace(position +(24*i)+110, data[position+(24*i)]);
			}else{
				contentet.onKeyDown(KeyEvent.KEYCODE_DEL, keyEventDown);
			}
		}
		
	}
	/**
	 * 添加表情
	 * @param faceTitle
	 * @param faceImg
	 */
	private void setFace(int faceTitle, int faceImg) {

		int start = contentet.getSelectionStart();
		Spannable ss = contentet.getText().insert(start, "[" + faceTitle + "]");
		Drawable d = getResources().getDrawable(faceImg);
		d.setBounds(0, 0, 32, 32);
		ImageSpan span = new ImageSpan(d, faceTitle + ".gif",
				ImageSpan.ALIGN_BOTTOM);
		ss.setSpan(span, start, start + ("[" + faceTitle + "]").length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
	}
	/**
	 * 删除表情（发现表情的长度不一，这个方法不适合）
	 */
	@SuppressWarnings("unused")
	private void deleteSmiley(){
		int len = contentet.getText().length();
		if (len>0) {
			contentet.getText().delete(len-3, len);
		}
	}
	 /**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			smileyPager.setCurrentItem(0);
			if(v==stab1){
				smileyNoFlag=0;
				stab1.setBackgroundResource(R.drawable.smiley_tab_pressed);
				if (currIndex == 1) {
					stab2.setBackgroundDrawable(getResources().getDrawable(R.drawable.smiley_tab_nor));
				} else if (currIndex == 2) {
					stab3.setBackgroundDrawable(getResources().getDrawable(R.drawable.smiley_tab_nor));
				}
				if(currIndex!=0){
					smileyPagerAdapter.views = viewsQQSmiley;
					smileyPagerAdapter.notifyDataSetChanged();
			        smileyPager.setAdapter(smileyPagerAdapter);
				}
				initLeaderView();
				currIndex = index;
				
			}else if (v==stab2) {
				smileyNoFlag=1;
				stab2.setBackgroundDrawable(getResources().getDrawable(R.drawable.smiley_tab_pressed));
				if (currIndex == 0) {
					
					stab1.setBackgroundDrawable(getResources().getDrawable(R.drawable.smiley_tab_nor));
				} else if (currIndex == 2) {
					stab3.setBackgroundDrawable(getResources().getDrawable(R.drawable.smiley_tab_nor));
					
				}
				if(currIndex!=1){
					smileyPagerAdapter.views = viewsEmjiSmiley;
					smileyPagerAdapter.notifyDataSetChanged();
			        smileyPager.setAdapter(smileyPagerAdapter);
				}
				currIndex = index;
				initLeaderView();
				
			}else if (v==stab3) {
				smileyNoFlag=2;
				stab3.setBackgroundDrawable(getResources().getDrawable(R.drawable.smiley_tab_pressed));
				if (currIndex == 0) {
					stab1.setBackgroundDrawable(getResources().getDrawable(R.drawable.smiley_tab_nor));
				} else if (currIndex == 1) {
					stab2.setBackgroundDrawable(getResources().getDrawable(R.drawable.smiley_tab_nor));
				}
				currIndex = index;
				initLeaderView();
			}
		}
	};
	/**
	 * 
	 * @param duration
	 * @return
	 */
	public boolean setBluetoothDiscovering(int duration) {
		
		try {
			Intent setDiscovery = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			setDiscovery.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,duration);
			ChattingActivity.this.startActivity(setDiscovery);
		} catch (Exception e) {
			Log.i("设置蓝牙可探测错误--->", e.toString());
			return false;
		}
		
		return true;
		
	}
	/**
	 * 各种回调事件
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case DEVICE_OPERATION:
			if (resultCode == 1) {
				if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
					setBluetoothDiscovering(300);
				}else{
					BluetoothAdapter.getDefaultAdapter().enable();
					setBluetoothDiscovering(300);
				}
			}else if (resultCode == 2) {
				Intent serverIntent = new Intent(this, DeviceListActivity.class);
				startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			}
			break;
		case REQUEST_ENABLE_BT: // 启动本地蓝牙
			if (resultCode == Activity.RESULT_OK) {
				//setupChat();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent();
						intent.setClass(ChattingActivity.this, BluetoothChatService.class);
						intent.putExtra("can_call_start", true);
						startService(intent);
					}
				}, 5000);
			} else {
				Toast.makeText(this, "蓝牙未启动",2000).show();
			}
			break;
		case REQUEST_CONNECT_DEVICE_SECURE: // 安全连接请求
			if (resultCode == Activity.RESULT_OK) {
				requestDeviceAdd = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				BluetoothDevice device = BluetoothChatService.mAdapter.getRemoteDevice(requestDeviceAdd);
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("mHandler", mHandler);
				param.put("device", device);
				param.put("secure", true);
				Task task = new Task(Task.TASK_CONECT_DEVICE, param);
				BluetoothChatService.newTask(task);
			}
			break;
		case GET_PHOTO_FROM_CARMERA_RETURN://照相机
			if (resultCode == Activity.RESULT_OK) {
				Bitmap bitmap = (Bitmap) data.getExtras().get("data");
				bitmap = ClippingPicture.Resize(bitmap);
				ClippingPicture.saveTalkBitmap(bitmap);
				mHandler.obtainMessage(MESSAGE_WRITE, 0, -1).sendToTarget();
			}
			break;
		case GET_PHOTO_FROM_LOCAL://从本地取
			if (data!=null&&data.getData()!=null) {
				Uri originalUri = data.getData();
				if (originalUri != null) {
					ClippingPicture.saveGetPicFromLocal(ChattingActivity.this, originalUri);
					mHandler.obtainMessage(MESSAGE_WRITE, 0, -1).sendToTarget();
				}
			}
			break;
		}
		
	}
	/**
	 * 页面改变监听器
	 * @author minggo
	 * @created 2013-2-18下午07:00:14
	 */
	private class MyOnPageChangeListener implements OnPageChangeListener{
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				page0iv.setImageResource(R.drawable.white_dot);
				if (currleader==1) {
					page1iv.setImageResource(R.drawable.dark_dot);
				}
				currleader=0;
				break;
			case 1:
				if (currleader==0) {
					page0iv.setImageResource(R.drawable.dark_dot);
				}else if (currleader==2) {
					page2iv.setImageResource(R.drawable.dark_dot);
				}
				page1iv.setImageResource(R.drawable.white_dot);
				currleader=1;
				break;
			case 2:
				if (currleader==1) {
					page1iv.setImageResource(R.drawable.dark_dot);
				}else if (currleader==3) {
					page3iv.setImageResource(R.drawable.dark_dot);
				}
				page2iv.setImageResource(R.drawable.white_dot);
				currleader=2;
				break;
			case 3:
				if (currleader==2) {
					page2iv.setImageResource(R.drawable.dark_dot);
				}else if (currleader==4) {
					page4iv.setImageResource(R.drawable.dark_dot);
				}
				page3iv.setImageResource(R.drawable.white_dot);
				currleader=3;
				break;
			case 4:
				if (currleader==3) {
					page3iv.setImageResource(R.drawable.dark_dot);
				}
				page4iv.setImageResource(R.drawable.white_dot);
				currleader=4;
				break;
			
			}
			
		}
		
	}
	
	public void initLeaderView(){
		currleader= 0;
		page0iv.setImageResource(R.drawable.white_dot);
		page1iv.setImageResource(R.drawable.dark_dot);
		page2iv.setImageResource(R.drawable.dark_dot);
		page3iv.setImageResource(R.drawable.dark_dot);
		page4iv.setImageResource(R.drawable.dark_dot);
	}
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}
	@Override
	public void onClick(View v) {
		
		if (v==backbt) {
			simulateKey(KeyEvent.KEYCODE_BACK);
		}else if (v==expressionbt) {
			addMorev.setVisibility(View.GONE);
			addMoreFlag = false;
			if(smileyTabFlag==false){
				smileyTabFlag = true;
				smileyTabv.setVisibility(View.VISIBLE);
			}
		}else if (picbt==v) {
			if (BluetoothChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
				Toast.makeText(ChattingActivity.this, R.string.not_connected, Toast.LENGTH_SHORT).show();
				return;
			}else{
				OptionAlert.showAlert(ChattingActivity.this, ChattingActivity.this.getString(R.string.set_myinfo), 
						ChattingActivity.this.getResources().getStringArray(R.array.set_myinfo_item),
						null, new OptionAlert.OnAlertSelectId(){
	
					@Override
					public void onClick(int whichButton) {	
							switch(whichButton){
							case GET_PHOTO_FROM_LOCAL:
								Intent intent = new Intent();
								intent.setType("image/*");
								intent.setAction(Intent.ACTION_GET_CONTENT);
								ChattingActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"),GET_PHOTO_FROM_LOCAL);
								break;
							case GET_PHOTO_FROM_CARMERA:
								Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								ChattingActivity.this.startActivityForResult(intent1,GET_PHOTO_FROM_CARMERA_RETURN);     
								break;
							}
						}
					}
				);
			}
		}else if (locationbt==v) {
			baiMapApiUtil = (BaiMapGlobalApplication)(ChattingActivity.this.getApplication());
			if (baiMapApiUtil.mBMapMan == null) {
				baiMapApiUtil.mBMapMan = new BMapManager(ChattingActivity.this.getApplication());
				baiMapApiUtil.mBMapMan.init(baiMapApiUtil.mStrKey, new BaiMapGlobalApplication.MyGeneralListener());
			}
			baiMapApiUtil.mBMapMan.start();
			
	        mLocationListener = new LocationListener(){

				@Override
				public void onLocationChanged(Location location) {
					if(location != null){
						baiDu_Lon = location.getLongitude();
	  					baiDu_Lat = location.getLatitude();
	  					mHandler.obtainMessage(MESSAGE_WRITE, 2, 2).sendToTarget();
					}
				}
	        };
	        baiMapApiUtil.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
	        baiMapApiUtil.mBMapMan.start();
			
		}else if (cardbt==v) {
			
		}else if (v==menubt) {
			Intent serverIntent = new Intent(this, DeviceDetectDialogActivity.class);
			startActivityForResult(serverIntent, DEVICE_OPERATION);
			
		}else if (v==voicebt) {
			voicebt.setVisibility(View.GONE);
			sendbt.setVisibility(View.GONE);
			contentet.setVisibility(View.GONE);
			
			keyboardbt.setVisibility(View.VISIBLE);
			voicesendbt.setVisibility(View.VISIBLE);
		}else if (v==addbt) {
			if(addMoreFlag==false){
				addMoreFlag = true;
				if (smileyTabFlag==true) {
					smileyTabFlag = false;
					smileyTabv.setVisibility(View.GONE);
					addMoreFlag = false;
				}else{
					addMorev.setVisibility(View.VISIBLE);
				}
			}else{
				addMoreFlag = false;
				addMorev.setVisibility(View.GONE);
			}
			
		}else if (v==keyboardbt) {
			voicebt.setVisibility(View.VISIBLE);
			sendbt.setVisibility(View.VISIBLE);
			contentet.setVisibility(View.VISIBLE);
			
			keyboardbt.setVisibility(View.GONE);
			voicesendbt.setVisibility(View.GONE);
		}else if (sendbt==v) {
			if (BluetoothChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
				Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(contentet.getText())){

			}else{
				String sendContent = contentet.getText().toString();
				System.out.println(contentet.getText().toString()+"++++发送内容");
				MessageEntity d = new MessageEntity(contentet.getText().toString(),
						R.layout.chatting_item_msg_text_right,"1",DateUtil.getCurrentTiem());
				
				d.setTtmContent(sendContent);
				d.setLayoutID(R.layout.chatting_item_msg_text_right);
				d.setTtmType(1);
				d.setRemoteDeviceNo(requestDeviceAdd);
				d.setMyDeviceNo(BluetoothChatService.mAdapter.getAddress());
				d.setTtmTime(DateUtil.getCurrentTiem());

				/*MessageSQLService.getInstance(TalkMessageAct.this).save(d);*/
				messageEntities.add(d);
				 
				chatMeassagelv.setAdapter(new ChatMessageAdapter(ChattingActivity.this,messageEntities,"1"));
				chatMeassagelv.setSelection(messageEntities.size());
				
				
				contentet.setText("");
				sendMessage(sendContent);
				
			}
			StatService.onEvent(ChattingActivity.this, "send_message_button", "chatting_message_send", 1);
		}
		
	}
	/**
	 * 发送短信息
	 * @param message
	 * 文本类型的字符串.
	 * @throws UnsupportedEncodingException 
	 */
	private void sendMessage(String message){
		if (message.length() > 0) {
			// 获取信息的字节
			byte[] send = message.getBytes();
			HashMap<Integer, byte[]> param = new HashMap<Integer, byte[]>();
			param.put(BluetoothChatService.STATE_SEND_MESSAGE, send);
			Task task = new Task(Task.TASK_SEND_MESSAGE, param);
			BluetoothChatService.newTask(task);

		}
	}
	/**
	 * 处理ui更新的handler
	 */
	private Handler mHandler = new Handler() {

		private MessageEntity d;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case MESSAGE_WRITE_BOUND_MAX:
				Toast.makeText(ChattingActivity.this, "抱歉，本软件不支持发送超过1M文件！", Toast.LENGTH_LONG).show();
				break;
			case MESSAGE_STATE_CHANGE:
				if (msg.arg1==STATE_CONNECTING) {
					friendname.setText("正在连接...");
				}else if (msg.arg1==STATE_CONNECTED) {
					friendname.setText("已连接");
				}
				break;
			case MESSAGE_TOAST:
				if (msg.arg1==1) {
					Toast.makeText(ChattingActivity.this, "连接失败", 3000).show();
					friendname.setText("未连接");
				}else if (msg.arg1==2) {
					Toast.makeText(ChattingActivity.this, "连接中断", 3000).show();
					friendname.setText("连接中断");
				}
				break;
			case MESSAGE_SEND_OK:
				try {
					PlaySound.play("sound/sent_message.mp3", assetManager);
				} catch (IOException e) {
					e.printStackTrace();
				}
				Toast.makeText(ChattingActivity.this, "发送成功", 3000).show();
				break;
			case MESSAGE_SEND_FAILED:
				Toast.makeText(ChattingActivity.this, "发送失败", 3000).show();
				break;
			case MESSAGE_READ:
				try {
					PlaySound.play("sound/shake_match.mp3", assetManager);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (msg.arg1==0) {//收字符串
					String readMessage = null;
					try {
						readMessage = new String(((String) msg.obj).getBytes("GBK"),"UTF8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} 
					d = new MessageEntity(readMessage,R.layout.chatting_item_msg_text_left,"1",DateUtil.getCurrentTiem());
					d.setTtmContent(readMessage);
					d.setLayoutID(R.layout.chatting_item_msg_text_left);
					d.setTtmType(1);
					d.setRemoteDeviceNo(requestDeviceAdd);
					d.setMyDeviceNo(BluetoothChatService.mAdapter.getAddress());

					/*MessageSQLService.getInstance(TalkMessageAct.this).save(d);*/
					messageEntities.add(d);
					 
					chatMeassagelv.setAdapter(new ChatMessageAdapter(ChattingActivity.this,messageEntities,"1"));
					chatMeassagelv.setSelection(messageEntities.size());
				}else if(msg.arg1==1){//发图片
					d = new MessageEntity(ClippingPicture.talkPicName,
							R.layout.chatting_item_msg_image_left,"1",DateUtil.getCurrentTiem());
					d.setTtmContent(ClippingPicture.talkPicName);
					d.setLayoutID(R.layout.chatting_item_msg_image_left);
					d.setTtmType(2);
					d.setRemoteDeviceNo(requestDeviceAdd);
					d.setMyDeviceNo(BluetoothChatService.mAdapter.getAddress());
					messageEntities.add(d);
					chatMeassagelv.setAdapter(new ChatMessageAdapter(ChattingActivity.this,messageEntities,"1"));
					chatMeassagelv.setSelection(messageEntities.size());
				}else if (msg.arg1==2) {//收声音
					d = new MessageEntity(ClippingSounds.talkSoundName,
							R.layout.chatting_item_msg_voice_left,"1",DateUtil.getCurrentTiem());
					d.setTtmContent(ClippingSounds.talkSoundName);
					d.setLayoutID(R.layout.chatting_item_msg_voice_left);
					d.setTtmType(2);
					d.setRemoteDeviceNo(requestDeviceAdd);
					d.setVoiceTime(msg.arg2+"");
					d.setMyDeviceNo(BluetoothChatService.mAdapter.getAddress());
					messageEntities.add(d);
					chatMeassagelv.setAdapter(new ChatMessageAdapter(ChattingActivity.this,messageEntities,"1"));
					chatMeassagelv.setSelection(messageEntities.size());
				}else if (msg.arg1==3) {
					d = new MessageEntity(msg.obj.toString(),
							R.layout.chatting_item_msg_location_left,"1",DateUtil.getCurrentTiem());
					d.setTtmContent(msg.obj.toString());
					d.setLayoutID(R.layout.chatting_item_msg_location_left);
					d.setTtmType(3);
					d.setRemoteDeviceNo(requestDeviceAdd);
					d.setMyDeviceNo(BluetoothChatService.mAdapter.getAddress());
					
					messageEntities.add(d);
					chatMeassagelv.setAdapter(new ChatMessageAdapter(ChattingActivity.this,messageEntities,"1"));
					chatMeassagelv.setSelection(messageEntities.size());
				}
				break;
			case MESSAGE_WRITE:
				if (msg.arg1==0) {//发图片
					d = new MessageEntity(ClippingPicture.talkPicName,
							R.layout.chatting_item_msg_image_right,"1",DateUtil.getCurrentTiem());
					d.setTtmContent(ClippingPicture.talkPicName);
					d.setLayoutID(R.layout.chatting_item_msg_image_right);
					d.setTtmType(2);
					d.setRemoteDeviceNo(requestDeviceAdd);
					d.setMyDeviceNo(BluetoothChatService.mAdapter.getAddress());

					//MessageSQLService.getInstance(TalkMessageAct.this).save(d);
					messageEntities.add(d);
					
					chatMeassagelv.setAdapter(new ChatMessageAdapter(ChattingActivity.this,messageEntities,"1"));
					chatMeassagelv.setSelection(messageEntities.size());
					
					HashMap<String, Object> param = new HashMap<String, Object>();
					param.put("talkPicPath", ClippingPicture.TALK_FILES+"/"+ClippingPicture.talkPicName);
					Task task = new Task(Task.TASK_SEND_IMAGE, param);
					BluetoothChatService.newTask(task);
				}else if (msg.arg1==1) {//发录音
					
					d = new MessageEntity(ClippingSounds.talkSoundName,R.layout.chatting_item_msg_voice_right,
							"1",DateUtil.getCurrentTiem());
					d.setTtmContent(ClippingSounds.talkSoundName);
					d.setLayoutID(R.layout.chatting_item_msg_voice_right);
					d.setTtmType(3);
					d.setRemoteDeviceNo(requestDeviceAdd);
					d.setMyDeviceNo(BluetoothChatService.mAdapter.getAddress());
					d.setVoiceTime(msg.obj.toString());
					//MessageSQLService.getInstance(TalkMessageAct.this).save(d);
					messageEntities.add(d);
					
					chatMeassagelv.setAdapter(new ChatMessageAdapter(ChattingActivity.this,messageEntities,"1"));
					chatMeassagelv.setSelection(messageEntities.size());
					
					
					HashMap<String, Object> param = new HashMap<String, Object>();
					param.put("talkSoundPath", ClippingSounds.TALKSOUND_FILE+ClippingSounds.talkSoundName);
					param.put("recordTime", msg.obj);
					Task task = new Task(Task.TASK_SEND_SOUND, param);
					BluetoothChatService.newTask(task);
				}else if (msg.arg1==2) {//发位置
					d = new MessageEntity(baiDu_Lat+","+baiDu_Lon,R.layout.chatting_item_msg_location_right,
							"1",DateUtil.getCurrentTiem());

					d.setTtmContent(baiDu_Lat+","+baiDu_Lon);
					d.setLayoutID(R.layout.chatting_item_msg_location_right);
					d.setTtmType(4);
					d.setRemoteDeviceNo(requestDeviceAdd);
					d.setMyDeviceNo(BluetoothChatService.mAdapter.getAddress());

					//MessageSQLService.getInstance(TalkMessageAct.this).save(d);
					messageEntities.add(d);
					 
					chatMeassagelv.setAdapter(new ChatMessageAdapter(ChattingActivity.this,messageEntities,"1"));
					chatMeassagelv.setSelection(messageEntities.size());
					
					HashMap<String, Object> param = new HashMap<String, Object>();
					param.put("location", baiDu_Lat+","+baiDu_Lon);
					Task task = new Task(Task.TASK_SEND_LOCATION, param);
					BluetoothChatService.newTask(task);
				}
				break;
			}
		}
	};
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK) {  //获取 back键
    		
    		if(addMoreFlag==true){
				addMoreFlag = false;
				if (smileyTabFlag==true) {
					smileyTabFlag = false;
					smileyTabv.setVisibility(View.GONE);
				}
				addMorev.setVisibility(View.GONE);
				
			}else{
				if(smileyTabFlag==true){
					smileyTabFlag = false;
					smileyTabv.setVisibility(View.GONE);
					addMorev.setVisibility(View.GONE);
					addMoreFlag = false;
				}else {
					finish();
				}
			}
    		
    	}
		return true;
	}
	@SuppressWarnings("unused")
	private void ensureDiscoverable() {
		if (BluetoothChatService.mAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}
	
	@Override
	protected void onPause() {
		BaiMapGlobalApplication app = (BaiMapGlobalApplication)this.getApplication();
		// 移除listener
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		app.mBMapMan.stop();
		super.onPause();
		StatService.onPause(this);
	}
	/**
	 * 回调函数用于在信息适配器中更新进度条或图片或声音的接发状态(暂时不启用)
	 * @author minggo
	 * @created 2013-2-22下午02:59:19
	 */
	public interface SendOrReceiveFinish{
		public void onFinishListener(int what,int position,int process,Object o);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
