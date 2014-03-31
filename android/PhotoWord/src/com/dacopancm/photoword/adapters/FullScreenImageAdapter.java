package com.dacopancm.photoword.adapters;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dacopancm.photoword.views.R;

public class FullScreenImageAdapter extends PagerAdapter {

	private Activity _activity;
	public ArrayList<String> _imagePaths;
	private LayoutInflater inflater;
	private int[] colorsdat = { Color.parseColor("#9F000000"),
			Color.parseColor("#9FFF00FF"), Color.parseColor("#9FA200FF"),
			Color.parseColor("#9F00ABA9"), Color.parseColor("#9F8CBF26"),
			Color.parseColor("#9FA05000"), Color.parseColor("#9FE671B8"),
			Color.parseColor("#9FF09609"), Color.parseColor("#9F1BA1E2"),
			Color.parseColor("#9FE51400"), Color.parseColor("#9F339933"),
			Color.parseColor("#9FF09609") };

	// constructor
	public FullScreenImageAdapter(Activity activity,
			ArrayList<String> imagePaths) {
		this._activity = activity;
		this._imagePaths = imagePaths;
	}

	@Override
	public int getCount() {
		return this._imagePaths.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imgDisplay;
		TextView labelx;

		inflater = (LayoutInflater) _activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image,
				container, false);

		imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
		labelx = (TextView) viewLayout.findViewById(R.id.labelx);
		//
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		Bitmap bitmap = BitmapFactory.decodeFile(_imagePaths.get(position),
				options);
		imgDisplay.setImageBitmap(bitmap);

		//
		String filename = new File(_imagePaths.get(position)).getName();
		labelx.setText(filename.split("_")[2].toUpperCase(Locale.getDefault()));
		labelx.setBackgroundColor(colorsdat[new Random().nextInt(11 - 0 + 1) + 0]);
		// close button click event

		((ViewPager) container).addView(viewLayout);

		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);

	}
}