package com.flopcode.android.networkbrowser.ftptcp;

import com.flopcode.android.networkbrowser.helper.Helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class FtpAdapter extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent i = getIntent();
    Helper.dumpIntent(i);
    String host = i.getStringExtra("host");
    int port = i.getIntExtra("port", 21);
    Intent ftpIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("ftp://" + host + ":" + port));
    startActivity(ftpIntent);
  }
}