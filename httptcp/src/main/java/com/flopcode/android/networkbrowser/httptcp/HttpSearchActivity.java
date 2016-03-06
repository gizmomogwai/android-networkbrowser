package com.flopcode.android.networkbrowser.httptcp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import static com.flopcode.android.networkbrowser.httptcp.HttpAdapter.LOG_TAG;

public class HttpSearchActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Log.d(LOG_TAG, "onCreate");
    super.onCreate(savedInstanceState);
    Uri uri = Uri.parse("zeroconf://_http._tcp.local.");
    Intent intent = new Intent(Intent.ACTION_PICK, uri);
    startActivityForResult(intent, 17);
  }

  @Override
  protected void onResume() {
    Log.d(LOG_TAG, "onResume");
    super.onResume();
  }

  @Override
  protected void onStop() {
    Log.d(LOG_TAG, "onStop");
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    Log.d(LOG_TAG, "onDestroy");
    super.onDestroy();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d(LOG_TAG, "onActivityResult");
    if ((requestCode == 17) && (resultCode == Activity.RESULT_OK)) {
      HttpAdapter.startBrowser(this, data);
    }
    finish();
  }

  @Override
  protected void onPause() {
    Log.d(LOG_TAG, "onPause");
    super.onPause();
  }
}
