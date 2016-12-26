package com.example.namsoo.s_riding_ui.navi;

import android.location.Location;
import android.util.Log;

import com.example.namsoo.s_riding_ui.db.UserProfileData;

import java.util.ArrayList;

/**
 * Created by namsoo on 2015-10-23.
 */
public class PathCalculator {


    public static ArrayList<GPoint> PathsListpathsList;//기존 path 정보 가져옴

    public static ArrayList<PathInfo> pathInfoArrayList;//새로만들 path정보

    public static ArrayList<PathInfo> routeInfoArrayList;//새로만들 path정보를 방향전환용으로만 가지고 있을 데이터터


   int size;


    public PathCalculator() {
        size = PathsListpathsList.size();

        pathInfoArrayList = new ArrayList<PathInfo>();

        routeInfoArrayList= new ArrayList<PathInfo>();

        init();
    }


    public void init() {
        for (int i = 0; i < size - 1; i++) {


            double angle = getAngle(PathsListpathsList.get(i).getX(), PathsListpathsList.get(i).getY()
                    , PathsListpathsList.get(i + 1).getX(), PathsListpathsList.get(i + 1).getY());

            double distance = getDistance(PathsListpathsList.get(i).getX(), PathsListpathsList.get(i).getY()
                    , PathsListpathsList.get(i + 1).getX(), PathsListpathsList.get(i + 1).getY());

            int position = 0;


            int x_sign;
            int y_sign;


            if (PathsListpathsList.get(i).getX() - PathsListpathsList.get(i + 1).getX() > 0) {
                x_sign = 1;
            } else if (PathsListpathsList.get(i).getX() - PathsListpathsList.get(i + 1).getX() == 0) {
                x_sign = 0;
            } else {
                x_sign = -1;
            }


            if (PathsListpathsList.get(i).getY() - PathsListpathsList.get(i + 1).getY() > 0) {
                y_sign = 1;
            } else if (PathsListpathsList.get(i).getY() - PathsListpathsList.get(i + 1).getY() == 0) {
                y_sign = 0;
            } else {
                y_sign = -1;
            }


            if (x_sign == y_sign) {
                if (x_sign > 0) {
                    position = 5;//++ 북동
                } else {
                    position = 1;//-- 남서
                }
            } else {
                if (x_sign > 0) {
                    position = 7;//+- 북서
                } else {
                    position = 3;//-+ 남동
                }
            }//포지션을 알맞게 대입


            PathInfo pathInfo = new PathInfo(
                    i
                    , PathsListpathsList.get(i).getX(), PathsListpathsList.get(i).getY()
                    , angle
                    , distance
                    , position
                    , x_sign
                    , y_sign
            );

            pathInfoArrayList.add(pathInfo);

         //   Log.d("path2", pathInfo.toString());

        }


        //끝 경로 추가
        PathInfo pathInfo = new PathInfo(
                size
                , PathsListpathsList.get(size - 1).getX(), PathsListpathsList.get(size - 1).getY()
                , 0
                , 0
                , 0
                , 0
                , 0
        );

        pathInfoArrayList.add(pathInfo);
      //  Log.d("path2", pathInfo.toString());

        //지금까지 기존의 경로 정보

        int count=0;

        for(int i =0; i<pathInfoArrayList.size()-1;i++)
        {


            if(pathInfoArrayList.get(i).position!=pathInfoArrayList.get(i+1).position &&pathInfoArrayList.get(i+1).position!=0)
            {

                int pos=0;

                if(pathInfoArrayList.get(i).position==1)
                {
                    if(pathInfoArrayList.get(i+1).position==3)
                    {pos=12;}//우회전
                    else if(pathInfoArrayList.get(i+1).position==7)
                    {pos=16;}//좌회전

                }
                else if(pathInfoArrayList.get(i).position==3)
                {
                    if(pathInfoArrayList.get(i+1).position==1)
                    {pos=36;}//좌회전
                    else if(pathInfoArrayList.get(i+1).position==5)
                    {pos=32;}//우회전

                }
                else if(pathInfoArrayList.get(i).position==5)
                {
                    if(pathInfoArrayList.get(i+1).position==3)
                    {pos=56;}//좌회전
                    else if(pathInfoArrayList.get(i+1).position==7)
                    {pos=52;}//우회전

                }
                else if(pathInfoArrayList.get(i).position==7)
                {
                    if(pathInfoArrayList.get(i+1).position==1)
                    {pos=72;}//우회전
                    else if(pathInfoArrayList.get(i+1).position==5)
                    {pos=76;}//좌회전

                }


                /**
                 *좌회전 6 우회전 2               781
                 *                         좌   602    우
                 *                              543
                 */



                PathInfo pi = new PathInfo(
                        count++
                        , pathInfoArrayList.get(i).x, pathInfoArrayList.get(i).y
                        , 0
                        , 0
                        , pos
                        , 0
                        , 0
                );

                routeInfoArrayList.add(pi);
                Log.d("path2", pi.toString()+" pos i : "+pathInfoArrayList.get(i).position+" pos i+1 : "+pathInfoArrayList.get(i+1).position);

            }//if

        }//for

        PathInfo pi = new PathInfo(
                count++
                , pathInfoArrayList.get(size-1).x, pathInfoArrayList.get(size-1).y
                , 0
                , 0
                , 0
                , 0
                , 0
        );

        routeInfoArrayList.add(pi);

        Log.d("path2", pi.toString());












        UserProfileData.userPathsListpathsList=routeInfoArrayList;

    }


    public static double getAngle(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;

        double rad = Math.atan2(dx, dy);
        double degree = (rad * 180) / Math.PI;

        return degree;
    }//각도

    public static double getDistance(double x1, double y1, double x2, double y2) {


        Location loc1 = new Location("location 1 name");
        Location loc2 = new Location("location 2 name");

        loc1.setLatitude(y1);
        loc1.setLongitude(x1);
        loc2.setLatitude(y2);
        loc2.setLongitude(x2);

        return loc1.distanceTo(loc2);
    }//거리

}
