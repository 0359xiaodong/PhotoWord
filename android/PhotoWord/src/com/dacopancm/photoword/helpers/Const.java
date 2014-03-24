package com.dacopancm.photoword.helpers;

import java.util.Arrays;
import java.util.List;

public class Const {
	public static String CONSUMER_KEY = "YOUR_TWITTER_CONSUMER_KEY";
	public static String CONSUMER_SECRET = "YOUR_TWITTER_CONSUMER_SECRET";

	public static String PREFERENCE_NAME = "twitter_oauth";
	public static final String PREF_KEY_SECRET = "oauth_token_secret";
	public static final String PREF_KEY_TOKEN = "oauth_token";

	public static final String CALLBACK_URL = "x-photoword://photowordtwitterlogin";

	public static final String IEXTRA_AUTH_URL = "auth_url";
	public static final String IEXTRA_OAUTH_VERIFIER = "oauth_verifier";
	public static final String IEXTRA_OAUTH_TOKEN = "oauth_token";

	public static final int LOGIN_TWITTER_REQUEST = 400;
	public static final int TWITTER_LOGIN_RESULT_CODE_SUCCESS = 1111;
	public static final int TWITTER_LOGIN_RESULT_CODE_FAILURE = 2222;
	public static final int GO_BACK_REQUEST = 911;
	public static final String PHOTOWORD_FILE_0 = "tmpPhotoWord.jpg";
	public static final String PHOTOWORD_FILE_1 = "tmpPhotoWord2.jpg";
	public static final String FAVS_ALBUM_NAME = "PhotoWord_Favs";

	// GALLERY FAVS
	// Number of columns of Grid View
	public static final int NUM_OF_COLUMNS = 3;
	// Gridview image padding
	public static final int GRID_PADDING = 8; // in dp
	// supported file formats
	public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg",
			"png");
}
