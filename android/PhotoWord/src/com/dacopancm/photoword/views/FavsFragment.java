package com.dacopancm.photoword.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dacopancm.photoword.helpers.Const;
import com.dacopancm.photoword.helpers.FileUtilities;
import com.dacopancm.photoword.helpers.GridViewImageAdapter;

public class FavsFragment extends Fragment {
	private static final String TAG = "dcm-FavsFragment";
	private ArrayList<String> imagePaths = new ArrayList<String>();
	private GridViewImageAdapter adapter;
	private GridView gridView;
	private View nofavs;
	private int columnWidth;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.e(TAG, "favsFragment onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e(TAG, "favsFragment onCreateView");
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_favs, container, false);
		gridView = (GridView) view.findViewById(R.id.grid_view);
		nofavs = view.findViewById(R.id.nofavsx);
		// Initilizing Grid View
		InitilizeGridLayout();
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e(TAG, "onresume.. update view");
		updateUI();
	}

	public void updateUI() {

		// loading all image paths from SD card
		imagePaths = FileUtilities.getFilePaths(getActivity());
		if (imagePaths.size() > 0) {
			// Gridview adapter
			adapter = new GridViewImageAdapter(getActivity(), imagePaths,
					columnWidth);

			// setting grid view adapter
			gridView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			nofavs.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.VISIBLE);
		} else {
			nofavs.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.INVISIBLE);
		}

	}

	private void InitilizeGridLayout() {
		Resources r = getResources();
		float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				Const.GRID_PADDING, r.getDisplayMetrics());

		columnWidth = (int) ((FileUtilities.getScreenWidth(getActivity()) - ((Const.NUM_OF_COLUMNS + 1) * padding)) / Const.NUM_OF_COLUMNS);

		gridView.setNumColumns(Const.NUM_OF_COLUMNS);
		gridView.setColumnWidth(columnWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setPadding((int) padding, (int) padding, (int) padding,
				(int) padding);
		gridView.setHorizontalSpacing((int) padding);
		gridView.setVerticalSpacing((int) padding);
	}

	// /eventos
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v(TAG, "onAttach");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		Log.v(TAG, "onViewStateRestored");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.v(TAG, "onStart");
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.v(TAG, "onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.v(TAG, "onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.v(TAG, "onDestroyView");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.v(TAG, "onDetach");
	}
}
