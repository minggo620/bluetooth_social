package edu.minggo.chat.util;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * 震动效果
 * @author minggo
 * @created 2013-2-23下午12:10:42
 */
public class VibratorUtil {
	/**
	 * 单次按时间震动
	 * @param context
	 * @param milliseconds
	 */
	public static void Vibrate(Context context,Long milliseconds){
		Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
	}
	/**
	 * 重复按时间震动
	 * @param context
	 * @param milliseconds
	 */
	public static void Vibrate(Context context,long[] milliseconds,boolean isRepeat){
		Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds,isRepeat?1:-1);
	}
}
