package edu.minggo.chat.util;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
/**
 * 声音播放工具类
 * @author minggo
 * @created 2013-2-23上午11:48:41
 */
public class PlaySound {
	private static FinishListen onFinishListen;
	public  static MediaPlayer player;
	@SuppressWarnings("static-access")
	public void setOnFinishListen(FinishListen onFinishListen) {
		this.onFinishListen = onFinishListen;
	}
	/**
	* 调用方式:play("across.mp3",getResources().getAssets());
	* @param filename 要播放的音频文件名
	* @param asm AssetManager
	* @throws IOException
	*/
	public static final void play(String filename,AssetManager asm) throws IOException{
		//AssetManager asm = getResources().getAssets();
		AssetFileDescriptor afd = asm.openFd(filename);
		player = new MediaPlayer();
		player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
		player.prepare();
		player.start();
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
	}
	/**
	 * 根据uri播放音频文件
	 * @param context
	 * @param uri
	 * @throws IOException
	 */
	public static final void play(Context context,Uri uri) throws IOException{
		player = new MediaPlayer();
		//uri.fromFile(file);
		player.setDataSource(context,uri);
		player.prepare();
		player.start();
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
	}
	/**
	 * 根据音频文件的路径
	 * @param soundpath
	 * @throws IOException
	 */
	public static final void play(String soundpath) throws IOException{
		player = new MediaPlayer();
		//uri.fromFile(file);
		player.setDataSource(soundpath);
		player.prepare();
		player.start();
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
	}
	/**
	 * 根据录音的路径播放
	 * @param soundpath
	 * @throws IOException
	 */
	public static final void playVoice(String soundpath,final AssetManager asm) throws IOException{
		player = new MediaPlayer();
		//uri.fromFile(file);
		player.setDataSource(soundpath);
		player.prepare();
		player.start();
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
				MediaPlayer player = new MediaPlayer();
				
				AssetFileDescriptor afd = null;
				try {
					afd = asm.openFd("sound/play_completed.mp3");
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					player.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				player.start();
				
				player.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO Auto-generated method stub
						if (onFinishListen!=null) {
							onFinishListen.onFinish();
						}
					}
				});
			}
		});
	}
	public interface FinishListen{
		public void onFinish();
	}
	public static void stopVoice(){
		if (player!=null) {
			//player.stop();
			player.release();
		}
	}
}
