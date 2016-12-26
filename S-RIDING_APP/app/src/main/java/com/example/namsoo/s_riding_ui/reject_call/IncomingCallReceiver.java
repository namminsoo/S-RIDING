package com.example.namsoo.s_riding_ui.reject_call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.example.namsoo.s_riding_ui.db.UserProfileData;
import com.example.namsoo.s_riding_ui.fragment_menu.BaseFragment;

import java.lang.reflect.Method;

public class IncomingCallReceiver extends BroadcastReceiver {
    String incomingNumber="";
    AudioManager audioManager;
    TelephonyManager telephonyManager;


    public void onReceive(Context context, Intent intent) {

            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                }
            }

            if (!incomingNumber.equals("")) {



                if (BaseFragment.flagRiding==true)//체크
                {

                    rejectCall();
                    startApp(context, incomingNumber);
                }



            }
        }



    private void startApp(Context context, String number){


        String message = UserProfileData.profile_name+"님이 현재 자전거 주행중 이므로 "+number + "에게서 걸려온 전화가 종료되었습니다.";


        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
            //Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }

        catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }




    }

    private void rejectCall(){
        try {
            Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method method = classTelephony.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            Object telephonyInterface = method.invoke(telephonyManager);
            Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
            methodEndCall.invoke(telephonyInterface);
        } catch (Exception e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}