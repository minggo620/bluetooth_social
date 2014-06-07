package edu.minggo.chat.ui;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import edu.minggo.chat.R;
import edu.minggo.chat.adapters.MainAddressAdapter;
import edu.minggo.chat.adapters.MainLansiAdapter;
import edu.minggo.chat.control.BluetoothChatInterface;
import edu.minggo.chat.control.BluetoothChatService;
import edu.minggo.chat.database.DataBaseOperator;
import edu.minggo.chat.database.InsertUserHealper;
import edu.minggo.chat.database.MyProviderMetaData.UserTableMetaData;
import edu.minggo.chat.model.Task;
import edu.minggo.chat.model.User;
import edu.minggo.chat.util.OptionAlert;
import edu.minggo.chat.util.PagingFriendList;
import edu.minggo.chat.util.SideBar;
import edu.minggo.game.icecream.DropIceCreamSurfaceView;
import edu.minggo.tencent.weibo.OAuth;
/**
 * 蓝星的主界面
 * @author minggo
 * @created 2013-1-28下午11:43:55
 */
public class MainTabActivity extends Activity implements BluetoothChatInterface{
	/*********************游戏栏需要的参数*******************/
	private View icecreamv;
	private View slotteryv;
	private View caiquanv;
	private View fiveGameV;
	private View chockGameV;
	private View tencentWeibov;
	/****************设置栏 需要的参数************************/
	private Button exitAppbt;//推出程序按钮
	private static int refresh_setting_kind=1;
	private View personInfov;
	private ImageView portraitiv;
	private TextView weiboBoundtv;
	private TextView photonumberstv; 
	private View aboutLansiv;
	private View photov;
	private View weibov;
	private View cancelHistoryv;
	private View helpv;
	/****************蓝星栏信息历史需要的参数*****************/
	private MainLansiAdapter lansiAdapter;
	public static int refresh_lansi_kind=1;
	public final static int REFREAH_LANSI = 0;
	private ListView lansiListView;//信息历史记录listview
	private View processBar0;
	private Button searchDelete0;
	private EditText autoEditView0;//搜索的输入框
	
	/****************通讯录栏需要的参数 *********************/
	private MainAddressAdapter addressAdapter;
	public static int refresh_friends_kind=1;/*1 代表从数据库加载，2 表示更新list页面*/
	public View process;  //加载条
	public static int pagenow = 1;//第几页
	public static int pagesize = 7;//每一页条数
	private ListView addressList;//好友简短信息ListView
	public static final int REFREAH_FRIEND = 1;//刷新通讯录信息
	private Button searchDelete1;//搜索删除按钮
	private EditText autoEditView;//搜索的输入框
	private SideBar indexBar;//导航字母菜单
	private WindowManager mWindowManager;
	private TextView mDialogText;//显示导航菜单
	private Button freindAddBt ;//添加朋友按钮
	
	/****************MainTab需要的参数 *********************/
	private final ArrayList<View> views = new ArrayList<View>();
	private View view1;/*蓝星tab*/
	private View view2;/*通讯录tab*/
	private View view3;/*游戏tab*/
	private View view4;/*设置tab*/
	private static int registContextMenu=1;
	private ViewPager mTabPager;	
	private View progressBar;
	private ImageView mTabImg;// 动画图片
	private ImageView mTab1,mTab2,mTab3,mTab4;
	private int zero = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int one;//单个水平动画位移
	private int two;
	private int three;
	@SuppressWarnings("unused")
	private LinearLayout mClose;
    private LinearLayout mCloseBtn;
    private View layout;	
	private boolean menu_display = false;
	private PopupWindow menuWindow;
	private LayoutInflater inflater;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate++++++++++++"+refresh_lansi_kind);
        setContentView(R.layout.main_lansi);
        
