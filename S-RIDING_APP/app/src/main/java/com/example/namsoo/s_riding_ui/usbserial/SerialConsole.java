/* Copyright 2011-2013 Google Inc.
 * Copyright 2013 mike wakerly <opensource@hoho.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * Project home page: https://github.com/mik3y/usb-serial-for-android
 */

package com.example.namsoo.s_riding_ui.usbserial;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.namsoo.s_riding_ui.MainActivity;
import com.example.namsoo.s_riding_ui.nayoung.GPSTracker;
import com.example.namsoo.s_riding_ui.usbserial.driver.UsbSerialPort;
import com.example.namsoo.s_riding_ui.usbserial.util.HexDump;
import com.example.namsoo.s_riding_ui.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Monitors a single {@link UsbSerialPort} instance, showing all data
 * received.
 *
 * @author mike wakerly (opensource@hoho.com)
 */
public class SerialConsole {

    public static final int LEFT_DIRECTION = 1;
    public static final int STRAIGHT_DIRECTION = 2;
    public static final int RIGHT_DIRECTION = 3;

    private final String TAG = SerialConsole.class.getSimpleName();


    Handler handler;


    public static UsbSerialPort sPort = null;


    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private SerialInputOutputManager mSerialIoManager;


    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.d(TAG, "Runner stopped.");
                    GPSTracker.serialConsole = null;
                    GPSTracker.mock_index = 0;
                    Toast.makeText(MainActivity.mContext, "연결 끊김", Toast.LENGTH_LONG).show();
                    Log.d("usb", "연결 끊김");
                }

                @Override
                public void onNewData(final byte[] data) {

                    SerialConsole.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SerialConsole.this.updateReceivedData(data);
                        }
                    });
                }
            };


    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }


    public SerialConsole() {
        Log.d(TAG, "Resumed, port=" + sPort);


        if (sPort == null) {
            //    mTitleTextView.setText("No serial device.");
        } else {
            final UsbManager usbManager = (UsbManager) MainActivity.mContext.getSystemService(Context.USB_SERVICE);
            // Open a connection to the first available driver.
            UsbDeviceConnection connection = usbManager.openDevice(sPort.getDriver().getDevice());

            if (connection == null) {
                // mTitleTextView.setText("Opening device failed");
                return;
            }

            try {
                sPort.open(connection);
                sPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
                //sPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            } catch (IOException e) {
                Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
                //  mTitleTextView.setText("Error opening device: " + e.getMessage());
                try {
                    sPort.close();
                } catch (IOException e2) {
                    // Ignore.
                }
                sPort = null;
                return;
            }
            // mTitleTextView.setText("Serial device: " + sPort.getClass().getSimpleName());
        }
        onDeviceStateChange();

    }


    public static void directionChange(int s) {
        if (s == STRAIGHT_DIRECTION)//직진
        {
            byte[] data = new byte[1];
            data[0] = (byte) STRAIGHT_DIRECTION;
            //byte[] data = ByteBuffer.allocate(4).putInt(STRAIGHT_DIRECTION).array();
            try {
                sendData(data);
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (s == LEFT_DIRECTION)//왼쪽
        {
            byte[] data = new byte[1];
            data[0] = (byte) LEFT_DIRECTION;
            //byte[] data = ByteBuffer.allocate(4).putInt(LEFT_DIRECTION).array();
            try {
                sendData(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (s == RIGHT_DIRECTION)//오른쪽
        {

            byte[] data = new byte[1];
            data[0] = (byte) RIGHT_DIRECTION;
            //byte[] data = ByteBuffer.allocate(4).putInt(RIGHT_DIRECTION).array();
            try {
                sendData(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    void onPause() {
        stopIoManager();
        if (sPort != null) {
            try {
                sPort.close();
            } catch (IOException e) {
                // Ignore.
            }
            sPort = null;
        }
    }

    void onResume() {

    }

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (sPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    //The part receiving data from the device connected
    private void updateReceivedData(byte[] data) {
        final String message = "Read " + data.length + " bytes: \n"
                + HexDump.dumpHexString(data) + "\n\n";
        // mDumpTextView.append(message);
        //  mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());

    }

    private static void sendData(byte[] data) throws IOException {
        sPort.write(data, 0);
    }


    public static void show(Context context, UsbSerialPort port) {


        sPort = port;

        GPSTracker.serialConsole = new SerialConsole();
        Toast.makeText(MainActivity.mContext, "Device가 연결되었습니다.", Toast.LENGTH_SHORT).show();

        MainActivity.usbConnectionChkFlag = true;

//        new LoadAssync().execute();




      /*  final Intent intent = new Intent(context, SerialConsole.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        context.startActivity(intent);*/
    }


    public static class LoadAssync extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        //사이트 접속해서 데이터 추출하는 부분
        @Override
        protected Void doInBackground(String... params) {

            try {
                GPSTracker.serialConsole = new SerialConsole();
                Toast.makeText(MainActivity.mContext, "Device가 연결되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (Exception e)

            {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(final Void unused) {


        }

    }
}



