package edu.minggo.game.icecream;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;
import edu.minggo.chat.R;
import edu.minggo.chat.ui.GameIceCreamActivity;
import edu.minggo.chat.ui.GameIceCreamActivity.OnResetGameListen;

/**
 * 堆雪糕
 * @author minggo
 * @date 2013-5-8上午09:58:33
 */
public class DropIceCreamSurfaceView extends SurfaceView implements Callback, Runnable {
	
	private final static int MAX_TOUCH = 3;
	// 按钮设置
	private Bitmap leftBt;
	private Bitmap midBt;
	private Bitmap rightBt;
	// 甜筒位置
	private Bitmap lefttong;
	private Bitmap midtong;
	private Bitmap righttong;
	// 按钮的x y坐标
	private int leftbtX, leftbtY;
	private int midbtX, midbtY;
	private int rightbtX, rightbtY;
	// 按钮上往下的图片
	private Bitmap downbt1;
	private Bitmap downbt2;
	private Bitmap downbt3;
	// 礼物堆积头和�?
	private Bitmap presentDown;
	private Bitmap presentUp;
	private Bitmap background;
	// 箭头
	private int down1X, down1Y;
	private int down2X, down2Y;
	private int down3X, down3Y;
	// 屏幕长宽
	private int displayW;
	private int displayH;

	private Canvas canvas;
	private Paint paint;

	SurfaceHolder surfaceHolder;
	private Context context;
	// 下落线程
	Thread th;
	private Bitmap[] present;
	private int random1;
	private int random2;
	private int random3;
	private boolean flag1;
	private boolean flag2;
	private boolean flag3;

	private double currentHeightA;
	private double currentHeightB;
	private double currentHeightC;

	private int index1;
	private int index2;
	private int index3;
	private int presentHieht;

	private List<Bitmap> a;
	private List<Bitmap> b;
	private List<Bitmap> c;

	private int leftClick = 0;
	private int midClick = 0;
	private int rightClick = 0;
	private boolean clickable;
	private Handler timeHandler;
	private Handler stopHandler;
	
	private int yesClick;
	private boolean moreClick;
	
	//游戏的开始和结束的监听器
	private DropPresentListen dropPresentListen;
	
	private OnResetGameListen onResetGameListen = new OnResetGameListen() {
		
		@Override
		public void onReset() {
				initData();
				onMyDraw();
		}
	};

	public DropPresentListen getDropPresentListen() {
		return dropPresentListen;
	}

	public void setDropPresentListen(DropPresentListen dropPresentListen) {
		this.dropPresentListen = dropPresentListen;
	}

