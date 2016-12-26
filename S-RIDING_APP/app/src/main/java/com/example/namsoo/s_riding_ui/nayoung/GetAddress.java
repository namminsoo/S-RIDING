package com.example.namsoo.s_riding_ui.nayoung;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import com.example.namsoo.s_riding_ui.db.UserProfileData;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by 김나영 on 2015-10-22.
 */
public class GetAddress {

    //http://areumwing.blogspot.kr/2012/05/blog-post.html


    public void GetAddress(){

    }

    public String Address(Context mContext){
        List<Address> address;
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        //GPSTracker gps = new GPSTracker(mContext);
        String realAddress=null;

        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                Log.d("namminsoofalut : ", UserProfileData.x +" , "+UserProfileData.y );
//                address = geocoder.getFromLocation(UserProfileData.x, UserProfileData.y, 1);
                address = geocoder.getFromLocation(UserProfileData.y, UserProfileData.x, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    realAddress = address.get(0).getAddressLine(0).toString();
                    Log.d("Location : ", realAddress);

                    // // 전송할 주소 데이터 (위도/경도 포함 편집)
                    // bf.append(currentLocationAddress).append("#");
                    // bf.append(lat).append("#");
                    // bf.append(lng);
                }
            }

        } catch (IOException e) {
            Toast.makeText(mContext, "주소취득 실패", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }

        return realAddress;

    }


}
