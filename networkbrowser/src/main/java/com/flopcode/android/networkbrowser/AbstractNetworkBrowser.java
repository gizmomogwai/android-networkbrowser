package com.flopcode.android.networkbrowser;

import java.net.InetAddress;
import java.util.Enumeration;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.flopcode.android.networkbrowser.ServiceAdapter.Service;

public class AbstractNetworkBrowser extends ListActivity {

  static final String SERVICE_DISCOVERY = "_services._dns-sd._udp.local.";

  static final String SCHEME = "zeroconf";

  private ServiceAdapter fListAdapter;

  private MulticastLock fLock;

  private JmDNS fDns;

  private String fQuery;

  private ProgressDialog fProgressDialog;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    fQuery = SERVICE_DISCOVERY;
    if (getIntent() != null) {
      Uri uri = getIntent().getData();
      if (uri != null) {
        if (uri.getScheme().equals(SCHEME)) {
          fQuery = uri.getHost();
        }
      }
    }

    fListAdapter = new ServiceAdapter(this, fQuery);

    try {
      WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
      if (!wifiManager.isWifiEnabled()) {
        AlertDialog dialog = new AlertDialog.Builder(this).setMessage("please enable wifi").create();
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            finish();
          }
        });
        dialog.show();
        return;
      }
      fProgressDialog = ProgressDialog.show(this, "searching for services", fQuery);
      fProgressDialog.setOnKeyListener(new OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog.dismiss();
            finish();
            return true;
          }
          return false;
        }
      });
      showTitle();

      fLock = wifiManager.createMulticastLock("mylock");
      fLock.acquire();
      byte[] wifiAddress = intToIp(wifiManager.getDhcpInfo().ipAddress);

      InetAddress wifi = InetAddress.getByAddress(wifiAddress);
      fDns = JmDNS.create(wifi);

      fDns.addServiceListener(fQuery, new ServiceListener() {
        @Override
        public void serviceResolved(ServiceEvent arg0) {
        }

        @Override
        public void serviceRemoved(ServiceEvent arg0) {
          removeServiceType(arg0);
        }

        @Override
        public void serviceAdded(ServiceEvent arg0) {
          addServiceType(arg0);
        }

      });
    } catch (Exception e) {
      e.printStackTrace();
    }
    setListAdapter(fListAdapter);
  }

  private void showTitle() {
    if (fQuery.equals(SERVICE_DISCOVERY)) {
      setTitle("Discovering Services");
    } else {
      setTitle("Browsing " + fQuery);
    }
  }

  private byte[] intToIp(int i) {
    byte[] res = new byte[4];
    res[0] = (byte) (i & 0xff);
    res[1] = (byte) ((i >> 8) & 0xff);
    res[2] = (byte) ((i >> 16) & 0xff);
    res[3] = (byte) ((i >> 24) & 0xff);
    return res;
  }

  @Override
  protected void onDestroy() {
    if (fDns != null) {
      fDns.close();
    }
    if (fLock != null) {
      fLock.release();
    }
    super.onDestroy();
  }

  protected void removeServiceType(final ServiceEvent arg0) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        fListAdapter.remove(arg0);
      }
    });
  }

  class AddServiceEventAsyncTask extends AsyncTask<ServiceEvent, Void, ServiceInfo> {

    @Override
    protected ServiceInfo doInBackground(ServiceEvent... args) {
      ServiceEvent e = args[0];
      return fDns.getServiceInfo(e.getType(), e.getName());
    }

    @Override
    protected void onPostExecute(ServiceInfo result) {
      fListAdapter.update(result);
    }
  }

  private void addServiceType(final ServiceEvent arg0) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        fProgressDialog.dismiss();
        fListAdapter.add(arg0);
        new AddServiceEventAsyncTask().execute(arg0);
      }
    });
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    Service service = fListAdapter.getTypedItem(position);
    if (fQuery == SERVICE_DISCOVERY) {
      Intent i = new Intent(this, getClass());
      i.setData(Uri.parse(SCHEME + "://" + service.getName()));
      startActivity(i);
    } else {
      Service s = fListAdapter.getTypedItem(position);
      ServiceInfo info = s.getInfo();
      Intent intent = new Intent(Intent.ACTION_VIEW);
      String uriString = SCHEME + "://" + info.getType();
      Uri uri = Uri.parse(uriString);
      intent.setData(uri);
      intent.putExtra("name", info.getName());
      intent.putExtra("host", info.getHostAddress());
      intent.putExtra("port", info.getPort());
      Enumeration<String> e = info.getPropertyNames();
      while (e.hasMoreElements()) {
        String propertyName = (String) e.nextElement();
        intent.putExtra("property." + propertyName, info.getPropertyString(propertyName));
      }

      dumpIntent(intent);
      selected(intent);
    }
  }

  void selected(Intent i) {

  }

  public void dumpIntent(Intent intent) {
    System.out.println("action: " + intent.getAction());
    System.out.println("data: " + intent.getData());
    System.out.println("extras:");
    Bundle bundle = intent.getExtras();
    for (String key : bundle.keySet()) {
      Object object = bundle.get(key);
      System.out.println(key + "->" + object + "(" + object.getClass().getName() + ")");
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.donate:
        openMarketForDonation();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void openMarketForDonation() {
    try {
      Intent i = new Intent("com.flopcode.android.networkbrowser.showdonation");
      startActivity(i);
    } catch (Exception e) {
      Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pname:com.flopcode.android.networkbrowser.donation"));
      startActivity(i);
    }
  }
}
