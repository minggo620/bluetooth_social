package edu.minggo.chat.util;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * 日期和事件操作工具类
 * @author minggo
 * @created 2013-2-17下午09:04:04
 */
public class DateUtil {

	public DateUtil(){
		
	}
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getCurrentTiem(){
		
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		String ss = String.valueOf(year);
		int end = ss.length();
		String type = ss.substring(2,end);
		
		String month2 = null;
		String day2 = null;
		String minute2 = null;
		
		if(month <10){
			month2 = "0"+month;
		}else{
			month2 = month+"";
		}
		
		if(day <10){
			day2 = "0"+day;
		}else{
			day2 = day+"";
		}
		
		if(minute < 10){
			minute2 = "0"+minute;
		}else{
			minute2 = minute+"";
		}
		
		
		return type+"-"+month2+"-"+day2+" "+hourOfDay+":"+minute2+":"+second;
	}
	
	
	/**
	 * 设置日期
	 * @param button  view
	 * @param context 上下文
	 */
	public static void setDate(final Button button,Context context){
		
		DatePickerDialog datePickerDialog = null;
		
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		datePickerDialog = new DatePickerDialog(context, dateListener(button), year, month, day);
		datePickerDialog.show();
	}
	
	
	/**
	 * 日期监听
	 * @param button
	 * @return
	 */
	public static OnDateSetListener dateListener(final Button button){
		
		OnDateSetListener odsl = new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {

				button.setText(year + "-" + (monthOfYear + 1) + "-"
						+ dayOfMonth);
			}
		};
		return odsl;
	}
	
	
	
	/**
	 * 设置时间
	 * @param button
	 * @param context
	 */
	public static void setTime(final Button button,Context context){
		
		TimePickerDialog timePickerDialog = null;
		
		Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		timePickerDialog = new TimePickerDialog(context,timeListener(button,second), hourOfDay, minute, true);
		timePickerDialog.show();
	}
	
	
	/**
	 * 时间监听
	 * @param button
	 * @param second
	 * @return
	 */
	public static OnTimeSetListener timeListener(final Button button,final int second){
		
		
		OnTimeSetListener otsl = new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				
				button.setText(hourOfDay + ":" + minute+":"+second);
			}
		};
			
		return otsl;
	}
	
	
}
