package edu.minggo.chat.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import edu.minggo.chat.adapters.LoginUserAdapter;
import edu.minggo.chat.database.MyProviderMetaData.UserTableMetaData;
import edu.minggo.chat.model.Task;
import edu.minggo.chat.model.User;
import edu.minggo.chat.ui.ChattingActivity;
import edu.minggo.chat.ui.ExitActivity;
import edu.minggo.chat.ui.GameFiveChessActivity;
import edu.minggo.chat.ui.LoginActivity;
import edu.minggo.chat.ui.MainTabActivity;
import edu.minggo.chat.util.ClippingPicture;
import edu.minggo.chat.util.ClippingSounds;
import edu.minggo.chat.util.MessageLen;
import edu.minggo.chat.util.PagingFriendList;

public class BluetoothChatService extends Service implements Runnable {
	public static String connectDeviceAdd=null;
	public static File soundFile;
	public static Context context = null;
	public static boolean isrun = true;
	public static User serviceExist= null;
	public static User nowuser = null;
	public static ArrayList<Activity> allActivity = new ArrayList<Activity>();
	public static ArrayList<Task> allTask = new ArrayList<Task>();
	public static int lastActivityId; // 前端的Activity的编号
	public static HashMap<Integer, BitmapDrawable> alluserIcon = new HashMap<Integer, BitmapDrawable>();// 报讯头像

	// 调试指定显示
	private static final String TAG = "BluetoothChatService";

	// 蓝牙启动串口服务名称
	private static final String NAME_SECURE = "LansiServer";
	@SuppressWarnings("unused")
	private static final String NAME_INSECURE = "LansiServer";

	// 蓝牙启动串口服务国际唯一uuid 00001101-0000-1000-8000-00805F9B34FB
	//private static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	public final static BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter(); // 蓝牙设配器
	private static AcceptThread mSecureAcceptThread; // 安全连接蓝牙的线程
	private static AcceptThread mInsecureAcceptThread; // 非安全连接的线程
	private static ConnectThread mConnectThread; // 蓝牙设备连接中的线程
	private static ConnectedThread mConnectedThread; // 蓝牙设备连接后的线程
	private static int mState ; // 蓝牙设配器的状态

	// 显示当前的连接状态
	public static final int STATE_NONE = 0; // 什么也不做的状态
	public static final int STATE_LISTEN = 1; // 监听被连接状态
	public static final int STATE_CONNECTING = 2; // 初始化一个主动连接庄陶
	public static final int STATE_CONNECTED = 3; // 已经连接一个远程设备状态
	public static final int STATE_ONRESUME = 4; // 界面重新回到当前
	public static final int STATE_SEND_MESSAGE = 5; // 发送信息
	public static final int STATE_SEND_IMAGE = 11; // 发送信息
	public static final int STATE_CONNECT_THE_SAME = 8; // 链接已连接的设备
	public static final int STATE_REFRESH_MAINACTIVITY = 12; // 刷新Mainactivity
	
	//作为ChattingActivities 穿过来的Handler
	private static Handler mHandler;
	//作为GameFiveChessActivity.java 穿过来的Handler
    @SuppressWarnings("unused")
	private static Handler gameHandler;
	/**
	 * 设置当前的状态
	 * @param state 当前连接的状态
	 */
	private synchronized static void setState(int state) {
		mState = state;
		if (mHandler!=null) {
			mHandler.obtainMessage(ChattingActivity.MESSAGE_STATE_CHANGE, state,-1).sendToTarget();
		}else{
			System.out.println("handler为空");
		}
	}
	/**
	 * 返回当前的连接装态 Return the current connection state.
	 */
	public synchronized static int getState() {
		return mState;
	}
	/**
	 * 从集合中根据名字获取Activity对象
	 * @param name
	 * @return
	 */
	public static Activity getActivitybyName(String name) {
		for (Activity ac : allActivity) {
			if (ac.getClass().getName().indexOf(name) >= 0) {
				return ac;
			}
		}
		return null;
	}

