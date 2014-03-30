package com.dacopancm.photoword.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.dacopancm.photoword.views.R;

public class AlertDialogManager {
	/**
	 * Function to display simple Alert Dialog
	 * 
	 * @param context
	 *            - application context
	 * @param title
	 *            - alert dialog title
	 * @param message
	 *            - alert message
	 * @param status
	 *            - success/failure (used to set icon) - pass null if you don't
	 *            want icon
	 * */
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		if (status != null)
			// Setting alert dialog icon
			alertDialog.setIcon((status) ? R.drawable.ic_action_about
					: R.drawable.ic_action_error);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Log.i("Dialogos", "Confirmacion Cancelada.");
						dialog.cancel();
					}
				});
		// Setting OK Button

		// Showing Alert Message
		alertDialog.show();
	}
}
