package com.example.namsoo.s_riding_ui.RecordFragment_menu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.namsoo.s_riding_ui.R;
import com.example.namsoo.s_riding_ui.db.DBManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by 김나영 on 2015-10-16.
 */
public class RecordPage1Activity extends Fragment {
    Context context;

    //맵
    MapView mMapView;
    GoogleMap googleMap;

    LatLng startPoint;
    LatLng endPoint;




    DBManager dbManager;

    TextView textLocation;
    TextView textRidingTime;
    TextView textRestTime;
    TextView textTotalTime;
    TextView textDistance;
    TextView textAveSpeed;
    TextView textBestSpeed;
    TextView textKcal;
    TextView textTemperature;

    static public int db_index= 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_page1, null);
        context = getContext();
        dbManager = new DBManager(context, "S_RidingTest2.db", null, 1);
        Log.d("dbData : ", dbManager.printData());


        textLocation = (TextView) view.findViewById(R.id.text_location);
        textRestTime = (TextView) view.findViewById(R.id.text_restTime);
        textRidingTime = (TextView) view.findViewById(R.id.text_ridingTime);
        textTotalTime = (TextView) view.findViewById(R.id.text_totalTime);
        textDistance = (TextView) view.findViewById(R.id.text_distance);
        textAveSpeed = (TextView) view.findViewById(R.id.text_aveSpeed);
        textBestSpeed = (TextView) view.findViewById(R.id.text_bestSpeed);
        textKcal = (TextView) view.findViewById(R.id.text_kcal);
        textTemperature = (TextView) view.findViewById(R.id.temperature);






        textLocation.setText(dbManager.printMainStartingLocation(db_index)+" > "+dbManager.printMainDestinationLocation(db_index));
        textRestTime.setText(dbManager.printBreakTime(db_index));
        textRidingTime.setText(dbManager.printRidingTime(db_index));
        textTotalTime.setText(dbManager.printTotalTime(db_index));
        textDistance.setText(dbManager.printDistance(db_index));
        textAveSpeed.setText(dbManager.printAveSpeed(db_index));
        textBestSpeed.setText(dbManager.printBestSpeed(db_index));
        textKcal.setText(dbManager.printKcal(db_index));
        //textTemperature.setText(dbManager.printTemperature(db_index));










        return view;

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) view.findViewById(R.id.page_map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Add this line

        googleMap = mMapView.getMap();


        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);



       // googleMap.clear();



        startPoint = new LatLng(dbManager.printStartingLongitude(db_index),dbManager.printStartingLatitude(db_index));//시작

        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(startPoint);
        startMarker.title("출발지점");
        startMarker.snippet("출발지점");
        startMarker.draggable(true);
        startMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.point_icon_01));

        googleMap.addMarker(startMarker);

        Log.d("destination : ", dbManager.printDestinationLongitude(db_index) + ", "+ dbManager.printdestinationLatitude(db_index));
        endPoint = new LatLng(dbManager.printDestinationLongitude(db_index), dbManager.printdestinationLatitude(db_index));//도착

        MarkerOptions destMarker = new MarkerOptions();
        destMarker.position(endPoint);
        destMarker.title("도착지점");
        destMarker.snippet("도착지점");
        destMarker.draggable(true);
        destMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.point_icon_02));

        googleMap.addMarker(destMarker);





    }
}