	/**
	 * 添加一个任务
	 * @param task
	 */
	public static void newTask(Task task) {
		allTask.add(task);
	}

	/**
	 * 执行任务的时候
	 */
	@Override
	public void run() {
		while (isrun == true) {
			Task lastTask = null;
			synchronized (allTask) {
				if (allTask.size() > 0) {
					lastTask = allTask.get(0);
					doTask(lastTask);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 执行任务
	 * @param task
	 */
	public void doTask(Task task) {
		Message message = hand.obtainMessage();
		message.what = task.getTaskID();// 定义刷新ui的变化
		try {
			switch (task.getTaskID()) {
			// TASK_DETECT_DEVICE必须用final
			case Task.TASK_REFREAH_GALLERY:
				message.obj = Task.TASK_REFREAH_GALLERY;
				break;
			case Task.TASK_GET_MY_INFORMATION:
				message.obj = (Context)(task.getTaskParam().get("context"));
				break;
			case Task.TASK_GET_USER_HOMETIMEINLINE:
				PagingFriendList paging0 = new PagingFriendList((Context)(task.getTaskParam().get("context")));
				List<User> friendList0 = paging0.getPagingNowFriend((Integer)task.getTaskParam().get("pagesize")
						,(Integer)(task.getTaskParam().get("pagenow")));
				message.obj = friendList0;
				break;
			case Task.TASK_GET_USER_ADDRESS:
				message.obj = PagingFriendList.allFriend;
				break;
			case Task.TASK_LOGIN_SUCCESS:
				nowuser = (User)task.getTaskParam().get("loginUser");
				message.obj = nowuser;
				break;
			case Task.TASK_SEND_MESSAGE://发送文字或表情
				write((byte[]) task.getTaskParam().get(BluetoothChatService.STATE_SEND_MESSAGE));
				break;
			case Task.TASK_SEND_IMAGE: //发图片
				File image = new File((String)task.getTaskParam().get("talkPicPath"));
				write(image);
				break;
			case Task.TASK_SEND_SOUND://发声音
				write((String)task.getTaskParam().get("talkSoundPath"),(Integer)task.getTaskParam().get("recordTime"));
				break;
			case Task.TASK_SEND_LOCATION://发位置
				write((String)task.getTaskParam().get("location"));
				break;
			case Task.TASK_CHESS_NEXT:
				write((String)task.getTaskParam().get("chess_location"),true);
				break;
			case Task.TASK_CONECT_DEVICE: //链接远程设备
				System.out.println("连接设备！"+((BluetoothDevice) task.getTaskParam().get("device")).toString()+(Boolean) task.getTaskParam().get("secure"));
				mHandler = (Handler)task.getTaskParam().get("mHandler");
				connect((BluetoothDevice) task.getTaskParam().get("device"),(Boolean) task.getTaskParam().get("secure"));
				break;
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.what = -100;
		}
		hand.sendMessage(message);
		allTask.remove(task);// 执行完销毁
	}
	public static BluetoothChatInterface bc1 = null;
	/**
	 * 更新ui
	 */
	public static Handler hand = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			System.out.println("这个Handler在执行");
			switch (msg.what) {
			case Task.TASK_REFREAH_GALLERY:
				bc1 = (BluetoothChatInterface) BluetoothChatService.getActivitybyName("MyGalleryActivity");
				bc1.refresh(Task.TASK_REFREAH_GALLERY);
				break;
			case Task.TASK_GET_USER_HOMETIMEINLINE: //更新朋友列表
				bc1 = (BluetoothChatInterface) BluetoothChatService.getActivitybyName("MainTabActivity");
				bc1.refresh(MainTabActivity.REFREAH_LANSI,msg.obj);
				break;
			case Task.TASK_GET_USER_ADDRESS:
				bc1 = (BluetoothChatInterface) BluetoothChatService.getActivitybyName("MainTabActivity");
				bc1.refresh(MainTabActivity.REFREAH_FRIEND,msg.obj);
				break;
			case Task.TASK_LOGIN_SUCCESS: //登陆成功
				// 更新ui中的登录名
				bc1 = (BluetoothChatInterface)BluetoothChatService.getActivitybyName("LoginActivity");
				bc1.refresh(LoginActivity.REFRESH_LOGIN, msg.obj);
				break;
			}
		}

	};

	/**
	 * 退出所有Activity
	 * 
	 * @param context
	 */
	public static void exitApplication(Context context) {
		for (Activity activity : allActivity) {
			activity.finish();
		}
		System.exit(0);
	}
	
	/**
	 * 退出程序
	 * @param context
	 */
	public static void promptExit(final Context context) {
		Intent it  = new Intent(context,ExitActivity.class);
		context.startActivity(it);
	}
	
	public static void delete( final Context context , final Uri uri ,final String id, final LoginUserAdapter loginUserAdapter,final int position) {
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setPositiveButton(edu.minggo.chat.R.string.confirm,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						context.getContentResolver().delete(uri, UserTableMetaData._ID+"="+"?", new String[]{id});
						loginUserAdapter.refresh(position);
					}
					
				});
		ab.setNegativeButton(edu.minggo.chat.R.string.no, null);
		ab.show();
	}
	/**
	 * 接受连接
	 * @param context
	 */
	public static void promptQuestConect(final Context context) {
		AlertDialog.Builder ab = new AlertDialog.Builder(context);
		ab.setPositiveButton(edu.minggo.chat.R.string.exit_confirm,
				new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					//	connected(socket, socket.getRemoteDevice(),mSocketType);
					}
				});
		ab.setNegativeButton(edu.minggo.chat.R.string.cancel, null);
		ab.show();
	}
	@Override
	public void onCreate() {
		super.onCreate();
		isrun = true;
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void onDestroy() {
		if (this != null) 
			super.stopSelf(); //停止蓝牙服务
		isrun = false;
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		if (intent.getExtras()!=null) {
			start();
		}
	}
	/**
	 * 开启监听蓝牙链接请求
	 */
	public synchronized void start() {
		if (mConnectThread != null) {// 关闭所有尝试连接的线程
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {// 关闭所有当前正在连接的线程
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		setState(STATE_LISTEN);

		if (mSecureAcceptThread == null) { // 安全接受线程是否为空
			mSecureAcceptThread = new AcceptThread(true);
			mSecureAcceptThread.start();
		}
		/*if (mInsecureAcceptThread == null) { // 非安全的接受线程是否为空
			mInsecureAcceptThread = new AcceptThread(false);
			mInsecureAcceptThread.start();
		}*/
	}
	
	/**
	 * 开启一个连接的线程初始化对远程设备的连接
	 * @param device 要连接的蓝牙设备
	 * @param secure 逻辑类型的参数 ，安全性
	 */
	public synchronized void connect(BluetoothDevice device, boolean secure) {
		System.out.println(device.getAddress());
		
		if (mState == STATE_CONNECTING) {
			if (mConnectThread != null) {
				mConnectThread.cancel();
				mConnectThread = null;
			}
		}
		if (connectDeviceAdd!=null&&connectDeviceAdd.equals(device.getAddress())) {
			mHandler.obtainMessage(STATE_CONNECT_THE_SAME).sendToTarget();
			//历史消息拿出来（代做）
		}else{
			System.out.println("链接线程进来了");
			if (mConnectedThread != null) {
				mConnectedThread.cancel();
				mConnectedThread = null;
			}
			mConnectThread = new ConnectThread(device, secure);
			mConnectThread.start();
			setState(STATE_CONNECTING);
		}
		
	}

	/**
	 * 开启一个已连接的线程去开始管理一个蓝牙的连接。
	 * @param socket连接好的BluetoothSocket
	 * @param device连接好的设备
	 */
	public synchronized void connected(BluetoothSocket socket,BluetoothDevice device, final String socketType) {

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		if (mSecureAcceptThread != null) { // 安全线程是否为空
			mSecureAcceptThread.cancel();
			mSecureAcceptThread = null;
		}
		if (mInsecureAcceptThread != null) { // 非安全设备是否为空
			mInsecureAcceptThread.cancel();
			mInsecureAcceptThread = null;
		}
		connectDeviceAdd = device.getAddress();
		// 开启一个线程去管理连接和执行传输事务
		mConnectedThread = new ConnectedThread(socket, socketType);
		mConnectedThread.start();

		// 发送一个已连接的设备名称返回ui更新
		mHandler.obtainMessage(ChattingActivity.MESSAGE_STATE_CHANGE,STATE_CONNECTED,-1).sendToTarget();
		// 设置已经连接设备状态
		setState(STATE_CONNECTED);
	}

	/**
	 * 停止当前回话的所有相关线程
	 */
	public synchronized void cancelCurrent() {

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		start();
		setState(STATE_LISTEN);
	}
	/**
	 * 停止蓝牙服务
	 */
	public synchronized static void stopService() {

		if (mConnectThread != null) {
			mConnectThread.cancel();
			mConnectThread = null;
		}
		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}
		if (mSecureAcceptThread != null) {
			mSecureAcceptThread.cancel();
			mSecureAcceptThread = null;
		}
		if (mInsecureAcceptThread != null) {
			mInsecureAcceptThread.cancel();
			mInsecureAcceptThread = null;
		}
		setState(STATE_NONE);
		
	}
	/**
	 * 写在连接线程当中是一个非同步的方法
	 * @param out 字节类型的out
	 * @see ConnectedThread#write(byte[])
	 */
	public void write(byte[] out) {
		ConnectedThread r;
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		r.write(out);
	}
	/**
	 * 写在连接线程当中是一个非同步的方法
	 * @param image
	 */
	public void write(File image) {
		ConnectedThread r;
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		r.write(image);
	}
	/**
	 * 写在连接线程当中是一个非同步的方法
	 * @param image
	 */
	public void write(String soundPath,int recordTime) {
		ConnectedThread r;
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		r.write(soundPath,recordTime);
	}
	/**
	 * 写在连接线程当中是一个非同步的方法
	 * @param image
	 */
	public void write(String location) {
		ConnectedThread r;
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		r.write(location);
	}
	/**
	 * 写在连接线程当中是一个非同步的方法
	 * @param image
	 */
	public void write(String location,boolean flag) {
		ConnectedThread r;
		synchronized (this) {
			if (mState != STATE_CONNECTED)
				return;
			r = mConnectedThread;
		}
		r.write(location,flag);
	}
	/**
	 * 显示连接意图失败和ui更新提示
	 */
	private void connectionFailed() {
		connectDeviceAdd = null;
		mHandler.obtainMessage(ChattingActivity.MESSAGE_TOAST,1,-1).sendToTarget();
		BluetoothChatService.this.start();
	}

	/**
	 * 显示连接中断和ui更新提示
	 */
	private void connectionLost() {
		connectDeviceAdd = null;
		mHandler.obtainMessage(ChattingActivity.MESSAGE_TOAST,2,-1).sendToTarget();
		BluetoothChatService.this.start();
	}

	/**
	 * 这个线程当有尝试来过来的连接。它表现为一个服务端这边的客户端。
	 * 它会运行直到被连接 或者线程被删除才会停止
	 */
	private class AcceptThread extends Thread {
		// 本定服务的socket
		private final BluetoothServerSocket mmServerSocket;
		private String mSocketType;
		BluetoothSocket socket = null;
		public AcceptThread(boolean secure) {
			BluetoothServerSocket tmp = null;
			mSocketType = secure ? "Secure" : "Insecure";

			// 创建新的监听服务器的socket
			try {
				tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, MY_UUID_SECURE);
			} catch (IOException e) {
				Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
			}
			
			// 实例化本机的socket
			mmServerSocket = tmp;
		}

		public void run() {
			setName("AcceptThread" + mSocketType);
			// 监听服务器的socket是否被连接
			while (mState != STATE_CONNECTED) {
				try {
					if(mmServerSocket==null){
						System.out.println("不为空tmp");
					}else{
						System.out.println(mmServerSocket.toString()+"====mmServerSocket");
					}
					// 如果没有连接的话就连接
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}

				// 连接不为空的情况
				if (socket != null) {
					synchronized (BluetoothChatService.this) {
						switch (mState) {
						case STATE_LISTEN:
							
						case STATE_CONNECTING:  //在这里拦截不想连接的设备。
							
							//System.out.println("捕捉在这里开始连接"+context.toString()+"Context的，名称");
							// 处于正在连接的状态就开始一个连接的线程
							
							//bc1.refresh(BluetoothChatActivity.PROMPT_DEVICE_CONECT,socket,socket.getRemoteDevice(),mSocketType);
							connected(socket, socket.getRemoteDevice(),mSocketType);
							
							break;
						case STATE_NONE:
							// 什么也不做
						case STATE_CONNECTED:
							// 准备或者已经连接，终止新的socket连接
							try {
								socket.close();
							} catch (IOException e) {
								Log.e(TAG, "Could not close unwanted socket", e);
							}
							break;
						}
					}
				}
			}
			
		}
		
		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 这个线程会这本设备试图主动连接外围设备的时候 直接运行，无论是是失败或者成功
	 */
	private class ConnectThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
		private String mSocketType;

		/*
		 * 传进来的是要连接的设备和连接的方式
		 */
		public ConnectThread(BluetoothDevice device, boolean secure) {
			mmDevice = device;
			BluetoothSocket tmp = null;
			mSocketType = secure ? "Secure" : "Insecure";

			try {
				if (secure) {
					System.out.println("发起连接secure");
					tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
				} else {
					System.out.println("发起连接insecure");
					tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID_INSECURE);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			mmSocket = tmp;
		}

		public void run() {
			setName("ConnectThread" + mSocketType);

			mAdapter.cancelDiscovery();

			// 让BluetoothSocket的连接
			try {
				// 这个是堵塞调用只有成功连接或者异常才会返回
				mmSocket.connect();
			} catch (IOException e) {
				/*try {
					//mmSocket.close(); // 关闭socket
				} catch (IOException e2) {
					e.printStackTrace();
				}*/
				connectionFailed();
				return;
			}

			// 重启服务
			synchronized (BluetoothChatService.this) {
				// 连接为空
				mConnectThread = null;
			}
			// 重新连接
			connected(mmSocket, mmDevice, mSocketType);
		}

		/*
		 * 关闭socket
		 */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				Log.e(TAG, "close() of connect " + mSocketType
						+ " socket failed", e);
			}
		}
	}

	/**
	 * 这个线程会这连接好一个远程设备的时候启动，它操控进入个出去的事务
	 */
	private class ConnectedThread extends Thread {
		public int messagekind = -1;
		private final BluetoothSocket mmSocket; // 蓝牙插口
		private InputStream mmInStream; // 输入信息流
		private final OutputStream mmOutStream; // 输出信息流
		private int lenght = 0;
		public ConnectedThread(BluetoothSocket socket, String socketType) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;

			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
			} catch (IOException e) {
				Log.e(TAG, "temp sockets not created", e);
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
			
		}
		
		/**
		 * 捕捉输入流
		 */
		public void run() {
			String shead = null;
			String subname1 = null;
			int recordTime = 0;
			// 当连接后保持监听输入流
			while (true) {
				byte[] head = new byte[10];
				byte[] len = new byte[8];
				try {
					if(mmInStream!=null){
						if (messagekind==-1) {
							mmInStream.read(head,0,10);
							shead = new String(head);
							mmInStream.read(len,0,8);
							lenght = Integer.parseInt(new String(len));
							System.out.println(shead+"----"+new String(len));
							if (shead.equals("[MessageA]")) {
								byte[] msgByte = new byte[lenght];
								mmInStream.read(msgByte);
								System.out.println(new String(msgByte));
								mHandler.obtainMessage(ChattingActivity.MESSAGE_READ,0, -1, new String(msgByte)).sendToTarget();
								System.out.println("可以接信息");
								messagekind = -1;
							}else if(shead.equals("[MessageB]")){
								byte[] subname2 = new byte[4];
								mmInStream.read(subname2,0,4);
								System.out.println(new String(subname2)+"<-------后缀名");
								subname1 =  new String(subname2);
								messagekind = 1;
							}else if (shead.equals("[MessageC]")) {
								byte[] recordTime0 = new byte[3];
								mmInStream.read(recordTime0,0,3);
								recordTime = Integer.parseInt(new String(recordTime0));
								messagekind = 2;
							}else if (shead.equals("[MessageD]")) {
								byte[] msgByte = new byte[lenght];
								mmInStream.read(msgByte);
								mHandler.obtainMessage(ChattingActivity.MESSAGE_READ,3, -1, new String(msgByte)).sendToTarget();
								messagekind = -1;
							}else if (shead.equals("[MessageF]")) {
								byte[] msgByte = new byte[lenght];
								mmInStream.read(msgByte);
								mHandler.obtainMessage(GameFiveChessActivity.MESSAGE_READ,0, 0, new String(msgByte)).sendToTarget();
								messagekind = -1;
							}
						}
						if(messagekind == 1){//接图片
							InputStream tempimage = mmInStream;
							File picFile = ClippingPicture.createTalkImage(subname1);            
					        FileOutputStream os = new FileOutputStream(picFile);
							try {
								byte[] buffer1 = new byte[1024*1024];
								int len1 = 0;
								int bytes1 = 0;
								while((len1)!=lenght){
									bytes1=tempimage.read(buffer1);
									len1 = len1+bytes1;
									os.write(buffer1,0,bytes1); 
								}
								os.flush();
								os.close();
								
							} catch (IOException e) {
								e.printStackTrace();
							}
							mHandler.obtainMessage(ChattingActivity.MESSAGE_READ,1, 1, picFile.getAbsolutePath()).sendToTarget();
							messagekind = -1;
								
						}else if(messagekind==2){//接录音
							InputStream tempimage = mmInStream;
							File soundFile = new File(ClippingSounds.saveSounds());            
					        FileOutputStream os;
							try {
								os = new FileOutputStream(soundFile);
								byte[] buffer1 = new byte[1024*1024];
								int len1 = 0;
								int bytes1 = 0;
								while((len1)!=lenght){
									bytes1=tempimage.read(buffer1);
									len1 = len1+bytes1;
									os.write(buffer1,0,bytes1); 
								}
								os.flush();
								os.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
							mHandler.obtainMessage(ChattingActivity.MESSAGE_READ,2, recordTime, soundFile.getAbsolutePath()).sendToTarget();
							messagekind = -1;
						}
					}	
				} catch (IOException e) {
					if (flag==0) {
						connectionLost();
					}
					break;
				}
			}
		}

		/**
		 * 写到输出流中
		 * @param buffer
		 * 字节类型写出
		 */
		public void write(byte[] buffer) {
			try {
				String head = "[MessageA]"+MessageLen.getLenght(buffer);
				byte[] allcontent = head.getBytes();
				allcontent = ArrayUtils.addAll(allcontent, buffer);
		        mmOutStream.write(allcontent);
		        mmOutStream.flush();
		       
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_OK, -1, -1,"0").sendToTarget();
			} catch (IOException e) {
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_FAILED, -1, -1,"1").sendToTarget();
				e.printStackTrace();
			}
		}
		/**
		 * 写到输出流中发送音频文件
		 * @param buffer
		 */
		public void write(String soundPath,int recordTime) {
			try {
				File soundf = new File(soundPath);
				System.out.println(soundPath+"<----------------");
				byte[] content = getBytesFromFile(soundf);
				if (content.length<=1024*1024) {
					String head = "[MessageC]"+MessageLen.getLenght(content)+MessageLen.getRecordTime(recordTime);
					byte[] headbyte = head.getBytes();
					
					mmOutStream.write(headbyte);
					mmOutStream.flush();
					mmOutStream.write(content);
			        mmOutStream.flush();
			       
					mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_OK, -1, -1,"0").sendToTarget();
				}else{
					mHandler.obtainMessage(ChattingActivity.MESSAGE_WRITE_BOUND_MAX, -1, -1,"0").sendToTarget();
				}
				
			} catch (IOException e) {
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_FAILED, -1, -1,"1").sendToTarget();
				e.printStackTrace();
			}
		}
		/**
		 * 写到输出流中
		 * @param buffer 文件类型
		 */
		public void write(File image) {
			try {
				byte[] content = getBytesFromFile(image);
				if (content.length<=1024*1024) {
					String fileName = image.getName();
					String subName = ".jpg";//长度为4
			        if(fileName.lastIndexOf(".")>0){
			        	subName = fileName.substring(fileName.lastIndexOf("."));
			        }
					String head = "[MessageB]"+MessageLen.getLenght(content)+subName;
					byte[] headbyte = head.getBytes();
					//先发送报文头
					mmOutStream.write(headbyte);
					mmOutStream.flush();
					//在发送内容
					mmOutStream.write(content);
					mmOutStream.flush();
					mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_OK, -1, -1,"0").sendToTarget();
				
				}else{
					mHandler.obtainMessage(ChattingActivity.MESSAGE_WRITE_BOUND_MAX, -1, -1,"0").sendToTarget();
				}
			} catch (IOException e) {
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_FAILED, -1, -1,"1").sendToTarget();
				e.printStackTrace();
			}
		}
		/**
		 * 发送位置
		 * @param location
		 */
		public void write(String location) {
			System.out.println(location+"<-------位置乒乒乓乓");
			byte[] content = location.getBytes();
			String head = "[MessageD]"+MessageLen.getLenght(content)+location;
			byte[] allcontent = head.getBytes();
			try {
				mmOutStream.write(allcontent);
				mmOutStream.flush();
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_OK, -1, -1,"0").sendToTarget();
			} catch (IOException e) {
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_FAILED, -1, -1,"1").sendToTarget();
				e.printStackTrace();
			}
		}
		/**
		 * 发送棋盘位置
		 * @param location
		 */
		public void write(String location,boolean flag) {
			System.out.println(location+"<-------位置棋盘");
			byte[] content = location.getBytes();
			String head = "[MessageF]"+MessageLen.getLenght(content)+location;
			byte[] allcontent = head.getBytes();
			try {
				mmOutStream.write(allcontent);
				mmOutStream.flush();
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_OK, -1, -1,"0").sendToTarget();
			} catch (IOException e) {
				mHandler.obtainMessage(ChattingActivity.MESSAGE_SEND_FAILED, -1, -1,"1").sendToTarget();
				e.printStackTrace();
			}
		}
		/**
		 * 将图片转化成byte[]
		 * @param file
		 * @return
		 * @throws IOException
		 */
		public  byte[] getBytesFromFile(File file) throws IOException {

	        InputStream is = new FileInputStream(file);
	        // 获取文件大小
	        long length = file.length();
	        
	        if (length > Integer.MAX_VALUE) {
	        // 文件太大，无法读取
	        throw new IOException("File is to large "+file.getName());
	        }
	        // 创建一个数据来保存文件数据
	        byte[] bytes = new byte[(int)length];
	        // 读取数据到byte数组中
	        int offset = 0;
	        int numRead = 0;
	        while (offset < bytes.length&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	            offset += numRead;
	        }
	        // 确保所有数据均被读取
	        if (offset < bytes.length) {
	            throw new IOException("Could not completely read file "+file.getName());
	        }
	        is.close();
	        return bytes;
	    }
		private int flag = 0;
		public void cancel() {
			try {
				flag = 1;
				mmOutStream.close();
				mmInStream.close();
				mmSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	

}
