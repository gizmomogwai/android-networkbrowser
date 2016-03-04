package com.flopcode.android.networkbrowser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.flopcode.android.networkbrowser.helper.Helper;

public class SimpleNetworkBrowserActivity extends AbstractNetworkBrowser {

  @Override
  void selected(Intent i) {
    try {
      Helper.dumpIntent(i);
      startActivity(i);
    } catch (ActivityNotFoundException e) {
      Toast.makeText(this, "no activity found for " + i.getData().toString() + "\nSearch market for plugin!", Toast.LENGTH_LONG).show();
      Pattern p = Pattern.compile(SCHEME + "://(.*)\\.local\\.");
      Matcher m = p.matcher(i.getData().toString());
      String s = "";
      if (m.matches()) {
        s = m.group(1);
        s = s.replaceAll("_", "");
        s = s.replaceAll("\\.", "");
      }
      if (s.length() > 0) {
        Intent marketQuery = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:com.flopcode.android.networkbrowser." + s));
        startActivity(marketQuery);
      }
    }
  }

}
