package com.example.namsoo.s_riding_ui.fragment_menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.namsoo.s_riding_ui.R;
import com.example.namsoo.s_riding_ui.db.DBManager;
import com.example.namsoo.s_riding_ui.db.UserProfileData;
import com.example.namsoo.s_riding_ui.nayoung.GPSTracker;
import com.example.namsoo.s_riding_ui.nayoung.GetAddress;
import com.example.namsoo.s_riding_ui.nayoung.ServerTest;
import com.example.namsoo.s_riding_ui.nayoung.kcalCalculation;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

public class BaseFragment extends Fragment {

    public Context context;

    public  static BaseFragment baseFragment;



    //나영
    public TextView textDistance;
    public TextView textKcal;

    public TextView textSpeed;
    DBManager dbManager;

    //GPSTracker class
    private GPSTracker gps;

    private kcalCalculation kcalCal ;

    public java.util.logging.Handler mHandler;
    public Thread myThread;

    public boolean startFlag = false;
    public boolean ridingFlag = true;
    public boolean clickFlag =false;
    public boolean clickFlag2 =false;
    // private TimerTask mTask;
    private Timer mTimer;
    int timecount=0;
    int restTime = 0;
    int ridingTime = 0;
    int totalTime=0;
    double aveSpeed=0;
    double bestSpeed=0;
    boolean restFlag = false;

    int m_restFlag = 0;
    int m_ridingFlag = 1;


    String speed= null;
    String height = null;

    static public double startingLatitude = 0.0;
    static public double startingLongtitude = 0.0;
    String startingLocation= null;
    double destinationLatitude = 0.0;
    double destinationLongitude = 0.0;
    String destinationLocation = null;

    double distance = 0.0;


    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd  hh:mm");
    String strNow = sdfNow.format(date);




    public static final String TAG = "RouteFragment";




    public static BaseFragment newinstance()
    {
        if(baseFragment==null) {
            baseFragment = new BaseFragment();
        }
        return baseFragment;

    }



    //////////////////////////////////////////////////////////////////////////////////////////////////
    Button btnServerTest; //serverTest
    static String hostUrl = "http://jkpark.kr"; //서버주소
    static String facebookId = "1";
    static int READ_TIMEOUT = 10;
    static int CONN_TIMEOUT = 10;
    HttpURLConnection conn = null;
    OutputStream os = null;
    InputStream is = null;
    ByteArrayOutputStream baos = null;
    String urlStr = null;
    android.os.Handler handler = new android.os.Handler();
    ServerTest serverTest = new ServerTest();
    AlertDialog.Builder alertriding;
    AlertDialog.Builder alertbreaking;
    boolean flagBreaking = false;
    public static boolean flagRiding = false;
    Chronometer chronometer;
    Chronometer chronometer2;
    long timeWhenStopped = 0;
    long timeWhenStopped2=0;
    String chronoTime1 = null;
    String chronoTime2 = null;
    String min1 = null;
    String min2 = null;
    int resultMin = 0;
    Boolean downToUpMin = false;
    String second1 = null;
    String second2 = null;
    int resultSecond= 0;
    Boolean downToupSecond = false;
    boolean checkFlag=false;
    String totalRidingTime= null;
    static public Location firstLocation;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        context = view.getContext();
        // 나영
        textDistance =(TextView)view.findViewById(R.id.txt_distance);
        textKcal =(TextView)view.findViewById(R.id.txt_kcal);

        textSpeed =(TextView)view.findViewById(R.id.text_speed);
        dbManager = new DBManager(context, "S_RidingTest2.db", null, 1);
        kcalCal = new kcalCalculation();


        alertriding = new AlertDialog.Builder(getContext());
        alertbreaking = new AlertDialog.Builder(getContext());
        chronometer = (Chronometer) view.findViewById(R.id.chrono);
        chronometer2 = (Chronometer) view.findViewById( R.id.chrono2);

