package com.flopcode.android.networkbrowser.httptcp;

import com.flopcode.android.networkbrowser.helper.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class HttpAdapter extends Activity {

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
    String uriString = "http://" + host + ":" + port + "/" + path;
    Helper.startBrowser(context, uriString);
  }

}
