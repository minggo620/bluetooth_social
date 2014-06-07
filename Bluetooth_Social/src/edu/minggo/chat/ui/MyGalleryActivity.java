package edu.minggo.chat.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.minggo.chat.R;
import edu.minggo.chat.adapters.MyGalleryAdapter;
import edu.minggo.chat.control.BluetoothChatInterface;
import edu.minggo.chat.control.BluetoothChatService;
import edu.minggo.chat.database.DataBaseOperator;
import edu.minggo.chat.model.MyPhoto;
import edu.minggo.chat.model.Task;
import edu.minggo.chat.util.BounceListView;
import edu.minggo.chat.util.ClippingPicture;
import edu.minggo.chat.util.OptionAlert;

/**
 * 我的相册
 * @author minggo
 * @created 2013-2-6下午12:48:47
 */
public class MyGalleryActivity extends Activity implements BluetoothChatInterface{
	
	public static final int GET_PHOTO_FROM_CARMERA = 0;
	public static final int SET_PHOTO_FROM_CARMERA = 3;
	public static final int GET_PHOTO_FROM_LOCAL = 1;
	public static final int SET_PHOTO_FROM_LOCAL = 2;
	private BounceListView mygalleryList;
	private MyGalleryAdapter listAdapter;
	private  List<MyPhoto>  photos;
	public static  List<MyPhoto>  photos1;
	private Button backButton;
	private ImageView takePhoto;
	private ImageView portraitiv;
	private TextView mymottotv;
	private TextView mynametv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mygallery);
		init();
	}
	public static void simulateKey(final int keyCode){
		new Thread(){
			public void run(){
				try {
					Instrumentation isn = new Instrumentation();
					isn.sendKeyDownUpSync(keyCode);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	@Override
	public void init() {
		mygalleryList = (BounceListView)findViewById(R.id.mygallery_listview);
		backButton = (Button)findViewById(R.id.mygallery_bt_left);
		
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(3, new Intent().putExtra("size", photos.size()));
				simulateKey(KeyEvent.KEYCODE_BACK);
					
				//finish();
			}
		});
		
		photos = new ArrayList<MyPhoto>();
		photos = DataBaseOperator.quryPhotos(getApplicationContext());
		if(photos.isEmpty()){
			for(int i = 0; i<3;i++){
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				System.out.println(df.format(new Date()));
				MyPhoto myPhoto = new MyPhoto();
				myPhoto.setPhotoDes(i+"");
				myPhoto.setTime(df.format(new Date()));
				myPhoto.setUsername(BluetoothChatService.nowuser.getUsername());
				photos.add(myPhoto);
			}
		}else{
			/*for (int i = 0; i < photos.size(); i++) {
				photos.get(i).setMyphoto(BitmapFactory.decodeFile(photos.get(i).getPicPath()));
			}*/
		}
		listAdapter = new MyGalleryAdapter(MyGalleryActivity.this.getApplicationContext(), photos);
		View listHead = LayoutInflater.from(this).inflate(R.layout.mygallery_list_head, null);
		View listFoot = LayoutInflater.from(this).inflate(R.layout.mygallery_list_foot, null);
		mymottotv = (TextView) listHead.findViewById(R.id.mygallery_tv_motto);
		portraitiv = (ImageView) listHead.findViewById(R.id.mygallery_iv_portrait);
		mynametv = (TextView) listHead.findViewById(R.id.mygallery_tv_myname);
		
		String myname = "未填写";
		if(BluetoothChatService.nowuser.getUsername()!=null)
			myname = BluetoothChatService.nowuser.getUsername();
		mynametv.setText(myname);
		
		String motto = "未填写";
		if(BluetoothChatService.nowuser.getMotto()!=null)
			motto = BluetoothChatService.nowuser.getMotto();
		mymottotv.setText(motto);
		
		if(BluetoothChatService.nowuser.getPhoto()!=null)
			portraitiv.setImageBitmap(BluetoothChatService.nowuser.getPhoto());
		else portraitiv.setImageResource(R.drawable.default_avatar);
		
		takePhoto = (ImageView) listHead.findViewById(R.id.mygallery_iv_take_photo);
		takePhoto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				OptionAlert.showAlert(MyGalleryActivity.this, MyGalleryActivity.this.getString(R.string.set_myinfo), 
						MyGalleryActivity.this.getResources().getStringArray(R.array.set_myinfo_item),
						null, new OptionAlert.OnAlertSelectId(){
	
					@Override
					public void onClick(int whichButton) {	
						
						switch(whichButton){
						case GET_PHOTO_FROM_LOCAL:
							Intent intent = new Intent();
							intent.setType("image/*");
							intent.setAction(Intent.ACTION_GET_CONTENT);
							((Activity) MyGalleryActivity.this).startActivityForResult(Intent.createChooser(intent, "Select Picture"),GET_PHOTO_FROM_LOCAL);
							break;
						case GET_PHOTO_FROM_CARMERA:
							Intent i = new Intent("android.media.action.IMAGE_CAPTURE"); 
							((Activity) MyGalleryActivity.this).startActivityForResult(i,GET_PHOTO_FROM_CARMERA);     
							break;
						default:
							break;
						}
						
					}
				});
			
				
			}
		});
		mygalleryList.addFooterView(listFoot);
		mygalleryList.addHeaderView(listHead);
		mygalleryList.setSelectionAfterHeaderView();//效果设置出来没用
		mygalleryList.setHeaderDividersEnabled(false);//效果设置出来也没用
		mygalleryList.setFooterDividersEnabled(false);
		mygalleryList.setAdapter(listAdapter);
		
		mygalleryList.setOnItemClickListener(new ListItemListener());
		
		BluetoothChatService.allActivity.add(this);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SET_PHOTO_FROM_CARMERA:
			if (data!=null&& data.getExtras().get("photo")!=null) {
				MyPhoto photo = (MyPhoto) data.getExtras().get("photo");
				photo.setMyphoto((Bitmap)data.getExtras().get("bitmap"));
				MyGalleryAdapter.photos.add(0, photo);
				listAdapter.refresh();
			}
			break;
		case SET_PHOTO_FROM_LOCAL:
			if (data!=null&& data.getExtras().get("photo")!=null) {
				MyPhoto photo = (MyPhoto) data.getExtras().get("photo");
				photo.setMyphoto((Bitmap)data.getExtras().get("bitmap"));
				MyGalleryAdapter.photos.add(0, photo);
				listAdapter.refresh();
			}
			break;
		case GET_PHOTO_FROM_LOCAL: // 本地取图片
			if (resultCode == Activity.RESULT_OK) {
				if (data!=null&& data.getData()!=null) {
					Map<String,Object> maps = ClippingPicture.saveGalleryPic(MyGalleryActivity.this, data.getData());
					Intent it = new Intent();
					it.putExtra("bitmap", (Bitmap)maps.get("bitmap"));
					it.putExtra("myphotoPath",(String)maps.get("galleryPicName") );
					it.setClass(MyGalleryActivity.this, MyphotoAddActivity.class);
					MyGalleryActivity.this.startActivityForResult(it, SET_PHOTO_FROM_LOCAL);
				}
			}
			break;
		
		case GET_PHOTO_FROM_CARMERA://拍照
			if (resultCode == Activity.RESULT_OK) {
				if (data!=null&&data.getExtras()!=null) {
					Bitmap bitmap = (Bitmap) data.getExtras().get("data");
					bitmap = ClippingPicture.Resize(bitmap);
					ClippingPicture.saveGalleryPic(bitmap);
					Intent it = new Intent();
					it.putExtra("bitmap", bitmap);
					it.putExtra("myphotoPath", ClippingPicture.saveGalleryPic(bitmap));
					it.setClass(MyGalleryActivity.this, MyphotoAddActivity.class);
					MyGalleryActivity.this.startActivityForResult(it, SET_PHOTO_FROM_CARMERA);
				}
			}
			break;
		default:
			if (resultCode == Activity.RESULT_OK) {
				listAdapter.refresh();
			}
			break;
		}
	}

	/**
	 * 相册列表的每一项监听器
	 * @author minggo
	 * @created 2013-2-6下午01:12:58
	 */
	public class ListItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(position==0){
				
			}else{
			}
		}
		
	}
	@Override
	public void refresh(Object... param) {
		int i = (Integer)param[0];
		
		if(i==Task.TASK_REFREAH_GALLERY){
			System.out.println("+++++++++++++++++++++++++++++");
			listAdapter.refresh();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
	
}
