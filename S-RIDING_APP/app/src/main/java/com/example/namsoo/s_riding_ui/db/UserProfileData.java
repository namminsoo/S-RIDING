package com.example.namsoo.s_riding_ui.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.example.namsoo.s_riding_ui.navi.PathInfo;
import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

/**
 * Created by namsoo on 2015-10-15.
 */

public  class UserProfileData {


    //신상정보
    public static ProfilePictureView profilePictureView;
    public static TextView profile_email;
    public static TextView profile_name;

    public static double x=0.0;
    public static double y=0.0;


    public static int userWeight=50;
    public static int userHeight;
    public static int userAge;
    public static int userGender;






    //길안내정보
    public static ArrayList<PathInfo> userPathsListpathsList;

    public static int user_riding_position;




    public static Context context;

    final  public  static  String SPName="s_riding";


    public static void putString(String key, String value) {
        if(key=="name")
        {
            profile_name.setText(value);
        }
        else if(key=="mail")
        {
            profile_email.setText(value);
        }
        else if(key=="img")
        {
            profilePictureView.setProfileId(value);
        }

        //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences pref = context.getSharedPreferences(SPName,0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }


    public static String getString(String key, String dftValue) {
        //SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences pref = context.getSharedPreferences(SPName, 0);

        try {
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }





}