package edu.minggo.tencent.weibo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * 主要是发表微博
 * @author minggo
 * @created 2013-2-28上午02:41:13
 */
public class WeiBoClient {
	private OAuthConsumer consumer;
	
	public WeiBoClient(String consumer_key,String consumer_key_secret,String oauth_token,String oauth_token_secret){
		System.out.println("oauth_token:"+oauth_token+";"+"oauth_token_secret:"+oauth_token_secret );
		consumer = new CommonsHttpOAuthConsumer(consumer_key, consumer_key_secret);
		consumer.setTokenWithSecret(oauth_token, oauth_token_secret);
	}
	
	@SuppressWarnings("static-access")
	public String doPost(String url,Map<String,String> addtionalParams,List<String> decodeNames){
		
		consumer = new OAuthUtils().addAddtionalParametersFromMap(consumer, addtionalParams);
		HttpPost postRequest = new HttpPost(url);
		try {
			//对请求进行签名（将认证信息和微博数据一起交给consumer对象）
			consumer.sign(postRequest);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		//腾讯微博不支持Header发送数据，而Oauth是使用Header传送数据，所以需从 Header取出数据
		Header oauthHeader = postRequest.getFirstHeader("Authorization");
		String baseString = oauthHeader.getValue().substring(5);
		
		Map<String,String> oauthMap = StringUtils.parseMapFromString(baseString);
		oauthMap = HttpUtils.decodeByDecodeNames(decodeNames, oauthMap);
		
		
		addtionalParams = HttpUtils.decodeByDecodeNames(decodeNames, addtionalParams);
		
		
		List<NameValuePair> pairs = ApacheUtils.convertMapToNameValuePairs(oauthMap);
		List<NameValuePair> weiBoPairs = ApacheUtils.convertMapToNameValuePairs(addtionalParams);
		pairs.addAll(weiBoPairs);
		
		
		HttpEntity entity = null;
		HttpResponse response = null;
		try {
			entity =  new UrlEncodedFormEntity(pairs,"UTF-8");
			postRequest.setEntity(entity);
			response = new DefaultHttpClient().execute(postRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
			String line = "";
			while((line = reader.readLine()) != null){
				System.out.println(line);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		String result = ApacheUtils.getResponseText(response);
		return result;
	}
	
	public String doGet(String url,Map<String,String> addtionalParams){
		url = UrlUtils.buildUrlByQueryStringMapAndBaseUrl(url, addtionalParams);
		try {
			//对请求进行签名（将认证信息和微博数据一起交给consumer对象）
			System.out.println("签名之前的URL-->"+url);
			url = consumer.sign(url);
			System.out.println("签名之后的url-->"+url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpGet getRequest =  new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = null;
		try {
			response =  httpClient.execute(getRequest);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ApacheUtils.getResponseText(response);
	}
	
	
}
