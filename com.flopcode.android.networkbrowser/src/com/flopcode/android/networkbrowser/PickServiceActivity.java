package com.flopcode.android.networkbrowser;

import android.app.Activity;
import android.content.Intent;

import com.flopcode.android.networkbrowser.helper.Helper;

public class PickServiceActivity extends AbstractNetworkBrowser {

  @Override
  protected void selected(Intent i) {
    Helper.dumpIntent(i);
    setResult(Activity.RESULT_OK, i);
    finish();
  }

}
