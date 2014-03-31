package com.dacopancm.photoword.views;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.dacopancm.photoword.helpers.Const;

public class LoginTwitterActivity extends Activity {
	public static final int TWITTER_LOGIN_RESULT_CODE_SUCCESS = 1111;
	public static final int TWITTER_LOGIN_RESULT_CODE_FAILURE = 2222;

	private static final String TAG = "LoginTwitterActivity";

	private WebView twitterLoginWebView;
	private AlertDialog mAlertBuilder;
	private static String twitterConsumerKey;
	private static String twitterConsumerSecret;

	private static Twitter twitter;
	private static RequestToken requestToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_twitter);
		twitterConsumerKey = Const.CONSUMER_KEY;
		twitterConsumerSecret = Const.CONSUMER_SECRET;

		if (twitterConsumerKey == null || twitterConsumerSecret == null) {
			Log.e(TAG, "ERROR: Consumer Key and Consumer Secret required!");
			LoginTwitterActivity.this
					.setResult(TWITTER_LOGIN_RESULT_CODE_FAILURE);
			LoginTwitterActivity.this.finish();
		}

		mAlertBuilder = new AlertDialog.Builder(this).create();
		mAlertBuilder.setCancelable(false);
		mAlertBuilder.setTitle(R.string.please_wait_title);
		View view = getLayoutInflater().inflate(R.layout.view_loading, null);
		((TextView) view.findViewById(R.id.messageTextViewFromLoading))
				.setText(getString(R.string.authenticating_your_app_message));
		mAlertBuilder.setView(view);
		mAlertBuilder.show();

		twitterLoginWebView = (WebView) findViewById(R.id.twitterLoginWebView);
		twitterLoginWebView.clearHistory();
		twitterLoginWebView.clearCache(true);

		twitterLoginWebView.setBackgroundColor(Color.TRANSPARENT);
		twitterLoginWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				if (url.contains(Const.CALLBACK_URL)) {
					Uri uri = Uri.parse(url);
					try {
						LoginTwitterActivity.this.saveAccessTokenAndFinish(uri);
					} catch (Exception ex) {
					}
					return true;
				}
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				if (mAlertBuilder != null) {
					mAlertBuilder.cancel();
				}
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

				if (mAlertBuilder != null) {
					mAlertBuilder.show();
				}
			}
		});

		Log.d(TAG, "Authorize....");
		askOAuth();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (mAlertBuilder != null) {
			mAlertBuilder.dismiss();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void saveAccessTokenAndFinish(final Uri uri) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String verifier = uri
						.getQueryParameter(Const.IEXTRA_OAUTH_VERIFIER);
				try {
					SharedPreferences sharedPrefs = getSharedPreferences(
							Const.PREFERENCE_NAME, Context.MODE_PRIVATE);
					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);
					Editor e = sharedPrefs.edit();
					e.putString(Const.PREF_KEY_TOKEN, accessToken.getToken());
					e.putString(Const.PREF_KEY_SECRET,
							accessToken.getTokenSecret());
					e.commit();

					Log.d(TAG,
							"TWITTER LOGIN SUCCESS ----TOKEN "
									+ accessToken.getToken());
					Log.d(TAG, "TWITTER LOGIN SUCCESS ----TOKEN SECRET "
							+ accessToken.getTokenSecret());
					LoginTwitterActivity.this
							.setResult(TWITTER_LOGIN_RESULT_CODE_SUCCESS);
				} catch (Exception e) {
					e.printStackTrace();
					if (e.getMessage() != null) {
						Log.e(TAG, e.getMessage());

					} else {
						Log.e(TAG, "ERROR: Twitter callback failed");
					}
					LoginTwitterActivity.this
							.setResult(TWITTER_LOGIN_RESULT_CODE_FAILURE);
				}
				LoginTwitterActivity.this.finish();
			}
		}).start();
	}

	public static boolean isActive(Context ctx) {
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(
				Const.PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Const.PREF_KEY_TOKEN, null) != null;
	}

	public static void logOutOfTwitter(Context ctx) {
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(
				Const.PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(Const.PREF_KEY_TOKEN, null);
		e.putString(Const.PREF_KEY_SECRET, null);
		e.commit();
	}

	public static String getAccessToken(Context ctx) {
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(
				Const.PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Const.PREF_KEY_TOKEN, null);
	}

	public static String getAccessTokenSecret(Context ctx) {
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(
				Const.PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Const.PREF_KEY_SECRET, null);
	}

	private void askOAuth() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(twitterConsumerKey);
		configurationBuilder.setOAuthConsumerSecret(twitterConsumerSecret);
		Configuration configuration = configurationBuilder.build();
		twitter = new TwitterFactory(configuration).getInstance();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					requestToken = twitter
							.getOAuthRequestToken(Const.CALLBACK_URL);
				} catch (Exception e) {
					final String errorString = e.toString();
					LoginTwitterActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mAlertBuilder.cancel();
							Toast.makeText(LoginTwitterActivity.this,
									errorString.toString(), Toast.LENGTH_SHORT)
									.show();
							finish();
						}
					});
					return;
				}

				LoginTwitterActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						twitterLoginWebView.loadUrl(requestToken
								.getAuthenticationURL());
					}
				});
			}
		}).start();
	}

}