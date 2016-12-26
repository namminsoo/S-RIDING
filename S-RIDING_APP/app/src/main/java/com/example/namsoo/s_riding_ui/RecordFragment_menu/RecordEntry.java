package com.example.namsoo.s_riding_ui.RecordFragment_menu;

/**
 * Created by 김나영 on 2015-10-22.
 */

//http://arsviator.tistory.com/168
public class RecordEntry {

    private String mainAddress;
 //  private String subAddress;
    private String distance;
    private String time;
    private String kcal;

    public RecordEntry(String _mainAddress, String _distance, String _time, String _kcal){

        this.mainAddress = _mainAddress;
       // this.subAddress = _subAddress;
        this.distance = _distance;
        this.time = _time;
        this.kcal =_kcal;

    }

    public void setRecordEntry(String _mainAddress,String _distance, String _time, String _kcal){

        this.mainAddress = _mainAddress;
       // this.subAddress = _subAddress;
        this.distance = _distance;
        this.time = _time;
        this.kcal =_kcal;

    }


    public String getMainAddress() {
        return mainAddress;
    }

//    public String getSubAddress() {
//        return subAddress;
//    }

    public String getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }

    public String getKcal() {
        return kcal;
    }
}
