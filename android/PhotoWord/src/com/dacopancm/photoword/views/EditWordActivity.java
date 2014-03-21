package com.dacopancm.photoword.views;

import java.io.File;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;
import com.dacopancm.photoword.helpers.AlertDialogManager;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

public class EditWordActivity extends ActionBarActivity {

	ImageView picShow;
	String picturePath;
	EditText inword;
	final String TAG = "dcm-editword";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_word);

		// added by me
		Bundle bundle = getIntent().getExtras();
		picturePath = bundle.getString("imgsrc");
		Log.e(TAG, "editword picpath" + picturePath);
		// aignar vistas
		picShow = (ImageView) findViewById(R.id.imgView);
		Log.e(TAG, "editword picShow" + picShow);
		inword = (EditText) findViewById(R.id.inword);
		// mostrar imagen
		picShow.setImageBitmap(BitmapFactory.decodeFile(picturePath));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_word, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.menu_done: {
			launchTemplates();
			return true;
		}
		case R.id.menu_edit: {
			launchAviary();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Log.e("dcm",
					" ingrese activresult in EditWordActivity; requestCode "
							+ requestCode);
			if (requestCode == 300) {
				// output image path
				Uri mImageUri = data.getData();
				Log.e(TAG, "uri object saved aviary " + mImageUri);
				Log.e(TAG, "uri string saved aviary " + mImageUri.toString());
				Log.e(TAG, "uri path saved aviary " + mImageUri.getPath());
				Bundle extra = data.getExtras();
				Log.e(TAG, "aviary extra is " + extra);
				if (null != extra) {
					// image has been changed by the user?
					boolean changed = extra
							.getBoolean(Constants.EXTRA_OUT_BITMAP_CHANGED);
					Log.e(TAG, "aviary changed pic? " + changed);
					picShow.setImageBitmap(BitmapFactory
							.decodeFile(picturePath));
					picShow.invalidate();
				}

			}
		}
	}

	private void launchTemplates() {
		String wordx = inword.getText().toString();
		if (wordx != "" && !wordx.contains(" ")) {
			Intent templatesI = new Intent(this, TemplatesActivity.class);
			templatesI.putExtra("wordx", wordx);
			templatesI.putExtra("imgsrc", picturePath);
			startActivity(templatesI);
		} else {
			new AlertDialogManager().showAlertDialog(this,
					getString(R.string.error_title),
					getString(R.string.no_word), false);
		}

	}

	private void launchAviary() {
		String imageFileName = "tmpPhotoWord.jpg";
		File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		File image = new File(storageDir, imageFileName);

		Intent aviaryI = new Intent(this, FeatherActivity.class);
		// aviaryI.setData(Uri.parse("content://media/external/images/media/32705"));
		aviaryI.setData(Uri.fromFile(image));

		aviaryI.putExtra(Constants.EXTRA_IN_API_KEY_SECRET, "d8e0fa821fec31a9");
		aviaryI.putExtra(Constants.EXTRA_OUTPUT, Uri.fromFile(image));
		Log.e(TAG, "file can write? " + image.canWrite());
		startActivityForResult(aviaryI, 300);
		Log.e(TAG, "file can write? " + image.canWrite());
	}

}
