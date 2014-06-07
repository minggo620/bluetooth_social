// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   CommonsHttpOAuthProvider.java

package edu.minggo.tencent.weibo;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

// Referenced classes of package oauth.signpost.commonshttp:
//            HttpRequestAdapter, HttpResponseAdapter

public class CommonsHttpOAuthProvider extends AbstractOAuthProvider {

	public CommonsHttpOAuthProvider(String requestTokenEndpointUrl,
			String accessTokenEndpointUrl, String authorizationWebsiteUrl) {
		super(requestTokenEndpointUrl, accessTokenEndpointUrl,
				authorizationWebsiteUrl);
		httpClient = new DefaultHttpClient();
	}

	public CommonsHttpOAuthProvider(String requestTokenEndpointUrl,
			String accessTokenEndpointUrl, String authorizationWebsiteUrl,
			HttpClient httpClient) {
		super(requestTokenEndpointUrl, accessTokenEndpointUrl,
				authorizationWebsiteUrl);
		this.httpClient = httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	protected HttpRequest createRequest(String endpointUrl) throws Exception {
		HttpPost request = new HttpPost(endpointUrl);
		request.addHeader("Content-Type", "application/x-www-form-urlencoded");
		return new HttpRequestAdapter(request);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected HttpResponse sendRequest(HttpRequest request) throws Exception {
		Map headers = request.getAllHeaders();
		String oauthHeader = (String) headers.get("Authorization");
		headers.remove("Authorization");
		HttpParameters httpParams = OAuth.oauthHeaderToParamsMap(oauthHeader);
		List formParams = new ArrayList();
		Set keys = httpParams.keySet();
		String key;
		String value;
		for (Iterator iterator = keys.iterator(); iterator.hasNext(); formParams
				.add(new BasicNameValuePair(key, URLDecoder.decode(value,
						"UTF-8")))) {
			key = (String) iterator.next();
			value = httpParams.getFirst(key);
			/*
			 * if(key.equals("oauth_timestamp")){ long longValue =
			 * Integer.parseInt(value); longValue = longValue - 8 * 60 * 60;
			 * value = new Long(longValue).toString(); }
			 */
		}

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams,
				"UTF-8");
		HttpPost postRequest = (HttpPost) request.unwrap();
		postRequest.setEntity(entity);
		org.apache.http.HttpResponse response = httpClient.execute(postRequest);
		return new HttpResponseAdapter(response);
	}

	protected void closeConnection(HttpRequest request, HttpResponse response)
			throws Exception {
		if (response != null) {
			HttpEntity entity = ((org.apache.http.HttpResponse) response
					.unwrap()).getEntity();
			if (entity != null)
				try {
					entity.consumeContent();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private static final long serialVersionUID = 1L;
	private transient HttpClient httpClient;
}
