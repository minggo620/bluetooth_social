package edu.minggo.chat.adapters;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.minggo.chat.R;
import edu.minggo.chat.model.MessageEntity;
import edu.minggo.chat.ui.ChattingMessageImageActivity;
import edu.minggo.chat.ui.PersonnelLocation;
import edu.minggo.chat.util.ClippingPicture;
import edu.minggo.chat.util.ClippingSounds;
import edu.minggo.chat.util.PlaySound;
import edu.minggo.chat.util.PlaySound.FinishListen;
import edu.minggo.chat.util.SmileyParser;


public class ChatMessageAdapter extends BaseAdapter {

	private Context ctx;

	private List<MessageEntity> entitys;
	
	private MessageEntity entity;

	@SuppressWarnings("unused")
	private TextView tvName;
	@SuppressWarnings("unused")
	private TextView tvDate;
	private TextView tvText;
	private TextView time;
	@SuppressWarnings("unused")
	private ImageView userImage;
	private ImageButton voiceBtn;
	private TextView voiceTime;
	private ImageButton locationBtn;
	private Bitmap bmpDefaultPics;
	private View view;
	private ImageView talkImage;
	private SmileyParser parser;
	private String picPath;
	@SuppressWarnings("unused")
	private ImageView talkImages;
	
	public static boolean isClick = false;
	private Handler hdl;//播放音频的时候波频跳动
	private boolean isRepeat0 = true;//播放音频的时候波频跳动
	private boolean isRepeat1 = true;//播放音频的时候波频跳动
	@SuppressWarnings("unused")
	private boolean canPlay0 = true;
	private List<MessageEntity> messageList;

	public ChatMessageAdapter(Context context, List<MessageEntity> entitys,String TFuid) {
		ctx = context;
		this.entitys = entitys;
		this.messageList = entitys;
		/*messageList = MessageSQLService.getInstance(context).getdatas(
				Integer.valueOf(UserInfomation.getUserID(ctx)),
				Integer.valueOf(TFuid));*/
		hdl = new Handler();
	}

