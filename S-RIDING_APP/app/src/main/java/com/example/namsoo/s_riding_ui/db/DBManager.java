package com.example.namsoo.s_riding_ui.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 김나영 on 2015-10-11.
 */
public class DBManager extends SQLiteOpenHelper {
    private final Context mContext;


    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        this.mContext = context;
        this.onCreate(getReadableDatabase());


    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        // Log.d("table생성","생성ㄴ한다");
        //새로운 테이블을 생성
        //db.execSQL("CREATE TABLE RIDING(_id integer primary key autoincrement, date text, bestSpeed text, aveSpeed text, kcal text, distance text, startingLocation text, destinationLocation text); ");
        //db.execSQL("CREATE TABLE TESTRIDING(_id integer primary key autoincrement, data text);");


        //  db.execSQL("delete  from TESTGRAPH");

//
//        db.execSQL("CREATE TABLE TESTGRAPH(_id integer primary key autoincrement, date text, distance text, speed text, height text);");
//        db.execSQL("insert into TESTGRAPH values(null, '10/08', '12f','35f37f47f49f23f46f80f83f65f68f100f28f70f73f63f85f70f73f73f17f33f15f18f25f28f25f28f80f43f25f28f55f58f50f53f53f57f48f50f53f54f25f27f15f37f50f80f82f55f59f85f82f60f55f63f65f58f60f63f60f','35f20f47f29f83f46f22f55f50f20f10f30f70f23f83f30f33f53f63f77f88f99f100f80f88f44f25f35f45f55f25f80f58f40f53f25f80f48f50f53f60f56f27f35f65f90f50f82f55f59f85f82f60f55f63f65f58f60f63f60f');");
//        db.execSQL("insert into TESTGRAPH values(null, '10/09', '7f',   '35f20f47f29f83f46f22f55f50f20f10f30f70f23f83f30f33f53f63f77f88f99f100f80f88f44f25f35f45f55f25f80f58f40f53f25f80f48f50f53f60f56f27f35f65f90f50f82f55f59f85f82f60f55f63f65f58f60f63f60f','65f85f45f85f25f35f45f55f45f55f45f25f75f85f95f95f15f25f35f45f55f50f55f25f85f65f85f75f75f25f85f25f85f85f85f15f15f45f45f55f65f77f55f85f85f85f45f85f45f85f35f25f25f85f15f45f55f55f85f85f');");
//        db.execSQL("insert into TESTGRAPH values(null, '10/10', '8f', '35f57f57f88f43f46f60f83f65f68f80f68f50f73f53f85f70f73f73f57f33f15f18f25f28f25f28f40f43f25f28f55f58f10f53f53f40f48f56f53f70f87f27f75f48f80f40f82f55f59f85f82f60f55f63f65f58f60f63f60f','85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f40f85f35f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f');");
//        db.execSQL("insert into TESTGRAPH values(null, '10/11', '5f', '35f77f97f29f43f26f50f43f35f38f50f50f30f53f83f85f70f73f73f77f33f15f18f25f28f85f28f40f10f25f28f55f58f20f53f40f57f48f40f53f80f65f27f65f89f15f30f82f55f59f85f82f60f55f63f65f58f60f63f60f','85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f35f85f30f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f');");
//        db.execSQL("insert into TESTGRAPH values(null, '10/12', '10f', '35f67f47f69f13f46f60f83f45f28f71f68f40f33f23f85f20f73f73f47f33f15f18f25f28f25f28f20f43f25f80f55f58f30f53f20f10f48f20f53f15f32f27f45f37f45f40f82f55f59f85f82f60f55f63f65f58f60f63f60f','85f27f77f49f33f46f20f53f65f68f65f68f70f13f83f85f50f73f73f27f33f15f18f55f28f65f28f40f43f25f50f55f40f50f53f40f20f48f80f53f20f25f27f58f87f68f70f82f55f59f85f82f60f55f63f65f58f60f63f60f');");
//        db.execSQL("insert into TESTGRAPH values(null, '10/13', '11f', '85f27f77f49f33f46f20f53f65f68f65f68f70f13f83f85f50f73f73f27f33f15f18f55f28f65f28f40f43f25f50f55f40f50f53f40f20f48f80f53f20f25f27f58f87f68f70f82f55f59f85f82f60f55f63f65f58f60f63f60f','85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f70f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f85f');");
//        db.execSQL("insert into TESTGRAPH values(null, '10/14', '8f',  '95f87f47f89f83f46f150f83f65f68f27f48f20f73f83f85f70f73f73f77f33f15f18f25f28f26f28f40f43f25f88f70f58f50f53f15f60f48f70f53f56f80f27f99f10f44f50f82f55f59f85f82f60f55f63f65f58f60f63f60f','85f27f77f49f33f46f20f53f65f68f65f68f70f13f83f85f50f73f73f27f33f15f18f55f28f65f28f40f43f25f50f55f40f50f53f40f20f48f80f53f20f25f27f58f87f68f70f82f55f59f85f82f60f55f63f65f58f60f63f60f');");

//insert into S_RidingTest values(null, '2015/10/27', '대한민국 서울특별시 광진구 군자동 98','0.0','0.0','대한민국 서울특별시 광진구 군자동 98','0.0','0.0','1','0','1','0.0','0 kcal','18℃','0.0','0.0','nullf0.0f','nullf0.0f');
//
//          db.execSQL("CREATE TABLE S_RidingTest(_id integer primary key autoincrement, db_date text, db_startingLocation text, db_startingLatitude text, db_startingLongitude text, destinationLocation text, destinationLatitude text, destinationLongitude text, db_ridingTime text, db_restTime text, db_totalTime text,distance text, kcal text, temperature text,aveSpeed text, bestSpeed text,speed text, height text ); ");
//
//        db.execSQL("insert into S_RidingTest values(null, '2015/10/27', '대한민국 서울특별시 광진구 군자동 98','127.072898',' 37.549035','대한민국 서울특별시 광진구 능동로 120','127.0771488','37.5407625','00:21:15','00:01:21','00:22:37','11.3km','120 kcal','15℃','13.2 km/h','21.2 km/h','test','test')");