        mTabPager = (ViewPager)findViewById(R.id.tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
        mTab1 = (ImageView) findViewById(R.id.img_weixin);
        mTab2 = (ImageView) findViewById(R.id.img_address);
        mTab3 = (ImageView) findViewById(R.id.img_friends);
        mTab4 = (ImageView) findViewById(R.id.img_settings);
        mTabImg = (ImageView) findViewById(R.id.img_tab_now);
        mTab1.setOnClickListener(new MyOnClickListener(0));
        mTab2.setOnClickListener(new MyOnClickListener(1));
        mTab3.setOnClickListener(new MyOnClickListener(2));
        mTab4.setOnClickListener(new MyOnClickListener(3));
        
        Display currDisplay = getWindowManager().getDefaultDisplay();//获取屏幕当前分辨率
        int displayWidth = currDisplay.getWidth();
        @SuppressWarnings("unused")
		int displayHeight = currDisplay.getHeight();
        one = displayWidth/4; //设置水平动画平移大小
        two = one*2;
        three = one*3;
        
        //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);
        view1 = mLi.inflate(R.layout.main_tab_lansi, null);
        view2 = mLi.inflate(R.layout.main_tab_address, null);
        view3 = mLi.inflate(R.layout.main_tab_games, null);
        view4 = mLi.inflate(R.layout.main_tab_settings, null);
        //每个页面的view数据
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
      //填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			@Override
			public int getCount() {
				return views.size();
			}
			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager)container).removeView(views.get(position));
			}
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(views.get(position));
				
				return views.get(position);
			}
		};
		mTabPager.setAdapter(mPagerAdapter);
		 //第一个页面呈现lansitab先初始化
        if(refresh_lansi_kind==1){
        	refresh_lansi_kind = 2;
			processBar0=view1.findViewById(R.id.progress0);
			processBar0.setVisibility(View.VISIBLE);
			initDetail(0);
			
		}else {
			//lansiAdapter.notifyDataSetChanged();
		}
		BluetoothChatService.allActivity.add(this);
		
		if(BluetoothChatService.nowuser.getPicpath()!=null&&BluetoothChatService.nowuser.getPicpath()!=null&&new File(BluetoothChatService.nowuser.getPicpath()).exists()){
			BluetoothChatService.nowuser.setPhoto( BitmapFactory.decodeFile(BluetoothChatService.nowuser.getPicpath()));
		}else{
			BluetoothChatService.nowuser.setPhoto(((BitmapDrawable)(this.getResources().getDrawable(R.drawable.xiaohei))).getBitmap());
		}
    }
    /**
     * 模拟数据用的
     * @param list
     * @return
     */
    List<User> intiFriendList(List<User> list){
    	list = new ArrayList<User>();
    	
    	for(int i=0;i<3;i++){
    		User u = new User();
    		u.setUsername("minggo"+i);
        	u.setMotto("我老婆爱我"+i);
        	list.add(u);
    	}
    	return list;
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
			mTabPager.setCurrentItem(index);
			
		}
	};
    
	/**
	 * 分页tab滑动
	 * @author minggo
	 * @created 2013-1-29下午12:28:16
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				System.out.println("0000000000000000000");
				if(refresh_lansi_kind==1){
		        	refresh_lansi_kind = 2;
					processBar0=view1.findViewById(R.id.progress0);
					processBar0.setVisibility(View.VISIBLE);
					initDetail(0);
					
				}else{
					registContextMenu = 1;
					if(addressList!=null)
						unregisterForContextMenu(addressList);
					registerForContextMenu(lansiListView);
					//lansiAdapter.notifyDataSetChanged();
				}
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 1:
				System.out.println("11111111111111111111111");
				if(refresh_friends_kind==1){
					refresh_friends_kind = 2;
					progressBar=MainTabActivity.this.findViewById(R.id.progress1);
					progressBar.setVisibility(View.VISIBLE);  
					
					new Handler().postDelayed(new Runnable(){
						@Override
						public void run(){
							initDetail(1);
						}
					}, 1000);
					
				}else{
					registContextMenu = 2;
					unregisterForContextMenu(lansiListView);
					registerForContextMenu(addressList);
					//addressAdapter.notifyDataSetChanged();
				}
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 2:
				System.out.println("22222222222222222222");
				
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				}
				else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				initDetail(2);
				break;
			case 3:
				System.out.println("333333333333333333333333");
				if(refresh_setting_kind==1){
					refresh_setting_kind=2;
					initDetail(3);
				}
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				}
				else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  //获取 back键
    		
        	if(menu_display){         //如果 Menu已经打开 ，先关闭Menu
        		menuWindow.dismiss();
        		menu_display = false;
        		}
        	else {
        		Intent intent = new Intent();
            	intent.setClass(MainTabActivity.this,ExitActivity.class);
            	startActivity(intent);
        	}
    	}
    	
    	else if(keyCode == KeyEvent.KEYCODE_MENU){   //获取 Menu键			
			if(!menu_display){
				//获取LayoutInflater实例
				inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
				//这里的main布局是在inflate中加入的哦，以前都是直接this.setContentView()的吧？呵呵
				//该方法返回的是一个View的对象，是布局中的根
				layout = inflater.inflate(R.layout.main_menu, null);
				
				//下面我们要考虑了，我怎样将我的layout加入到PopupWindow中呢？？？很简单
				menuWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); //后两个参数是width和height
				//menuWindow.showAsDropDown(layout); //设置弹出效果
				//menuWindow.showAsDropDown(null, 0, layout.getHeight());
				menuWindow.showAtLocation(this.findViewById(R.id.mainweixin), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
				//如何获取我们main中的控件呢？也很简单
				mClose = (LinearLayout)layout.findViewById(R.id.menu_close);
				mCloseBtn = (LinearLayout)layout.findViewById(R.id.menu_close_btn);
				
				
				//下面对每一个Layout进行单击事件的注册吧。。。
				//比如单击某个MenuItem的时候，他的背景色改变
				//事先准备好一些背景图片或者颜色
				mCloseBtn.setOnClickListener (new View.OnClickListener() {					
					@Override
					public void onClick(View arg0) {						
						//Toast.makeText(Main.this, "退出", Toast.LENGTH_LONG).show();
						Intent intent = new Intent();
			        	intent.setClass(MainTabActivity.this,ExitActivity.class);
			        	startActivity(intent);
			        	menuWindow.dismiss(); //响应点击事件之后关闭Menu
					}
				});				
				menu_display = true;				
			}else{
				//如果当前已经为显示状态，则隐藏起来
				menuWindow.dismiss();
				menu_display = false;
				}
			
			return false;
		}
    	return false;
    }
	
	/**
	 * 搜索通讯录时候动态列出相关对象监听器
	 * @author minggo
	 * @created 2013-1-30下午02:42:22
	 */
	public class MyTextWatcher1 implements TextWatcher{
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			 if(s.length()!=0){
				 searchDelete1.setVisibility(View.VISIBLE);
				 List<User> userlist =DataBaseOperator.quryData(MainTabActivity.this, UserTableMetaData.USER_NAME+" like ?", new String[]{"%"+s+"%"});
				 MainAddressAdapter.friendlist  = userlist;
				 addressAdapter.refresh();
				 
			 }else{
				 searchDelete1.setVisibility(View.GONE);
				 MainAddressAdapter.friendlist  = PagingFriendList.allFriend;
				 addressAdapter.refresh();
			 }
		}
		@Override
		public void afterTextChanged(Editable s) {
			
		}
		
	}
	/**
	 * 搜索蓝星时候动态列出相关对象监听器
	 * @author minggo
	 * @created 2013-1-31上午02:08:57
	 */
	public class MyTextWatcher0 implements TextWatcher{
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			 MainLansiAdapter.searchFlag = true;
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			System.out.println(s+"====="+count);
			 if(s.length()!=0){
				 searchDelete0.setVisibility(View.VISIBLE);
				 List<User> userlist =DataBaseOperator.quryData(MainTabActivity.this, UserTableMetaData.USER_NAME+" like ?", new String[]{"%"+s+"%"});
				 System.out.println("userlist长度===="+userlist.size());
				 MainLansiAdapter.friendlist  = userlist;
				 lansiAdapter.initHeigh();
				 lansiAdapter.notifyDataSetChanged();
				 
			 }else{
				 searchDelete0.setVisibility(View.GONE);
				 MainLansiAdapter.searchFlag = false;
				 pagenow = 1;
				 List<User> friendsList = new PagingFriendList(MainTabActivity.this)
				 .getPagingNowFriend(pagesize,pagenow);
				 MainLansiAdapter.friendlist  = friendsList;
				 lansiAdapter.initHeigh();
				 lansiAdapter.notifyDataSetChanged();
			 }
		}
		@Override
		public void afterTextChanged(Editable s) {
			
		}
		
	}
	/**
	 * 初始化每个tab控件和事件
	 * @param tabNo
	 */
	public void initDetail(int tabNo){
		if(tabNo==0){
			/************ 初始化"蓝星"第一次分页显示************/
			lansiListView = (ListView) view1.findViewById(R.id.main_tab_lansi_listview);
			/*注册上下文菜单(长按listview的item出现的浮现菜单*/
			registContextMenu=1;
			if(addressList!=null)
				this.unregisterForContextMenu(addressList);
			else registerForContextMenu(lansiListView);
			
			HashMap<String, Object> param =new HashMap<String, Object>();
			param.put("pagenow", new Integer(pagenow));
			param.put("pagesize", new Integer(pagesize));
			param.put("context", MainTabActivity.this.getApplicationContext());
			Task task = new Task(Task.TASK_GET_USER_HOMETIMEINLINE,param);
			BluetoothChatService.newTask(task);
			
			autoEditView0 = (EditText) view1.findViewById(R.id.search_lansi_ev0);
			searchDelete0 = (Button) view1.findViewById(R.id.search_clear_icon);
			searchDelete0.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					autoEditView0.setText("");
				}
			});
			autoEditView0.addTextChangedListener(new MyTextWatcher0());
			
			lansiListView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if(id==-1){ //更多
						/********************* 点击更多的请求时*********************/
						MainTabActivity.this.findViewById(R.id.textView).setVisibility(View.GONE);
						MainTabActivity.this.findViewById(R.id.tail_progressBar).setVisibility(View.VISIBLE);
						pagenow++;
						HashMap<String, Object> param =new HashMap<String, Object>();
						param.put("pagenow", new Integer(pagenow));
						param.put("pagesize", new Integer(pagesize));
						param.put("context", MainTabActivity.this.getApplicationContext());
						Task task = new Task(Task.TASK_GET_USER_HOMETIMEINLINE,param);
						BluetoothChatService.newTask(task);
					}else{
						Intent it = new Intent(MainTabActivity.this,ChattingActivity.class);
						MainTabActivity.this.startActivity(it);
					}
				}
				
			});
			
		}else if(tabNo==1){
			
			/************ 初始化通讯录************/
			mWindowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
			freindAddBt = (Button) view2.findViewById(R.id.maintab_head_addbt);
			addressList = (ListView) view2.findViewById(R.id.main_tab_address_listview);
			registContextMenu=2;
			/*注册上下文菜单(长按listview的item出现的浮现菜单*/
			if(lansiListView!=null){
				unregisterForContextMenu(lansiListView);
			}else registerForContextMenu(addressList);
			
			View listHead = LayoutInflater.from(this).inflate(R.layout.main_tab_address_listitem_head, null);
			
			if(addressAdapter==null){
				addressAdapter = new MainAddressAdapter(MainTabActivity.this, PagingFriendList.allFriend);
				addressList.addHeaderView(listHead);
				addressList.setSelectionAfterHeaderView();//效果设置出来没用
				//addressList.setHeaderDividersEnabled(false);//效果设置出来也没用
				addressList.setAdapter(addressAdapter);
				
			}
			indexBar = (SideBar) findViewById(R.id.sideBar); 
			
	        indexBar.setListView(addressList); //将list与导航bar绑定
	        
	        mDialogText = (TextView) LayoutInflater.from(this).inflate(R.layout.main_tab_address_listposition, null);
	        mDialogText.setVisibility(View.INVISIBLE);
	       
	        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
	                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
	                WindowManager.LayoutParams.TYPE_APPLICATION,
	                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
	                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
	                PixelFormat.TRANSLUCENT);
	        mWindowManager.addView(mDialogText, lp);
	        indexBar.setTextView(mDialogText);
	        
			progressBar.setVisibility(View.GONE);
			
			freindAddBt.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					InsertUserHealper.insertData(MainTabActivity.this);
				}
			});
			autoEditView = (EditText) listHead.findViewById(R.id.search_address_ev);
			searchDelete1 = (Button)listHead.findViewById(R.id.search_clear_icon1);
			searchDelete1.setOnClickListener(new Button.OnClickListener() {
				@Override
				public void onClick(View v) {
					autoEditView.setText("");
				}
			});
			autoEditView.addTextChangedListener(new MyTextWatcher1());
			addressList.setEnabled(true);
			addressList.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					//查询个人信息详细
					Intent it = new Intent();
					Bundle bd = new Bundle();
					bd.putSerializable("user", (User) addressAdapter.getItem(position-1));
					it.putExtra("user", bd);
					it.putExtra("position", position-1);
					it.setClass(MainTabActivity.this, PersonalInforAcitivity.class);
					MainTabActivity.this.startActivityForResult(it, 1);
				}
				
			});
		
		}else if(tabNo==2){
			/********************初始化游戏页***************/
			icecreamv = view3.findViewById(R.id.game_icecream_v);
			slotteryv = view3.findViewById(R.id.game_slottery_v);
			caiquanv = view3.findViewById(R.id.game_caiquan_v);
			tencentWeibov = view3.findViewById(R.id.game_weibo_v);
			fiveGameV = view3.findViewById(R.id.game_five_v);
			chockGameV = view3.findViewById(R.id.game_chock);
			
			caiquanv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainTabActivity.this.startActivity(new Intent(MainTabActivity.this,GameStoneScissorsClothActivity.class));
				}
			});
			
			slotteryv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainTabActivity.this.startActivity(new Intent(MainTabActivity.this,GameCrazyLottery.class));
				}
			});
			
			icecreamv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainTabActivity.this.startActivity(new Intent(MainTabActivity.this,GameIceCreamActivity.class));
				}
			});
			
			fiveGameV.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainTabActivity.this.startActivity(new Intent(MainTabActivity.this,GameFiveChessActivity.class));
				}
			});
			chockGameV.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent it = new Intent(MainTabActivity.this, GameShakeActivity.class);
					MainTabActivity.this.startActivity(it);
				}
			});
			tencentWeibov.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainTabActivity.this,TencentWeiboActivity.class);
					MainTabActivity.this.startActivityForResult(intent, 3);
				}
			});
		}else if(tabNo==3){
			/*******************初始化设置页**********************/
			personInfov = view4.findViewById(R.id.setting_v_personalinfo);
			exitAppbt = (Button) view4.findViewById(R.id.setting_bt_exitapp);
			portraitiv = (ImageView) view4.findViewById(R.id.setting_iv_portrait);
			aboutLansiv = view4.findViewById(R.id.setting_v_about);
			photov = view4.findViewById(R.id.setting_v_photos);
			weibov = view4.findViewById(R.id.setting_v_weibo);
			photonumberstv = (TextView) view4.findViewById(R.id.setting_tv_photonums);
			weiboBoundtv = (TextView) view4.findViewById(R.id.setting_tv_bound);
			cancelHistoryv = view4.findViewById(R.id.setting_cancel_history_v);
			helpv = view4.findViewById(R.id.setting_v_help);
			
			portraitiv.setImageBitmap(BluetoothChatService.nowuser.getPhoto());
			exitAppbt.setOnClickListener(new SettingOnclickListener());
			personInfov.setOnClickListener(new SettingOnclickListener());
			aboutLansiv.setOnClickListener(new SettingOnclickListener());
			photov.setOnClickListener(new SettingOnclickListener());
			weibov.setOnClickListener(new SettingOnclickListener());
			cancelHistoryv.setOnClickListener(new SettingOnclickListener());
			helpv.setOnClickListener(new SettingOnclickListener());
			
			SharedPreferences  prefs = PreferenceManager.getDefaultSharedPreferences(this);
		    String oauth_token = prefs.getString(OAuth.OAUTH_TOKEN, "");//从prefs中取出OAuth_Token，若无则赋空值
		    String oauth_token_secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		    if (oauth_token!=null&&oauth_token_secret!=null&&!oauth_token_secret.equals("")&&!oauth_token.equals("")) {
				weiboBoundtv.setText("已绑定");
			}
			photonumberstv.setText("共"+DataBaseOperator.getSizePhotos(MainTabActivity.this)+"张");
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==1){//删除用户
			if(DataBaseOperator.deleteData(MainTabActivity.this, data.getLongExtra("userid", -1))){
				addressAdapter.deleItem(data.getIntExtra("position", -2));
				Toast.makeText(getApplicationContext(), "成功删除", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_SHORT).show();
			}
		}else if (resultCode==2) {//更新头像
			portraitiv.setImageBitmap((Bitmap)data.getExtras().get("bitmap"));
		}else if(resultCode==3){//更新相册相片书
			photonumberstv.setText("总"+data.getExtras().get("size")+"张");
		}else if (resultCode==4) {//绑定微博
			weiboBoundtv.setText("已绑定");
		}else if (requestCode==5) {//取消绑定
			weiboBoundtv.setText("未绑定");
		}
	}
	/**
	 * 设置栏的按钮监听器
	 * @author minggo
	 * @created 2013-2-3下午03:49:48
	 */
	public class SettingOnclickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v==exitAppbt){
				Intent intent = new Intent (MainTabActivity.this,ExitFromSettingsActivity.class);			
				startActivity(intent);	
			}else if(v==personInfov){
				Intent intent = new Intent (MainTabActivity.this,PersonalInfoSettingAcitivity.class);			
				startActivityForResult(intent, 2);	
			}else if(v==aboutLansiv){
				Intent intent = new Intent(MainTabActivity.this,AppAboutActivity.class);
				MainTabActivity.this.startActivity(intent);
			}else if(v==photov){
				Intent intent = new Intent(MainTabActivity.this,MyGalleryActivity.class);
				MainTabActivity.this.startActivityForResult(intent, 3);
			}else if (v==weibov) {
				Intent intent = new Intent(MainTabActivity.this,TencentWeiboBoundActivity.class);
				MainTabActivity.this.startActivityForResult(intent, 4);
			}else if (v==cancelHistoryv) {
				OptionAlert.showAlert(MainTabActivity.this, "删除全部信息记录",new String[]{"确定删除"},null, new OptionAlert.OnAlertSelectId(){
					@Override
					public void onClick(int whichButton) {						
						switch(whichButton){
						case 0:
							Toast.makeText(MainTabActivity.this, "删除成功", 2000).show();
							break;
						default:
							break;
						}
					}
				});
			}else if (v==helpv) {
				Uri uri = Uri.parse("http://user.qzone.qq.com/1053200192"); 
		    	Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
		    	startActivity(intent);
			}
		}
		
	}
	@Override
	public boolean onContextItemSelected(MenuItem item){
		
		AdapterContextMenuInfo lm = (AdapterContextMenuInfo)item.getMenuInfo();
		switch (item.getItemId()) {
		case 1: //发信息
			if(registContextMenu == 1){
				Toast.makeText(getApplicationContext(), "成功删除", Toast.LENGTH_SHORT).show();
			}else if(registContextMenu==2){
				if(DataBaseOperator.deleteData(MainTabActivity.this, lm.id)){
					addressAdapter.deleItem((lm.position-1));
					Toast.makeText(getApplicationContext(), "成功删除", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		}
		return super.onContextItemSelected(item);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo lm = (AdapterContextMenuInfo)menuInfo;
		if(registContextMenu ==1){
			if(lm.id!=0&&lm.id!=-1){
				//MainAddressAdapter.friendlist.get(lm.position-1).getUsername()
				System.out.println(lm.position);
				menu.setHeaderTitle("wwwwwwwww");
				menu.add(1, 1, 1, "删除联系人");
			}
		}else if(registContextMenu==2){
			if(lm.id!=0&&lm.id!=-1){
				System.out.println(lm.position+"+++++"+lm.id);
				menu.setHeaderTitle(MainAddressAdapter.username[lm.position-1].substring(0,MainAddressAdapter.username[lm.position-1].lastIndexOf(".")));
				menu.add(1, 1, 1, "删除联系人");
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Object... param) {
		switch(((Integer)(param[0])).intValue()){
		case REFREAH_LANSI: //更新蓝星
			if(pagenow==1){
				if(lansiAdapter==null){
					lansiAdapter = new MainLansiAdapter(MainTabActivity.this, (List<User>)param[1],lansiListView);
			        lansiListView.setAdapter(lansiAdapter);
				}
				lansiAdapter.notifyDataSetChanged();
			}else{
				((MainLansiAdapter)lansiListView.getAdapter()).addMoreData((List<User>)param[1]);
			}
			processBar0.setVisibility(View.GONE);
			break;
	    case REFREAH_FRIEND://更新朋友列表
			
		}
	}
	
	//设置标题栏右侧按钮的作用
	public void btnmainright(View v) {  
		Intent intent = new Intent (MainTabActivity.this,MainTopRightDialog.class);			
		startActivity(intent);	
		//Toast.makeText(getApplicationContext(), "点击了功能按钮", Toast.LENGTH_LONG).show();
      }  	
	
	@Override
	public void init() {
		
	}
	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}
	
}
    
    

