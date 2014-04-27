package com.eduportal.videoportal.scrollpages;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.eduportal.videoportal.R;


public class Player extends Activity {

	public static final String TAG = "com.eduportal.videoportal.VideoPlayerActivity";

	public static final String MXVP = "com.mxtech.videoplayer.ad";
	public static final String MXVP_PLAYBACK_CLASS = "com.mxtech.videoplayer.ad.ActivityScreen";
	public static final String RESULT_VIEW = "com.mxtech.intent.result.VIEW";
	public static final String EXTRA_DECODE_MODE = "decode_mode"; // (byte)
	public static final String EXTRA_VIDEO_LIST = "video_list";
	public static final String EXTRA_SUBTITLES = "subs";
	public static final String EXTRA_SUBTITLES_ENABLE = "subs.enable";
	public static final String EXTRA_TITLE = "title";
	public static final String EXTRA_POSITION = "position";
	public static final String EXTRA_RETURN_RESULT = "return_result";
	public static final String EXTRA_HEADERS = "headers";

	Intent intent;
	Bundle bundle; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		intent = getIntent();
		bundle = intent.getExtras(); 
		String sUri = bundle.getString("url");
		Uri videoUri = Uri.parse(sUri);
		Intent myIntent = new Intent(Intent.ACTION_VIEW);
		myIntent.setDataAndType(videoUri, "application/*");
		myIntent.putExtra(EXTRA_DECODE_MODE, (byte) 2);
		myIntent.putExtra(EXTRA_RETURN_RESULT, true);
		String[] headers = new String[] { "User-Agent",
				"MX Player Caller App/1.0", "Extra-Header", "911" };
		myIntent.putExtra(EXTRA_HEADERS, headers);

		try {
			myIntent.setPackage(MXVP);
			myIntent.setClassName(MXVP, MXVP_PLAYBACK_CLASS);
			startActivityForResult(myIntent, 0);
			return;
		} catch (ActivityNotFoundException e2) {
			Intent intent = null;
			try {
				intent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id=" + MXVP));
				startActivity(intent);
			} catch (ActivityNotFoundException anfe) {
				intent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://play.google.com/store/apps/details?id="
								+ MXVP));
				startActivity(intent);
			}
		}

		Log.e(TAG, "Can't find MX Video Player.");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		finish();

		if (resultCode != RESULT_OK) {
			Log.d(TAG, "Canceled.");
			return;
		}
		Uri lastVideoUri = data.getData();
		byte lastDecodingMode = data.getByteExtra(EXTRA_DECODE_MODE, (byte) 0);
		int lastPosition = data.getIntExtra(EXTRA_POSITION, 0);

		Log.i(TAG, "OK: " + lastVideoUri + " last-decoding-mode="
				+ lastDecodingMode + " last-position=" + lastPosition);
	}
}