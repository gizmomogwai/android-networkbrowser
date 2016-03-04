package com.flopcode.android.networkbrowser.helper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class Helper {

  public static void startBrowser(Context context, String uriString) {
    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(i);
  }

  public static void dumpIntent(Intent intent) {
    System.out.println("action: " + intent.getAction());
    System.out.println("data: " + intent.getData());
    System.out.println("extras:");
    Bundle bundle = intent.getExtras();
    for (String key : bundle.keySet()) {
      Object object = bundle.get(key);
      System.out.println(key + "->" + object + "(" + object.getClass().getName() + ")");
    }
  }
}
