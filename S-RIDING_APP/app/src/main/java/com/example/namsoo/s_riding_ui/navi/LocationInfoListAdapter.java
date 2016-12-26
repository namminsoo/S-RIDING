package com.example.namsoo.s_riding_ui.navi;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

class LocationInfoListAdapter extends ArrayAdapter<LocationInfo> {
	private Context mContext;
	private ArrayList<LocationInfo> mLocationList = null;

	public LocationInfoListAdapter(Context context, ArrayList<LocationInfo> objects) {
		super(context, 0, objects);

		mContext = context;
		setLocationList(objects);
	}

	public int getCount() {
		int count = 0;
		if (mLocationList != null) {
			count = mLocationList.size();
		}

		return count;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LocationInfoView itemView;
		if (convertView == null) {
			itemView = new LocationInfoView(mContext);
		} else {
			itemView = (LocationInfoView) convertView;
		}

		LocationInfo locInfo = mLocationList.get(position);

		itemView.mLocName.setText(locInfo.getName());
		itemView.mLocAddress.setText(locInfo.getAddress());

		return itemView;
	}


	public void setLocationList(ArrayList<LocationInfo> locationList) {
		mLocationList = locationList;
	}


	public ArrayList<LocationInfo> getLocationList() {
		return mLocationList;
	}


}
