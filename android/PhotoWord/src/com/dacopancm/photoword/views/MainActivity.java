package com.dacopancm.photoword.views;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;

import com.dacopancm.photoword.helpers.Const;
import com.dacopancm.photoword.helpers.FileUtilities;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	SettingsFragment settingsFragment;
	FavsFragment favsFragment;
	private static int RESULT_LOAD_IMAGE = 100;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	Resources res;
	boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// get resources
		res = getResources();
		flag = false;

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// Create a tab with text corresponding to the page title defined by
		// the adapter. Also specify this Activity object, which implements
		// the TabListener interface, as the callback (listener) for when
		// this tab is selected.
		actionBar.addTab(actionBar.newTab()
				.setIcon(res.getDrawable(R.drawable.ic_action_picture))
				.setText(mSectionsPagerAdapter.getPageTitle(0))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setIcon(res.getDrawable(R.drawable.ic_action_star))
				.setText(mSectionsPagerAdapter.getPageTitle(1))
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab()
				.setIcon(res.getDrawable(R.drawable.ic_action_settings))
				.setText(mSectionsPagerAdapter.getPageTitle(2))
				.setTabListener(this));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("dcm", "super onresult result code: " + resultCode + " "
				+ RESULT_OK + "data is" + data + " reuqestcode: " + requestCode);
		if (resultCode == RESULT_OK) {

			String picturePath = "";

			if (requestCode == RESULT_LOAD_IMAGE && null != data) {
				// get path from select file
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
				cursor.close();
				Log.e("dcm", picturePath);
				// save pic selected to my constant file
				FileOutputStream out = null;
				try {
					Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

					out = new FileOutputStream(FileUtilities.createImageFile(
							MainActivity.this, "tmpPhotoWord.jpg")
							.getAbsolutePath());
					thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, out);
					flag = true;
				} catch (Exception e) {
					Log.e("dcm", "excepio1");
					flag = false;
				} finally {
					try {
						out.close();
					} catch (Throwable ignore) {
						Log.e("dcm", "exception2");
						flag = false;
					}
				}

			} else if (requestCode == 200) {
				flag = true;
				Log.e("dcm", "requestCode " + requestCode + " flag" + flag);

			}
			Log.e("dcm", picturePath + " flag " + flag);
			if (flag) {
				File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
				picturePath = storageDir.getAbsolutePath() + File.separator
						+ "tmpPhotoWord.jpg";

				Intent i = new Intent(MainActivity.this, EditWordActivity.class);
				i.putExtra("imgsrc", picturePath);
				startActivity(i);
			}
		}

		Log.e("main activity", "action  bar position is: "
				+ getSupportActionBar().getSelectedTab().getPosition());
		// FACEBOOK
		if (getSupportActionBar().getSelectedTab().getPosition() == 2)
			settingsFragment.onActivityResult(requestCode, resultCode, data);
		//
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings: {
			mViewPager.setCurrentItem(2);
			return true;
		}
		/*
		 * case R.id.tmp_templates: { Intent templatesI = new Intent(this,
		 * TemplatesActivity.class); templatesI.putExtra("wordx",
		 * "12345678909876543210"); templatesI .putExtra( "imgsrc",
		 * "/storage/sdcard/Android/data/com.dacopancm.photoword.views/files/Pictures/tmpPhotoWord.jpg"
		 * ); startActivity(templatesI); return true; } case R.id.tmp_sharex: {
		 * // String imageFileName = Const.PHOTOWORD_FILE_1; File storageDir =
		 * getExternalFilesDir(Environment.DIRECTORY_PICTURES); File image = new
		 * File(storageDir, imageFileName); // Intent IShare = new Intent(this,
		 * SharexActivity.class); IShare.putExtra("filename",
		 * image.getAbsolutePath()); IShare.putExtra("wordx", "dos");
		 * IShare.putExtra("wordres", "#sod"); startActivityForResult(IShare,
		 * Const.GO_BACK_REQUEST); return true; }
		 */
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int tabindex) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			switch (tabindex) {
			case 0:
				// Top Rated fragment activity
				return new TakePicFragment();
			case 1:
				// Games fragment activity
				return new FavsFragment();
			case 2: {
				settingsFragment = new SettingsFragment();
				return settingsFragment;
			}
			}

			return null;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

}
