package com.flopcode.android.networkbrowser.rfbtcp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;

public class RfbAdapter extends Activity {
  private String fHost;
  private int fPort;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    fHost = getIntent().getStringExtra("host");
    fPort = getIntent().getIntExtra("port", 5900);
    getPasswordAndStartActivity();
  }

  private void getPasswordAndStartActivity() {
    AlertDialog.Builder alert = new AlertDialog.Builder(this);

    alert.setTitle("Please enter password");

    final EditText input = new EditText(this);
    alert.setView(input);

    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        String password = input.getText().toString();
        Intent vncIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnc://" + fHost + ":" + fPort + "/C24bit/" + password));
        startActivity(vncIntent);
        finish();
      }
    });

    alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
        finish();
      }
    });

    alert.show();
  }
}
