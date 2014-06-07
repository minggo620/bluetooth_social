package edu.minggo.chat;

import edu.minggo.chat.ui.LoginActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

public class LogoActivity extends Activity {
	private AlphaAnimation alphaAnimation;
	private ImageView logostart;
	private BluetoothAdapter mBluetoothAdapter = null; //本地蓝牙适配器
	@Override  
    public void onCreate(Bundle savedInstanceState) {
		 //设置为标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        logostart = (ImageView)findViewById(R.id.logostart);
        alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
        logostart.setAnimation(alphaAnimation);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setAnimationListener(new LogoAnimationListener());
        
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //获取本地蓝牙

        if (mBluetoothAdapter == null) { //如果本地没有蓝牙
            Toast.makeText(this, "本地没有蓝牙", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    }
	private final class LogoAnimationListener implements AnimationListener{

		@Override
		public void onAnimationStart(Animation animation) {
		}
		@Override
		public void onAnimationEnd(Animation animation) {
			try {
				Thread.sleep(900);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
			logostart.startAnimation(alphaAnimation);
			alphaAnimation.setDuration(3000);
			alphaAnimation.setAnimationListener(new ToLoginListener());
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
		
	}
	 @Override
    public void onStart() {
        /*if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 0);
        }*/
        super.onStart();
    }
	public void onActivityResult(int requestCode, int resultCode, Intent address) {
	        switch (requestCode) {
	        case 0:  //启动本地蓝牙
	            if (resultCode == Activity.RESULT_OK) {
	            } else {
	                Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
	                finish();
	            }
	        }
	    }
	private final class ToLoginListener implements AnimationListener{

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			Intent it = new Intent();
			it.setClass(LogoActivity.this, LoginActivity.class);
			LogoActivity.this.startActivity(it);
			finish();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
	}
}