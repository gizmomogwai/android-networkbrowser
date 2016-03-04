package com.flopcode.android.networkbrowser.sshtcp;

import com.flopcode.android.networkbrowser.helper.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class SshTcpAdapter extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent i = getIntent();
    Helper.dumpIntent(i);
    final String host = i.getStringExtra("host");
    final int port = i.getIntExtra("port", 22);
    final String nick = i.getStringExtra("name"); 

    final EditText user = new EditText(this);
    new AlertDialog.Builder(this).setMessage("Please enter username").setView(user).setPositiveButton("ok", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Intent sshIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("ssh://" + user.getEditableText().toString() + "@" + host + ":" + port + "#" + nick));
        try {
          startActivity(sshIntent);
        } catch (Exception e) {
          Toast.makeText(SshTcpAdapter.this, "problems while launching activity: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
          finish();
        }
      }
    }).setNegativeButton("cancel", new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        finish();
      }
    }).create().show();
  }
}