	public DropIceCreamSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);

		paint = new Paint();
		paint.setColor(Color.WHITE);
	}

	public DropIceCreamSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);

		paint = new Paint();
		paint.setColor(Color.WHITE);
	}
	public DropIceCreamSurfaceView(Context context) {
		super(context);
		this.context = context;
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);

		paint = new Paint();
		paint.setColor(Color.TRANSPARENT);
		
	}
	
	/**
	 * 画面
	 */
	public void onMyDraw() {
		canvas = surfaceHolder.lockCanvas();
		//canvas.drawBitmap(BitmapFactory.decodeResource(this.getResources(),R.drawable.present_head), 0, 0, null);
		try {
			if (canvas != null) {
				canvas.drawBitmap(background, 0, 0, null);
				//canvas.drawRGB(255, 255, 255);
				
				canvas.drawBitmap(lefttong, leftbtX+displayW/3/10, displayH-(displayW/2), paint);
				canvas.drawBitmap(midtong, leftbtX+displayW/3/10+displayW/3, displayH-(displayW/2), paint);
				canvas.drawBitmap(righttong, leftbtX+displayW/3/10+2*displayW/3, displayH-(displayW/2), paint);
				
				canvas.drawBitmap(leftBt, leftbtX, leftbtY, paint);
				canvas.drawBitmap(midBt, midbtX, midbtY, paint);
				canvas.drawBitmap(rightBt, rightbtX, rightbtY, paint);

				canvas.drawBitmap(downbt1, down1X, down1Y, paint);
				canvas.drawBitmap(downbt2, down2X, down2Y, paint);
				canvas.drawBitmap(downbt3, down3X, down3Y, paint);
				//canvas.drawBitmap(rondombt, 0, 0, paint);

				for (int i = 0; i < random1; i++) {
					if (i == random1 - 1) {
						canvas.drawBitmap(presentUp, leftbtX,
								displayH-displayW/2 - presentDown.getHeight() * i
								- presentUp.getHeight(), paint);
					} else {
						canvas.drawBitmap(presentDown, leftbtX, displayH-displayW/2
								- presentDown.getHeight() * (i + 1), paint);
					}

				}
				for (int i = 0; i < random2; i++) {
					if (i == random2 - 1) {
						canvas.drawBitmap(presentUp, midbtX,
								displayH-displayW/2 - presentDown.getHeight() * i
								- presentUp.getHeight(), paint);
					} else {
						canvas.drawBitmap(presentDown, midbtX,  displayH-displayW/2
								- presentDown.getHeight() * (i + 1), paint);
					}
				}
				for (int i = 0; i < random3; i++) {
					if (i == random3 - 1) {
						canvas.drawBitmap(presentUp, rightbtX,
								displayH-displayW/2 - presentDown.getHeight() * i
								- presentUp.getHeight(), paint);
					} else {
						canvas.drawBitmap(presentDown, rightbtX,  displayH-displayW/2
								- presentDown.getHeight() * (i + 1), paint);
					}
				}
				if (!a.isEmpty()) {
					for (int i = 0; i < a.size(); i++) {
						if (i <= random1 && i < a.size() - 1) {
							canvas.drawBitmap(
									a.get(i),
									leftbtX,
									(float) (displayH - lefttong.getHeight() - presentHieht - leftBt.getHeight()/2 * (i)),
									paint);
						}
						if (i == a.size() - 1 && !flag1) {
							canvas.drawBitmap(
									a.get(i),
									leftbtX,
									(float) (displayH - lefttong.getHeight() - presentHieht - leftBt.getHeight()/2 * (i)),
									paint);
						}
					}
				}

				if (!b.isEmpty()) {

					for (int i = 0; i < b.size(); i++) {
						if (i <= random2 && i < b.size() - 1) {
							canvas.drawBitmap(
									b.get(i),
									midbtX,
									(float) (displayH - midtong.getHeight() - presentHieht - leftBt.getHeight()/2 * (i)),
									paint);
						}
						if (i == b.size() - 1 && !flag2) {
							canvas.drawBitmap(
									b.get(i),
									midbtX,
									(float) (displayH -  midtong.getHeight() - presentHieht - leftBt.getHeight()/2 * (i)),
									paint);
						}
					}
				}

				if (!c.isEmpty()) {

					for (int i = 0; i < c.size(); i++) {
						if (i <= random3 && i < c.size() - 1) {
							canvas.drawBitmap(
									c.get(i),
									rightbtX,
									(float) (displayH - righttong.getHeight()- presentHieht - leftBt.getHeight()/2 * (i)), paint);
						}
						if (i == c.size() - 1 && !flag3) {
							canvas.drawBitmap(
									c.get(i),
									rightbtX,
									(float) (displayH - righttong.getHeight()
											- presentHieht -leftBt.getHeight()/2 * (i)), paint);
						}
					}
				}
				canvas.save();
				if (flag2 == true) {
					canvas.drawBitmap(present[index2], midbtX,
							(float) currentHeightB, paint);
				}
				canvas.restore();

				canvas.save();
				if (flag1 == true) {
					canvas.drawBitmap(present[index1], leftbtX,
							(float) currentHeightA, paint);
				}
				canvas.restore();

				canvas.save();
				if (flag3 == true) {
					canvas.drawBitmap(present[index3], rightbtX,
							(float) currentHeightC, paint);
				}
				canvas.restore();

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null) {
				surfaceHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	/**
	 * 左边的雪糕下落的轨迹
	 */
	public void onDownLogicA() {
		if (currentHeightA < (displayH - lefttong.getHeight() - presentHieht - leftBt.getHeight()/2 * (leftClick - 1))) {
			currentHeightA += 40;
			if (currentHeightA >= (displayH - lefttong.getHeight() - presentHieht - leftBt.getHeight()/2 * (leftClick - 1))) {
				currentHeightA = (displayH - lefttong.getHeight() - presentHieht - leftBt.getHeight()/2 * (leftClick - 1));
			}
		} else {
			flag1 = false;
			currentHeightA = 0;
		}

	}
	/**
	 * 中间的雪糕下落的轨迹
	 */
	public void onDownLogicB() {
		if (currentHeightB < (displayH - midtong.getHeight() - presentHieht - leftBt.getHeight()/2 * (midClick - 1))) {
			currentHeightB += 40;
			if (currentHeightB >= (displayH - midtong.getHeight() - presentHieht - leftBt.getHeight()/2 * (midClick - 1))) {
				currentHeightB = (displayH - midtong.getHeight() - presentHieht - leftBt.getHeight()/2 * (midClick - 1));
			}
		} else {
			flag2 = false;
			currentHeightB = 0;
		}

	}
	/**
	 * 右边的雪糕的下落轨迹
	 */
	public void onDownLogicC() {
		if (currentHeightC < (displayH - righttong.getHeight() - presentHieht - leftBt.getHeight()/2 * (rightClick - 1))) {
			currentHeightC += 40;
			if (currentHeightC >= (displayH - righttong.getHeight() - presentHieht - leftBt.getHeight()/2 * (rightClick - 1))) {
				currentHeightC = (displayH - righttong.getHeight() - presentHieht - leftBt.getHeight()/2 * (rightClick - 1));
			}
		} else {
			flag3 = false;
			currentHeightC = 0;
		}

	}

	@Override
	public void run() {
	}
	/**
	 * 创建游戏画面初始化参�?
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		GameIceCreamActivity.OnResetGameListen = onResetGameListen;
		flag1 = false;
		flag2 = false;
		flag3 = false;
		clickable = false;
		
		displayW = this.getWidth();
		displayH = this.getHeight();
		background = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.droppresent_bg), displayW, displayH, true);
		presentUp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.present_head),displayW/3,displayW/6+5,true);
		presentDown =Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.present_tail),displayW/3,displayW/6+5,true);

		leftBt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.leftbt), displayW/3, displayW/3, true);
		
		midBt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.midbt), displayW/3, displayW/3, true);
		
		rightBt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.rightbt), displayW/3, displayW/3, true);
		
		downbt1 =  Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.down),displayW/7,displayW/7,true);
		downbt2 =  Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.down),displayW/7,displayW/7,true);
		downbt3 =  Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.down),displayW/7,displayW/7,true);


		present = new Bitmap[6];

		present[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.present1),displayW/3,displayW/3,true);
		present[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.present2),displayW/3,displayW/3,true);
		present[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.present3),displayW/3,displayW/3,true);
		present[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.present4),displayW/3,displayW/3,true);
		present[4] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.present5),displayW/3,displayW/3,true);
		present[5] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(this.getResources(),
				R.drawable.present6),displayW/3,displayW/3,true);
		
		lefttong = BitmapFactory.decodeResource(this.getResources(),R.drawable.tong);
		lefttong = Bitmap.createScaledBitmap(lefttong,displayW*4/14,lefttong.getHeight(),true);
		
		midtong = BitmapFactory.decodeResource(this.getResources(),R.drawable.tong);
		midtong = Bitmap.createScaledBitmap(midtong,displayW*4/14,lefttong.getHeight(),true);
		
		righttong = BitmapFactory.decodeResource(this.getResources(),R.drawable.tong);
		righttong = Bitmap.createScaledBitmap(righttong,displayW*4/14,lefttong.getHeight(),true);
		
		presentHieht = present[0].getHeight();

		leftbtX = (displayW - ((leftBt.getWidth() * 3))) / 2;
		leftbtY = displayH - leftBt.getHeight();

		midbtX = leftBt.getWidth() + leftbtX;
		midbtY = displayH - midBt.getHeight();

		rightbtX = midbtX + rightBt.getWidth();
		rightbtY = displayH - rightBt.getHeight();

		//down1X = (displayW - ((leftBt.getWidth() * 3))) / 2 + 25;
		down1X = displayW/10;
		down2X = midbtX + displayW/10;
		down3X = rightbtX + displayW/10;

		down1Y = displayH-(displayW/8)*2;
		down2Y = displayH-(displayW/8)*2;
		down3Y = displayH-(displayW/8)*2;
		
		
		initData();
		timeHandler = new Handler();
		stopHandler = new Handler();
		
		timeHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				clickable = true;
				dropPresentListen.gameStart();
			}
		}, 3000);
		onMyDraw();
		
		
	}
	/**
	 * 随机生成�?��下落雪糕个数
	 */
	public void initData() {
		
		a = new ArrayList<Bitmap>();
		b = new ArrayList<Bitmap>();
		c = new ArrayList<Bitmap>();
		leftClick = 0;
		midClick = 0;
		rightClick = 0;
		yesClick = 0;
		moreClick = false;
		random1 = (int) (Math.random() * 4) + 1;
		random2 = (int) (Math.random() * 4) + 1;
		random3 = (int) (Math.random() * 4) + 1;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int pointerCount = event.getPointerCount();
		if (pointerCount > MAX_TOUCH) {
			pointerCount = MAX_TOUCH;
		}
	
		float x = event.getX();
		float y = event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN && clickable) {
			System.out.println("x---->" + x + "++++y----->" + y);
			if (x > leftbtX && x < midbtX && y > leftbtY && y < displayH
					&& leftClick <= random1) {
				if (leftClick == random1) {
					Toast.makeText(context, "A你按多了哦！", Toast.LENGTH_SHORT)
							.show();

				}
				leftClick++;
				flag1 = true;
				index1 = (int) (Math.random() * 6);
				a.add(present[index1]);

				new Thread() {
					public void run() {
						while (flag1) {
							onMyDraw();
							onDownLogicA();
						}
						if (leftClick == random1 + 1) {
							initData();
							timeHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									onMyDraw();
								}
							}, 500);
						} else if (leftClick - 1 < random1) {
							yesClick++;
						} else {

						}
					}
				}.start();

			} else if (x > midbtX && x < rightbtX && y > midbtY && y < displayH
					&& midClick <= random2) {
				if (midClick == random2) {
					Toast.makeText(context, "B你按多了哦！", Toast.LENGTH_SHORT)
							.show();
				}
				midClick++;
				flag2 = true;
				index2 = (int) (Math.random() * 6);
				b.add(present[index2]);
				new Thread() {
					public void run() {
						while (flag2) {
							onMyDraw();
							onDownLogicB();
						}
						if (random2 + 1 == midClick) {
							initData();
							timeHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									onMyDraw();
								}
							}, 500);
						} else if (midClick - 1 < random2) {
							yesClick++;
						} else {

						}
					}
				}.start();
			} else if (x > rightbtX && x < rightbtX + rightBt.getWidth()
					&& y > rightbtY && y < displayH && rightClick <= random3) {
				if (rightClick == random3) {
					Toast.makeText(context, "C你按多了哦！", Toast.LENGTH_SHORT)
							.show();
				}
				rightClick++;
				flag3 = true;
				index3 = (int) (Math.random() * 6);
				c.add(present[index3]);
				new Thread() {
					public void run() {
						while (flag3) {
							onMyDraw();
							onDownLogicC();
						}
						if (random3 + 1 == rightClick) {
							initData();
							timeHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									onMyDraw();
								}
							}, 500);
						} else if (rightClick - 1 < random3) {
							yesClick++;
						} else {
							
						}
					}
				}.start();
			}
			if (moreClick) {
				moreClick = false;
			}
			if (yesClick+1 == random1+random2+random3) {
				moreClick = true;;
				
				stopHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						System.out.println(moreClick+"<------------time");
						if (moreClick) {
							dropPresentListen.gameStop();
						}
					}
				}, 800);
			}
		}
		
		return true;
	}
	
	/**
	 * 游戏的开始和结束的事件监听接�?
	 * @author minggo
	 * @date 2013-5-16下午04:32:39
	 */
	public interface DropPresentListen{
		public void gameStart();
		public void gameStop();
	}
	
}
