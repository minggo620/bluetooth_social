package edu.minggo.tencent.weibo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ApacheUtils {
	public static List<NameValuePair> convertMapToNameValuePairs(Map<String, String> oauthMap) {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		Set<String> keys = oauthMap.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = oauthMap.get(key);
			NameValuePair pair = new BasicNameValuePair(key, value);
			pairs.add(pair);
		}

		return pairs;
	}
	public static String getResponseText(HttpResponse response) {
		HttpEntity responseEntity = response.getEntity();
		InputStream input = null;
		String result = null;
		try {
			input = responseEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line = reader.readLine()) != null){
				sb.append(line);
			}
			result = sb.toString();
//			System.out.println("reuslt---->" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static String parseStringFromEntity(HttpEntity entity){
		String result = null;
		try{
			InputStream input = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line = null;
			StringBuffer sb = new StringBuffer();
			while((line = reader.readLine()) != null){
				sb.append(line);
			}
			result = sb.toString();
		}
		catch(Exception e){
			System.out.println(e);
		}
		return result;
	}
}
