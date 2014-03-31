package com.dacopancm.photoword.helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class FileUtilities {

	public static File createImageFile(Context context, String imageFileName)
			throws IOException {
		if (isSdCardReady()) {
			File storageDir = context
					.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

			File older = new File(storageDir, imageFileName);
			if (older.exists()) {
				older.delete();
			}
			File image = new File(storageDir, imageFileName);
			image.createNewFile();
			// Save a file: path for use with ACTION_VIEW intents
			// mCurrentPhotoPath = "file:" + image.getAbsolutePath();
			Log.e("dcm", "image dir 1 take photo " + image.getAbsolutePath()
					+ " exist? " + image.exists());
			return image;
		} else {
			return null;
		}
	}

	public static boolean isSdCardReady() {
		// Comprobamos el estado de la memoria externa (tarjeta SD)
		String estado = Environment.getExternalStorageState();
		return (estado.equals(Environment.MEDIA_MOUNTED) && !estado
				.equals(Environment.MEDIA_MOUNTED_READ_ONLY));
	}

	public static File createFavFile(Context context, String imageFileName)
			throws IOException {
		if (isSdCardReady()) {
			File storageDir = context
					.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
			File albumDir = new File(storageDir, Const.FAVS_ALBUM_NAME);
			albumDir.mkdir();

			File older = new File(albumDir, imageFileName);
			if (older.exists()) {
				older.delete();
			}
			File image = new File(albumDir, imageFileName);
			image.createNewFile();
			// Save a file: path for use with ACTION_VIEW intents
			// mCurrentPhotoPath = "file:" + image.getAbsolutePath();
			Log.e("dcm",
					"image dir 2 Fav File photo " + image.getAbsolutePath()
							+ " exist? " + image.exists());
			return image;
		} else {
			return null;
		}
	}

	public static Bitmap getBitmapFromView(View view) {
		Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),
				view.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(returnedBitmap);
		Drawable bgDrawable = view.getBackground();
		if (bgDrawable != null)
			bgDrawable.draw(canvas);
		/*
		 * else canvas.drawColor(Color.WHITE);
		 */
		view.draw(canvas);
		return returnedBitmap;
	}

	// GALLERY UTILS
	// Reading file paths from SDCard
	public static ArrayList<String> getFilePaths(Context _context) {
		ArrayList<String> filePaths = new ArrayList<String>();
		if (isSdCardReady()) {
			File directory = new File(
					_context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
							+ File.separator + Const.FAVS_ALBUM_NAME);

			// getting list of file paths
			File[] listFiles = directory.listFiles();

			// Check for count
			if (listFiles != null && listFiles.length > 0) {

				// loop through all files
				for (int i = 0; i < listFiles.length; i++) {

					// get file path
					String filePath = listFiles[i].getAbsolutePath();

					// check for supported file extension
					if (IsSupportedFile(filePath)) {
						// Add image path to array list
						filePaths.add(filePath);
					}
				}
			}
		}
		return filePaths;
	}

	// Check supported file extensions
	private static boolean IsSupportedFile(String filePath) {
		String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
				filePath.length());
		if (Const.FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault())))
			return true;
		else
			return false;

	}

	/*
	 * getting screen width
	 */
	public static int getScreenWidth(Context _context) {
		int columnWidth;
		WindowManager wm = (WindowManager) _context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		final Point point = new Point();
		try {
			display.getSize(point);
		} catch (java.lang.NoSuchMethodError ignore) { // Older device
			/*
			 * point.x = display.getWidth(); point.y = display.getHeight();
			 */
			display.getSize(point);
		}
		columnWidth = point.x;
		return columnWidth;
	}
}
