package com.flopcode.android.networkbrowser.httptcp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.flopcode.android.networkbrowser.helper.Helper;

public class HttpAdapter extends Activity {

  public static final String LOG_TAG = "HttpAdapter";
  private static final String PROPERTY_PATH = "property.path";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();

		Helper.dumpIntent(intent);
		startBrowser(this, intent);
		finish();
	}

	static void startBrowser(Context context, Intent intent) {
		String host = intent.getStringExtra("host");
		int port = intent.getIntExtra("port", 80);
		String path = "";
		if (intent.hasExtra(PROPERTY_PATH)) {
			path = intent.getStringExtra(PROPERTY_PATH);
		}
		String uriString = host + ":" + port + "/" + path;
		uriString = uriString.replaceAll("/$", "");
		uriString = uriString.replaceAll("//", "/");
		uriString = "http://" + uriString;
		Log.d(LOG_TAG, "opening url: '" + uriString + "'");
		Helper.startBrowser(context, uriString);
	}

}