	@Override
	public int getCount() {
		return entitys.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		entity = entitys.get(position);

		int itemLayout = entity.getLayoutID();
		
		/*if("".equals(entity.getText())){
			view = LayoutInflater.from(ctx).inflate(R.layout.list_say_null, null);
			return view;
		}*/
		
		switch(itemLayout){
		case R.layout.chatting_item_msg_text_right:
			view = LayoutInflater.from(ctx).inflate(itemLayout, null);
			tvText = (TextView) view.findViewById(R.id.chatcontent_tv_right);
			SmileyParser.init(ctx);
			parser = SmileyParser.getInstance();
			tvText.setText(parser.addSmileySpans(entity.getTtmContent()));

			break;
		case R.layout.chatting_item_msg_text_left:
			view = LayoutInflater.from(ctx).inflate(itemLayout, null);
			tvText = (TextView) view.findViewById(R.id.chatcontent_tv_left);
			SmileyParser.init(ctx);
			parser = SmileyParser.getInstance();
			tvText.setText(parser.addSmileySpans(entity.getTtmContent()));
			
			break;	
		case R.layout.chatting_item_msg_image_right:
			
			view = LayoutInflater.from(ctx).inflate(itemLayout, null);
			talkImage = (ImageView) view.findViewById(R.id.chatcontent_iv_right);
			picPath = ClippingPicture.TALK_FILES2+entity.getTtmContent();
			bmpDefaultPics = BitmapFactory.decodeFile(picPath);
			talkImage.setImageBitmap(bmpDefaultPics);
			
			talkImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if("".equals(messageList.get(position).getTtmContent())){
						Toast.makeText(ctx, "图片不存在或已删除", 300).show();
					}else{
						Toast.makeText(ctx, "查看图片", 300).show();
						Intent intent = new Intent(ctx,ChattingMessageImageActivity.class);
						intent.putExtra("picPath", picPath);
						ctx.startActivity(intent);
					}

				}
			});
			
			break;
		case R.layout.chatting_item_msg_image_left:
			
			view = LayoutInflater.from(ctx).inflate(itemLayout, null);
			talkImage = (ImageView)view.findViewById(R.id.chatcontent_iv_left);
			picPath = ClippingPicture.TALK_FILES2+entity.getTtmContent();
			bmpDefaultPics = BitmapFactory.decodeFile(picPath);
			talkImage.setImageBitmap(bmpDefaultPics);
			
			talkImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if("".equals(messageList.get(position).getTtmContent())||messageList.get(position).getTtmContent()==null){
						Toast.makeText(ctx, "图片不存在或已删除", 300).show();
					}else{
						Toast.makeText(ctx, "查看图片", 300).show();
						Intent intent = new Intent(ctx,ChattingMessageImageActivity.class);
						intent.putExtra("picPath", picPath);
						ctx.startActivity(intent);
					}

				}
			});
			
			break;
		case R.layout.chatting_item_msg_voice_right:
			view = LayoutInflater.from(ctx).inflate(itemLayout, null);
			voiceBtn = (ImageButton)view.findViewById(R.id.chatcontent_ib_right);
			voiceTime = (TextView)view.findViewById(R.id.chatto_voice_time_tv);
			
			voiceBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					try {
						hdl.post(new Runnable() {
							int i = 0;
							@Override
							public void run() {
								if (isRepeat0==true) {
									if (i==0) {
										((ImageButton)v).setImageResource(R.drawable.chatto_voice_playing_f1);
									}else if(i==1){
										((ImageButton)v).setImageResource(R.drawable.chatto_voice_playing_f2);
									}else if(i==3){
										((ImageButton)v).setImageResource(R.drawable.chatto_voice_playing_f3);
										i=-1;
									}
									i++;
									hdl.postDelayed(this,250);
								}else{
									isRepeat0 = true;
									((ImageButton)v).setImageResource(R.drawable.chatto_voice_playing);
								}
							}
						});
						new PlaySound().setOnFinishListen(new FinishListen() {
							@Override
							public void onFinish() {
								isRepeat0 = false;
							}
						});
						PlaySound.playVoice(ClippingSounds.TALKSOUND_FILE+messageList.get(position).getTtmContent(),ctx.getResources().getAssets());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			voiceTime.setText(entity.getVoiceTime()+"''");
			break;
		case R.layout.chatting_item_msg_voice_left:
			view = LayoutInflater.from(ctx).inflate(itemLayout, null);
			voiceBtn = (ImageButton)view.findViewById(R.id.chatcontent_ib_left);
			voiceTime = (TextView)view.findViewById(R.id.chatfrom_voice_time_tv);
			voiceTime.setText(entity.getVoiceTime()+"''");
			voiceBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					if(entity.getTtmContent()!=null){
						bmpDefaultPics = ((BitmapDrawable)(ctx.getResources().getDrawable(R.drawable.download_image_icon))).getBitmap();
						try {
							hdl.post(new Runnable() {
								int i = 0;
								@Override
								public void run() {
									if (isRepeat1==true) {
										if (i==0) {
											((ImageButton)v).setImageResource(R.drawable.chatfrom_group_voice_playing_f1);
										}else if(i==1){
											((ImageButton)v).setImageResource(R.drawable.chatfrom_group_voice_playing_f2);
										}else if(i==3){
											((ImageButton)v).setImageResource(R.drawable.chatfrom_group_voice_playing_f3);
											i=-1;
										}
										i++;
										hdl.postDelayed(this,250);
									}else{
										isRepeat1 = true;
										((ImageButton)v).setImageResource(R.drawable.chatfrom_voice_playing);
									}
								}
							});
							new PlaySound().setOnFinishListen(new FinishListen() {
								@Override
								public void onFinish() {
									isRepeat1 = false;
								}
							});
							PlaySound.playVoice(ClippingSounds.TALKSOUND_FILE+messageList.get(position).getTtmContent(),ctx.getResources().getAssets());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else{
						Toast.makeText(ctx, "正在下载..", 3000).show();
					}
				}
			});
			
			break;
		case R.layout.chatting_item_msg_location_right:
			
			view = LayoutInflater.from(ctx).inflate(itemLayout, null);
			
			locationBtn = (ImageButton)view.findViewById(R.id.chatto_location_iv_right);
			
			final String[] location = entity.getTtmContent().split(",");
			
			locationBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Intent intentss = new Intent(ctx,PersonnelLocation.class);
					intentss.putExtra("Lon", location[1]);
					intentss.putExtra("Lat", location[0]);
					ctx.startActivity(intentss);
				}
			});
			
			break;
		case R.layout.chatting_item_msg_location_left:
			
			view = LayoutInflater.from(ctx).inflate(itemLayout, null);
			
			locationBtn = (ImageButton)view.findViewById(R.id.chatfrom_location_iv_left);
			
			final String[] locationsx = entity.getTtmContent().split(",");
			
			locationBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intentss = new Intent(ctx,PersonnelLocation.class);
					intentss.putExtra("Lon", locationsx[1]);
					intentss.putExtra("Lat", locationsx[0]);
					ctx.startActivity(intentss);
				}
			});
			
			break;
			
		}
		
		time = (TextView)view.findViewById(R.id.tv_sendtime);
		time.setText(entity.getTtmTime());
		
		userImage = (ImageView)view.findViewById(R.id.iv_userhead);

		return view;
	}

	
}
