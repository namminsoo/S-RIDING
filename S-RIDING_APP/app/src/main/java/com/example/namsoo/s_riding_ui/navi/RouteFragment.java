package com.example.namsoo.s_riding_ui.navi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.namsoo.s_riding_ui.R;
import com.example.namsoo.s_riding_ui.db.DBManager;
import com.example.namsoo.s_riding_ui.db.UserProfileData;
import com.example.namsoo.s_riding_ui.nayoung.GPSTracker;
import com.example.namsoo.s_riding_ui.nayoung.kcalCalculation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPInputStream;


public class RouteFragment extends Fragment {

    // public static TextView bearing;

    public static RouteFragment routeFragment;

    //나영
    public TextView textDistance;
    public TextView textKcal;
    public TextView textRidingTime;
    public TextView textSpeed;
    DBManager dbManager;

    //GPSTracker class

    private kcalCalculation kcalCal;

    public java.util.logging.Handler mHandler;
    public Thread myThread;

    public boolean startFlag = false;
    public boolean ridingFlag = false;
    public boolean clickFlag = false;
    public boolean buttonFlag = false;


    private TimerTask mTask;
    private Timer mTimer;
    int timecount = 0;
    int restTime = 0;
    int ridingTime = 0;
    int totalTime = 0;
    double aveSpeed = 0;
    double bestSpeed = 0;
    boolean restFlag = false;

    String speed = null;
    String height = null;

    double startingLatitude = 0.0;
    double startingLongtitude = 0.0;
    String startingLocation = null;
    double destinationLatitude = 0.0;
    double destinationLongitude = 0.0;
    String destinationLocation = null;

    double distance = 0.0;

    Button btnStop_base;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd");
    String strNow = sdfNow.format(date);
    ImageButton routeSearchBtn;


    double totalKcal = 0;

//위 나영


    //GPSTracker class
    private GPSTracker gps;


    public static final String TAG = "RouteFragment";

    public static final int SEARCH_STARTPOINT = 2001;
    public static final int SEARCH_DESTPOINT = 2002;
    public static int locationSearchMode;
    public static int timeoutMs = 10000;


    //===== DIALOG CONSTANTS =====//
    public static final int LOCATION_INFO_LIST = 1001;
    public static final int ROUTE_INFO_LIST = 1002;
    public static final int SEARCH_PROGRESS_DIALOG = 1003;
    public static final int RECENTLY_ROUTE_LIST = 1004;
    public static final int WARNING_INSERT_SDCARD = 1005;
    public static final int RECENTLY_DEST_LIST = 1006;


    public ProgressDialog progressDialog;
    private AlertDialog mDialog = null;
    public Context context;


    boolean myLocationFound = false;
    LocationManager locationManager;

    int pageNo;
    int maxPageNo;


    //Button startSearchBtn;
    ImageButton endSearchBtn;
    ImageButton startRouteBtn;
    ImageButton endRouteBtn;
    //Button routeInfoBtn;

    //EditText startSearchEdit;
    EditText endSearchEdit;


    TextView endText;

    //TextView routeText;

    StringBuffer StrBuf;

    Handler handler = new Handler();


    ArrayList<LocationInfo> locationInfoList;
    ListView locationInfoListView;
    LocationInfoListAdapter locationInfoListAdapter;

    ArrayList<RouteInfo> routeInfoList;
    ListView routeInfoListView;
    RouteInfoListAdapter routeInfoListAdapter;


    ListView recentlyRouteListView;


    ListView recentlyDestListView;



    StringParser strParser;
    String encodedStr;
    String searchStr;


    ArrayList<GPoint> boundsList;
    ArrayList<GPoint> pathsList;


    LocationInfo selectedStartLocation;
    LocationInfo selectedDestLocation;

    public static MapView mMapView;
    public static GoogleMap googleMap;

    public static LatLng myGeoPoint;
    public static LatLng startPoint;
    public static LatLng endPoint;


    public static RouteFragment newinstance() {
        if (routeFragment == null) {
            routeFragment = new RouteFragment();
        }
        return routeFragment;

    }

