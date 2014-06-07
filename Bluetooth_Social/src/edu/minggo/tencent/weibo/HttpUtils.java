package edu.minggo.tencent.weibo;

import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpUtils {
	public static Map<String,String> decodeByDecodeNames(List<String> decodeNames,Map<String,String> map){
		Set<String> keys = map.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = map.get(key);
			for(String decodeName : decodeNames){
				if(key.equals(decodeName)){
					value = URLDecoder.decode(value);
					map.put(key, value);
				}
			}
		}
		return map;
	}
}
