package edu.minggo.tencent.weibo;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
/**
 * 腾讯微博OAuthRequestToken请求
 * @author minggo
 * @created 2013-3-1下午01:49:18
 */
public class OAuthRequestTokenTask extends AsyncTask<Void, Void, Void>{

	private Context context;
	private OAuthConsumer consumer;
	private OAuthProvider provider;
	
	public OAuthRequestTokenTask(Context context, OAuthConsumer consumer,
			OAuthProvider provider) {
		super();
		this.context = context;
		this.consumer = consumer;
		this.provider = provider;
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			System.out.println("请求Request Token之前" + consumer.getToken());
			final String url = provider.retrieveRequestToken(consumer, Constants.OAUTH_CALLBACK_URL);
			System.out.println("请求Request Token之后" + consumer.getToken());
			System.out.println("url---->" + url);
			Uri uri = Uri.parse(url);
			
			Intent intent = new Intent(Intent.ACTION_VIEW, uri).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
			context.startActivity(intent);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
