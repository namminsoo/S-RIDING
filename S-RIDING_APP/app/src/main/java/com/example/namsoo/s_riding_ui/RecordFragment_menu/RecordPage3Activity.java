package com.example.namsoo.s_riding_ui.RecordFragment_menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.namsoo.s_riding_ui.MainActivity;
import com.example.namsoo.s_riding_ui.R;
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
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


//http://al02000.tistory.com/23
public class RecordPage3Activity extends Fragment {

    public static String TAG = "FacebookHelperFragment";

    UserProfileData userProfileData;

    public static boolean userLoginState = false;//로그인 상태

 /*   public static String userName = "";//이름
    public static String userEmail = "";//이메일
    public static Uri userImage;//프로필사진

    public static int userSex = 0; //성별 0 = init , 1 = male, 2 = female
    public static int userAge = 0;//나이

    public static int USER_MALE = 1;//머시마
    public static int USER_FEMALE = 2;//지지바*/


    private TextView mTextDetail;
    private TextView mTextEmail;
    private ProfilePictureView mImgProfile;
    private String profileId;

    private CallbackManager mCallbackManager;

    AccessTokenTracker accessTokenTracker;
    //LoginManager loginManager;

    //CallbackManager callbackManager;
    FrameLayout flContainer;
    ShareDialog shareDialog;
    ShareButton shareButton;
    private Bitmap image;

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




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        userProfileData= new UserProfileData();

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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_page3, container, false);

    }

    private void displayWelcomeMessage(Profile profile) {
        if (profile != null) {


            mTextDetail.setText(profile.getName());

            UserInfo.userName = profile.getName();
            UserInfo.userImage = profile.getProfilePictureUri(200, 200);

            UserProfileData.profile_name.setText(UserInfo.userName);


            profileId=profile.getId();
            mImgProfile.setProfileId(profile.getId());

            Log.d(TAG, profile.getProfilePictureUri(200, 200).toString());
            //mImgProfile.setImageURI(profile.getProfilePictureUri(100,100));

        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        mTextDetail = (TextView) view.findViewById(R.id.text_details);
        mTextEmail = (TextView) view.findViewById(R.id.texEmail);
        mImgProfile = (ProfilePictureView) view.findViewById(R.id.img_profile);

        mTextDetail.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/BMJUA.ttf"));
        mTextEmail.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/BMJUA.ttf"));



        FacebookSdk.sdkInitialize(getContext());
        mCallbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(getActivity());


        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        shareButton = (ShareButton) view.findViewById(R.id.share_button);


        ShareLinkContent content = new ShareLinkContent.Builder().setContentUrl(Uri.parse("http://developers.facebook.com")).build();

        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("S-Riding")
                .setContentDescription("S-Riding 입니다")
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.psychology.tarot"))
                        //.setContentUrl(Uri.parse(String.valueOf(R.drawable.icon)))
                        //.setImageUrl(Uri.parse(R.drawable.icon));
                .build();

        shareButton.setShareContent(linkContent);


//        shareButton.setClickable(true);
//        Toast.makeText(getContext(), shareButton.isClickable()+"", Toast.LENGTH_LONG).show();
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getContext(), "sharebutton", Toast.LENGTH_LONG).show();
               // postSriding();
            }
        });



        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallback);


        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile", "AccessToken");


        //페이스북 로그아웃시
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    //write your code here what to do when user logout
                    userLoginState = false;

                    registerProfile(false);



                    mTextEmail.setText("");
                    mTextDetail.setText("");
                    mImgProfile.setProfileId("111111111111111");

                    UserProfileData.profile_name.setText("S-RIDING");
                    UserProfileData.profile_email.setText("S-RIDING@s_riding.com");


                    Toast.makeText(MainActivity.mContext, "로그아웃 성공" + userLoginState, Toast.LENGTH_LONG).show();
                }
            }
        };





        //페이스북 로그인시
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                userLoginState = true;
                Toast.makeText(getContext(), "로그인 성공 " + userLoginState, Toast.LENGTH_LONG).show();
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
                                    mTextEmail.setText(email);
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




                if(ShareDialog.canShow(ShareLinkContent.class)){
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("S-Riding")
                            .setContentDescription("S-Riding 입니다")
                            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.psychology.tarot"))
                                    //.setContentUrl(Uri.parse(String.valueOf(R.drawable.icon)))
                                    //.setImageUrl(Uri.parse(R.drawable.icon));
                            .build();

                    shareDialog.show(linkContent);
                }


                if(shareDialog.canShow(ShareContent.class)){
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("S-Riding")
                            .setContentDescription("S-Riding 입니다")
                            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.psychology.tarot"))
                                    //.setContentUrl(Uri.parse(String.valueOf(R.drawable.icon)))
                                    //.setImageUrl(Uri.parse(R.drawable.icon));
                            .build();

                    shareDialog.show(linkContent);
                }