    //onCreateView=========================================================================================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        try {
            setDismiss(mDialog);
        } catch (Exception e) {
        }

        return inflater.inflate(R.layout.fragment_route, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        //bearing = (TextView) view.findViewById(R.id.bearing);


        // startSearchBtn = (Button) view.findViewById(R.id.startSearchBtn);
        endSearchBtn = (ImageButton) view.findViewById(R.id.endSearchBtn);


        // routeInfoBtn = (Button) view.findViewById(R.id.routeInfoBtn);

        // startSearchEdit = (EditText) view.findViewById(R.id.startSearchEdit);
        endSearchEdit = (EditText) view.findViewById(R.id.endSearchEdit);

        //    routeText = (TextView) view.findViewById(R.id.textView_route);

        // initialze location list
        locationInfoList = new ArrayList<LocationInfo>();

        locationInfoListView = new ListView(context);
        locationInfoListView.setCacheColorHint(Color.argb(0, 0, 0, 0));
        locationInfoListAdapter = new LocationInfoListAdapter(context, locationInfoList);
        locationInfoListView.setAdapter(locationInfoListAdapter);


        // initialze route list
        routeInfoList = new ArrayList<RouteInfo>();
        routeInfoListView = new ListView(context);
        routeInfoListView.setCacheColorHint(Color.argb(0, 0, 0, 0));
        routeInfoListAdapter = new RouteInfoListAdapter(context, routeInfoList);
        routeInfoListView.setAdapter(routeInfoListAdapter);
        routeInfoListView.setClickable(false);


        // initialize recent route
        recentlyRouteListView = new ListView(context);
        recentlyRouteListView.setCacheColorHint(Color.argb(0, 0, 0, 0));

        // initialize recent dest
        recentlyDestListView = new ListView(context);
        recentlyDestListView.setCacheColorHint(Color.argb(0, 0, 0, 0));


        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Add this line
        try {
            googleMap = mMapView.getMap();


            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {


                    Location location = googleMap.getMyLocation();
                    UserProfileData.x = location.getLongitude();
                    UserProfileData.y = location.getLatitude();

                    LatLng latLng2 = new LatLng(UserProfileData.y, UserProfileData.x);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng2, 15));
                    return false;
                }
            });

        } catch (Exception e) {
        }
        GPSTracker.bool_riding = false;

        initMyLocation();


        //도착지 검색
        endSearchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                searchStr = endSearchEdit.getText().toString();

                if (TextUtils.isEmpty(searchStr)) {
                    Toast.makeText(context, "목적지 검색어를 입력하세요.", Toast.LENGTH_LONG).show();
                } else {


                    locationSearchMode = SEARCH_DESTPOINT;

                    locationInfoList.clear();


                    locationInfoListView.setAdapter(locationInfoListAdapter);

                    pageNo = 1;
                    encodedStr = URLEncoder.encode(searchStr.trim());

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(endSearchEdit.getWindowToken(), 0);

                    searchLocation();
                }
            }
        });


        //리스트뷰에서 경로 선택 했을 때 이벤트
        locationInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                LocationInfo locationInfo = locationInfoList.get(position);

                String locationName = locationInfo.getName();
                String xCoord = locationInfo.getX();
                String yCoord = locationInfo.getY();
                String address = locationInfo.getAddress();

                if (address == null || address.equals("null")) {
                    Toast.makeText(context, locationName + "\n" + xCoord + ", " + yCoord, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, locationName + "\n" + address, Toast.LENGTH_LONG).show();
                }


                switch (locationSearchMode) {
                 /*   case SEARCH_STARTPOINT:
                        selectedStartLocation = locationInfo;
                        startSearchEdit.setText(locationName);
                        startText.setTextColor(Color.BLUE);

                        break;*/

                    case SEARCH_DESTPOINT:
                        selectedDestLocation = locationInfo;
                        endSearchEdit.setText(locationName);
                        endText.setTextColor(Color.BLUE);


                        break;
                    default:
                        break;
                }
                setDismiss(mDialog);


                //===
                Log.d(TAG, "routeSearchBtn clicked.");

                LocationInfo locationInfo2 = new LocationInfo();

                String xCoord2 = String.valueOf(UserProfileData.x);
                String yCoord2 = String.valueOf(UserProfileData.y);

                Log.d("minsoo", "x : " + xCoord2 + " y : " + yCoord2);
                //Toast.makeText(getContext(), "x : " + xCoord2 + " y : " + yCoord2, Toast.LENGTH_LONG).show();
                locationInfo2.setName("내 위치");
                locationInfo2.setX(xCoord2);
                locationInfo2.setY(yCoord2);

                selectedStartLocation = locationInfo2;


                Log.d("minsoo", "x : " + selectedStartLocation.getX() + " y : " + selectedStartLocation.getY());
                requestRoute(selectedStartLocation, selectedDestLocation);


            }
        });


        endText = (TextView) view.findViewById(R.id.endText);

        endText.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/BMJUA.ttf"));
        //  startText.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/BMJUA.ttf"));

        LatLng latLng = new LatLng(UserProfileData.y, UserProfileData.x);

        try {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }catch (Exception e){}

    }

    //onCreateView end======================================================================================================================================================


    public void initMyLocation() {


        //내 gps 지우기
        try {

            myLocationFound = false;

            if (gps == null) {
                gps = new GPSTracker(context);

            }

            //  Toast.makeText(context, "내 위치 확인을 시작합니다." + " x : " + UserProfileData.x + " y : " + UserProfileData.y, Toast.LENGTH_LONG).show();


            myLocationFound = true;

        } catch (Exception e) {
            Toast.makeText(context, "위치를 찾을 수 없습니다.", Toast.LENGTH_LONG).show();
        }
    }


    //지역검색=============================================================================================================================================
    private void searchLocation() {
        try {

            mDialog = createDialog(SEARCH_PROGRESS_DIALOG);
            mDialog.show();
            //showDialog(SEARCH_PROGRESS_DIALOG);

            RequestLocationThread thread = new RequestLocationThread();
            thread.start();
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
        }
    }

    //지역검색
    class RequestLocationThread extends Thread {
        public RequestLocationThread() {
        }

        public void run() {
            try {
                Socket sock = createSocket(new URL(BasicInfo.base_url));
                DataOutputStream outstream = new DataOutputStream(sock.getOutputStream());
                DataInputStream instream = new DataInputStream(sock.getInputStream());

                String outStr = "";
                if (maxPageNo < 2) {
                    outStr = "GET /search2/local.nhn?query=" + encodedStr
                            + "&menu=location HTTP/1.1\r\n";

                    Log.e(TAG, "mp < 2");
                } else {
                    outStr = "GET /search2/local.nhn?queryRank=1&type=SITE_1&siteOrder=41241211&siteSort=0&page="
                            + pageNo
                            + "&busLinkYN=0&re=1&query="
                            + encodedStr
                            + "&menu=location HTTP/1.1\r\n";

                    Log.e(TAG, "mp =< 2");
                }

                outStr = outStr
                        + "Host: map.naver.com\r\n"
                        + "Connection: keep-alive\r\n"
                        + "User-Agent: Mozilla/5.0\r\n"
                        + "Referer: http://map.naver.com/\r\n"
                        + "Content-Type: application/x-www-form-urlencoded; charset=utf-8\r\n"
                        + "charset: utf-8\r\n" + "Accept: */*\r\n"
                        + "Accept-Encoding: gzip,deflate,sdch\r\n"
                        + "Accept-Language: ko-KR,ko\r\n"
                        + "Accept-Charset: windows-949,utf-8\r\n" + "\r\n";

                outstream.write(outStr.getBytes());
                outstream.flush();

                byte[] inBytes = new byte[1024];
                ArrayList<byte[]> ByteBuffer = new ArrayList<byte[]>();
                StrBuf = new StringBuffer();
                int totalBytes = 0;

                while (true) {
                    int readCount = instream.read(inBytes, 0, inBytes.length);
                    if (readCount <= 0) {
                        break;
                    }

                    totalBytes = totalBytes + readCount;
                    byte[] aBytes = new byte[readCount];
                    System.arraycopy(inBytes, 0, aBytes, 0, readCount);
                    ByteBuffer.add(aBytes);

                    Log.d(TAG, "readCount : " + readCount);
                }

                Log.d(TAG, "totalBytes : " + totalBytes);
                byte[] outBytes = new byte[totalBytes];
                int curIndex = 0;
                for (int i = 0; i < ByteBuffer.size(); i++) {
                    byte[] aBytes = ByteBuffer.get(i);
                    System.arraycopy(aBytes, 0, outBytes, curIndex, aBytes.length);
                    curIndex = curIndex + aBytes.length;
                }

                String inStr = new String(outBytes, 0, curIndex, "UTF8");
                StrBuf.append(inStr);


                Log.e(TAG, "full : " + StrBuf);


                // trim contents
                int curSepIndex = 0;
                int addCount = 0;
                for (int i = 0; i < outBytes.length; i++) {
                    if (outBytes[i] == '\r' || outBytes[i] == '\n') {
                        addCount++;
                    } else {
                        addCount = 0;
                    }
                    if (addCount > 3) {
                        curSepIndex = i + 1;
                        break;
                    }
                }

                Log.d("DEBUG", "curSepIndex : " + curSepIndex);
                String curContentsStr = "";
                if (curSepIndex > 0) {
                    curContentsStr = new String(outBytes, curSepIndex, curIndex - curSepIndex, "UTF8");
                }
                Log.d("DEBUG", "curContents : " + curContentsStr);


                // content encoding
                boolean isPlain = true;
                String curStr = new String(outBytes);
                int startIndex = curStr.indexOf("Content-Encoding:");
                int endIndex = curStr.indexOf("\n", startIndex + 17);
                if (startIndex > 0 && endIndex > 0) {
                    String contentTypeStr = curStr.substring(startIndex + 18, endIndex);
                    Log.e(TAG, "Content-Encoding String : " + contentTypeStr);

                    if (contentTypeStr != null && contentTypeStr.indexOf("gzip") > -1) {
                        isPlain = false;
                    }
                }


                // conversion of gzip
                if (!isPlain) {
                    ByteArrayInputStream curInstream = new ByteArrayInputStream(outBytes, curSepIndex, curIndex - curSepIndex);
                    GZIPInputStream curStream = new GZIPInputStream(curInstream);

                    int totalCount = 0;
                    int count = 1;
                    byte data[] = new byte[1024];
                    ArrayList<byte[]> parsedStrList = new ArrayList<byte[]>();
                    while (count > 0) {
                        count = curStream.read(data, 0, 1024);
                        if (count > 0) {
                            byte[] curData = new byte[count];
                            System.arraycopy(data, 0, curData, 0, count);
                            parsedStrList.add(curData);
                            totalCount = totalCount + count;
                        }
                    }

                    byte[] curContentsBytes = new byte[totalCount];
                    int curOffset = 0;
                    for (int i = 0; i < parsedStrList.size(); i++) {
                        byte[] aBytes = parsedStrList.get(i);
                        System.arraycopy(aBytes, 0, curContentsBytes, curOffset, aBytes.length);
                        curOffset = curOffset + aBytes.length;
                    }

                    String curContentsData = new String(curContentsBytes, 0, totalCount, "UTF8");
                    Log.d("DEBUG", "curContentsData : " + curContentsData);

                    curStream.close();

                    // replace the string buffer data
                    StrBuf = new StringBuffer();
                    StrBuf.append(curContentsData);

                }


                closeSocket(sock, outstream, instream);

                // post for the display of search result
                handler.post(locationSearchResultRunnable);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    public Socket createSocket(URL url) {

        String hostname = url.getHost();
        int port = url.getPort();
        if (port < 1) {
            port = 80;
        }

        Log.d(TAG, "URL in createSocket() : " + hostname + ", " + port);

        long now = SystemClock.uptimeMillis();

        // Create a socket with a timeout
        try {
            InetAddress addr = InetAddress.getByName(hostname);

            SocketAddress sockaddr = new InetSocketAddress(addr, port);
            Socket sock = new Socket();
            sock.connect(sockaddr, timeoutMs);

            Log.d(TAG, "Connected to " + sockaddr);
            Log.d(TAG, "Spent " + (SystemClock.uptimeMillis() - now)
                    + " millis connecting to the server.");

            return sock;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void closeSocket(Socket sock, DataOutputStream outstream,
                            DataInputStream instream) throws IOException {
        if (outstream != null) {
            outstream.close();
        }

        if (instream != null) {
            instream.close();
        }

        if (sock != null) {
            sock.close();
        }
    }


    Runnable locationSearchResultRunnable = new Runnable() {
        public void run() {
            try {

                String allStr = StrBuf.toString();
                //Log.d("DEBUG", "RESULT : \n\n" + allStr);

                // JSON 객체로 변환
                JSONObject json = new JSONObject(allStr);

                Iterator iter = json.keys();
                int curIndex = 0;
                while (iter.hasNext()) {
                    String attName = (String) iter.next();
                    //Log.d(TAG, "attribute name for root #" + curIndex + " : " + attName);

                    if (attName != null && attName.equals("result")) {
                        JSONObject resultObj = json.getJSONObject(attName);

                        if (resultObj != null) {
                            //Log.d(TAG, "result object found.");
                            Iterator resultIter = resultObj.keys();
                            int curIndex2 = 0;
                            while (resultIter.hasNext()) {
                                String resultAttName = (String) resultIter.next();
                                //Log.d(TAG, "attribute name result #" + curIndex2 + " : " + resultAttName);

                                if (resultAttName != null && resultAttName.equals("totalCount")) {
                                    int totalCount = resultObj.getInt("totalCount");
                                    Log.d(TAG, "totalCount : " + totalCount);
                                }

                                if (resultAttName != null && resultAttName.equals("site")) {
                                    JSONObject siteObj = resultObj.getJSONObject(resultAttName);
                                    if (siteObj != null) {
                                        Log.d(TAG, "site object found.");

                                        Iterator siteIter = siteObj.keys();
                                        int curIndex3 = 0;
                                        while (siteIter.hasNext()) {
                                            String siteAttName = (String) siteIter.next();
                                            //Log.d(TAG, "attribute name site #" + curIndex3 + " : " + siteAttName);
                                            if (siteAttName != null && siteAttName.equals("list")) {
                                                JSONArray listArr = siteObj.getJSONArray(siteAttName);
                                                if (listArr != null) {
                                                    Log.d(TAG, "list array object found.");

                                                    for (int i = 0; i < listArr.length(); i++) {
                                                        JSONObject itemObj = (JSONObject) listArr.get(i);
                                                        Iterator itemIter = itemObj.keys();

                                                        Log.d(TAG, "processing record #" + i + " ...");
                                                        LocationInfo aInfo = new LocationInfo();

                                                        String nameStr = "";
                                                        String xStr = "";
                                                        String yStr = "";

                                                        int curIndex4 = 0;
                                                        while (itemIter.hasNext()) {
                                                            String itemAttrName = (String) itemIter.next();
                                                            //Log.d(TAG, "attribute name item #" + curIndex4 + " : " + itemAttrName);

                                                            try {

                                                                // name
                                                                if (itemAttrName != null && itemAttrName.equals("name")) {
                                                                    nameStr = itemObj.getString(itemAttrName);
                                                                    aInfo.setName(nameStr);
                                                                }

                                                                // tel
                                                                if (itemAttrName != null && itemAttrName.equals("tel")) {
                                                                    String telStr = itemObj.getString(itemAttrName);
                                                                    aInfo.setTel(telStr);
                                                                }

                                                                // address
                                                                if (itemAttrName != null && itemAttrName.equals("address")) {
                                                                    String addressStr = itemObj.getString(itemAttrName);
                                                                    aInfo.setAddress(addressStr);
                                                                }

                                                                // x
                                                                if (itemAttrName != null && itemAttrName.equals("x")) {
                                                                    xStr = itemObj.getString(itemAttrName);
                                                                    aInfo.setX(xStr);
                                                                }

                                                                // y
                                                                if (itemAttrName != null && itemAttrName.equals("y")) {
                                                                    yStr = itemObj.getString(itemAttrName);
                                                                    aInfo.setY(yStr);
                                                                }

                                                            } catch (Exception ext) {
                                                                ext.printStackTrace();
                                                                break;
                                                            }

                                                            curIndex4++;
                                                        }


                                                        Log.d(TAG, "name : " + nameStr + ", x : " + xStr + ", y : " + yStr);

                                                        locationInfoList.add(aInfo);

                                                    }
                                                }
                                            }

                                            curIndex3++;
                                        }
                                    }

                                }

                                curIndex2++;
                            }
                        }
                    }

                    curIndex++;
                }

                locationInfoListAdapter.notifyDataSetChanged();

                setDismiss(mDialog);

                mDialog = createDialog(LOCATION_INFO_LIST);
                mDialog.show();

                handler.removeCallbacks(locationSearchResultRunnable);

            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(context, "locationSearchResultRunnable Catch", Toast.LENGTH_LONG).show();
            }

            try {
                //Thread.sleep(200);
            } catch (Exception ext) {
                ext.printStackTrace();
            }

            // get more
            if (pageNo < maxPageNo) {
                // getNextPage();
            } else {

            }

        }
    };
    //지역검색 end=============================================================================================================================================


    //경로검색===================출발지 좌표        도착지 좌표==========================================================================================================================================
    private void requestRoute(LocationInfo sLoc, LocationInfo eLoc) {
        googleMap.clear();

        routeInfoList.clear();
        routeInfoListAdapter.clear();

        double startLatitude = Double.parseDouble(sLoc.getY());
        double startLongitude = Double.parseDouble(sLoc.getX());

        double destLatitude = Double.parseDouble(eLoc.getY());
        double destLongitude = Double.parseDouble(eLoc.getX());

        //Log.d("minsoo", "인자 " + startLatitude + "  " + startLongitude);

        // Toast.makeText(getContext(), "11x : "+startLongitude+ " y : "+startLatitude, Toast.LENGTH_LONG).show();

        //LatLng latLng = new LatLng(startLongitude,startLatitude);


        startPoint = new LatLng(startLatitude, startLongitude);

        // Log.d("ㅇㅇㅇㅇ",startPoint.toString()+" "+startPoint.latitude+" "+startPoint.longitude);
        //Toast.makeText(getContext(), "12x : "+startPoint.latitude+ " y : "+startPoint.longitude, Toast.LENGTH_LONG).show();

        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(startPoint);
        startMarker.title("출발지점");
        startMarker.snippet("출발지점");
        startMarker.draggable(true);
        startMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.point_icon_01));

        googleMap.addMarker(startMarker);

        endPoint = new LatLng(destLatitude, destLongitude);

        MarkerOptions destMarker = new MarkerOptions();
        destMarker.position(endPoint);
        destMarker.title("도착지점");
        destMarker.snippet("도착지점");
        destMarker.draggable(true);
        destMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.point_icon_02));

        googleMap.addMarker(destMarker);

        try {
            mDialog = createDialog(SEARCH_PROGRESS_DIALOG);
            mDialog.show();

            RequestRouteThread thread = new RequestRouteThread(sLoc, eLoc);
            thread.start();
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 15));

    }

    //출발 도착 좌표를 넘겨받아 길을 표시해주는 곳인지 찾아주는 곳인지 보자.
    class RequestRouteThread extends Thread {

        LocationInfo startLocInfo;
        LocationInfo destLocInfo;

        public RequestRouteThread(LocationInfo sLoc, LocationInfo eLoc) {
            startLocInfo = sLoc;
            destLocInfo = eLoc;
        }

        public void run() {
            try {
                Socket sock = createSocket(new URL(BasicInfo.base_url));
                DataOutputStream outstream = new DataOutputStream(sock
                        .getOutputStream());
                DataInputStream instream = new DataInputStream(sock
                        .getInputStream());

                String outStr = "";
                String encodedStartName = URLEncoder.encode(startLocInfo.getName());
                String encodedDestName = URLEncoder.encode(destLocInfo.getName());

                outStr = "GET /findroute2/findCarRoute.nhn?via=&call=route2&output=json&car=0&mileage=12.4&start="
                        + startLocInfo.getX()
                        + "%2C"
                        + startLocInfo.getY()
                        + "%2C"
                        + encodedStartName // 출발지
                        + "&destination="
                        + destLocInfo.getX()
                        + "%2C"
                        + destLocInfo.getY() + "%2C" + encodedDestName // 도착지
                        + "&search=2 HTTP/1.1\r\n";

                outStr = outStr
                        + "Host: map.naver.com\r\n"
                        + "Connection: keep-alive\r\n"
                        + "User-Agent: Mozilla/5.0\r\n"
                        + "Referer: http://map.naver.com/\r\n"
                        + "Content-Type: application/x-www-form-urlencoded; charset=utf-8\r\n"
                        + "charset: utf-8\r\n" + "Accept: */*\r\n"
                        + "Accept-Encoding: gzip,deflate,sdch\r\n"
                        + "Accept-Language: ko-KR,ko\r\n"
                        + "Accept-Charset: windows-949,utf-8\r\n" + "\r\n";

                // debugging
                // Log.d(TAG, "REQUEST : " + outStr);

                Log.d(TAG, "request sending...");
                outstream.write(outStr.getBytes());
                outstream.flush();

                byte[] inBytes = new byte[1024];
                ArrayList<byte[]> ByteBuffer = new ArrayList<byte[]>();
                StrBuf = new StringBuffer();
                int totalBytes = 0;

                while (true) {
                    int readCount = instream.read(inBytes, 0, inBytes.length);
                    if (readCount <= 0) {
                        break;
                    }

                    totalBytes = totalBytes + readCount;
                    byte[] aBytes = new byte[readCount];
                    System.arraycopy(inBytes, 0, aBytes, 0, readCount);
                    ByteBuffer.add(aBytes);

                    Log.d(TAG, "readCount : " + readCount);
                }

                Log.d(TAG, "totalBytes : " + totalBytes);
                byte[] outBytes = new byte[totalBytes];
                int curIndex = 0;
                for (int i = 0; i < ByteBuffer.size(); i++) {
                    byte[] aBytes = ByteBuffer.get(i);
                    System.arraycopy(aBytes, 0, outBytes, curIndex, aBytes.length);
                    curIndex = curIndex + aBytes.length;
                }

                String inStr = new String(outBytes, 0, curIndex, "UTF8");
                StrBuf.append(inStr);


                Log.e(TAG, "full : " + StrBuf);
                String curStr = StrBuf.toString();

                outBytes = curStr.getBytes();

                // trim contents
                int curSepIndex = 0;
                int addCount = 0;
                for (int i = 0; i < outBytes.length; i++) {
                    if (outBytes[i] == '\r' || outBytes[i] == '\n') {
                        addCount++;
                    } else {
                        addCount = 0;
                    }
                    if (addCount > 3) {
                        curSepIndex = i + 1;
                        break;
                    }
                }

                Log.d("DEBUG", "curSepIndex : " + curSepIndex);
                String curContentsStr = "";
                if (curSepIndex > 0) {
                    curContentsStr = new String(outBytes, curSepIndex, curIndex - curSepIndex, "UTF8");
                }
                Log.d("DEBUG", "curContents : " + curContentsStr);


                // content encoding
                boolean isPlain = true;
                int startIndex = curStr.indexOf("Content-Encoding:");
                int endIndex = curStr.indexOf("\n", startIndex + 17);
                if (startIndex > 0 && endIndex > 0) {
                    String contentTypeStr = curStr.substring(startIndex + 18, endIndex);
                    Log.e(TAG, "Content-Encoding String : " + contentTypeStr);

                    if (contentTypeStr != null && contentTypeStr.indexOf("gzip") > -1) {
                        isPlain = false;
                    }
                }


                // conversion of gzip
                if (!isPlain) {
                    ByteArrayInputStream curInstream = new ByteArrayInputStream(outBytes, curSepIndex, curIndex - curSepIndex);
                    GZIPInputStream curStream = new GZIPInputStream(curInstream);
                    int curSize = curStream.available();
                    Log.d("DEBUG", "curSize : " + curSize);
                    int count;
                    byte data[] = new byte[curSize];
                    count = curStream.read(data, 0, curSize);
                    Log.d("DEBUG", "curData : " + new String(data, 0, count));

                    curStream.close();

                    StrBuf = new StringBuffer();
                    StrBuf.append(new String(data, 0, count));

                } else {

                    StrBuf = new StringBuffer();
                    StrBuf.append(new String(outBytes, curSepIndex, curIndex - curSepIndex));

                }

                closeSocket(sock, outstream, instream);

                // post for the display of search result
                handler.post(routeSearchResultRunnable);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }


    //학정 4층에서이까지 봄 어렵다...... 짜잉나

    Runnable routeSearchResultRunnable = new Runnable() {
        public void run() {

            String allStr = StrBuf.toString();

            try {
                /***/

                strParser = new StringParser(context);

                // initialize lists
                boundsList = new ArrayList<GPoint>();
                pathsList = new ArrayList<GPoint>();

                allStr = StrBuf.toString();

                char[] allBytes = allStr.toCharArray();

                String targetStr = null;
                int foundLength = 0;
                int startIndex = 0;
                int endIndex = 0;

                strParser.setCurrentOffset(0);

                // parse totalDistance
                String foundStr = strParser.findData(allStr, allBytes, "totalDistance\":", ",");
                if (foundStr == null) {
                    Log.d(TAG, "foundStr is null.");
                }
                // Log.d(TAG, "found totalDistance : " + foundStr);
                String totalDistance = foundStr;

                // route section
                foundStr = strParser.findData(allStr, allBytes, "point\":", "]");
                if (foundStr == null) {
                    Log.d(TAG, "foundStr is null.");
                }
                // Log.d(TAG, "found route section : " + foundStr);

                String routeSectionStr = foundStr;

                char[] routeSectionBytes = routeSectionStr.toCharArray();

                // path
                foundStr = strParser.findData(allStr, allBytes, "path\":\"",
                        "\"");
                if (foundStr == null) {
                    Log.d(TAG, "foundStr is null.");
                }
                // Log.d(TAG, "found path : " + foundStr);

                String[] paths = foundStr.split("\\ ");
                if (paths == null) {
                    Log.d(TAG, "paths is invalid.");
                }
                Log.d(TAG, "length of path points : " + paths.length);

                for (int i = 0; i < paths.length; i++) {
                    String[] outCoord = paths[i].split("\\,");
                    if (outCoord == null || outCoord.length < 2) {
                        Log.d(TAG, "splitted coord is null or less than 2 : " + paths[i]);
                        continue;
                    }
                    GPoint outPnt = transformPoint(outCoord[0], outCoord[1]);
                    if (outPnt == null) {
                        Log.d(TAG, "out point is null.");
                        continue;
                    }
                    pathsList.add(outPnt);

                    // Log.d(TAG, "out path #" + i + " : " + outPnt.y + ", " +
                    // outPnt.x);
                }

                // bound
                foundStr = strParser.findData(allStr, allBytes, "bound\":\"", "\"");
                if (foundStr == null) {
                    Log.d(TAG, "foundStr is null.");
                }
                // Log.d(TAG, "found bound : " + foundStr);

                String[] bounds = foundStr.split("\\,");
                if (bounds == null || bounds.length != 4) {
                    Log.d(TAG, "bounds is invalid.");
                }
                Log.d(TAG, "bounds : " + bounds[0] + ", " + bounds[1] + ", " + bounds[2] + ", " + bounds[3]);

                GPoint outPnt = transformPoint(bounds[0], bounds[1]);
                boundsList.add(outPnt);

                outPnt = transformPoint(bounds[2], bounds[3]);
                boundsList.add(outPnt);

                int routeItemCount = 0;
                int routeSectionOffset = 0;

                strParser.setCurrentOffset(0);

                ArrayList<String> routeItemList = new ArrayList<String>();
                while (true) {
                    Log.d(TAG, "RouteItem #" + routeItemCount);

                    targetStr = "{\"name\":\"";
                    startIndex = strParser.findString(routeSectionBytes,
                            targetStr.toCharArray(), strParser
                                    .getCurrentOffset());
                    foundLength = targetStr.length();

                    targetStr = "{\"name\":\"";
                    endIndex = strParser.findString(routeSectionBytes, targetStr.toCharArray(), startIndex + foundLength);
                    strParser.setCurrentOffset(endIndex);

                    if (endIndex < 0) {
                        Log.d(TAG, "foundStr is null.");

                        String outStr = routeSectionStr
                                .substring(routeSectionOffset);
                        // Log.d(TAG, "found route item : " + outStr);
                        routeItemList.add(outStr);
                        break;
                    } else {
                        foundStr = routeSectionStr.substring(startIndex,
                                endIndex);

                        // Log.d(TAG, "found route item : " + foundStr);
                        routeItemList.add(foundStr);
                    }

                    routeItemCount++;
                    routeSectionOffset = strParser.getCurrentOffset() - 1;
                }

                for (int i = 0; i < routeItemList.size(); i++) {
                    Log.d(TAG, "processing RouteItem #" + i);

                    String routeItemStr = routeItemList.get(i);
                    char[] routeItemBytes = routeItemStr.toCharArray();

                    // name
                    targetStr = "name\":\"";
                    startIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), 0);
                    foundLength = targetStr.length();

                    targetStr = "\"";
                    endIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), startIndex + foundLength);

                    foundStr = routeItemStr.substring(startIndex + foundLength,
                            endIndex);
                    // Log.d(TAG, "found route name : " + foundStr);
                    String routeName = foundStr;

                    foundLength = targetStr.length();

                    // key
                    targetStr = "key\":\"";
                    startIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), endIndex + foundLength);
                    foundLength = targetStr.length();

                    targetStr = "\"";
                    endIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), startIndex + foundLength);

                    foundStr = routeItemStr.substring(startIndex + foundLength,
                            endIndex);
                    // Log.d(TAG, "found route key : " + foundStr);
                    String routeKey = foundStr;

                    foundLength = targetStr.length();

                    // x
                    targetStr = "x\":";
                    startIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), endIndex + foundLength);
                    foundLength = targetStr.length();

                    targetStr = ",";
                    endIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), startIndex + foundLength);

                    foundStr = routeItemStr.substring(startIndex + foundLength,
                            endIndex);
                    // Log.d(TAG, "found route x : " + foundStr);
                    String routeX = foundStr;

                    foundLength = targetStr.length();

                    // y
                    targetStr = "y\":";
                    startIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), endIndex + foundLength);
                    foundLength = targetStr.length();

                    targetStr = ",";
                    endIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), startIndex + foundLength);

                    foundStr = routeItemStr.substring(startIndex + foundLength,
                            endIndex);
                    // Log.d(TAG, "found route y : " + foundStr);
                    String routeY = foundStr;

                    foundLength = targetStr.length();

                    // guide no
                    targetStr = "no\":";
                    startIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), endIndex + foundLength);
                    foundLength = targetStr.length();

                    targetStr = ",";
                    endIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), startIndex + foundLength);

                    foundStr = routeItemStr.substring(startIndex + foundLength,
                            endIndex);
                    // Log.d(TAG, "found guide no : " + foundStr);
                    String guideNo = foundStr;

                    foundLength = targetStr.length();

                    // guide name
                    targetStr = "name\":\"";
                    startIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), endIndex + foundLength);
                    foundLength = targetStr.length();

                    targetStr = "\"";
                    endIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), startIndex + foundLength);

                    foundStr = routeItemStr.substring(startIndex + foundLength,
                            endIndex);
                    // Log.d(TAG, "found guide name : " + foundStr);
                    String guideName = foundStr;

                    foundLength = targetStr.length();

                    // road distance
                    targetStr = "distance\":";
                    startIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), endIndex + foundLength);
                    foundLength = targetStr.length();

                    targetStr = ",";
                    endIndex = strParser.findString(routeItemBytes, targetStr
                            .toCharArray(), startIndex + foundLength);

                    foundStr = routeItemStr.substring(startIndex + foundLength,
                            endIndex);

                    // Log.d(TAG, "found road distance : " + foundStr);
                    String roadDistance = foundStr;

                    foundLength = targetStr.length();

                    //추가===============================================================
                    RouteInfo aInfo = new RouteInfo();
                    aInfo.setName(routeName);
                    aInfo.setKey(routeKey);
                    aInfo.setX(routeX);
                    aInfo.setY(routeY);
                    aInfo.setGuideNo(guideNo);
                    aInfo.setGuideName(guideName);
                    aInfo.setRoadDistance(roadDistance);

                    routeInfoList.add(aInfo);//===============================================================================경로추가?!!

                    if (i == routeItemList.size() - 1) {
                        RouteInfo destInfo = new RouteInfo();
                        destInfo.setName(selectedDestLocation.getName());
                        destInfo.setX(selectedDestLocation.getX());
                        destInfo.setY(selectedDestLocation.getY());
                        destInfo.setRoadDistance("0");
                        destInfo.setGuideName("도착");
                        routeInfoList.add(destInfo);

                    }

                }

                routeInfoListAdapter.notifyDataSetChanged();
                setDismiss(mDialog);//프로그레스바 제거

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {


                for (int i = 0; i < routeInfoList.size(); i++) {
                    Log.d("info", routeInfoList.size() + " " + routeInfoList.get(0).getX() + " " + routeInfoList.get(0).getY());
                }


                Thread.sleep(200);
                drawRoute(Color.BLUE);

                Toast.makeText(getContext(), "자전거 주행을 시작합니다.", Toast.LENGTH_LONG).show();

                handler.removeCallbacks(routeSearchResultRunnable);

            } catch (Exception ext) {
                ext.printStackTrace();
            }


            //===================


           /* final Intent intent = new Intent(context, NavigationDisplayActivity.class);
            intent.putExtra("selectedStartLocation", selectedStartLocation);
            intent.putExtra("selectedDestLocation", selectedDestLocation);
            intent.putExtra("routeInfoList", routeInfoList);
            intent.putExtra("pathsList", pathsList);*/


             /*   mDialog = createDialog(SEARCH_PROGRESS_DIALOG);
                mDialog.show();
*/
           /* Handler handler = new Handler()
            {
                @Override
                public void handleMessage(Message msg)
                {

                    setDismiss(mDialog);
                    startActivity(intent);
                }
            };
            handler.sendEmptyMessageDelayed(0, 500);    // ms, 3초후 종료시킴*/


            // setDismiss(mDialog);
            //  startActivity(intent);


        }
    };


    private void drawRoute(int color) {

        PathCalculator.PathsListpathsList = pathsList;

        PathCalculator pathCalculator = new PathCalculator();


        LatLng oldPnt = null;

        PolylineOptions polyline = new PolylineOptions();
        polyline.width(4);
        polyline.color(color);
        polyline.geodesic(true);

        for (int i = 0; i < pathsList.size(); i++) {
            GPoint pnt = pathsList.get(i);
            LatLng curPnt = new LatLng(pnt.y, pnt.x);

            Log.d("path", pathsList.size() + " " + curPnt.longitude + " " + curPnt.latitude);

            if (oldPnt == null) {
                oldPnt = curPnt;
                continue;
            } else {
                polyline.add(oldPnt, curPnt);
                oldPnt = curPnt;
            }
        }

        googleMap.addPolyline(polyline);
        GPSTracker.bool_riding = true;

        handler.removeCallbacks(routeSearchResultRunnable);
    }//경로추가


    private GPoint transformPoint(String xStr, String yStr) {
        double x = 0.0D;
        double y = 0.0D;
        try {
            x = new Double(xStr).doubleValue();
            y = new Double(yStr).doubleValue();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return transformPoint(x, y);
    }

    private GPoint transformPoint(double x, double y) {

        double utmX = (x - 340000000.0D) / 10.0D;
        double utmY = (y - 130000000.0D) / 10.0D;

        GPoint pnt = new GPoint(utmX, utmY);

        GPoint newPnt = CoordinateTransformation.fromRectangularToGeodetic(pnt);

        newPnt.x *= UTMK.u;
        newPnt.y *= UTMK.u;

        return newPnt;
    }


    //경로검색 end============================================================================================================================


    /**
     * base 다이얼로그
     *
     * @return ab
     */
    private AlertDialog createDialog(int id) {
        AlertDialog.Builder builder;


        switch (id) {
            case LOCATION_INFO_LIST:
                builder = new AlertDialog.Builder(context);

                if (locationSearchMode == SEARCH_STARTPOINT) {
                    builder.setTitle("출발지 선택");
                } else if (locationSearchMode == SEARCH_DESTPOINT) {
                    builder.setTitle("목적지 선택");
                }

                builder.setView(locationInfoListView);

                return builder.create();
            case ROUTE_INFO_LIST:
                builder = new AlertDialog.Builder(context);

                int routeInfoCount = routeInfoListAdapter.getCount();
                builder.setTitle("경로 정보");

                builder.setView(routeInfoListView);

                return builder.create();

            case RECENTLY_ROUTE_LIST:
                builder = new AlertDialog.Builder(context);

                //int recentlyRouteCount = routeDataCursorAdapter.getCount();
                builder.setTitle("최근 경로");

                builder.setView(recentlyRouteListView);

                return builder.create();

            case RECENTLY_DEST_LIST:
                builder = new AlertDialog.Builder(context);

                //int recentlyDestCount = routeDestCursorAdapter.getCount();

                builder.setTitle("최근 목적지");

                builder.setView(recentlyDestListView);

                return builder.create();

            case SEARCH_PROGRESS_DIALOG:
                progressDialog = new ProgressDialog(context);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("검색중입니다.");
                progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            progressDialog.dismiss();
                        }

                        return true;
                    }
                });

                return progressDialog;
        }

        return null;
    }

    /**
     * 다이얼로그 종료
     *
     * @param dialog
     */
    private void setDismiss(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            try {
                ((ViewGroup) locationInfoListView.getParent()).removeView(locationInfoListView);
            } catch (Exception e) {
            }
        }
    }


    @Override
    public void onDestroy() {

        Intent intent = new Intent(getContext(), GPSTracker.class);

        getActivity().stopService(intent);




        super.onDestroy();
    }
}
