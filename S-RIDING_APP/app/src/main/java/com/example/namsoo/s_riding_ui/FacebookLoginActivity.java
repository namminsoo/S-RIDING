package com.example.namsoo.s_riding_ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.namsoo.s_riding_ui.db.UserProfileData;
import com.example.namsoo.s_riding_ui.supporter.UserInfo;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by namsoo on 2015-11-13.
 */
public class FacebookLoginActivity extends Activity{


    LoginButton btnFacebookLogin;
ProgressBar fb_login_progressbar;

    boolean loginFlag=false;


    public static int first_start=0;



    public static String TAG = "FacebookHelperFragment";


    public static boolean userLoginState = false;//로그인 상태

 /*   public static String userName = "";//이름
    public static String userEmail = "";//이메일
    public static Uri userImage;//프로필사진

    public static int userSex = 0; //성별 0 = init , 1 = male, 2 = female
    public static int userAge = 0;//나이

    public static int USER_MALE = 1;//머시마
    public static int USER_FEMALE = 2;//지지바*/


    private String profileId;

    private CallbackManager mCallbackManager;

    AccessTokenTracker accessTokenTracker;
    //LoginManager loginManager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        btnFacebookLogin =(LoginButton)findViewById(R.id.btnFacebookLogin);


        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        fb_login_progressbar = (ProgressBar)findViewById(R.id.fb_login_progressbar);
        fb_login_progressbar.setVisibility(View.INVISIBLE);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        AccessTokenTracker tracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken newToken) {

            }
        };
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

                displayWelcomeMessage(newProfile);
            }
        };
        tracker.startTracking();
        profileTracker.startTracking();






        btnFacebookLogin.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        //btnFacebookLogin.setFragment(this);
        btnFacebookLogin.registerCallback(mCallbackManager, mCallback);


        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile", "AccessToken");


        //페이스북 로그아웃시
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {

                    loginFlag=true;

                    //write your code here what to do when user logout
                    userLoginState = false;

                    registerProfile(false);




                    UserProfileData.profile_name.setText("S-RIDING");
                    UserProfileData.profile_email.setText("S-RIDING@s_riding.com");
                    UserProfileData.profilePictureView.setProfileId("111111111111111");

                    Toast.makeText(getApplicationContext(), "로그아웃 완료", Toast.LENGTH_LONG).show();
                }
            }
        };


        //페이스북 로그인시
        btnFacebookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                userLoginState = true;
                Toast.makeText(getApplicationContext(), "로그인 완료", Toast.LENGTH_LONG).show();

                //System.out.println("onSuccess");

                String accessToken = loginResult.getAccessToken().getToken();

                Log.i(TAG, accessToken);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {
                                Log.d("LoginActivity", response.toString());
                                try {
                                    String id = object.getString("id");
                                    URL profile_pic = null;
                                    try {
                                        profile_pic = new URL("http://graph.facebook.com/" + id + "/picture?type=large");
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d(TAG, profile_pic + "");

                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    UserInfo.userEmail = email;

                                    // String gender = object.getString("gender");
                                    //  String birthday = object.getString("birthday");
                                    Log.d(TAG, "email : " + email);


                                    registerProfile(true);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

                finish();
            }


            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                Log.v(TAG, exception.getCause().toString());
            }
        });








    }





    //...
    public boolean isFacebookLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
    }

    public FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();


            displayWelcomeMessage(profile);

        }

        @Override
        public void onCancel() {
            Log.d(TAG, "cancel");
        }

        @Override
        public void onError(FacebookException e) {
            Log.d(TAG, e.getCause().toString());
        }

    };




    private void displayWelcomeMessage(Profile profile) {
        if (profile != null) {



            UserInfo.userName = profile.getName();

            UserInfo.userImage = profile.getProfilePictureUri(200, 200);

            profileId=profile.getId();


            Log.d(TAG, profile.getProfilePictureUri(200, 200).toString());
            //mImgProfile.setImageURI(profile.getProfilePictureUri(100,100));

        }
    }



    void registerProfile(boolean b)
    {

        if(b==true) {
            UserProfileData.putString("name", UserInfo.userName);
            UserProfileData.putString("mail", UserInfo.userEmail);
            UserProfileData.putString("img", profileId);
        }
        else if(b=false)
        {
            UserProfileData.putString("name", "S-RIDING");
            UserProfileData.putString("mail", "S-RIDING@sriding.com");
            UserProfileData.putString("img", "111111111111111");


            UserInfo.userSex = 0;
            UserInfo.userAge = 0;

        }
        //UserProfileData.profile_name.setText(UserInfo.userName);
        // UserProfileData.profile_email.setText(UserInfo.userEmail);

        //UserProfileData.profilePictureView.setProfileId(profileId);

    }


    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMessage(profile);



        loginFlag = isFacebookLoggedIn();

        if(first_start==0) {
            if (loginFlag) {


                Toast.makeText(getApplicationContext(), "로그인 성공",Toast.LENGTH_LONG).show();


                finish();

            } else {

                UserProfileData.profile_name.setText(UserProfileData.getString("name", "S-RIDING"));
                UserProfileData.profile_email.setText(UserProfileData.getString("mail", "S-RIDING@sriding.com"));
                UserProfileData.profilePictureView.setProfileId(UserProfileData.getString("img", null));
                first_start++;
            }

        }
        first_start=0;







    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }








}
