package edu.minggo.chat.ui;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.minggo.chat.R;
/**
 * 转盘--疯狂摇奖
 * 1.难点在于两边转盘的产生的组合比较多�?
 * 2.难点在于刚刚点击完应该点击的数目后还有一小段时间，如果再点击的控制�?
 * 解决的方法：
 * 1.每次争取的先判断正确的点击数是否符合右边转盘产生的数�?
 * 2.判断是否点击正确如果正确allcount�?
 * 3.刚刚点击的数符合要求，那就开启一个handler延迟执行
 * 4.多点击的情况将多点击的boolean设置。在3.的发送执行时就用到这个判断�?
 * @author minggo
 * @date 2013-5-15上午10:19:23
 */
public class GameCrazyLottery extends Activity {
	private ImageView leftdialiv;
	private ImageView rightdialiv;
	private TextView timetv;
	
	private Button leftbt;
	private Button midbt;
	private Button rightbt;
	private Button backbt;
	
	private boolean startFlag;
	private boolean runningFlag;
	private boolean clickable;
	private boolean clickMore;
	
	private int gameround;
	private int randomLeft;
	private int randomRight;
	
	private RotateAnimation leftAnimation;
	private RotateAnimation rightAnimation;
	
	private Handler handler;
	private Handler handlerOverClick;
	//�?��倒计�?
	private int i;
	//记录两次时间
	private long timeRecod;
	private long animEnd;
	private long onclickEnd;
	
	private Drawable[] imageSource;
	private String time;
	
	private int leftClickCount ;
	private int midClickCount ;
	private int rightClickCount ;
	//每一轮点击了多少�?
	private int allClickCount;
	private int neesClick[];
	
