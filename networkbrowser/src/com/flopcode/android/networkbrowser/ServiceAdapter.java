/**
 * 
 */
package com.flopcode.android.networkbrowser;

import java.util.TreeSet;

import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class ServiceAdapter extends BaseAdapter {

    static class Service implements Comparable<ServiceAdapter.Service> {
      private final String fType;
      private final String fName;
      private final ServiceInfo fInfo;

      public Service(String type, String name) {
        fType = type;
        fName = name;
        fInfo = null;
      }

      public Service(ServiceEvent arg0) {
        fType = arg0.getType();
        fName = arg0.getName();
        fInfo = null;
      }

      public Service(ServiceInfo result) {
        fType = result.getType();
        fName = result.getName();
        fInfo = result;
      }

      public String getType() {
        return fType;
      }

      public String getName() {
        return fName;
      }

      @Override
      public int compareTo(ServiceAdapter.Service another) {
        return getName().compareTo(another.getName());
      }

      @Override
      public boolean equals(Object o) {
        return getType().equals(((ServiceAdapter.Service)o).getType()) && getName().equals(((ServiceAdapter.Service)o).getName());
      }
      
      @Override
      public int hashCode() {
        return (getType()+getName()).hashCode();
      }

      public ServiceInfo getInfo() {
        return fInfo;
      }
      
    }
    
    TreeSet<ServiceAdapter.Service> fEvents = new TreeSet<ServiceAdapter.Service>();

    private final Context fContext;
    private final String fQuery;

	private LayoutInflater fInflater;

    public ServiceAdapter(Context context, String query) {
      fContext = context;
      fQuery = query;
    }

    @Override
    public int getCount() {
      return fEvents.size();
    }

    @Override
    public Object getItem(int position) {
      return getTypedItem(position);
    }

    public ServiceAdapter.Service getTypedItem(int position) {
      int count = 0;
      for (ServiceAdapter.Service e : fEvents) {
        if (count == position) {
          return e;
        }
        ++count;
      }
      return null;
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
    	  fInflater = (LayoutInflater)fContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	  convertView = fInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
      }
      TextView t = (TextView) convertView.findViewById(android.R.id.text1);
      t.setText(getTypedItem(position).getName());
      return t;
    }

    public void add(ServiceEvent arg0) {
      add(new Service(arg0));
    }

    private void add(ServiceAdapter.Service service) {
      fEvents.add(service);
      notifyDataSetChanged();
    }

    public void remove(ServiceEvent arg0) {
      boolean changed = fEvents.remove(new Service(arg0));
      if (!changed) {
        throw new RuntimeException("should have changed the collection");
      }
      notifyDataSetChanged();
    }
    @Override
    public boolean isEnabled(int position) {
      if (fQuery.equals(AbstractNetworkBrowser.SERVICE_DISCOVERY)) {
        return true;
      }
      ServiceAdapter.Service e = getTypedItem(position);
      return e.getInfo() != null;
    }
    
    @Override
    public boolean areAllItemsEnabled() {
      return fQuery.equals(AbstractNetworkBrowser.SERVICE_DISCOVERY);
    }
    public void enableStateChanged() {
      notifyDataSetChanged();
    }

    public void update(ServiceInfo result) {
      if (result == null) {
        return;
      }
      ServiceAdapter.Service s = new Service(result);
      boolean removed = fEvents.remove(s);
      if (!removed) {
        throw new RuntimeException("war nicht in der liste");
      }
      add(s);
    }
  }