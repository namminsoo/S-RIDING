package com.example.namsoo.s_riding_ui;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.namsoo.s_riding_ui.db.UserProfileData;
import com.example.namsoo.s_riding_ui.fragment_menu.BaseFragment;
import com.example.namsoo.s_riding_ui.fragment_menu.BodyInfoFragment;
import com.example.namsoo.s_riding_ui.fragment_menu.RecordFragment;
import com.example.namsoo.s_riding_ui.fragment_menu.SettingFragment;
import com.example.namsoo.s_riding_ui.navi.RouteFragment;
import com.example.namsoo.s_riding_ui.usbserial.DeviceListActivity;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.ProfilePictureView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static boolean usbConnectionChkFlag = false;

   /* 볼륨 조절 부분 */


    public static int APPROACH_MUSIC_VOLUME = 3;
    public int initVoulme = 0;
    public int musicVolumeValue;
    AudioManager am;


    /* 볼륨 조절 부분 */


    Set<BluetoothDevice> devices;
    CharSequence[] items;
    int mPariedDeviceCount = 0;


    static final int ACTION_ENABLE_BT = 101;
    TextView mTextMsg;
    TextView mEditData;
    BluetoothAdapter mBA;
    ListView mListDevice;
    ArrayList<String> mArDevice; // 원격 디바이스 목록
    static final String BLUE_NAME = "BluetoothEx";  // 접속시 사용하는 이름
    // 접속시 사용하는 고유 ID

    static final UUID BLUE_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    ClientThread mCThread = null; // 클라이언트 소켓 접속 스레드
    ServerThread mSThread = null; // 서버 소켓 접속 스레드
    SocketThread mSocketThread = null; // 데이터 송수신 스레드


    /*                     BlunoLibrary Add Section                      */
    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton float_action_btn_facebook;
    private FloatingActionButton float_action_btn_device;
    private FloatingActionButton float_action_btn_bluetooth;
    FrameLayout mInterceptorFrame;
    private TextView serialReceivedText;




    public static Context mContext;

    public static String TAG = "MainActivity";
    public static int startFagment = 1;
    public static boolean ridingState = false;



    Fragment newFragment = null;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();


        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        floatingActionsMenu.collapse();



        UserProfileData.context = getApplicationContext();

        init();
        setView();


        fragmentReplace(1);


        Intent intent = new Intent(getApplicationContext(), FacebookLoginActivity.class);
        startActivity(intent);


    }


    //init
    private void init() {

        FacebookSdk.sdkInitialize(getApplicationContext());

        RouteFragment.newinstance();
        BaseFragment.newinstance();
        BodyInfoFragment.newinstance();
        RecordFragment.newinstance();
        SettingFragment.newinstance();




    }//init end


    //setViewsetView=======================================================================================================================
    private void setView() {

        UserProfileData.profilePictureView = (ProfilePictureView) findViewById(R.id.profile_img);
        UserProfileData.profile_email = (TextView) findViewById(R.id.profile_email);
        UserProfileData.profile_name = (TextView) findViewById(R.id.profile_name);

        UserProfileData.profile_name.setText(UserProfileData.getString("name", "S-RIDING"));
        UserProfileData.profile_email.setText(UserProfileData.getString("mail", "S-RIDING@sriding.com"));
        UserProfileData.profilePictureView.setProfileId(UserProfileData.getString("img", null));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(getResources().getColor( R.color.menu_text_color));

        toolbar.setTitleTextColor(getResources().getColor(R.color.fer_white));
            toolbar.setTitle("S-RIDING");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        navigationView.setNavigationItemSelectedListener(this);

        //device
        float_action_btn_device = (FloatingActionButton) findViewById(R.id.action_device);
        float_action_btn_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insert code
                fragmentReplace(11);
                //Alert Dialog for selecting the BLE device
            }
        });

        //bluetooth
        float_action_btn_bluetooth = (FloatingActionButton) findViewById(R.id.action_bluetooth);
        float_action_btn_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //insert code

                // bluetooth 페어링
                boolean isBlue = canUseBluetooth();
                if (isBlue)
                // 페어링된 원격 디바이스 목록 구하기
                {
                    getParedDevice();
                }


                //Alert Dialog for selecting the BLE device
            }
        });


        //facebook
        float_action_btn_facebook = (FloatingActionButton) findViewById(R.id.action_facebook);
        float_action_btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentReplace(12);
            }
        });



        mInterceptorFrame = (FrameLayout) findViewById(R.id.fl_interceptor);
        mInterceptorFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch  " + "");
                if (floatingActionsMenu.isExpanded()) {
                    floatingActionsMenu.collapse();
                    return true;
                }
                return false;
            }
        });






    }
    //setViewsetView end=======================================================================================================================


    public boolean canUseBluetooth() {
        // 블루투스 어댑터를 구한다
        mBA = BluetoothAdapter.getDefaultAdapter();
        // 블루투스 어댑터가 null 이면 블루투스 장비가 존재하지 않는다.
        if (mBA == null) {
            return false;
        }

        Log.e(TAG, "before 1");
        // 블루투스 활성화 상태라면 함수 탈출
        if (mBA.isEnabled()) {
            return true;
        }
        // 사용자에게 블루투스 활성화를 요청한다
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, ACTION_ENABLE_BT);
        return false;

    }

    // 블루투스 활성화 요청 결과 수신
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_ENABLE_BT) {
            // 사용자가 블루투스 활성화 승인했을때
            if (resultCode == RESULT_OK) {
                // 페어링된 원격 디바이스 목록 구하기
                getParedDevice();
            }
            // 사용자가 블루투스 활성화 취소했을때
            else {
            }
        }
    }

    // 원격 디바이스 검색 시작
    public void startFindDevice() {
        // 원격 디바이스 검색 중지
        stopFindDevice();
        // 디바이스 검색 시작
        mBA.startDiscovery();
        // 원격 디바이스 검색 이벤트 리시버 등록
        registerReceiver(mBlueRecv, new IntentFilter(BluetoothDevice.ACTION_FOUND));
    }

    // 디바이스 검색 중지
    public void stopFindDevice() {
        // 현재 디바이스 검색 중이라면 취소한다
        try {
            if (mBA.isDiscovering()) {
                mBA.cancelDiscovery();
                // 브로드캐스트 리시버를 등록 해제한다
                unregisterReceiver(mBlueRecv);
            }
        }
        catch (Exception e)
        {}
    }

    // 원격 디바이스 검색 이벤트 수신
    BroadcastReceiver mBlueRecv = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == BluetoothDevice.ACTION_FOUND) {
                // 인텐트에서 디바이스 정보 추출
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 페어링된 디바이스가 아니라면
                if (device.getBondState() != BluetoothDevice.BOND_BONDED)
                {
                    Log.d(TAG, "cicicicicicici");
                }                // 디바이스를 목록에 추가
            }
        }
    };


    public void addDeviceToAlertDialog() {

        // 페어링된 장치가 있는 경우.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 장치 선택");

        String deviceInfo;


        // 각 디바이스는 이름과(서로 다른) 주소를 가진다. 페어링 된 디바이스들을 표시한다.
        List<String> listItems = new ArrayList<String>();
        for (BluetoothDevice device : devices) {
            // device.getName() : 단말기의 Bluetooth Adapter 이름을 반환.
//            listItems.add(device.getName());

            deviceInfo = device.getName() + " - " + device.getAddress();

            listItems.add(deviceInfo);
        }

        listItems.add("취소");  // 취소 항목 추가.

        // CharSequence : 변경 가능한 문자열.
        // toArray : List형태로 넘어온것 배열로 바꿔서 처리하기 위한 toArray() 함수.
        items = listItems.toArray(new CharSequence[listItems.size()]);
        // toArray 함수를 이용해서 size만큼 배열이 생성 되었다.
        listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                // TODO Auto-generated method stub
                if (item == mPariedDeviceCount) { // 연결할 장치를 선택하지 않고 '취소' 를 누른 경우.
                    Toast.makeText(getApplicationContext(), "연결할 장치를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                    //finish();
                } else { // 연결할 장치를 선택한 경우, 선택한 장치와 연결을 시도함.

                    String strItem = items[item].toString();

                    // 사용자가 선택한 디바이스의 주소를 구한다
                    int pos = strItem.indexOf(" - ");
                    if (pos <= 0) return;
                    String address = strItem.substring(pos + 3);

                    // 디바이스 검색 중지
                    stopFindDevice();
                    // 서버 소켓 스레드 중지
                    mSThread.cancel();
                    mSThread = null;

                    if (mCThread != null) return;
                    // 상대방 디바이스를 구한다
                    BluetoothDevice device = mBA.getRemoteDevice(address);
                    // 클라이언트 소켓 스레드 생성 & 시작
                    mCThread = new ClientThread(device);
                    mCThread.start();


                }

            }

        });

        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
        Log.e(TAG, "addDeviceToAlertDialog");


    }


    // 디바이스를 ListView 에 추가
    public void addDeviceToList(String name, String address) {
        // ListView 와 연결된 ArrayList 에 새로운 항목을 추가
        String deviceInfo = name + " - " + address;
        Log.d("tag1", "Device Find: " + deviceInfo);
        mArDevice.add(deviceInfo);
        // 화면을 갱신한다
        ArrayAdapter adapter = (ArrayAdapter) mListDevice.getAdapter();
        adapter.notifyDataSetChanged();
    }


    // 다른 디바이스에게 자신을 검색 허용
    public void setDiscoverable() {
        // 현재 검색 허용 상태라면 함수 탈출
        if (mBA.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
            return;
        // 다른 디바이스에게 자신을 검색 허용 지정
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        startActivity(intent);
    }

    // 페어링된 원격 디바이스 목록 구하기
    public void getParedDevice() {
        if (mSThread != null) return;
        // 서버 소켓 접속을 위한 스레드 생성 & 시작
        mSThread = new ServerThread();
        mSThread.start();

        // 블루투스 어댑터에서 페어링된 원격 디바이스 목록을 구한다
        devices = mBA.getBondedDevices();

        mPariedDeviceCount = devices.size();

        addDeviceToAlertDialog();


        // 원격 디바이스 검색 시작
        startFindDevice();

        // 다른 디바이스에 자신을 노출
        setDiscoverable();
    }

    // ListView 항목 선택 이벤트 함수
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        // 사용자가 선택한 항목의 내용을 구한다
        String strItem = mArDevice.get(position);

        // 사용자가 선택한 디바이스의 주소를 구한다
        int pos = strItem.indexOf(" - ");
        if (pos <= 0) return;
        String address = strItem.substring(pos + 3);

        // 디바이스 검색 중지
        stopFindDevice();
        // 서버 소켓 스레드 중지
        mSThread.cancel();
        mSThread = null;

        if (mCThread != null) return;
        // 상대방 디바이스를 구한다
        BluetoothDevice device = mBA.getRemoteDevice(address);
        // 클라이언트 소켓 스레드 생성 & 시작
        mCThread = new ClientThread(device);
        mCThread.start();
    }

    // 클라이언트 소켓 생성을 위한 스레드
    private class ClientThread extends Thread {
        private BluetoothSocket mmCSocket;

        // 원격 디바이스와 접속을 위한 클라이언트 소켓 생성
        public ClientThread(BluetoothDevice device) {
            try {
                mmCSocket = device.createInsecureRfcommSocketToServiceRecord(BLUE_UUID);
            } catch (IOException e) {
                showMessage("Create Client Socket error");
                return;
            }
        }

        public void run() {
            // 원격 디바이스와 접속 시도
            try {
                mmCSocket.connect();
            } catch (IOException e) {
                showMessage("Connect to server error");
                // 접속이 실패했으면 소켓을 닫는다
                try {
                    mmCSocket.close();
                } catch (IOException e2) {
                    showMessage("Client Socket close error");
                }
                return;
            }

            // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
            onConnected(mmCSocket);
        }

        // 클라이언트 소켓 중지
        public void cancel() {
            try {
                mmCSocket.close();
            } catch (IOException e) {
                showMessage("Client Socket close error");
            }
        }
    }

    // 서버 소켓을 생성해서 접속이 들어오면 클라이언트 소켓을 생성하는 스레드
    private class ServerThread extends Thread {
        private BluetoothServerSocket mmSSocket;

        // 서버 소켓 생성
        public ServerThread() {
            try {
                mmSSocket = mBA.listenUsingInsecureRfcommWithServiceRecord(BLUE_NAME, BLUE_UUID);
            } catch (IOException e) {
                showMessage("Get Server Socket Error");
            }
        }

        public void run() {
            BluetoothSocket cSocket = null;

            // 원격 디바이스에서 접속을 요청할 때까지 기다린다
            try {
                cSocket = mmSSocket.accept();
            } catch (IOException e) {
                showMessage("Socket Accept Error");
                return;
            }

            // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
            onConnected(cSocket);
        }

        // 서버 소켓 중지
        public void cancel() {
            try {
                mmSSocket.close();
            } catch (IOException e) {
                showMessage("Server Socket close error");
            }
        }
    }

    // 메시지를 화면에 표시
    public void showMessage(String strMsg) {
        // 메시지 텍스트를 핸들러에 전달
        Message msg = Message.obtain(mHandler, 0, strMsg);
        mHandler.sendMessage(msg);
        Log.d("tag1", strMsg);
    }

    // 메시지 화면 출력을 위한 핸들러
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String strMsg = (String) msg.obj;
            }
        }
    };

    // 원격 디바이스와 접속되었으면 데이터 송수신 스레드를 시작
    public void onConnected(BluetoothSocket socket) {
        showMessage("Socket connected");


        //연결시에 최초 볼륨 값을 가져옴
        initVoulme = volumeGetText();
        savePreferences(initVoulme);


        // 데이터 송수신 스레드가 생성되어 있다면 삭제한다
        if (mSocketThread != null)
            mSocketThread = null;
        // 데이터 송수신 스레드를 시작
        mSocketThread = new SocketThread(socket);
        mSocketThread.start();
    }

    // 데이터 송수신 스레드
    private class SocketThread extends Thread {
        private final BluetoothSocket mmSocket; // 클라이언트 소켓
        private InputStream mmInStream; // 입력 스트림
        private OutputStream mmOutStream; // 출력 스트림

        public SocketThread(BluetoothSocket socket) {
            mmSocket = socket;

            // 입력 스트림과 출력 스트림을 구한다
            try {
                mmInStream = socket.getInputStream();
                mmOutStream = socket.getOutputStream();
                String str = mmInStream.toString();

            } catch (IOException e) {
                showMessage("Get Stream error");
            }
        }

        // 소켓에서 수신된 데이터를 화면에 표시한다
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    // 입력 스트림에서 데이터를 읽는다
                    bytes = mmInStream.read(buffer);
                    String strBuf = new String(buffer, 0, bytes);


                    //approach
                    if (strBuf.equals("a")) {
                        volumeApproachSet();
                    }
                    //recede
                    else if (strBuf.equals("r")) {
                        volumeInitSet();
                    }

                    showMessage("Receive: " + strBuf);
                    SystemClock.sleep(1);
                } catch (IOException e) {
                    showMessage("Socket disconneted");
                    break;
                }
            }
        }

        // 데이터를 소켓으로 전송한다
        public void write(String strBuf) {
            try {
                // 출력 스트림에 데이터를 저장한다
                byte[] buffer = strBuf.getBytes();
                mmOutStream.write(buffer);
                showMessage("Send: " + strBuf);
            } catch (IOException e) {
                showMessage("Socket write error");
            }
        }
    }





    /* 볼륨 조절 */

    int volumeGetText() {

        musicVolumeValue = am.getStreamVolume(AudioManager.STREAM_MUSIC); //volume은 0~15 사이어야 함
//        tvCurMusic.setText(musicVolumeValue + " ");

        return musicVolumeValue;
    }


    void volumeApproachSet() {
        am.setStreamVolume(AudioManager.STREAM_MUSIC, APPROACH_MUSIC_VOLUME, 0);
        musicVolumeValue = am.getStreamVolume(AudioManager.STREAM_MUSIC); //volume은 0~15 사이어야 함
//        tvCurMusic.setText(musicVolumeValue + " ");
    }


    void volumeInitSet() {
        int musicValue = getPreferences();
        am.setStreamVolume(AudioManager.STREAM_MUSIC, musicValue, 0);
        musicVolumeValue = am.getStreamVolume(AudioManager.STREAM_MUSIC); //volume은 0~15 사이어야 함
//        tvCurMusic.setText(musicVolumeValue + " ");
    }


    // 값 불러오기
    private int getPreferences() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        return pref.getInt("musicValue", 0);
    }

    // 값 저장하기
    private void savePreferences(int musicValue) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("musicValue", musicValue);
        editor.commit();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //메뉴바에서 선택 할 때
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_sriding) {

            fragmentReplace(1);
        } else if (id == R.id.nav_route) {
            fragmentReplace(2);
            // Handle the camera action
        } else if (id == R.id.nav_bodyinfo) {
            fragmentReplace(3);
        } else if (id == R.id.nav_record) {
            fragmentReplace(4);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //Replace 함수 설정=======================================================================================================================
    public void fragmentReplace(int reqNewFragmentIndex) {

        newFragment = getFragment(reqNewFragmentIndex);

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.contentPanel, newFragment);

        transaction.commit();
    }

    private Fragment getFragment(int idx) {

        //메뉴바
        switch (idx) {
            case 1:
                newFragment = RouteFragment.newinstance();
                toolbar.setTitle("S-RIDING");
                break;
            case 2:
                newFragment = BaseFragment.newinstance();
                toolbar.setTitle("주행정보");
                break;

            case 3:
                newFragment = BodyInfoFragment.newinstance();
                toolbar.setTitle("신체정보");
                break;
            case 4:
                newFragment = RecordFragment.newinstance();
                toolbar.setTitle("주행기록");
                break;
            case 5:
                newFragment = SettingFragment.newinstance();
                toolbar.setTitle("설정");
                break;


            case 6:
//                newFragment = new RecordItemFragment();
//                toolbar.setTitle("RecordItem");
                break;


            //툴바
            case 11://디바이스
                floatingActionsMenu.collapse();
                Intent intent = new Intent(getApplicationContext(), DeviceListActivity.class);
                startActivity(intent);

                //toolbar.setTitle("설정");
                break;
            case 12://페이스북

                floatingActionsMenu.collapse();
                FacebookLoginActivity.first_start=1;
                Intent intent2 = new Intent(getApplicationContext(), FacebookLoginActivity.class);
                startActivity(intent2);

                break;
            default:
                break;
        }
        return newFragment;
    }
    //Replace 함수 설정 끝~=======================================================================================================================

    @Override
    protected void onResume() {
        super.onResume();

        if (usbConnectionChkFlag == true) {
            Log.e(TAG, "usbConnectionChkFlag after");
            usbConnectionChkFlag = false;

            boolean isBlue = canUseBluetooth();
            if (isBlue)
            // 페어링된 원격 디바이스 목록 구하기
            {
                getParedDevice();
            }


        } else {
            Log.e(TAG, "usbConnectionChkFlag before");
        }

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 디바이스 검색 중지
        stopFindDevice();

        // 스레드를 종료
        if (mCThread != null) {
            mCThread.cancel();
            mCThread = null;
        }
        if (mSThread != null) {
            mSThread.cancel();
            mSThread = null;
        }
        if (mSocketThread != null)
            mSocketThread = null;
    }

}