        alertriding.setTitle("Break/Stop").setMessage("휴식하시겠습니까? 종료하시겠습니까?").setCancelable(false)
                .setPositiveButton("휴식하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "휴식하기", Toast.LENGTH_LONG);
                        btnServerTest.setText("휴식중 입니다");
                        timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                        chronometer.stop();

                        chronometer2.setBase(SystemClock.elapsedRealtime() + timeWhenStopped2);
                        chronometer2.start();

                        flagBreaking = true;

                        flagRiding = false;
                    }
                })
                .setNegativeButton("종료하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "종료하기", Toast.LENGTH_LONG);
                        btnServerTest.setText("자전거 시작하기");


                        Log.d("ridingtime : ", (String) chronometer.getText());
                        Log.d("breaktime : ", (String) chronometer2.getText());
                        chronoTime1 = (String) chronometer.getText();
                        chronoTime2 = (String) chronometer2.getText();

                        min1 = chronoTime1.substring(0, 2);
                        min2 = chronoTime2.substring(0, 2);
                        second1 = chronoTime1.substring(3, 5);
                        second2 = chronoTime2.substring(3, 5);

                        resultMin = Integer.valueOf(min1) + Integer.valueOf(min2);
                        if (resultMin >= 60) {
                            downToUpMin = true;
                            resultMin -= 60;
                        }

                        resultSecond = Integer.valueOf(second1) + Integer.valueOf(second2);
                        if (resultSecond >= 60) {
                            downToupSecond = true;
                            resultSecond -= 60;
                        }

                        if(resultMin <10){
                            //   totalRidingTime = '0'+(resultMin-48)+":";
                            totalRidingTime = "0";
                            totalRidingTime += resultMin+":";
                        }else {
                            totalRidingTime = resultMin+":";

                        }
                        if(resultSecond<10){
                            //totalRidingTime +='0'+(resultSecond-48);
                            totalRidingTime += "0";
                            totalRidingTime += resultSecond;
                        }else{
                            totalRidingTime += resultSecond;
                        }

                      //  totalRidingTime = resultMin + ":" + resultSecond;


                        Log.d("totalTime : ", totalRidingTime);

                        GetAddress getAddress = new GetAddress();
                        destinationLocation = getAddress.Address(context);
                        destinationLatitude =  gps.getLongitude();
                        destinationLongitude = gps.getLatitude();


                        //db insert
                        String insertQuery = "insert into S_RidingTest2 values(null, '"+ UserProfileData.profile_email+" ', '"+strNow+"', '"+strNow+"','"+chronometer.getText()+"','"+chronometer2.getText()+"','"+totalRidingTime+"','"+gps.getDistance()+"','"+startingLocation+"','"+startingLatitude+"','"+startingLongtitude+"','"+destinationLocation+"','"+destinationLatitude+"','"+destinationLongitude+"','"+gps.getKcal()+"','"+gps.getAveSpeed()+"','"+gps.getMaxSpeed()+"', '"+gps.getinstantSpeed()+"','"+gps.getinstantHeight()+"');";
                        Log.d("insertQuery : ", insertQuery);


                        serverTest.setTest(0, String.valueOf(UserProfileData.profile_email), strNow, strNow, chronometer.getText() + "", chronometer2.getText() + "", String.valueOf(totalRidingTime), gps.getDistance() + "", startingLocation + "", startingLatitude + "", startingLongtitude + "", destinationLocation, destinationLatitude + "", destinationLongitude + "", gps.getKcal() + "", gps.getAveSpeed() + "", gps.getMaxSpeed() + "", gps.getinstantSpeed(), gps.getinstantHeight());
                        urlStr = hostUrl + "/ridingInfo";
                        SendRidingInfoThread thread = new SendRidingInfoThread(urlStr); //riding정보 보내는 thread
                        thread.start();

                        dbManager.insert(insertQuery);

                        chronometer2.setBase(SystemClock.elapsedRealtime());
                        chronometer2.stop();

                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.stop();

                        flagBreaking = false;
                        flagRiding = false;






                    }
                }).create();



        alertbreaking.setTitle("Riding/Stop").setMessage("주행하시겠습니까? 종료하시겠습니까?").setCancelable(false)
                .setPositiveButton("더 주행하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "더 주행하기", Toast.LENGTH_LONG);
                        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                        chronometer.start();

                        timeWhenStopped2 = chronometer2.getBase() - SystemClock.elapsedRealtime();
                        chronometer2.stop();

                        btnServerTest.setText("주행중 입니다");
                        flagRiding = true;
                        flagBreaking = false;
                    }
                })
                .setNegativeButton("종료하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        Log.d("ridingtime : ", (String) chronometer.getText());
                        Log.d("breaktime : ", (String) chronometer2.getText());
                        chronoTime1 = (String) chronometer.getText();
                        chronoTime2 = (String) chronometer2.getText();

                        min1 = chronoTime1.substring(0, 2);
                        min2 = chronoTime2.substring(0, 2);
                        second1 = chronoTime1.substring(3, 5);
                        second2 = chronoTime2.substring(3, 5);

                        resultMin = Integer.valueOf(min1) + Integer.valueOf(min2);
                         Log.d("resultMin : ", String.valueOf(resultMin));
                        if (resultMin >= 60) {
                            downToUpMin = true;
                            resultMin-=60;
                        }

                        resultSecond = Integer.valueOf(second1) + Integer.valueOf(second2);
                       // Log.d("resultSecond : ", String.valueOf(resultSecond));
                        if(resultSecond >=60){
                            downToupSecond = true;
                            resultSecond-=60;
                        }

                        if(resultMin <10){
                         //   totalRidingTime = '0'+(resultMin-48)+":";
                            totalRidingTime = "0";
                            totalRidingTime += resultMin+":";
                        }else {
                            totalRidingTime = resultMin+":";

                        }
                        if(resultSecond<10){
                            //totalRidingTime +='0'+(resultSecond-48);
                            totalRidingTime += "0";
                            totalRidingTime += resultSecond;
                        }else{
                            totalRidingTime += resultSecond;
                        }
                       // totalRidingTime = resultMin + ":"+ resultSecond;



                        Log.d("totalTime : ", totalRidingTime);



                        Log.d("min1 : ", min1);
                        Log.d("min2 : ", min2);
                        Log.d("second1 : ", second1);
                        Log.d("second2 : ", second2);



                        Toast.makeText(getContext(), "주행을 종료합니다", Toast.LENGTH_LONG);
                        btnServerTest.setText("자전거 시작하기");

                        GetAddress getAddress = new GetAddress();
                        destinationLocation = getAddress.Address(context);
//                        destinationLatitude = gps.getLatitude();
//                        destinationLongitude = gps.getLongitude();
                        destinationLatitude = gps.getLongitude();
                        destinationLongitude = gps.getLatitude();

                        //db insert
                        String insertQuery = "insert into S_RidingTest2 values(null, '"+ UserProfileData.profile_email+" ', '"+strNow+"', '"+strNow+"','"+chronometer.getText()+"','"+chronometer2.getText()+"','"+totalRidingTime+"','"+gps.getDistance()+"','"+startingLocation+"','"+startingLatitude+"','"+startingLongtitude+"','"+destinationLocation+"','"+destinationLatitude+"','"+destinationLongitude+"','"+gps.getKcal()+"','"+gps.getAveSpeed()+"','"+gps.getMaxSpeed()+"', '"+gps.getinstantSpeed()+"','"+gps.getinstantHeight()+"');";
                        Log.d("insertQuery : ", insertQuery);
                        dbManager.insert(insertQuery);

                        serverTest.setTest(0 , String.valueOf(UserProfileData.profile_email), strNow,strNow,chronometer.getText()+"",chronometer2.getText()+"",String.valueOf(totalRidingTime),gps.getDistance()+"",startingLocation+"",startingLatitude+"",startingLongtitude+"",destinationLocation,destinationLatitude+"",destinationLongitude+"",gps.getKcal()+"",gps.getAveSpeed()+"",gps.getMaxSpeed()+"",gps.getinstantSpeed(),gps.getinstantHeight());
                        urlStr = hostUrl + "/ridingInfo";
                        SendRidingInfoThread thread = new SendRidingInfoThread(urlStr); //riding정보 보내는 thread
                        thread.start();

                        chronometer2.setBase(SystemClock.elapsedRealtime());
                        chronometer2.stop();

                        timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime(); //zzz
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.stop();




                        flagBreaking = false;
                        flagRiding = false;

//
//                        serverTest.setTest(0, "qgq7@naver.com", "2015-11-12 10:22", "2015-11-12 11:00","00:33:33", "00:05:00", "00:38:33", "100", "군자동", "37.3232343","127.23342", "세종대학교", "37.234","127.3243","120", "10","25","0f10f30f30f30f20f10f23f", "1f1f1f1f3f3f2f2f3f");
//                        urlStr = hostUrl + "/ridingInfo";
//                        SendRidingInfoThread thread = new SendRidingInfoThread(urlStr); //riding정보 보내는 thread
//                        thread.start();



                        //init
                        resultSecond =0;
                        resultMin = 0;
                        totalRidingTime=null;

                    }
                }).create();





        btnServerTest = (Button)view.findViewById(R.id.serverTest);




        btnServerTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flagRiding == true){
                    alertriding.show();
                }else if(flagBreaking == true){
                    alertbreaking.show();
                }

                if(flagBreaking == false && flagRiding == false){ //라이딩 첫 시작시
                    Toast.makeText(getContext(), "주행을 시작합니다", Toast.LENGTH_LONG);

                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    flagRiding=true;
                    btnServerTest.setText("주행중입니다");

                    if(gps==null) {
                        gps = new GPSTracker(context);
                    }

                    GetAddress getaddress =new GetAddress();
                    startingLocation = getaddress.Address(context);
                    startingLatitude = UserProfileData.y;
                    startingLongtitude = UserProfileData.x;
                    firstLocation = gps.getLocation();
                }






                //ServerTest serverTest = new ServerTest(null, "qgq7@naver.com", "2015-11-12 10:22", "2015-11-12 11:00", "00:38:33", "100", "군자동", "37.3232343","127.23342", "세종대학교", "37.234","127.3243","120", "10","25","0f10f30f30f30f20f10f23f", "1f1f1f1f3f3f2f2f3f");

