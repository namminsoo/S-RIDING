package com.example.namsoo.s_riding_ui.nayoung;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.namsoo.s_riding_ui.MainActivity;
import com.example.namsoo.s_riding_ui.R;
import com.example.namsoo.s_riding_ui.bluno.BlunoLibrary;
import com.example.namsoo.s_riding_ui.db.UserProfileData;
import com.example.namsoo.s_riding_ui.fragment_menu.BaseFragment;
import com.example.namsoo.s_riding_ui.navi.GPoint;
import com.example.namsoo.s_riding_ui.navi.PathCalculator;
import com.example.namsoo.s_riding_ui.navi.RouteFragment;
import com.example.namsoo.s_riding_ui.usbserial.SerialConsole;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by 김나영 on 2015-10-04.
 */
//http://stackoverflow.com/questions/18711568/onlocationchanged-only-called-once-not-update-automatically
public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;


    public static SerialConsole serialConsole;
    public static BlunoLibrary blunoLibrary;


    //라이딩 길찾기
    public static boolean bool_riding = false;
    public static int RADIUS = 20;//반경

    public static int mock_index = 0;

    public static MarkerOptions markerOptions;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    boolean changedLocation = false;

    int timecount = 0;

    static double dSpeed = 0;

    public static Location location; // location
    public static Location firstLocation;
    public static Location beforeLocation;

    double altitude;

    double latitude; // latitude
    double longitude; // longitude
    //float speed=0;
    double distance;
    double kcal ;
    double aveSpeed;
    double maxSpeed;
    String instantSpeed;
    String instantHeight;
    kcalCalculation kcalCal = new kcalCalculation();
    double totalDistance = 0.0;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 0; // 1 second

    // Declaring a Location Manager
    protected LocationManager locationManager;


    float mBearing = 0;

    TodoOnLocationChanged todoOnLocationChanged = new TodoOnLocationChanged();

    public GPSTracker(Context context) {
        this.mContext = context;


        getLocation();


    }

    public Location getLocation() {
        try {


            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);



            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            {
                Toast.makeText(mContext, "GPS를 활성화 시켜 주세요.", Toast.LENGTH_LONG).show();

                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addCategory(Intent.CATEGORY_DEFAULT));
            }


            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
