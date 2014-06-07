package edu.minggo.chat.util;
/**
 * 计算消息实体byte长度转化字符串（eg：00000018）
 * @author minggo
 * @created 2013-2-21下午06:33:41
 */
public class MessageLen {
	/**
	 * 获取8位的整形长度字符串
	 * @param i
	 * @return
	 */
	public static String getLenght(byte[] i) {
		int len = i.length;
		int j = len/10;
		if(j==0) {
			return "0000000"+len;
		}else if(0<j&&j<10){
			return "000000"+len;
		}else if (10<=j&&j<100) {
			return "00000"+len;
		}else if (10<=j&&j<100) {
			return "00000"+len;
		}else if (100<=j&&j<1000) {
			return "0000"+len;
		}else if (1000<=j&&j<10000) {
			return "000"+len;
		}else if (10000<=j&&j<100000) {
			return "00"+len;
		}else if (100000<=j&&j<1000000) {
			return "0"+len;
		}else if (1000000<=j&&j<10000000) {
			return ""+len;
		}else{
			return "";
		}
	}
	/**
	 * 将录音时间转化成长度为3的字符串（eg:009）
	 * @param recordTime
	 * @return
	 */
	public static String getRecordTime(int recordTime){
		
		int j = recordTime/10;
		if(j==0) {
			return "00"+recordTime;
		}else if(0<j&&j<10){
			return "0"+recordTime;
		}else if (10<=j&&j<100) {
			return ""+recordTime;
		}else{
			return "";
		}
	}
}