//
//                serverTest.setTest(0, "qgq7@naver.com", "2015-11-12 10:22", "2015-11-12 11:00","00:33:33", "00:05:00", "00:38:33", "100", "군자동", "37.3232343","127.23342", "세종대학교", "37.234","127.3243","120", "10","25","0f10f30f30f30f20f10f23f", "1f1f1f1f3f3f2f2f3f");
//                urlStr = hostUrl + "/ridingInfo";
//                SendRidingInfoThread thread = new SendRidingInfoThread(urlStr); //riding정보 보내는 thread
//                thread.start();
//







            }
        });










    }


















    /**
     * 주행정보 서버로 전송할 스레드 정의
     */
    class SendRidingInfoThread extends Thread {
        String urlStr;
        public SendRidingInfoThread(String inStr) {
            urlStr = inStr;
        }

        public void run() {

            try {
                //JSON 오브젝트를 생성한다, 서버로 전송할
                JSONObject job = new JSONObject(); //아래 제이슨 형식 만들어서 전송
                //job.put("_id", serverTest.getid());
                job.put("email", serverTest.getEmail());
                job.put("ridingStartTime", serverTest.getRidingStartTime());
                job.put("ridingStopTime", serverTest.getRidingStopTime());
                job.put("ridingTime", serverTest.getRidingTime());
                job.put("ridingBreakTime", serverTest.getRidingBreakTime());
                job.put("entireRidingTime", serverTest.getEntireRidingTime());
                job.put("entireRidingDistance", serverTest.getEntireRidingDistance());
                job.put("startingLocationName", serverTest.getStartingLocationName());
                job.put("startingLocationLatitude", serverTest.getStartingLocationLatitude());
                job.put("startingLocationLongitude", serverTest.getStartingLocationLongitude());
                job.put("destinationName", serverTest.getDestinationName());
                job.put("destinationLatitude", serverTest.getDestinationLatitude());
                job.put("destinationLongitude", serverTest.getDestinationLongitude());
                job.put("consumedCal", serverTest.getConsumedCal());
                job.put("averageSpeed", serverTest.getAverageSpeed());
                job.put("maxSpeed", serverTest.getMaxSpeed());

                job.put("instantSpeed", serverTest.getInstantSpeed());
                job.put("instantHeight", serverTest.getInstantHeight());

                final String output = request(urlStr, job);
                handler.post(new Runnable() {
                    public void run() {
                        //   txtMsg.setText(output);
                    }
                });

            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        private String request(String urlStr, JSONObject job) {
            String response = null;
            try{

                URL url = new URL(urlStr);
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(CONN_TIMEOUT * 1000);
                conn.setReadTimeout(READ_TIMEOUT * 1000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);



                os = conn.getOutputStream();
                os.write(job.toString().getBytes());
                os.flush();

                //실제 서버로 Request 하는 부분. (응답 코드를 받는다. 200성공, 나머지 에러)
                int responseCode = conn.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();
                    response = new String(byteData);
                }

            }catch(Exception ex){
                ex.printStackTrace();
            }
            return response;
        }
    }

    /**
     * 주행정보 서버로 전송할 스레드 정의
     */
    class GetRidingInfoThread extends Thread {
        String urlStr;
        public GetRidingInfoThread(String inStr) {
            urlStr = inStr;
        }

        public void run() {

            try {
                //JSON 오브젝트를 생성한다, 서버로 전송할
                JSONObject job = new JSONObject();
                job.put("_id", 12);
                job.put("startingLocation", 12345);
                final String output = request(urlStr, job);
                handler.post(new Runnable() {
                    public void run() {
                        //txtMsg.setText(output);
                        Toast.makeText(context,output, Toast.LENGTH_LONG ).show();
                    }
                });

            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        private String request(String urlStr, JSONObject job) {
            String response = null;
            try{

                URL url = new URL(urlStr);
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(CONN_TIMEOUT * 1000);
                conn.setReadTimeout(READ_TIMEOUT * 1000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                os = conn.getOutputStream();
                os.write(job.toString().getBytes());
                os.flush();

                //실제 서버로 Request 하는 부분. (응답 코드를 받는다. 200성공, 나머지 에러)
                int responseCode = conn.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while ((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();
                    response = new String(byteData);

                    //JSONObject responseJSON = new JSONObject(response);
                    //response = responseJSON.toString();
                }

            }catch(Exception ex){
                ex.printStackTrace();
            }
            return response;
        }
    }



















}