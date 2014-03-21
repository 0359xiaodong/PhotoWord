package com.dacopancm.photoword.views;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.dacopancm.photoword.helpers.AlertDialogManager;
import com.dacopancm.photoword.helpers.ConnectionDetector;
import com.dacopancm.photoword.helpers.Const;
import com.facebook.LoggingBehavior;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

public class SettingsFragment extends Fragment {
	private Switch twitterSwitch;
	private Switch facebookSwitch;
	private TextView messtwitter;
	private TextView messfacebook;
	// SharedPreferences.Editor prefs;

	// twitter
	private Context context;
	private static final String TAG = "dcm-SettingsFrgament";
	// facebook
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "settings oncreateview");
		// Inflate the layout for this fragment

		View view = inflater.inflate(R.layout.fragment_settings, container,
				false);
		facebookSwitch = (Switch) view.findViewById(R.id.facebookSwitch);
		messtwitter = (TextView) view.findViewById(R.id.messtwitter);
		messfacebook = (TextView) view.findViewById(R.id.messfacebook);
		twitterSwitch = (Switch) view.findViewById(R.id.twitterSwitch);
		context = getActivity();

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(getActivity(), null,
						statusCallback, savedInstanceState);
			}

			if (session == null) {
				session = new Session(getActivity());
			}
			Session.setActiveSession(session);
			if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
				session.openForPublish(new Session.OpenRequest(this)
						.setPermissions(
								Arrays.asList("publish_actions",
										"publish_stream")).setCallback(
								statusCallback));
			}
		}
		twitterSwitch.setChecked(checkTwitter());
		facebookSwitch.setChecked(checkFacebook());
		facebookSwitch
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean isChecked) {
						if (isChecked) {
							if (new ConnectionDetector(getActivity())
									.isConnectingToInternet()) {
								facebookLogin();
							} else {
								new AlertDialogManager().showAlertDialog(
										getActivity(),
										getString(R.string.error_title),
										getString(R.string.no_net), false);
							}
						} else {
							facebookLogout();
						}
					}
				});
		twitterSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							try {
								if (new ConnectionDetector(getActivity())
										.isConnectingToInternet()) {
									// getActivity().
									startActivityForResult(new Intent(context,
											LoginTwitterActivity.class),
											Const.LOGIN_TWITTER_REQUEST);
								} else {
									new AlertDialogManager().showAlertDialog(
											getActivity(),
											getString(R.string.error_title),
											getString(R.string.no_net), false);
								}
							} catch (Exception ex) {
							}
						} else {
							LoginTwitterActivity.logOutOfTwitter(context);
							Log.e(TAG, "twitter logout");
							checkTwitter();
						}
					}
				});
		// TEMP
		/*
		 * PackageInfo packageInfo; try { packageInfo =
		 * getActivity().getPackageManager().getPackageInfo(
		 * "com.dacopancm.photoword.views", PackageManager.GET_SIGNATURES); for
		 * (Signature signature : packageInfo.signatures) { MessageDigest md =
		 * MessageDigest.getInstance("SHA"); md.update(signature.toByteArray());
		 * String key = new String(Base64.encode(md.digest(), 0)); // String key
		 * = new String(Base64.encodeBytes(md.digest())); Log.e("Hash key",
		 * key); } } catch (NameNotFoundException e1) { Log.e("Name not found",
		 * e1.toString()); }
		 * 
		 * catch (NoSuchAlgorithmException e) { Log.e("No such an algorithm",
		 * e.toString()); } catch (Exception e) { Log.e("Exception",
		 * e.toString()); } // TEMP
		 */
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.e(TAG, "settings onactivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	protected boolean checkTwitter() {
		// aqui chequear o deschequear twitterSwitch
		if (LoginTwitterActivity.isActive(context)) {
			messtwitter.setText(getString(R.string.si_config));
			Log.e(TAG,
					"twitter is active? "
							+ LoginTwitterActivity.isActive(context)
							+ " accesstoken is: "
							+ LoginTwitterActivity.getAccessToken(context));
			return true;
		} else {
			messtwitter.setText(getString(R.string.no_config));
		}
		return false;
	}

	private boolean checkFacebook() {
		Session session = Session.getActiveSession();
		Log.e(TAG, "checkfacebook, session is: " + session.isOpened());
		if (session.isOpened() && checkPermissions()) {
			messfacebook.setText(getString(R.string.si_config));
			return true;
		}
		messfacebook.setText(getString(R.string.no_config));
		return false;
	}

	public boolean checkPermissions() {

		Session s = Session.getActiveSession();
		if (s != null) {
			Log.e(TAG, "checkPermissions: " + s.getPermissions());
			boolean b = s.getPermissions().contains("publish_actions");
			Log.e(TAG, "checkPermissions is: " + b);
			return b;
		} else
			return false;

		// return true;
	}

	private boolean requestingPermissions = false;

	public void requestPermissions() {

		Session s = Session.getActiveSession();
		if (s != null && s.isOpened() && !checkPermissions()) {
			requestingPermissions = true;
			Log.e(TAG, "requesting permissions");
			s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
					this, Arrays.asList("publish_actions", "publish_stream"))
					.setCallback(statusCallback));
		}
	}

	private void facebookLogout() {
		Session session = Session.getActiveSession();
		if (!session.isClosed()) {
			session.closeAndClearTokenInformation();
			requestingPermissions = false;
		}
		Log.e(TAG, "facebook logout");
	}

	private void facebookLogin() {
		Session session = Session.getActiveSession();

		if (!session.isOpened() && !session.isClosed()) {
			Log.e(TAG, "facebookLogin() open for publish");
			session.openForPublish(new Session.OpenRequest(this)
					.setPermissions(
							Arrays.asList("publish_actions", "publish_stream"))
					.setCallback(statusCallback));
		} else {
			try {
				Log.e(TAG, "facebookLogin open login dialog");
				Session.openActiveSession(getActivity(), true, statusCallback);
				requestingPermissions = false;
			} catch (Exception ex) {
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		Session.getActiveSession().addCallback(statusCallback);
	}

	@Override
	public void onStop() {
		super.onStop();
		Session.getActiveSession().removeCallback(statusCallback);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e(TAG, "onactivityresult settings fragment");
		super.onActivityResult(requestCode, resultCode, data);
		Log.e(TAG, "onactivityresult settings fragment2");
		if (requestCode == Const.LOGIN_TWITTER_REQUEST) {
			checkTwitter();
			return;
		}
		// FACEBOOK
		Session.getActiveSession().onActivityResult(getActivity(), requestCode,
				resultCode, data);
		Session s = Session.getActiveSession();
		if (s != null && s.isOpened() && !checkPermissions()
				&& !requestingPermissions) {
			requestPermissions();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, outState);
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// facebookSwitch.setChecked(checkFacebook());
			Log.e(TAG, "session status callback settings fragment");
			// temp
			checkFacebook();
		}
	}

	public void onTwitterResult(int requestCode, int resultCode, Intent data) {

		/*
		 * Log.e(TAG, "entre settingsfragment acivity result; " + " " +
		 * requestCode + " " + resultCode);
		 * 
		 * if (requestCode == Const.LOGIN_TWITTER_REQUEST) { if (resultCode ==
		 * Const.TWITTER_LOGIN_RESULT_CODE_SUCCESS) {
		 * twitterSwitch.setChecked(true); } else {
		 * twitterSwitch.setChecked(false); } }
		 */

	}
}
