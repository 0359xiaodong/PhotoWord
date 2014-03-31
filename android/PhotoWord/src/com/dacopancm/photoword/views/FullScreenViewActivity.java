package com.dacopancm.photoword.views;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dacopancm.photoword.adapters.FullScreenImageAdapter;
import com.dacopancm.photoword.helpers.AlertDialogManager;
import com.dacopancm.photoword.helpers.Const;
import com.dacopancm.photoword.helpers.FileUtilities;

public class FullScreenViewActivity extends ActionBarActivity {
	private ViewPager viewPager;
	private FullScreenImageAdapter adapter;
	//
	int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen_view);

		viewPager = (ViewPager) findViewById(R.id.pager);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.containsKey("filename")) {
			position = bundle.getInt("position", 0);
		}
		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
				FileUtilities.getFilePaths(this));

		viewPager.setAdapter(adapter);

		// displaying selected image first
		viewPager.setCurrentItem(position);

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
			launchShare();
			return true;
		}
		case R.id.menu_del: {
			DialogoDelPic dialogo = new DialogoDelPic();
			dialogo.show(getSupportFragmentManager(), "tagAlerta");
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void launchShare() {

		String filepath = adapter._imagePaths.get(viewPager.getCurrentItem());
		String filename = new File(filepath).getName();
		String wordx = filename.split("_")[2];

		Intent IShare = new Intent(this, SharexActivity.class);
		IShare.putExtra("filename", filepath);
		IShare.putExtra("wordx", wordx);
		IShare.putExtra("wordres", " ");
		startActivityForResult(IShare, Const.GO_BACK_REQUEST);

	}

	private void delPic() {
		if (FileUtilities.isSdCardReady()) {
			String filepath = adapter._imagePaths.get(viewPager
					.getCurrentItem());
			File file = new File(filepath);
			if (file.exists() ? file.delete() : false)
				finish();

		} else {
			new AlertDialogManager().showAlertDialog(this,
					getString(R.string.error_title),
					getString(R.string.no_sdcard), false);
		}
	}

	private void cancelShare() {
		finish();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.fullscreen_view, menu);
		return true;
	}

	public static class DialogoDelPic extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			builder.setMessage(getString(R.string.del_pic_dlg))
					.setTitle("PhotoWord.")
					.setPositiveButton("Aceptar",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									Log.i("Dialogos", "Confirmacion Aceptada.");
									((FullScreenViewActivity) getActivity())
											.delPic();
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