//
//
//                Log.e("TEST ", "Result" + loginResult);
//                Toast.makeText(getContext(), "success", Toast.LENGTH_LONG).show();
//                shareDialog = new ShareDialog(getActivity());
//                ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                        .setContentTitle("S-Riding")
//                        .setContentDescription("S-Riding입니다").build();
//                shareDialog.show(linkContent);
//                getActivity().finish();
//


            }


            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
                Log.v(TAG, exception.getCause().toString());
            }
        }); //로그인버튼


        FacebookSdk.sdkInitialize(getContext());
        mCallbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);




        shareButton.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if(ShareDialog.canShow(ShareLinkContent.class)){
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("S-Riding")
                            .setContentDescription("S-Riding 입니다")
                            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.psychology.tarot"))
                                    //.setContentUrl(Uri.parse(String.valueOf(R.drawable.icon)))
                                    //.setImageUrl(Uri.parse(R.drawable.icon));
                            .build();

                    shareDialog.show(linkContent);
                }


                if(shareDialog.canShow(ShareContent.class)){
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("S-Riding")
                            .setContentDescription("S-Riding 입니다")
                            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.psychology.tarot"))
                                    //.setContentUrl(Uri.parse(String.valueOf(R.drawable.icon)))
                                    //.setImageUrl(Uri.parse(R.drawable.icon));
                            .build();

                    shareDialog.show(linkContent);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

//
//        shareButton.setFragment(this);
//        shareButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(getContext(), "dsfsdfs", Toast.LENGTH_LONG).show();
//
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
//                alertDialogBuilder.setTitle("Really?");
//                alertDialogBuilder.setMessage("Are you sure?");
//                //null should be your on click listener
//                alertDialogBuilder.setPositiveButton("OK", null);
//                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//
//
//                Dialog dialog  = alertDialogBuilder.create();
//                dialog.show();
//
//            }
//        });
//        shareButton.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
//            @Override
//            public void onSuccess(Sharer.Result result) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//
//
    }


    void registerProfile(boolean b)
    {

        if(b==true) {
            userProfileData.putString("name", UserInfo.userName);
            userProfileData.putString("mail", UserInfo.userEmail);
            userProfileData.putString("img", profileId);
        }
        else if(b=false)
        {
            userProfileData.putString("name", "S-RIDING");
            userProfileData.putString("mail", "S-RIDING@sriding.com");
            userProfileData.putString("img", "111111111111111");


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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);


    }








    public void postSriding(){



        //image = Bitmap.createBitmap(R.drawable.icon);

        final AlertDialog.Builder shareDialog = new AlertDialog.Builder(getContext());
        shareDialog.setTitle("Share my S-Riding");
        shareDialog.setMessage("Share S-Riding to Facebook?");
        shareDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //share the image to Facebook
                BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.icon);
                Bitmap bitmap = drawable.getBitmap();
              //  SharePhoto photo = new SharePhoto.Builder().setBitmap(Bitmap.)
              //  SharePhoto photo = new SharePhoto.Builder().setImageUrl(Uri.parse("https://www.naver.com")).build();
                SharePhoto photo = new SharePhoto.Builder().setBitmap(bitmap).build();
                SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                shareButton.setShareContent(content);
                shareButton.performClick();
            }
        });
        shareDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        shareDialog.show();
    }




}
