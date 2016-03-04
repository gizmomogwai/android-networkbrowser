package com.flopcode.android.networkbrowser.httptcp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class HttpSearchActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Uri uri = Uri.parse("zeroconf://_http._tcp.local.");
    Intent intent = new Intent(Intent.ACTION_PICK, uri);
    startActivityForResult(intent, 17);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if ((requestCode == 17) && (resultCode == Activity.RESULT_OK)) {
      HttpAdapter.startBrowser(this, data);
    }
  }
}
