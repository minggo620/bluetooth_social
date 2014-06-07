package edu.minggo.tencent.weibo;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {
	//key1=value1,key2=value2........
	public static Map<String,String> parseMapFromString(String baseString) {
		String [] arr = baseString.split(",");
		Map<String,String > result = new HashMap<String,String>();
		for (int i = 0; i < arr.length; i++) {
			String temp [] = arr[i].split("=");
			String key = temp[0].trim();
			String value = temp[1].substring(1, temp[1].length()-1);
			result.put(key, value);
		}
		return result;
	}
}
