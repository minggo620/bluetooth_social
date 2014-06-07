package edu.minggo.chat.ui;

import java.util.HashMap;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import edu.minggo.chat.R;
import edu.minggo.chat.control.BluetoothChatService;
import edu.minggo.chat.model.Task;
import edu.minggo.game.five.Chessboard;
import edu.minggo.game.five.Chessboard.FinishDownListener;

//程序入口Activity
public class GameFiveChessActivity extends Activity {
    
	private Chessboard gameView;
	private Button backbt;
	private ImageButton menubt;
	public static final int MESSAGE_STATE_CHANGE = 1; // 消息状态的改变
	public static final int STATE_CONNECTING = 2; // 初始化一个主动连接庄陶
	public static final int STATE_CONNECTED = 3; // 已经连接一个远程设备状态
	public static final int MESSAGE_READ =2222;
	public static final int MESSAGE_WRITE =3;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_five);
        gameView = (Chessboard) findViewById(R.id.five);
        gameView.setTextView((TextView)findViewById(R.id.text));
        backbt = (Button) findViewById(R.id.game_five_bt_left);
        menubt = (ImageButton) findViewById(R.id.game_five_right_btn);
        
       
        backbt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				finish();
			}
		});
        menubt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { 
				Intent it = new Intent(GameFiveChessActivity.this, GameFiveTopRightDialog.class);
				GameFiveChessActivity.this.startActivityForResult(it, 0);
			}
		});
        gameView.setFinishDownListener(new FinishDownListener() {
			@Override
			public void onFinishDown(String location) {
				if (location!=null) {
					HashMap<String, Object> param = new HashMap<String, Object>();
					param.put("chess_location", location);
					Task task = new Task(Task.TASK_CHESS_NEXT, param);
					BluetoothChatService.newTask(task);
				}else{
					Toast.makeText(GameFiveChessActivity.this, "对方还没有下", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if (resultCode==1) {
				gameView.moduleChoose(1);
			}else if(resultCode==2){
				Intent serverIntent = new Intent(this, DeviceListActivity.class);
				startActivityForResult(serverIntent, 0);
				
			}else if (resultCode == Activity.RESULT_OK) {
				
				String requestDeviceAdd = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				BluetoothDevice device = BluetoothChatService.mAdapter.getRemoteDevice(requestDeviceAdd);
				
				HashMap<String, Object> param = new HashMap<String, Object>();
				param.put("mHandler", gameHandler);
				param.put("device", device);
				param.put("secure", true);
				Task task = new Task(Task.TASK_CONECT_DEVICE, param);
				BluetoothChatService.newTask(task);
				gameView.moduleChoose(2);
			}
			break;
		
		default:
			break;
		}
	}
	private Handler gameHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (msg.arg1==STATE_CONNECTING) {
					Toast.makeText(GameFiveChessActivity.this,"正在连接...",Toast.LENGTH_SHORT).show();
				}else if (msg.arg1==STATE_CONNECTED) {
					Toast.makeText(GameFiveChessActivity.this,"已连接",Toast.LENGTH_SHORT).show();
				}
				break;
			case MESSAGE_READ:
				gameView.myHandler.obtainMessage(0, msg.obj.toString()).sendToTarget();
				Toast.makeText(GameFiveChessActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
				break;
			case 8://链接已连接的设备
				Toast.makeText(GameFiveChessActivity.this, "该设备已经处于链接状态", Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	};
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