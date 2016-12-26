package com.example.namsoo.s_riding_ui.navi;

/**
 * Created by namsoo on 2015-10-23.
 */
public class PathInfo {

    public  int  index;

    public double x;

    public double y;

    public double angle;

    public double distance;

    public int position;

    public int x_sign;

    public int y_sign;


    public PathInfo(int index, double x, double y, double a, double d, int p, int x_s, int y_s)
    {
        this.index=index;
        this.x = x;
        this.y = y;
        this.angle = a;
        this.distance = d;
        this.position=p;
        this.x_sign=x_s;
        this.y_sign=y_s;
    }



    public  String toString()
    {
        return index +". yx : "+y+" "+x
                //+" angle : "+angle+" distance : "+distance
                +" x_sign : "+x_sign+" y_sign : "+y_sign
                +" position : "+position;
    }

}
