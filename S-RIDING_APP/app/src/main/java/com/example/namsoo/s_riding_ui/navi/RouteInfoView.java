package com.example.namsoo.s_riding_ui.navi;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.namsoo.s_riding_ui.R;

public class RouteInfoView extends LinearLayout {

	public TextView mRouteName;
	public TextView mRouteGuide;

	public RouteInfoView(Context context) {
		super(context);

		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.routeinfoitem, this, true);

		mRouteName = (TextView)findViewById(R.id.routeName);
		mRouteGuide = (TextView)findViewById(R.id.routeGuide);
	}

	public RouteInfoView(Context context, RouteInfo routeInfo) {
		super(context);

		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.routeinfoitem, this, true);

		mRouteName = (TextView)findViewById(R.id.routeName);
		mRouteGuide = (TextView)findViewById(R.id.routeGuide);

		mRouteName.setText(routeInfo.getName());
		mRouteGuide.setText(routeInfo.getRoadDistance() + "m 앞 " + routeInfo.getGuideName());
	}
}
