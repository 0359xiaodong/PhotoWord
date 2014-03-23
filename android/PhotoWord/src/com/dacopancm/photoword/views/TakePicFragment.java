package com.dacopancm.photoword.views;

import java.io.File;
import java.io.IOException;

import com.dacopancm.photoword.helpers.AlertDialogManager;
import com.dacopancm.photoword.helpers.FileUtilities;
import com.facebook.Session;
import com.facebook.SessionState;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class TakePicFragment extends Fragment {
	ImageButton piccamera;
	ImageButton picgallery;
	private static int RESULT_LOAD_IMAGE = 100;
	private static int REQUEST_TAKE_PHOTO = 200;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_take_pic, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		piccamera = (ImageButton) getView().findViewById(R.id.piccamera);
		picgallery = (ImageButton) getView().findViewById(R.id.picgallery);
		// asignar eventos
		picgallery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkSocials()) {
					getActivity()
							.startActivityForResult(
									new Intent(
											Intent.ACTION_PICK,
											android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
									RESULT_LOAD_IMAGE);
				} else {
					new AlertDialogManager().showAlertDialog(getActivity(),
							getString(R.string.error_title),
							getString(R.string.error_no_social), false);
				}
			}
		});

		piccamera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (checkSocials()) {
					Intent takePictureIntent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					// Ensure that there's a camera activity to handle the
					// intent
					if (takePictureIntent.resolveActivity(getActivity()
							.getPackageManager()) != null) {
						// Create the File where the photo should go

						File photoFile = null;
						try {
							if (FileUtilities.isSdCardReady()) {
								photoFile = FileUtilities.createImageFile(
										getActivity(), "tmpPhotoWord.jpg");
							} else {
								new AlertDialogManager().showAlertDialog(
										getActivity(),
										getString(R.string.error_title),
										getString(R.string.no_sdcard), false);
							}
						} catch (IOException ex) {
							// Error occurred while creating the File
							new AlertDialogManager().showAlertDialog(
									getActivity(),
									getString(R.string.error_title),
									getString(R.string.error_file_mess), false);
						}
						// Continue only if the File was successfully created
						if (photoFile != null) {
							Log.e("dcm", "start activity");
							takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(photoFile));
							getActivity().startActivityForResult(
									takePictureIntent, REQUEST_TAKE_PHOTO);
						} else {
							new AlertDialogManager().showAlertDialog(
									getActivity(),
									getString(R.string.error_title),
									getString(R.string.error_file_mess), false);
						}
					}

				} else {
					new AlertDialogManager().showAlertDialog(getActivity(),
							getString(R.string.error_title),
							getString(R.string.error_no_social), false);
				}
			}
		});

	}

	private boolean checkSocials() {
		return (checkTwitter() || checkFacebook());
	}

	private boolean checkFacebook() {
		try {
			Session session = Session.getActiveSession();
			session = new Session(getActivity());
			Session.setActiveSession(session);
			/*
			 * if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED))
			 * { session.openForRead(new Session.OpenRequest(this)); }
			 */
			return (session.getState()
					.equals(SessionState.CREATED_TOKEN_LOADED) || session
					.isOpened());
		} catch (Exception ex) {
			return false;
		}
	}

	private boolean checkTwitter() {
		return LoginTwitterActivity.isActive(getActivity());
	}
}
