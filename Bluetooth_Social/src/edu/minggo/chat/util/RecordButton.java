package edu.minggo.chat.util;

import java.io.File;
import java.io.IOException;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import edu.minggo.chat.R;
/**
 * 按住录音按钮
 * @author minggo
 * @created 2013-2-16上午03:42:13
 */
public class RecordButton extends Button{
	
	public RecordButton(Context context) {
		super(context);
		init();
	}

	public RecordButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RecordButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void setOnEventListener(OnEventListener onEventListener) {
		this.onEventListener = onEventListener;
	}

	private String mFileName = null;
	private OnEventListener onEventListener;
	private static final int MIN_INTERVAL_TIME = 2000;// 2s
	private long startTime;
	private Dialog recordIndicator;

	private static int[] res = { R.drawable.amp1, R.drawable.amp2,
			R.drawable.amp3, R.drawable.amp4,R.drawable.amp5 ,
			R.drawable.amp6,R.drawable.amp7};

	private static ImageView view;

	private MediaRecorder recorder;

	private ObtainDecibelThread thread;

	private Handler volumeHandler;
	
	
	private void init() {
		volumeHandler = new ShowVolumeHandler();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
			setText("松手 保存");
			initDialogAndStartRecord();
			break;
		case MotionEvent.ACTION_UP:
			finishRecord();
			setText("按住 说话");
			setBackgroundResource(R.drawable.voice_rcd_btn_nor);
			break;
		case MotionEvent.ACTION_CANCEL:// 当手指移动到view外面，会cancel
			cancelRecord();
			setText("按住 说话");
			setBackgroundResource(R.drawable.voice_rcd_btn_nor);
			Toast.makeText(getContext(), "已放弃录音", 2000).show();
			break;
		}

		return true;
	}
	

	private void initDialogAndStartRecord() {
		startTime = System.currentTimeMillis();
		recordIndicator = new Dialog(getContext(), R.style.LoadingDialogStyle);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.voice_record_dialog, null);
		view = (ImageView) (layout.findViewById(R.id.linearLayout1).findViewById(R.id.voice_record_iv_heigher));
		view.setImageResource(res[0]);
		recordIndicator.setContentView(layout);
		recordIndicator.setOnDismissListener(onDismiss);
		recordIndicator.show();
		
		try {
			startRecording();
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}

	private void finishRecord() {
		stopRecording();
		recordIndicator.dismiss();
		System.out.println(System.currentTimeMillis()+"==ddddddd==="+startTime+"ffff"+mFileName);
		long intervalTime = System.currentTimeMillis() - startTime;
		if (intervalTime < MIN_INTERVAL_TIME) {
			Toast.makeText(getContext(), "时间太短！", Toast.LENGTH_SHORT).show();
			File f = new File(mFileName);
			f.delete();
		}else{
			if (onEventListener != null)
				onEventListener.onFinishedRecord(mFileName,(int)intervalTime/1000);
		}
		
	}

	private void cancelRecord() {
		stopRecording();
		recordIndicator.dismiss();
		Toast.makeText(getContext(), "取消录音！", Toast.LENGTH_SHORT).show();
		File file = new File(mFileName);
		file.delete();
	}

	private void startRecording()throws Exception{
		mFileName=ClippingSounds.saveSounds();
		System.out.println("开始的时候-----》"+mFileName);
		recorder = new MediaRecorder();
		if(onEventListener!=null){
			onEventListener.onStartRecord();
		}
		try {
			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(mFileName);
			recorder.setMaxDuration(180*1000);
			recorder.prepare();
			//startRecordListener.onStartRecord();
			recorder.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		thread = new ObtainDecibelThread();
		thread.start();
	}

	private void stopRecording() {
		if (thread != null) {
			thread.exit();
			thread = null;
		}
		if (recorder != null) {
			try{
				recorder.stop();
				recorder.release();
				recorder = null;
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

	private class ObtainDecibelThread extends Thread {

		private volatile boolean running = true;

		public void exit() {
			running = false;
		}

		@Override
		public void run() {
			while (running) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (recorder == null || !running) {
					break;
				}
				int x = recorder.getMaxAmplitude();
				if (x != 0) {
					int f = (int) (10 * Math.log(x) / Math.log(10));
					if (f < 25)
						volumeHandler.sendEmptyMessage(0);
					else if (f < 30)
						volumeHandler.sendEmptyMessage(1);
					else if (f < 35)
						volumeHandler.sendEmptyMessage(2);
					else if (f < 40)
						volumeHandler.sendEmptyMessage(3);
					else if (f < 45)
						volumeHandler.sendEmptyMessage(4);
					else if (f < 48)
						volumeHandler.sendEmptyMessage(5);
					else
						volumeHandler.sendEmptyMessage(6);
				}

			}
		}

	}

	private OnDismissListener onDismiss = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			stopRecording();
		}
	};

	static class ShowVolumeHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			view.setImageResource(res[msg.what]);
		}
	}

	public interface OnEventListener {
		public void onFinishedRecord(String audioPath,int time);
		public void onStartRecord();
	}
	
}