//                if (isNetworkEnabled) {
//                    Toast.makeText( this.mContext,"Network",Toast.LENGTH_LONG).show();
//                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
//                    Log.d("Network", "Network");
//                    if (locationManager != null) {
//                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        if (location != null) {
//
//
//                            latitude = location.getLatitude();
//                            longitude = location.getLongitude();
//                            //speed = location.getSpeed();
//                        }
//                    }
//                }


                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {


                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_FINE);
                        criteria.setAltitudeRequired(false);
                        criteria.setBearingRequired(false);
                        criteria.setCostAllowed(true);
                        criteria.setPowerRequirement(Criteria.POWER_LOW);
                        String provider = locationManager.getBestProvider(criteria, true);
                        Location temp = locationManager.getLastKnownLocation(provider);
                        List<String> provider2 = locationManager.getProviders(true);
                        Location l;
                        for (String providers : provider2) {
                            l = locationManager.getLastKnownLocation(providers);
                            if (l == null) {
                                continue;
                            }
                            if (temp == null || l.getAccuracy() < temp.getAccuracy()) {
                                // Found best last known location: %s", l);
                                temp = l;
                            }
                        }

                        location = temp;

                        if (location.getLongitude() == 0.0) {
                            location.setLongitude(37.551930);
                            location.setLatitude(127.073978);
                        }


                        UserProfileData.x = location.getLongitude();
                        UserProfileData.y = location.getLatitude();


                        firstLocation = location;
                        beforeLocation = location;

                        mBearing = location.getBearing();


                        //Toast.makeText(this.mContext, "GPS", Toast.LENGTH_LONG).show();
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {


                                firstLocation = location;
                                beforeLocation = location;
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }


    public double getAltitude() {
        if (location != null) {
            altitude = location.getAltitude();
        }

        // return altitude
        return altitude;
    }


    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    public double getSpeed() {

        if (location != null) {
            dSpeed = location.getSpeed() * 3.6;
            //changedLocation =false;
        }
        return dSpeed;
    }

    public int getDistance() {
        if (location != null && firstLocation != null) {
            distance = location.distanceTo(firstLocation);
        }
        return (int)(distance/1000);
    }


    public double getMaxSpeed(){

        if(maxSpeed <getSpeed()){
            maxSpeed = getSpeed();
        }

        return maxSpeed;
    }

    public float getmBearing() {
        return mBearing;
    }


    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    public double getAveSpeed(){

        if(timecount!=0 && getSpeed() >0){
            aveSpeed = ((timecount * aveSpeed) + getSpeed())/ (timecount+1);
        }else{

        }

        return aveSpeed;
    }

    public int getKcal(){
        if(location != null && firstLocation != null){
            kcal = kcalCal.Calculation(UserProfileData.userWeight, getAveSpeed(), timecount);
        }


        return (int) kcal;
    }

    public String getinstantSpeed(){
        instantSpeed += getSpeed();

        return instantSpeed;

    }
    public String getinstantHeight(){
        instantHeight += instantHeight;

        return instantHeight;

    }

    //위치 바뀔때
    public void onLocationChanged(Location location) {

        timecount++;

        todoOnLocationChanged.ChangedText(getDistance(), getSpeed());


        changedLocation = true;

        this.location = location;
        totalDistance = totalDistance / 10;


        UserProfileData.x = this.location.getLongitude();
        UserProfileData.y = this.location.getLatitude();

        //Log.d("minsoogps", "x : "+UserProfileData.x+" y : "+UserProfileData.y);

        LatLng latLng = new LatLng(UserProfileData.y, UserProfileData.x);

        //RouteFragment.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


        try {
            RouteFragment.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            RouteFragment.googleMap.clear();


            MarkerOptions startMarker = new MarkerOptions();
            startMarker.position(RouteFragment.startPoint);
            startMarker.title("출발지점");
            startMarker.snippet("출발지점");
            startMarker.draggable(true);

            startMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.point_icon_01));

            RouteFragment.googleMap.addMarker(startMarker);


            MarkerOptions destMarker = new MarkerOptions();
            destMarker.position(RouteFragment.endPoint);
            destMarker.title("도착지점");
            destMarker.snippet("도착지점");
            destMarker.draggable(true);
            destMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.point_icon_02));

            RouteFragment.googleMap.addMarker(destMarker);

            drawRoute(Color.BLUE);



        } catch (Exception e) {
        }


        altitude = this.getAltitude();
        dSpeed = this.getSpeed();


        //속도출력
        dSpeed = location.getSpeed() * 3.6;

        if (this.location.hasSpeed()) {
            //  NaviFragment.txtSpeed.setText(String.valueOf((int) dSpeed));
            //   Toast.makeText(this.mContext, "speed : " + getSpeed(), Toast.LENGTH_SHORT).show();
        } else {
            //NaviFragment.txtSpeed.setText("0");
        }


        mBearing = location.getBearing();


        //여기서부터 시작~

        //사용자 포지션 정하기

        int x_sign;
        int y_sign;
        try {

            if (beforeLocation.getLongitude() - location.getLongitude() > 0) {
                x_sign = 1;
            } else if (beforeLocation.getLongitude() - location.getLongitude() == 0) {
                x_sign = 0;
            } else {
                x_sign = -1;
            }


            if (beforeLocation.getLatitude() - location.getLatitude() > 0) {
                y_sign = 1;
            } else if (beforeLocation.getLatitude() - location.getLatitude() == 0) {
                y_sign = 0;
            } else {
                y_sign = -1;
            }


            if (x_sign == y_sign) {
                if (x_sign > 0) {
                    UserProfileData.user_riding_position = 5;//++ 북동
                } else {
                    UserProfileData.user_riding_position = 1;//-- 남서
                }
            } else {
                if (x_sign > 0) {
                    UserProfileData.user_riding_position = 7;//+- 북서
                } else {
                    UserProfileData.user_riding_position = 3;//-+ 남동
                }
            }


            if (bool_riding && serialConsole != null && UserProfileData.userPathsListpathsList != null)//체크
            {
                int min_index = 0;

                double min_distance = 999999999;

                for (int i = 0; i < UserProfileData.userPathsListpathsList.size(); i++) {


                    if (min_distance > PathCalculator.getDistance(UserProfileData.x, UserProfileData.y, UserProfileData.userPathsListpathsList.get(i).x, UserProfileData.userPathsListpathsList.get(i).y)) {
                        min_index = i;
                        min_distance = PathCalculator.getDistance(UserProfileData.x, UserProfileData.y, UserProfileData.userPathsListpathsList.get(i).x, UserProfileData.userPathsListpathsList.get(i).y);


                    }

                }

                Log.d("gpsriding", "min_index : " + min_index + " min_distance : " + min_distance);

                if (min_distance <= RADIUS) {
                    for (int i = min_index; i < UserProfileData.userPathsListpathsList.size(); i++) {
                        if (PathCalculator.getDistance(
                                UserProfileData.x
                                , UserProfileData.y
                                , UserProfileData.userPathsListpathsList.get(UserProfileData.userPathsListpathsList.size() - 1).x
                                , UserProfileData.userPathsListpathsList.get(UserProfileData.userPathsListpathsList.size() - 1).y) <= 10) {
                            bool_riding = false;
                            mock_index = 0;

                            Toast.makeText(mContext, "목적지에 도착했습니다", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (UserProfileData.user_riding_position == UserProfileData.userPathsListpathsList.get(i).position/10) {



                            if(UserProfileData.userPathsListpathsList.get(i).position%10==6)
                            {
                                try {
                                    serialConsole.directionChange(SerialConsole.LEFT_DIRECTION);
                                } catch (Exception e) {
                                }
                                try {
                                    //  MainActivity.serialSend("l");
                                } catch (Exception e) {
                                }

                                break;
                                //좌회전

                            }
                            else if(UserProfileData.userPathsListpathsList.get(i).position%10==2)
                            {
                                try {
                                    serialConsole.directionChange(SerialConsole.RIGHT_DIRECTION);
                                } catch (Exception e) {
                                }
                                try {
                                    //    MainActivity.serialSend("r");
                                } catch (Exception e) {
                                }
                                break;
                                //우회전
                            }



                        } else {
                            //  Toast.makeText(mContext, "직진", Toast.LENGTH_SHORT).show();
                            try {
                                serialConsole.directionChange(SerialConsole.STRAIGHT_DIRECTION);
                            } catch (Exception e) {
                            }
                            try {
                              //  MainActivity.serialSend("g");
                            } catch (Exception e) {
                            }
                            break;

                        }


                        Log.d("gpsriding", "user_pos : " + UserProfileData.user_riding_position + " ri_po : " + UserProfileData.userPathsListpathsList.get(i).position);
                    }//if
                }//지정한 반경 이하
                else {
                    // Toast.makeText(mContext, "직진", Toast.LENGTH_SHORT).show();
                    try {
                        serialConsole.directionChange(SerialConsole.STRAIGHT_DIRECTION);
                    } catch (Exception e) {
                    }
                    try {
                      //  MainActivity.serialSend("g");
                    } catch (Exception e) {
                    }


                }//반경 이상


            }//체크
            else {
                //   Toast.makeText(mContext, "둘다 null", Toast.LENGTH_SHORT).show();
            }

            beforeLocation = location;
        }catch (Exception e){}

    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    private void drawRoute(int color) {
//Color.BLUE


        // PathCalculator pathCalculator = new PathCalculator();


        LatLng oldPnt = null;

        PolylineOptions polyline = new PolylineOptions();
        polyline.width(4);
        polyline.color(color);
        polyline.geodesic(true);

        for (int i = 0; i < PathCalculator.PathsListpathsList.size(); i++) {
            GPoint pnt = PathCalculator.PathsListpathsList.get(i);
            LatLng curPnt = new LatLng(pnt.y, pnt.x);

            // Log.d("path",  PathCalculator.PathsListpathsList.size() + " " + curPnt.longitude + " " + curPnt.latitude);

            if (oldPnt == null) {
                oldPnt = curPnt;
                continue;
            } else {
                polyline.add(oldPnt, curPnt);
                oldPnt = curPnt;
            }
        }

        RouteFragment.googleMap.addPolyline(polyline);
    }

}
