/**
 * 
 */
package com.dacopancm.photoword.views;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dacopancm.photoword.helpers.AlertDialogManager;
import com.dacopancm.photoword.helpers.ConnectionDetector;
import com.dacopancm.photoword.helpers.FileUtilities;
import com.dacopancm.photoword.twitter.TwitterHelper;
import com.dacopancm.photoword.twitter.TwitterHelper.TwitterCallback;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;

/**
 * @author DarwinAlejandro
 * 
 */
public class SharexActivity extends ActionBarActivity {
	private static final String TAG = "dcm-sahrex";
	private static int MAX_LENGTH = 123;
	String textosh;
	String wordx;
	String filename = "";
	boolean fcompleted = true;
	boolean tcompleted = true;
	// UI CONTROLS
	private CheckBox tcheck;
	private CheckBox facecheck;
	private CheckBox addfav;
	private EditText bubble;
	private TextView countx;
	// FACEBOOK
	private Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharex);
		Bundle bundle = getIntent().getExtras();

		if (bundle != null && bundle.containsKey("filename")) {
			filename = bundle.getString("filename");
			textosh = "#PhotoWord " + getString(R.string.theme_0_1) + " "
					+ bundle.getString("wordx") + " "
					+ bundle.getString("wordres");
			wordx = bundle.getString("wordx");
		}

		// find Ui Controls on View

		facecheck = (CheckBox) findViewById(R.id.facecheck);
		tcheck = (CheckBox) findViewById(R.id.tcheckx);
		addfav = (CheckBox) findViewById(R.id.addfav);
		bubble = (EditText) findViewById(R.id.bubble);
		countx = (TextView) findViewById(R.id.countx);
		// prepare facebook
		prepareFacebook(savedInstanceState);
		// chequear redes accestokens
		Log.e(TAG, "tcheck is: " + tcheck + " facecheck is: " + facecheck);
		tcheck.setEnabled(checkTwitter());
		facecheck.setEnabled(checkFacebook());
		// llenar edittext bubble
		bubble.setText(textosh);
		countx.setText(textosh.length() + "/" + MAX_LENGTH);
		checkTextToShare();
		// eventos del check
		tcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					MAX_LENGTH = 123;
					if (bubble.getText().toString().length() > MAX_LENGTH) {
						FragmentManager fragmentManager = getSupportFragmentManager();
						DialogoCortarTexto dialogo = new DialogoCortarTexto();
						dialogo.show(fragmentManager, "tagAlerta");
					}

				} else
					MAX_LENGTH = 350;

			}
		});
		// check true por defecto si estan habilitados
		tcheck.setChecked(tcheck.isEnabled());
		facecheck.setChecked(facecheck.isEnabled());
		bubble.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				countx.setText(s.length() + "/" + MAX_LENGTH);
			}
		});
	}

	private void checkTextToShare() {
		String text = bubble.getText().toString();
		String _answer = "";
		for (String _r : text.split(" ")) {
			if ((_answer.length() + _r.length() + 2) < MAX_LENGTH)
				_answer += _r + " ";
		}
		bubble.setText(_answer);
	}

	private void prepareFacebook(Bundle savedInstanceState) {

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
		Session session = Session.getActiveSession();
		if (session == null) {
			if (savedInstanceState != null) {
				session = Session.restoreSession(this, null, statusCallback,
						savedInstanceState);
			}

			if (session == null) {
				session = new Session(this);
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
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.menu_cancel: {
			cancelShare();
			return true;
		}
		case R.id.menu_share: {
			shareTF();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.sharex, menu);
		return true;
	}

	private void shareTF() {
		if (!new ConnectionDetector(this).isConnectingToInternet()) {
			new AlertDialogManager().showAlertDialog(this,
					getString(R.string.error_title),
					getString(R.string.no_net), false);
			return;
		}
		if (tcheck.isChecked()
				&& bubble.getText().toString().length() > MAX_LENGTH) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			DialogoCortarTexto dialogo = new DialogoCortarTexto();
			dialogo.show(fragmentManager, "tagAlerta");
			return;
		}
		if (tcheck.isChecked()) {
			shareTwitter();
		}
		if (facecheck.isChecked()) {
			shareFacebook();
		}
	}

	private void shareFacebook() {
		fcompleted = false;
		try {
			//
			if (checkPermissions()) {
				Request.Callback callback = new Request.Callback() {
					public void onCompleted(com.facebook.Response response) {
						fcompleted = true;

						FacebookRequestError error = response.getError();
						Log.e(TAG, "Request.Callback error: "
								+ (error != null ? error.getErrorMessage()
										: "no error"));
						if (error != null)
							Toast.makeText(getApplicationContext(),
									error.getErrorMessage(), Toast.LENGTH_SHORT)
									.show();
						else
							Toast.makeText(getApplicationContext(),
									"Posted successfully.", Toast.LENGTH_LONG)
									.show();
						finallyShared();
					}
				};
				// prepare file

				Bitmap bi = BitmapFactory.decodeFile(filename);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bi.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] data = baos.toByteArray();
				//
				Log.e(TAG, "getsession share facebook");
				Session session = Session.getActiveSession();
				Log.e(TAG, "get session completed-share facebook");
				Bundle postParams = new Bundle();

				postParams.putString("name", bubble.getText().toString());
				// postParams.putString("link", "http://www.stackoverflow.com");
				postParams.putString("description", "PhotoWord");
				postParams.putString("caption", bubble.getText().toString());
				postParams.putByteArray("picture", data);
				Request request = new Request(session, "me/photos", postParams,
						HttpMethod.POST, callback);

				RequestAsyncTask task = new RequestAsyncTask(request);
				task.execute();

			} else {
				Log.e(TAG, "shareFacebook: requesting permissions ");
				requestPermissions();
			}
			//
		} catch (Exception ex) {
			fcompleted = true;
			finallyShared();
			Log.e(TAG, "sharefacebook error: " + ex.getMessage());
		}

	}

	private void shareTwitter() {
		tcompleted = false;
		try {
			//
			TwitterHelper.postToTwitterWithImage(this, ((Activity) this),
					filename, bubble.getText().toString(),
					new TwitterCallback() {
						@Override
						public void onFinsihed(Boolean response) {
							Log.d(TAG,
									"---------------- twitter response----------------"
											+ response);
							Toast.makeText(
									SharexActivity.this,
									getString(R.string.image_posted_on_twitter),
									Toast.LENGTH_LONG).show();
							tcompleted = true;
							finallyShared();
							Log.e(TAG, "twitter finalizado tcompleted: "
									+ tcompleted);
						}
					});
			//
		} catch (Exception ex) {
			tcompleted = true;
			finallyShared();
			Log.e(TAG, "shareTwitter error: " + ex.getMessage());
		}
	}

	private void finallyShared() {
		if (tcompleted && fcompleted) {
			if (addfav.isChecked()) {
				// agrego a favoritos
				// save pic selected to my constant file
				FileOutputStream out = null;
				try {
					Bitmap thumbnail = (BitmapFactory.decodeFile(filename));

					out = new FileOutputStream(FileUtilities.createFavFile(
							this, createFavName()).getAbsolutePath());
					thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
				} catch (Exception e) {
					Log.e("dcm", "No save Favorito");
					new AlertDialogManager().showAlertDialog(this,
							getString(R.string.error_title),
							getString(R.string.error_no_save_fav), false);
				} finally {
					try {
						out.close();
					} catch (Throwable ignore) {
						Log.e("dcm", "No save Favorito");
					}
				}
			} else {
			}
		}
	}

	/**
	 * Crea un nombre para guardar en favoritos
	 * 
	 * @return String nombre del archivo favorito
	 */
	private String createFavName() {
		String name = "";
		int x = new Random().nextInt(100 - 2 + 1) + 2;
		name = "PHOTOWORD_fav_" + wordx + "_" + x + ".jpg";
		return name;
	}

	private void cancelShare() {
		// TODO Auto-generated method stub

	}

	protected boolean checkTwitter() {
		// aqui chequear o deschequear twitterSwitch
		if (LoginTwitterActivity.isActive(this)) {

			Log.e(TAG,
					"twitter is active? " + LoginTwitterActivity.isActive(this)
							+ " accesstoken is: "
							+ LoginTwitterActivity.getAccessToken(this));
			return true;
		}
		return false;
	}

	private boolean checkFacebook() {

		Session session = Session.getActiveSession();
		Log.e(TAG, "checkfacebook, session is: " + session.isOpened());
		if (session.isOpened()) {

			return checkPermissions();
		}
		return false;

		// return checkPermissions();
	}

	public boolean checkPermissions() {

		Session s = Session.getActiveSession();
		if (s != null && s.isOpened()) {
			Log.e(TAG, "checkPermissions: " + s.getPermissions());
			boolean b = s.getPermissions().contains("publish_actions");
			Log.e(TAG, "checkPermissions is: " + b);
			return b;
		} else
			return false;

		// return true;
	}

	public void requestPermissions() {
		Session s = Session.getActiveSession();
		if (s != null)
			s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
					this, PERMISSIONS));
	}

	private class SessionStatusCallback implements Session.StatusCallback {

		@Override
		public void call(Session session, SessionState state,
				Exception exception) { // facebookSwitch.setChecked(checkFacebook());
			Log.e(TAG, "session status callback sharex activity"); // temp
																	// checkFacebook();
																	// }
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
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		// uiHelper.onSaveInstanceState(savedState);
		Session session = Session.getActiveSession();
		Session.saveSession(session, savedState);
	}

	public static class DialogoCortarTexto extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setMessage(getString(R.string.texto_largo))
					.setTitle("PhotoWord.")
					.setPositiveButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Log.i("Dialogos", "Confirmacion Aceptada.");
									((SharexActivity) getActivity())
											.checkTextToShare();
									dialog.cancel();
								}
							})
					.setNegativeButton("Cancelar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Log.i("Dialogos", "Confirmacion Cancelada.");
									dialog.cancel();
								}
							});

			return builder.create();
		}
	}
}
