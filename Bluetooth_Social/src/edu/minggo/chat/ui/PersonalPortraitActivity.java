package edu.minggo.chat.ui;



import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import edu.minggo.chat.R;
/**
 * 个人头像显示
 * @author minggo
 * @created 2013-2-3下午01:52:59
 */
public class PersonalPortraitActivity extends Activity{
	@SuppressWarnings("unused")
	private ImageView portraitiv;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.personal_info_portrait);
		portraitiv = (ImageView) findViewById(R.id.infor_iv_big_portrait);
   }
	@Override
	public boolean onTouchEvent(MotionEvent event){
		finish();
		return true;
	}
	
}