	private final int ROUND = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_crazy_lottery);
		leftdialiv = (ImageView) findViewById(R.id.game_left_dial);
		rightdialiv = (ImageView) findViewById(R.id.game_right_dial);
		
		timetv = (TextView)findViewById(R.id.game_dial_time_tv);
		
		leftbt = (Button) findViewById(R.id.game_dial_left_bt);
		rightbt = (Button) findViewById(R.id.game_dial_right_bt);
		midbt = (Button)findViewById(R.id.game_dial_mid_bt);
		backbt = (Button)findViewById(R.id.game_bt_left);
		
		leftbt.setOnClickListener(new GameDialOnclickListener());
		backbt.setOnClickListener(new GameDialOnclickListener());
		rightbt.setOnClickListener(new GameDialOnclickListener());
		midbt.setOnClickListener(new GameDialOnclickListener());
		
		startFlag = false;
		runningFlag = false;
		
		clickable = false;
		clickMore = false;
		
		gameround = 0;
		
		handler = new Handler();
		handlerOverClick = new Handler();
		imageSource = new Drawable[2];
		imageSource[0] = this.getResources().getDrawable(R.drawable.left_dial);
		imageSource[1] = this.getResources().getDrawable(R.drawable.right_dial);
		
		neesClick = new int[]{5,6,1,2,3,4};
		
		readyGo();
		
	}
	/**
	 * 倒计时
	 */
	public void readyGo(){
		startFlag = true;
		i=0;
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
					runningFlag = true;
					handler1.removeCallbacks(this);
					startGame();
				}
			}
		});
	}
	/**
	 *  开始游戏
	 */
	public void startGame(){
		
		//leftAnimation.setRepeatCount(6);//设置重复次数 
		//animation.setFillAfter(boolean);//动画执行完后是否停留在执行完的状�?
		//animation.setStartOffset(long startOffset);//执行前的等待时间 
		
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (gameround<ROUND) {
					if (!startFlag) {
						time = (System.currentTimeMillis() + "").substring(9)+ "ms";
						timetv.setTextColor(Color.WHITE);
						timetv.setText(time);
					}
					if (runningFlag) {
						runningFlag = false;
						randomLeft = (int) (Math.random() * 6);
						randomRight = (int) (Math.random() * 6);
						leftAnimation = new RotateAnimation(0, 360 * 3+randomLeft*60,
								Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
								0.5f);
						rightAnimation = new RotateAnimation(0, -360 * 3-randomRight*60,
								Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
								0.5f);
						leftAnimation.setFillAfter(true);
						leftAnimation.setDuration(1000);
						
						rightAnimation.setFillAfter(true);
						rightAnimation.setDuration(1000);
						
						leftAnimation.setAnimationListener(new DialAnimationListener());
						
						leftdialiv.setAnimation(leftAnimation);
						leftdialiv.startAnimation(leftAnimation);
						
						rightdialiv.setAnimation(rightAnimation);
						rightdialiv.startAnimation(rightAnimation);
					}
					handler.post(this);
				}else{
					handler.removeCallbacks(this);
					AlertDialog.Builder builder = new Builder(GameCrazyLottery.this);
					builder.setMessage("用时"+timeRecod+"ms");
					builder.setTitle("得分");
					builder.create().show();
					
				}
			}
		});
		
	}
	/**
	 * 判断点击的正确定�?
	 * @param index 点击的哪�?��按钮
	 */
	public void charge(int index){
		onclickEnd = System.currentTimeMillis();
		timeRecod = timeRecod+(onclickEnd - animEnd);
		System.out.println("randomLeft--->"+randomLeft+"<--->randomRight--->"+randomRight
				+"<--->allcount--->"+allClickCount);
		if (allClickCount-neesClick[randomRight]<-1) {
		
			switch (randomLeft) {
			case 0://黄色
				if (index==1) {
					allClickCount++;
				}else{
					Toast.makeText(GameCrazyLottery.this, "按错了，重来吧！", Toast.LENGTH_SHORT).show();
					newInit();
				}
				break;
			case 1://蓝色
				if (index==2) {
					allClickCount++;
				}else{
					Toast.makeText(GameCrazyLottery.this, "按错了，重来吧！", Toast.LENGTH_SHORT).show();
					newInit();
				}
				break;
			case 2://非红�?
				if (index==2||index==1) {
					allClickCount++;
				}else{
					Toast.makeText(GameCrazyLottery.this, "按错了，重来吧！", Toast.LENGTH_SHORT).show();
					newInit();
				}
				break;
			case 3://非黄�?
				if (index==2||index==0) {
					allClickCount++;
				}else{
					Toast.makeText(GameCrazyLottery.this, "按错了，重来吧！", Toast.LENGTH_SHORT).show();
					newInit();
				}
				break;
			case 4://非蓝�?
				if (index==1||index==0) {
					allClickCount++;
				}else{
					Toast.makeText(GameCrazyLottery.this, "按错了，重来吧！", Toast.LENGTH_SHORT).show();
					newInit();
				}
				break;
			case 5://红色
				if (index==0) {
					allClickCount++;
				}else{
					Toast.makeText(GameCrazyLottery.this, "按错了，重来吧！", Toast.LENGTH_SHORT).show();
					newInit();
				}
				break;
			
			}
		}else if (allClickCount-neesClick[randomRight]==-1) {//等于的时候要�?��短时间的监听多按线程
			allClickCount++;
			handlerOverClick.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (clickMore) {
						Toast.makeText(GameCrazyLottery.this, "按多了按多了", Toast.LENGTH_SHORT).show();
						newInit();
					}else{
						gameround++;
						newInit();
					}
				}
			},500);
			
		}else{
			clickMore = true;
		}
		
	}
	
	/**
	 * 新一轮初始化数据
	 */
	public void newInit(){
		timetv.setText("0000ms");
		leftClickCount = 0;
		rightClickCount = 0;
		midClickCount = 0;
		allClickCount = 0;

		leftdialiv.setImageDrawable(imageSource[0]);
		rightdialiv.setImageDrawable(imageSource[1]);
		
		clickMore = false;
		runningFlag = true;
		clickable = false;
	}
	/**
	 * 按钮监听�?
	 * @author minggo
	 * @date 2013-5-15上午10:31:56
	 */
	public class GameDialOnclickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if (v.equals(leftbt)&&clickable) {
				leftClickCount++;
				leftbt.setText(leftClickCount+"");
				charge(0);
			}else if (v.equals(midbt)&&clickable) {
				midClickCount++;
				midbt.setText(midClickCount+"");
				charge(1);
			}else if (v.equals(rightbt)&&clickable) {
				rightClickCount++;
				rightbt.setText(rightClickCount+"");
				charge(2);
			}else if (v.equals(backbt)) {
				finish();
			}
		}
		
	}
	
	/**
	 * 动画监听器
	 * @author minggo
	 * @date 2013-5-15上午11:25:04
	 */
	public class DialAnimationListener implements AnimationListener{

		@Override
		public void onAnimationStart(Animation animation) {
			leftbt.setText("0");
			midbt.setText("0");
			rightbt.setText("0");
			leftbt.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				@Override
				public void afterTextChanged(Editable s) {
				}
			});
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			animEnd = System.currentTimeMillis();
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
		case R.id.game_reset:
			startFlag = false;
			runningFlag = false;
			clickable = false;
			clickMore = false;
			gameround = 0;
			readyGo();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_menu, menu);
		return true;
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
