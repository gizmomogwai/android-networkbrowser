package com.flopcode.android.networkbrowser.donation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ThanksForDonating extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Button b = (Button) findViewById(R.id.take_me_to_the_market);
    b.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:com.flopcode.android.networkbrowser"));
        startActivity(i);
        finish();
      }
    });
  }
}