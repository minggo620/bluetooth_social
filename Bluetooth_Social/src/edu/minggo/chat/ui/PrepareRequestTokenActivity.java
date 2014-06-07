package edu.minggo.chat.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import edu.minggo.tencent.weibo.CommonsHttpOAuthConsumer;
import edu.minggo.tencent.weibo.CommonsHttpOAuthProvider;
import edu.minggo.tencent.weibo.Constants;
import edu.minggo.tencent.weibo.OAuthConsumer;
import edu.minggo.tencent.weibo.OAuthProvider;
import edu.minggo.tencent.weibo.OAuthRequestTokenTask;
import edu.minggo.tencent.weibo.RetrieveAccessTokenTask;
/**
 * RequestToken请求前的准备
 * @author minggo
 * @created 2013-3-1下午01:51:43
 */
public class PrepareRequestTokenActivity extends Activity {

	private OAuthConsumer consumer;
	private OAuthProvider provider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		System.setProperty("debug", "true");
		consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY,
				Constants.CONSUMER_SECRET);
		provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL,
				Constants.ACCESS_URL, Constants.AUTHORIZE_URL);
		new OAuthRequestTokenTask(this, consumer, provider).execute();
	}

	//在AndroidManifest.xml文件中作了android:launchMode="singleTask"配置，所以
	//当执行回调url，Activity第二次启动启动时，不执行onCreate方法而是直接执行此方法
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//主要用于存取简单 的键值对
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		final Uri uri = intent.getData();
		System.out.println("uri--->"+uri.toString());
		if (uri != null
				&& uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			//获取Access_Token，并将Access_Token放到prefs中
			new RetrieveAccessTokenTask(this, consumer, provider, prefs)
					.execute(uri);
			finish();
		}
	}
}
