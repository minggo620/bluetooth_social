package edu.minggo.chat.ui;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.minggo.chat.R;
import edu.minggo.game.icecream.DropIceCreamSurfaceView;
import edu.minggo.game.icecream.DropIceCreamSurfaceView.DropPresentListen;
/**
 * 堆雪糕
 * @author minggo
 * @date 2013-5-17下午03:50:11
 */
public class GameIceCreamActivity extends Activity {
	
	private DropIceCreamSurfaceView mySurfaceView;
	public static OnResetGameListen OnResetGameListen;
	private TextView timetv;
	private DropPresentListen dropPresentListen;
	private Handler handler;
	private Button backbt;
	private long startTime;
	private long endTime;
	private long finalTime;
	private boolean stopflag;
	private boolean startflag;
	private boolean readyflag;
	private int i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(new MySurfaceView(this));
		setContentView(R.layout.game_drop_present);
		mySurfaceView = (DropIceCreamSurfaceView)findViewById(R.id.mysurface_view);
		timetv = (TextView)findViewById(R.id.game_droppresent_time);
		backbt = (Button) findViewById(R.id.game_bt_left);
		
		backbt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		stopflag = false;
		startflag = false;
		readyflag = true;
		i =3;
		
		handler = new Handler();
		dropPresentListen = new DropPresentListen() {
			@Override
			public void gameStop() {
				endTime = System.currentTimeMillis();
				finalTime = endTime-startTime;
				stopflag = true;
				startflag = false;
				
				AlertDialog.Builder builder = new Builder(GameIceCreamActivity.this);
				builder.setMessage("用时"+finalTime+"ms");
				builder.setTitle("得分");
				builder.create().show();
				
			}
			
			@Override
			public void gameStart() {
				startTime = System.currentTimeMillis();
				startflag = true;
				readyflag = false;
				
			}
		};
		mySurfaceView.setDropPresentListen(dropPresentListen);
		updateTime();
	}
	/**
	 * 
	 */
	public void updateTime(){
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (stopflag) {
					handler.removeCallbacks(this);
					timetv.setText((endTime+"").substring(9)+"ms");
				}else if(startflag){
					timetv.setText((System.currentTimeMillis()+"").substring(9)+"ms");
					handler.post(this);
				}else if (readyflag) {
					timetv.setText(i--+"");
					handler.postDelayed(this, 1000);
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_menu, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.game_finish:
			finish();
			break;
		case R.id.game_reset:
			OnResetGameListen.onReset();
			break;
		}
		return super.onOptionsItemSelected(item);
		
	}
	/**
	 * 
	 * @author minggo
	 * @date 2013-5-31
	 * @time 下午12:15:26
	 */
	public interface OnResetGameListen{
		public void onReset();
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