     //
      try {
          db.execSQL("CREATE TABLE S_RidingTest2(_id integer primary key autoincrement, _email text, _ridingStartTime text, _ridingStopTime text, _ridingTime text, _ridingBreakTime text, _entireRidingTime text, _entireRidindDistance text,  _startingLocationName text, _startingLocationLatitude text,_startLocationLongitude text, _destinationName text, _destinationLatitude text, _destinationLongitude text,  _consumedCal text,  _averageSpeed text, _maxSpeed text,  _instantSpeed text, _instantHeight text);");
      }catch (Exception e){}
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }


    public void insert(String _query){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();


    }

    public String printBreakTime(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        Cursor cursor = db.rawQuery("select _ridingBreakTime from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0);//+"\n";
        }
        return str;
    }

    public int printCountData(){
        SQLiteDatabase db = getReadableDatabase();

        int count =0;
        Cursor cursor = db.rawQuery("select * from S_RidingTest2;",null);
        while(cursor.moveToNext()){
            count++;
        }
        return count;
    }

    public String printRidingTime(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        Cursor cursor = db.rawQuery("select _ridingTime from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0);//+"\n";
        }
        return str;

    }
    public String printTotalTime(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        Cursor cursor = db.rawQuery("select _entireRidingTime from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0);//+"\n";
        }
        return str;

    }

    public String printTemperature(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        Cursor cursor = db.rawQuery("select temperature from S_RidingTest where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0);//+"\n";
        }
        return str;
    }


    public String printStartingLocation(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        Cursor cursor = db.rawQuery("select startingLocationName from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0);//+"\n";
        }
        return str;
    }

    public String printMainStartingLocation(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        String main ="";
        Cursor cursor = db.rawQuery("select _startingLocationName from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0);//+"\n";
        }

        Log.d("전체주소 : ", str);
        for(int i=1; i<str.length(); i++){
            if(str.substring(i-1, i).equals("구")){
                main = str.substring(i+1, str.length());

            }
        }

        Log.d("구구구 : ", main);
        return main;
    }
    public String printDestinationLocation(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        Cursor cursor = db.rawQuery("select destinationLocation from S_RidingTest where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0);//+"\n";
        }
        return str;
    }
    public String printMainDestinationLocation(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        String main="";
        Cursor cursor = db.rawQuery("select _destinationName from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0);//+"\n";
        }


        for(int i=1; i<str.length(); i++){
            if(str.substring(i-1, i).equals("구")){
                main = str.substring(i+1, str.length());

            }
        }

        return main;
    }
    public String printHeight(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        Cursor cursor = db.rawQuery("select _instantHeight from S_RidingTest2 where _id='"+_id+"'",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0)+"\n";
        }
        return str;
    }


    public String printDistance(int _id) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";
        Cursor cursor = db.rawQuery("select _entireRidindDistance from S_RidingTest2 where _id=" + _id + ";", null);
        while (cursor.moveToNext()) {
            str += cursor.getString(0);//+"\n";
        }
        return str;
    }

    public String printAveSpeed(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        Cursor cursor = db.rawQuery("select _averageSpeed from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0);//+"\n";
        }
        return str;


    }

    public String printSpeed(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        Cursor cursor = db.rawQuery("select _instantSpeed from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0);//+"\n";
        }
        return str;
    }

    public String printKcal(int _id){

        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        Cursor cursor = db.rawQuery("select _consumedCal from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0);//+"\n";
        }
        return str;
    }



    public double printStartingLongitude(int _id){

        SQLiteDatabase db = getReadableDatabase();
        double str= 0;
        Cursor cursor = db.rawQuery("select _startLocationLongitude from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getDouble(0);//cursor.getString(0);//+"\n";
        }
        return str;
    }

    public double printStartingLatitude(int _id){

        SQLiteDatabase db = getReadableDatabase();
        double str= 0;
        Cursor cursor = db.rawQuery("select _startingLocationLatitude from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getDouble(0);//cursor.getString(0);//+"\n";
        }
        return str;
    }




    public double printDestinationLongitude(int _id){

        SQLiteDatabase db = getReadableDatabase();
        double str= 0;
        Cursor cursor = db.rawQuery("select _destinationLongitude from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getDouble(0);//cursor.getString(0);//+"\n";
        }
        return str;
    }

    public double printdestinationLatitude(int _id){

        SQLiteDatabase db = getReadableDatabase();
        double str= 0;
        Cursor cursor = db.rawQuery("select _destinationLatitude from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getDouble(0);//cursor.getString(0);//+"\n";
        }
        return str;
    }

    public String printBestSpeed(int _id){
        SQLiteDatabase db = getReadableDatabase();
        String str= "";
        Cursor cursor = db.rawQuery("select _maxSpeed from S_RidingTest2 where _id="+_id+";",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0);//+"\n";
        }
        return str;
    }

    public int dbCount(){
        int i = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select _id from S_RidingTest2",null);
        while(cursor.moveToNext()){
            i++;

        }


        return i;
    }

    public String printData(){
        SQLiteDatabase db = getReadableDatabase();
        String str ="";

        Cursor cursor = db.rawQuery("select _id from S_RidingTest2",null);
        while(cursor.moveToNext()){
            str+= cursor.getString(0)+"\n";

        }

        return str;
    }





}