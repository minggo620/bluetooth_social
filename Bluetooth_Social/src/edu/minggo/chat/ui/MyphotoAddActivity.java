package edu.minggo.chat.ui;


import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import edu.minggo.chat.R;
import edu.minggo.chat.control.BluetoothChatService;
import edu.minggo.chat.database.DataBaseOperator;
import edu.minggo.chat.model.MyPhoto;
import edu.minggo.chat.util.ClippingPicture;
/**
 * ÃÌº”œ‡∆¨
 * @author minggo
 * @created 2013-2-6œ¬ŒÁ05:36:50
 */
public class MyphotoAddActivity extends Activity {
	private Button backbt;
	private Button savebt;
	private Button imgAddbt;
	private ImageView addImage;
	private EditText descev;
	private String photoPath;
	private Bitmap addBitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myphoto_add);
		Intent it = getIntent();
		
		backbt = (Button)findViewById(R.id.myphoto_record_bt_left);
		savebt = (Button)findViewById(R.id.myphoto_record_save_bt);
		imgAddbt = (Button)findViewById(R.id.myphoto_add_bt);
		addImage = (ImageView)findViewById(R.id.myphoto_add_iv);
		descev = (EditText)findViewById(R.id.myphoto_desc_ev);
		
		backbt.setOnClickListener(new MyphotoAddListener());
		savebt.setOnClickListener(new MyphotoAddListener());
		imgAddbt.setOnClickListener(new MyphotoAddListener());
		photoPath = it.getStringExtra("myphotoPath");
		addBitmap = (Bitmap) it.getExtras().get("bitmap");
		addImage.setImageBitmap(addBitmap);
		
	}
	public class MyphotoAddListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.equals(backbt)){
				MyphotoAddActivity.this.finish();
				ClippingPicture.deleteSDFile(photoPath);
				
			}else if(v.equals(savebt)){
				String description = descev.getText().toString();
				MyPhoto photo = new MyPhoto();
				photo.setUsername(BluetoothChatService.nowuser.getLoginname());
				photo.setPicPath(photoPath);
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				photo.setTime(df.format(new Date()));
				photo.setPhotoDes(description);
				
			    DataBaseOperator.insertPhoto(MyphotoAddActivity.this.getApplicationContext(),photo);
			    Intent it = new Intent();
			    it.putExtra("bitmap", addBitmap);
			    it.putExtra("photo", photo);
			    setResult(1, it);
				finish();
				
			}else if(v.equals(imgAddbt)){
				
			}else if(v.equals(addImage)){
				
			}
		}
		
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
}
