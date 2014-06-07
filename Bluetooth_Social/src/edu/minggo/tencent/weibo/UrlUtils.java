package edu.minggo.tencent.weibo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

public class UrlUtils {
	public static String buildQueryString(Map<String,String> map){
		List<NameValuePair> pairs = buildNameValuePairs(map);
		String queryStr = URLEncodedUtils.format(pairs, "UTF-8");
		return queryStr;
	}
	
	public static String buildUrlByQueryStringAndBaseUrl(String baseUrl,String queryString){
		return baseUrl + "?" + queryString;
	}
	
	public static String buildUrlByQueryStringMapAndBaseUrl(String baseUrl,Map<String,String> map){
		return buildUrlByQueryStringAndBaseUrl(baseUrl, buildQueryString(map));
	}
	
	public static List<NameValuePair> buildNameValuePairs(Map<String,String> map){
		Set<String> keySet = map.keySet();
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		for(String key: keySet){
			NameValuePair pair = new BasicNameValuePair(key, map.get(key));
			pairs.add(pair);
		}
		return pairs;
	}
}
