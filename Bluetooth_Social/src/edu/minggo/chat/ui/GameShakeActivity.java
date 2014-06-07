package edu.minggo.chat.ui;



import java.io.IOException;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import edu.minggo.chat.R;
import edu.minggo.chat.util.PlaySound;
import edu.minggo.chat.util.ShakeListener;
import edu.minggo.chat.util.ShakeListener.OnShakeListener;

public class GameShakeActivity extends Activity{
	
	ShakeListener mShakeListener = null;
	Vibrator mVibrator;
	private RelativeLayout mImgUp;
	private RelativeLayout mImgDn;
	private RelativeLayout mTitle;
	public static AssetManager assetManager;
	private SlidingDrawer mDrawer;
	private Button mDrawerBtn;
	private ImageView seZiiv;
	private int i = 0;
	private int j = 0;
	Handler hdl;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.shake_activity);
		
		assetManager = this.getResources().getAssets();
		hdl = new Handler();
		mVibrator = (Vibrator)getApplication().getSystemService(VIBRATOR_SERVICE);
		
		mImgUp = (RelativeLayout) findViewById(R.id.shakeImgUp);
		mImgDn = (RelativeLayout) findViewById(R.id.shakeImgDown);
		mTitle = (RelativeLayout) findViewById(R.id.shake_title_bar);
		seZiiv = (ImageView) findViewById(R.id.game_shack_iv);
		mDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer1);
        mDrawerBtn = (Button) findViewById(R.id.handle);
        mDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener()
		{	public void onDrawerOpened()
			{	
				mDrawerBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shake_report_dragger_down));
				TranslateAnimation titleup = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-1.0f);
				titleup.setDuration(200);
				titleup.setFillAfter(true);
				mTitle.startAnimation(titleup);
			}
		});
		 /* 设定SlidingDrawer被关闭的事件处理 */
		mDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener()
		{	public void onDrawerClosed()
			{	
				mDrawerBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shake_report_dragger_up));
				TranslateAnimation titledn = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-1.0f,Animation.RELATIVE_TO_SELF,0f);
				titledn.setDuration(200);
				titledn.setFillAfter(false);
				mTitle.startAnimation(titledn);
			}
		});
		final int sourceIds[] = new int[]{R.drawable.dice_1,R.drawable.dice_2,R.drawable.dice_3,
				R.drawable.dice_4,R.drawable.dice_5,R.drawable.dice_6};
		mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				startAnim();  //开始 摇一摇手掌动画
				mShakeListener.stop();
				if(startVibrato()){
				new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							 mVibrator.cancel();
							 mShakeListener.start();
							
						}
					}, 800);
				}
				
				hdl.post(new Runnable(){
					@Override
					public void run(){
						if (i<=60) {
							i+=j;
							if (j==0) {
								seZiiv.setImageResource(R.drawable.dice_action_0);
							}else if (j==1) {
								seZiiv.setImageResource(R.drawable.dice_action_1);
								
							}else if (j==2) {
								seZiiv.setImageResource(R.drawable.dice_action_2);
								
							}else if (j==3) {
								seZiiv.setImageResource(R.drawable.dice_action_3);
								j=-1;
							}
							j++;
							//让色子逐渐变慢
							if (i<35) {
								hdl.postDelayed(this,100);
							}else if (35<=i&&i<58) {
								hdl.postDelayed(this,100+i);
							}else{
								hdl.postDelayed(this,160);
							}
							
						}else{
							i=0;
							seZiiv.setImageResource(sourceIds[(int)(Math.random()*6)]);
							hdl.removeCallbacks(this);
						}
					}
				});
				
			}
		});
   }
	public void startAnim () {   //定义摇一摇动画动画
		AnimationSet animup = new AnimationSet(true);
		TranslateAnimation mytranslateanimup0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-0.5f);
		mytranslateanimup0.setDuration(1000);
		TranslateAnimation mytranslateanimup1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,+0.5f);
		mytranslateanimup1.setDuration(1000);
		mytranslateanimup1.setStartOffset(1000);
		animup.addAnimation(mytranslateanimup0);
		animup.addAnimation(mytranslateanimup1);
		mImgUp.startAnimation(animup);
		
		AnimationSet animdn = new AnimationSet(true);
		TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,+0.5f);
		mytranslateanimdn0.setDuration(1000);
		TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,-0.5f);
		mytranslateanimdn1.setDuration(1000);
		mytranslateanimdn1.setStartOffset(1000);
		animdn.addAnimation(mytranslateanimdn0);
		animdn.addAnimation(mytranslateanimdn1);
		mImgDn.startAnimation(animdn);	
	}
	public boolean startVibrato(){		//定义震动
		mVibrator.vibrate( new long[]{500,200,500,200}, -1); //第一个｛｝里面是节奏数组， 第二个参数是重复次数，-1为不重复，非-1则从pattern的指定下标开始重复
		try {
			PlaySound.play("sound/shake_sound_male.mp3", assetManager);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void shake_activity_back(View v) {     //标题栏 返回按钮
      	this.finish();
      }  
	public void linshi(View v) {     //标题栏
		startAnim();
      }  
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
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