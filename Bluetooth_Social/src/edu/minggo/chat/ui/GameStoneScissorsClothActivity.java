package edu.minggo.chat.ui;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.minggo.chat.R;
/**
 *
 * @author minggo
 * @date 2013-5-14上午11:43:21
 */
public class GameStoneScissorsClothActivity extends Activity {
	private ImageView chockAiv;
	private ImageView chockBiv;
	private Button redbt;
	private Button yellowbt;
	private Button bluebt;
	private Button backbt;
	private TextView timetv;
	private Drawable[] leftiv;
	private Drawable[] rightiv;
	//
	private String time;
	private Handler handler;
	
	private Animation animation1;
	private Animation animation2;
	
	private boolean startFlag;
	private boolean runningFlage;
	@SuppressWarnings("unused")
	private boolean timeFlage;
	
	//随机产生手头剪刀�?
	private int leftRadom;
	private int rightRandom;
	
	//记录两次时间
	private long timeRecod;
	private long animEnd;
	private long onclickEnd;
	
	//选择哪一边正�?
	private int selectRight;
	private boolean clickable;
	//游戏预备秒数
	private int i;
	
	//正确的次�?
	private int count;
	private final int GAME_ROUND = 5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_stone_scissors_cloth);
		
		chockAiv = (ImageView) findViewById(R.id.game_guest_a_iv);
		chockBiv = (ImageView) findViewById(R.id.game_gesture_b_iv);
		
		redbt = (Button)findViewById(R.id.game_left_bt);
		yellowbt = (Button)findViewById(R.id.game_mid_bt);
		bluebt = (Button)findViewById(R.id.game_right_bt);
		backbt = (Button)findViewById(R.id.game_bt_left);
		
		timetv = (TextView)findViewById(R.id.time);
		
		redbt.setOnClickListener(new MyOnclickListener());
		backbt.setOnClickListener(new MyOnclickListener());
		yellowbt.setOnClickListener(new MyOnclickListener());
		bluebt.setOnClickListener(new MyOnclickListener());
		
		leftiv = new Drawable[3];
		rightiv = new Drawable[3];
		
		//顺序   --> 剪刀、石头�?布（�?
		leftiv[0] = this.getResources().getDrawable(R.drawable.a09_hand010);
		leftiv[1] = this.getResources().getDrawable(R.drawable.a09_hand020);
		leftiv[2] = this.getResources().getDrawable(R.drawable.a09_hand030);
		//顺序   --> 剪刀、石头�?布（�?
		rightiv[0] = this.getResources().getDrawable(R.drawable.a09_hand01);
		rightiv[1] = this.getResources().getDrawable(R.drawable.a09_hand02);
		rightiv[2] = this.getResources().getDrawable(R.drawable.a09_hand03);
		
		handler = new Handler();
		runningFlage = false;
		startFlag = false;
		timeFlage = false;
		clickable = false;
		readyGo();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_menu, menu);
		return true;
	}
	/**
	 * ready game
	 */
	public void readyGo(){
		i = 0;
		startFlag = true;
		final Handler handler1 = new Handler();
		handler1.post(new Runnable() {
			@Override
			public void run() {
				if (i<4) {
					if (i==0) {
						timetv.setText("Ready...");
						timetv.setTextColor(Color.RED);
					}else if (i==1) {
						timetv.setText("3");
					}else if (i==2) {
						timetv.setText("2");
					}else if (i==3) {
						timetv.setText("1");
					}
					i++;
					handler1.postDelayed(this, 1000);
				}else{
					startFlag = false;
					timeFlage = true;
					runningFlage = true;
					handler1.removeCallbacks(this);
					startGame();
				}
			}
		});
	}
	/**
	 *��ʼ��Ϸ
	 */
	public void startGame(){
		
		leftRadom = (int) (Math.random()*3);
		rightRandom = (int) (Math.random()*3);
		
		chockAiv.setAnimation(animation1);
		chockBiv.setAnimation(animation2);
		
		animation1 = AnimationUtils.loadAnimation(GameStoneScissorsClothActivity.this, R.anim.left_shock);
		animation1.setAnimationListener(new MyAnimationListener());
		
		animation2 = AnimationUtils.loadAnimation(GameStoneScissorsClothActivity.this, R.anim.right_shock);
		
		
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (count<GAME_ROUND) {
					if (!startFlag) {
						time = (System.currentTimeMillis() + "").substring(9)
								+ "ms";
						timetv.setTextColor(Color.WHITE);
						timetv.setText(time);
					}
					if (runningFlage) {
						runningFlage = false;
						chockAiv.startAnimation(animation1);
						chockBiv.startAnimation(animation2);
					}
					handler.post(this);
				}else{
					handler.removeCallbacks(this);
					AlertDialog.Builder builder = new Builder(GameStoneScissorsClothActivity.this);
					builder.setMessage("时间"+timeRecod+"ms");
					builder.setTitle("得分结果");
					builder.create().show();
					
				}
				
			}
		});
	}
	/**
	 * 判断按对与否
	 */
	public void charge(int index){
		onclickEnd = System.currentTimeMillis();
		timeRecod = timeRecod+(onclickEnd - animEnd);
		if (leftRadom-rightRandom==-1||leftRadom-rightRandom==2) {//右边�?
			selectRight = 1;
		}else if (leftRadom-rightRandom==1||leftRadom-rightRandom==-2) {//左边�?
			selectRight = 0;
		}else if (leftRadom-rightRandom==0) {
			selectRight = 2;
		}
		if(selectRight==index){
			count++;
		}else{
			Toast.makeText(GameStoneScissorsClothActivity.this, "按错了，再来！", Toast.LENGTH_SHORT).show();
		}
		timetv.setText("0000ms");
		clickable = false;
		
		leftRadom = (int) (Math.random()*3);
		rightRandom = (int) (Math.random()*3);
		runningFlage = true;
	}
	/**
	 * 
	 * @author minggo
	 * @date 2013-5-14下午02:55:12
	 */
	public class MyAnimationListener implements AnimationListener{

		@Override
		public void onAnimationStart(Animation animation) {
			chockAiv.setImageDrawable(leftiv[1]);
			chockBiv.setImageDrawable(rightiv[1]);
		}
		@Override
		public void onAnimationEnd(Animation animation) {
			animEnd = System.currentTimeMillis();
			chockAiv.setImageDrawable(leftiv[leftRadom]);
			chockBiv.setImageDrawable(rightiv[rightRandom]);
			clickable = true;
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
			
		}
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.game_finish:
			finish();
			break;
		default:
			timeRecod = 0;
			animEnd = 0;
			onclickEnd = 0;;
			selectRight = 0;
			i = 0;
			count = 0;
			
			runningFlage = false;
			startFlag = false;
			timeFlage = false;
			clickable = false;
			readyGo();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	/**
	 * 按钮监听
	 * @author minggo
	 * @date 2013-5-14上午11:45:51
	 */
	public class MyOnclickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			
			if (v.equals(redbt)&&clickable) {
				charge(0);
			}else if (v.equals(yellowbt)&&clickable) {
				charge(2);
			}else if (v.equals(bluebt)&&clickable) {
				charge(1);
			}else if (v.equals(backbt)) {
				finish();
			}
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
