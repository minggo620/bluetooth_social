package edu.minggo.chat.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.minggo.chat.R;
import edu.minggo.chat.adapters.MyGalleryAdapter;
import edu.minggo.chat.control.BluetoothChatService;
import edu.minggo.chat.database.DataBaseOperator;
import edu.minggo.chat.model.MyPhoto;
import edu.minggo.chat.model.Task;
import edu.minggo.chat.util.ImageUitl;
import edu.minggo.chat.util.OptionAlert;
/**
 * 每张照片的具体信息
 * @author minggo
 * @created 2013-2-6下午07:49:11
 */
public class MyGalleryDetailActivity extends Activity{
	private Button backbt;
	private ImageButton menubt;
	private TextView timetv;
	private ImageView photoiv;
	private MyPhoto photo;
	private int position;
	private Bitmap bitmap;
	
	private Button bigButton;
	private Button smallButton;
	private Button tuenLeftButton;
	private Button tuenRightButton;

	private float scaleWidth=1; 
	private float scaleHeight=1;
	private int screenWidth;
	private int screenHeight; 
	private Bitmap bitmapTurn;
	private float leftRote;
	private float rightTote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mygallery_photo);
		
		Intent it = getIntent();
		
		bigButton = (Button) findViewById(R.id.imageview_bigbtn);
		smallButton = (Button) findViewById(R.id.imageview_smallbtn);
		tuenLeftButton = (Button) findViewById(R.id.imageview_turnleft);
		tuenRightButton = (Button) findViewById(R.id.imageview_turnright);
		bigButton.setOnClickListener(new DetailListener());
		smallButton.setOnClickListener(new DetailListener());
		tuenLeftButton.setOnClickListener(new DetailListener());
		tuenRightButton.setOnClickListener(new DetailListener());

		photo = (MyPhoto) it.getExtras().get("MyPhoto");
		position = it.getExtras().getInt("position");
		bitmap = (Bitmap)it.getExtras().get("bitmap");
		backbt = (Button) findViewById(R.id.mygallery_bt_left);
		menubt = (ImageButton) findViewById(R.id.mygallery_bt_menue);
		timetv = (TextView) findViewById(R.id.mygallery_tv_time);
		photoiv = (ImageView) findViewById(R.id.mygallery_iv_big_portrait);
		backbt.setOnClickListener(new DetailListener());
		menubt.setOnClickListener(new DetailListener());
		String time = photo.getTime();
		timetv.setText(time.substring(4,6)+"月"+time.substring(6,8)+"日 "+time.substring(8,10)+":"+time.substring(10,12));
		
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		if (bitmap==null) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeFile(photo.getPicPath(),options);
			options.inJustDecodeBounds = false;
			int be = (int) (options.outHeight / (float) 200);
			if (be <= 0)
				be = 1;
			options.inSampleSize = be;
			bitmap = BitmapFactory.decodeFile(photo.getPicPath(),options);
		}
		photoiv.setImageBitmap(ImageUitl.createReflectionImageWithOrigin(bitmap));
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	/**
	 * 
	 * @author minggo
	 * @created 2013-2-6下午07:58:06
	 */
	public class DetailListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if (v==backbt) {
				finish();
			}else if (v==bigButton) {
				resizeBigImage(bitmap,photoiv,bigButton);
			}else if (v==smallButton) {
				resizeSmallImage(bitmap,photoiv,smallButton);
			}else if (v==tuenLeftButton) {
				leftRote = leftRote+90;
				if(bitmapTurn!=null){
					turnLeftBitmap(bitmapTurn,photoiv,leftRote);
				}
				else turnLeftBitmap(bitmap,photoiv,leftRote);
			}else if (v==tuenRightButton) {
				rightTote = rightTote-90;
				if (bitmapTurn!=null) {
					turnRightBitmap(bitmapTurn,photoiv,rightTote);
				}
				else turnRightBitmap(bitmap,photoiv,rightTote);
			}else if (v==menubt) {
				OptionAlert.showAlert(MyGalleryDetailActivity.this, "图片操作", 
						new String[]{"删除"},
						null, new OptionAlert.OnAlertSelectId(){
	
					@Override
					public void onClick(int whichButton) {						
						switch(whichButton){
						case 0:
							
							if(DataBaseOperator.deletePhoto(MyGalleryDetailActivity.this,photo)){
								MyGalleryAdapter.photos.remove(position);
								Task task = new Task(Task.TASK_REFREAH_GALLERY, null);
								BluetoothChatService.allTask.add(task);
								finish();
								Toast.makeText(MyGalleryDetailActivity.this, "删除成功", 2000).show();
							}else{
								Toast.makeText(MyGalleryDetailActivity.this, "删除失败", 2000).show();
							}
							
							break;
						
						}
						
					}
				});
			}
		}
		
	}
	public void resizeSmallImage(Bitmap bmp,ImageView mImageView,Button btn) {
		
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		
		double scale = 0.8;
		
		scaleWidth = (float) (scaleWidth * scale);
		scaleHeight = (float) (scaleHeight * scale);
		if (100 < bmpWidth|| 100 < bmpHeight) {
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth,bmpHeight, matrix, true);
			mImageView.setImageBitmap(ImageUitl.createReflectionImageWithOrigin(resizeBmp));
			bitmapTurn = resizeBmp;
		}else{
			Toast.makeText(MyGalleryDetailActivity.this, "已是最小了", Toast.LENGTH_SHORT).show();
		}
	}

	public void resizeBigImage(Bitmap bmp,ImageView mImageView,Button btn) {

		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		double scale = 1.25;
		scaleWidth = (float) (scaleWidth * scale);
		scaleHeight = (float) (scaleHeight * scale);

		if (scaleWidth * scale * bmpWidth > screenWidth|| scaleHeight * scale * bmpHeight > screenHeight) {
			Toast.makeText(MyGalleryDetailActivity.this, "已是最大了", Toast.LENGTH_SHORT).show();
		}else{
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth,bmpHeight, matrix, true);

			mImageView.setImageBitmap(ImageUitl.createReflectionImageWithOrigin(resizeBmp));
			bitmapTurn = resizeBmp;
		}
	}
	
	
	private void turnLeftBitmap(Bitmap bitmapOrg ,ImageView mImageView,float rote){
		
		Matrix matrix = new Matrix();  
		matrix.postRotate(rote);  
		
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,  
					width, height, matrix, true);  
		
		mImageView.setImageBitmap(ImageUitl.createReflectionImageWithOrigin(resizedBitmap));
		
	}
	
	private void turnRightBitmap(Bitmap bitmapOrg ,ImageView mImageView,float rote){
		
		Matrix matrix = new Matrix();  
		matrix.postRotate(rote);  
		
		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,  
					width, height, matrix, true); 
		
		mImageView.setImageBitmap(ImageUitl.createReflectionImageWithOrigin(resizedBitmap));
	
	}
	
}
