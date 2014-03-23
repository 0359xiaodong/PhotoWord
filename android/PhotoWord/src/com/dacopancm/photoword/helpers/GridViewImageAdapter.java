package com.dacopancm.photoword.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.dacopancm.photoword.views.FullScreenViewActivity;
import com.dacopancm.photoword.views.R;

public class GridViewImageAdapter extends ArrayAdapter<String> {
	private int imageWidth;
	Activity _activity;

	public GridViewImageAdapter(Activity activity, ArrayList<String> filePaths,
			int imageWidth) {
		//
		super(activity, 0, filePaths);
		Log.e("GridViewImageAdapter", "GridViewImageAdapter constructor");
		this._activity = activity;
		this.imageWidth = imageWidth;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View item = convertView;
		Log.e("GridViewImageAdapter", "getView called, convertView is: "
				+ convertView + " position is: " + position);
		ViewHolder holder;
		if (item == null) {
			LayoutInflater inflater = _activity.getLayoutInflater();
			item = inflater.inflate(R.layout.layout_grid_cell, null);
			holder = new ViewHolder();
			holder.cellx = (ImageView) item.findViewById(R.id.cellx);
			// holder.labelx = (TextView) item.findViewById(R.id.labelx);
			item.setTag(holder);
		} else {
			Log.e("GridViewAdapter", "item holder is? "
					+ item.getTag().getClass() + " position is: " + position);
			holder = (ViewHolder) item.getTag();
		}

		// get screen dimensions
		Bitmap image = decodeFile(getItem(position), imageWidth, imageWidth);

		holder.cellx.setImageBitmap(image);
		holder.cellx.setClickable(true);
		holder.cellx.setFocusable(true);
		// image view click listener
		holder.cellx.setOnClickListener(new OnImageClickListener(position));
		// holder.labelx.setText(getItem(position).split("_")[2]);
		// holder.labelx.setBackgroundColor(colorsdat[new Random().nextInt(11 -
		// 0 + 1) + 0]);
		return item;
	}

	static class ViewHolder {
		ImageView cellx;
		// TextView labelx;
	}

	class OnImageClickListener implements OnClickListener {

		int _postion;

		// constructor
		public OnImageClickListener(int position) {
			Log.e("GridViewImageAdapter:",
					"OnImageClickListener constructor, position: " + position);
			this._postion = position;
		}

		@Override
		public void onClick(View v) {
			// on selecting grid view image
			// launch full screen activity
			Log.e("GridViewImageAdapter:", "image clicked position: "
					+ _postion);
			Intent i = new Intent(getContext(), FullScreenViewActivity.class);
			i.putExtra("position", _postion);
			getContext().startActivity(i);
		}

	}

	/*
	 * Resizing image size
	 */
	public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
		try {

			File f = new File(filePath);

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			final int REQUIRED_WIDTH = WIDTH;
			final int REQUIRED_HIGHT = HIGHT;
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
					&& o.outHeight / scale / 2 >= REQUIRED_HIGHT)
				scale *= 2;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}