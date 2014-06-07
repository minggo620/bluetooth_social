package edu.minggo.chat.ui;


import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import edu.minggo.chat.R;
import edu.minggo.chat.control.BluetoothChatService;
/**
 * 游戏的详细菜单
 * @author minggo
 * @created 2013-2-27下午02:14:58
 */
public class GameFiveTopRightDialog extends Activity {
	@SuppressWarnings("unused")
	private LinearLayout layout;
	private View connectPerson;
	private View connectComputer;
	private BluetoothAdapter mBluetoothAdapter = null; //本地蓝牙适配器
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_five_top_right_dialog);
		
		layout=(LinearLayout)findViewById(R.id.game_five_dialog_layout);
		connectPerson = findViewById(R.id.game_five_connect);
		connectComputer = findViewById(R.id.game_five_computer);
		connectComputer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(1);
				finish();
			}
		});
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //获取本地蓝牙
        if (mBluetoothAdapter == null) { //如果本地没有蓝牙
            Toast.makeText(this, "本地没有蓝牙", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
		connectPerson.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mBluetoothAdapter.isEnabled()) {
		            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		            startActivityForResult(enableIntent, 0);
				}
				setResult(2);
				finish();
			}
		});
	}
	public void onActivityResult(int requestCode, int resultCode, Intent address) {
        switch (requestCode) {
        case 0:  //启动本地蓝牙
            if (resultCode == Activity.RESULT_OK) {
            	new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent();
						intent.setClass(GameFiveTopRightDialog.this, BluetoothChatService.class);
						intent.putExtra("can_call_start", true);
						startService(intent);
					}
				}, 5000);
            } else {
                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	/*
	public void exitbutton1(View v) {  
    	this.finish();    	
      }  
	public void exitbutton0(View v) {  
    	this.finish();
    	MainWeixin.instance.finish();//关闭Main 这个Activity
      }  
	*/
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
