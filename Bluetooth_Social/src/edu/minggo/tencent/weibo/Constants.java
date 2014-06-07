package edu.minggo.tencent.weibo;


public class Constants {

	//腾讯所分配的APP_KEY
	public static final String CONSUMER_KEY = "801105818";
	//腾讯所分配的APP_SECRET
	public static final String CONSUMER_SECRET 	= "a07de74b551ef840fc21bd9e9b8f2c64";
	//用于获取未授权的request token
	public static final String REQUEST_URL = "http://open.t.qq.com/cgi-bin/request_token";
	//用于获取access token
	public static final String ACCESS_URL = "http://open.t.qq.com/cgi-bin/access_token";
	//用于对未授权的request token进行授权
	public static final String AUTHORIZE_URL = "http://open.t.qq.com/cgi-bin/authorize";
	
	public static final String ENCODING = "UTF-8";
	
	public static final String	OAUTH_CALLBACK_SCHEME = "x-oauthflow";
	public static final String	OAUTH_CALLBACK_HOST	= "callback";
	//回调地址
	public static final String	OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

	public class WeiBoAPI{
		//主页时间线
		public static final String HOME_TIME_LINE = "http://open.t.qq.com/api/statuses/home_timeline"; 
	}
	
}
