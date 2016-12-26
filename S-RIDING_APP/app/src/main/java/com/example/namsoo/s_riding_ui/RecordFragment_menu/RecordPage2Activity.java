package com.example.namsoo.s_riding_ui.RecordFragment_menu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.style.DashAnimation;
import com.example.namsoo.s_riding_ui.R;
import com.example.namsoo.s_riding_ui.db.DBManager;

/**
 * Created by 김나영 on 2015-10-16.
 */
public class RecordPage2Activity extends Fragment {

    Context context;
    DBManager dbManager;




    /**
     * Second chart
     */
    private LineChartView mChartTwo;
    private ImageButton mPlayTwo;
    private boolean mUpdateTwo;
    private final String[] mLabelsTwo = {"", "", "", "", "START", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
            "", "", "", "", "", "", "", "", "", "", "FINISH", "", "", "", ""};
//    private final float[][] mValuesTwo = {{35f, 37f, 47f, 49f, 43f, 46f, 80f, 83f, 65f, 68f, 100f, 68f, 70f, 73f, 83f, 85f, 70f, 73f, 73f, 77f,
//            33f, 15f, 18f, 25f, 28f, 25f, 28f, 40f, 43f, 25f, 28f, 55f, 58f, 50f, 53f, 53f, 57f, 48f, 50f, 53f, 54f,
//            25f, 27f, 35f, 37f, 35f, 80f, 82f, 55f, 59f, 85f, 82f, 60f, 55f, 63f, 65f, 58f, 60f, 63f, 60f},
//            {85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f,
//                    85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f,
//                    85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f}};

   // float[] speed= new float[100];

   // float[] height = new float[100];


    float[] speed = {0f, 10f, 8f, 12f, 15f, 19f, 20f, 23f, 25f, 15f, 10f, 12f, 17f, 15f, 15f, 15f, 16f, 20f, 20f, 20f,
            25f, 22f, 22f, 22f, 17f, 15f, 10f, 0f, 14f, 15f, 16f, 17f, 17f, 19f, 20f, 19f, 17f, 20f, 25f, 24f, 28f,
            29f, 30f, 29f, 28f, 28f, 25f, 24f, 20f, 15f, 15f, 15f, 12f, 15f, 10f, 8f, 7f, 6f, 2f, 0f};
    float[] height =   {0f, 0f, 0f, 0f, 0f, 0f, 1f, 2f, 1f, 2f, 3f, 4f, 5f, 5f, 4f, 4f, 5f, 5f, 5f, 3f,
                    1f, 2f, 0f, 0f, 0f, 0f, 0f, 1f, 2f, 2f, 3f, 4f, 5f, 4f, 3f, 2f, 2f, 2f, 2f, 2f, 1f,
                    1f, 5f, 3f, 2f, 1f, 1f, 1f, 2f, 2f, 1f, 1f, 1f, 1f, 1f, 0f, 0f, 0f, 0f, 0f};


    //    float[] what= {35f, 37f, 47f, 49f, 43f, 46f, 80f, 83f, 65f, 68f, 100f, 68f, 70f, 73f, 83f, 85f, 70f, 73f, 73f, 77f,
//            33f, 15f, 18f, 25f, 28f, 25f, 28f, 40f, 43f, 25f, 28f, 55f, 58f, 50f, 53f, 53f, 57f, 48f, 50f, 53f, 54f,
//            25f, 27f, 35f, 37f, 35f, 80f, 82f, 55f, 59f, 85f, 82f, 60f, 55f, 63f, 65f, 58f, 60f, 63f, 60f};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_record_page2, null);


        context = getContext();
        dbManager = new DBManager(context, "S_RidingTest.db", null, 1);
        CreateLine2Chart(mChartTwo);


        // Init second chart Line
        mUpdateTwo = true;
        mChartTwo = (LineChartView) view.findViewById(R.id.linechart2);
        mPlayTwo = (ImageButton) view.findViewById(R.id.play2);
        mPlayTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mUpdateTwo)
                    updateLine2Chart(1, mChartTwo, mPlayTwo);
                else
                    dismissLine2Chart(1, mChartTwo, mPlayTwo);
                mUpdateTwo = !mUpdateTwo;

            }
        });


        showLine2Chart(1, mChartTwo, mPlayTwo);



        return view;
    }//onCreate



    //----------------------------------------------------------------------------------------------------------------------------
    //      Line
    //----------------------------------------------------------------------------------------------------------------------------

    /**
     * Show a CardView chart
     *
     * @param tag   Tag specifying which chart should be dismissed
     * @param chart Chart view
     * @param btn   Play button
     */
    private void showLine2Chart(final int tag, final LineChartView chart, final ImageButton btn) {

        Toast.makeText(context,"나와라ㅏ",Toast.LENGTH_LONG);

        dismissLine2Play(btn);
        Runnable action = new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        showLine2Play(btn);
                    }
                }, 500);
            }
        };

        switch (tag) {
            case 0:
                // produceOne(chart, action); break;
            case 1:
                produceTwo(chart, action);
                break;
            case 2:
                // produceThree(chart, action); break;
            default:
        }
    }


    /**
     * Update the values of a CardView chart
     *
     * @param tag   Tag specifying which chart should be dismissed
     * @param chart Chart view
     * @param btn   Play button
     */
    private void updateLine2Chart(final int tag, final LineChartView chart, ImageButton btn) {

        dismissLine2Play(btn);

        switch (tag) {
            case 0:
                // updateOne(chart); break;
            case 1:
                updateTwo(chart);
                break;
            case 2:
                //updateThree(chart); break;
            default:
        }
    }


    /**
     * Dismiss a CardView chart
     *
     * @param tag   Tag specifying which chart should be dismissed
     * @param chart Chart view
     * @param btn   Play button
     */
    private void dismissLine2Chart(final int tag, final LineChartView chart, final ImageButton btn) {

        dismissLine2Play(btn);

        Runnable action = new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        showLine2Play(btn);
                        showLine2Chart(tag, chart, btn);
                    }
                }, 500);
            }
        };

        switch (tag) {
            case 0:
                // dismissOne(chart, action); break;
            case 1:
                dismissTwo(chart, action);
                break;
            case 2:
                //dismissThree(chart, action); break;
            default:
        }
    }


    /**
     * Show CardView play button
     *
     * @param btn Play button
     */
    private void showLine2Play(ImageButton btn) {
        btn.setEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            btn.animate().alpha(1).scaleX(1).scaleY(1);
        else
            btn.setVisibility(View.VISIBLE);
    }


    /**
     * Dismiss CardView play button
     *
     * @param btn Play button
     */
    private void dismissLine2Play(ImageButton btn) {
        btn.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            btn.animate().alpha(0).scaleX(0).scaleY(0);
        else
            btn.setVisibility(View.INVISIBLE);
    }

    /**
     * Chart 2 Line
     */

    public void produceTwo(LineChartView chart, Runnable action) {

        //  LineSet dataset = new LineSet(mLabelsTwo, mValuesTwo[0]);
        LineSet dataset = new LineSet(mLabelsTwo, speed);
        dataset.setColor(Color.WHITE)
                .setThickness(Tools.fromDpToPx(3))
                .beginAt(4).endAt(55);
        chart.addData(dataset);

        dataset = new LineSet(mLabelsTwo, height);
        // dataset = new LineSet(mLabelsTwo, mValuesTwo[1]);
        dataset.setColor(Color.parseColor("#97b867"))
                .setThickness(Tools.fromDpToPx(3))
                .setDashed(new float[]{10, 10});
        chart.addData(dataset);

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#7F97B867"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        chart.setBorderSpacing(Tools.fromDpToPx(0))
                .setXLabels(AxisController.LabelPosition.OUTSIDE)
                .setLabelsColor(Color.parseColor("#304a00"))
                .setYLabels(AxisController.LabelPosition.NONE)
                .setXAxis(false)
                .setYAxis(false)
                .setAxisBorderValues(0, 125, 25)
                .setGrid(ChartView.GridType.HORIZONTAL, gridPaint);

        Animation anim = new Animation().setStartPoint(0, .5f).setEndAction(action);

        chart.show(anim);
        chart.animateSet(1, new DashAnimation());
    }

    public void updateTwo(LineChartView chart) {
        // chart.updateValues(0, mValuesTwo[1]);
        // chart.updateValues(1, mValuesTwo[0]);
        chart.updateValues(0, height);
        chart.updateValues(1, speed);

        chart.notifyDataUpdate();
    }

    public static void dismissTwo(LineChartView chart, Runnable action) {
        chart.dismiss(new Animation().setStartPoint(1, .5f).setEndAction(action));
    }


    //LineChart Data 설정
    public void CreateLine2Chart(final LineChartView chart){



        if(!dbManager.printSpeed(RecordPage1Activity.db_index).equals("test")){


            String str =   dbManager.printSpeed(RecordPage1Activity.db_index);
            String str2 = dbManager.printHeight(RecordPage1Activity.db_index);

            float[] goodStr = new float[100];
            float[] goodStr2 = new float[100];

            String data="";
            int count=0;



            // chart.updateValues(0, mValuesTwo[1]);
            //chart.updateValues(1, mValuesTwo[0]);

            //   Log.d("printStr.length", str);

            //speed
            for(int i=0; i<str.length() ; i++){

                if(!str.substring(i,i+1).equals("f") || !str.substring(i,i+1).equals(",")){
                    data += str.substring(i,i+1);


                } else if(str.substring(i,i+1).equals("f") || str.substring(i,i+1).equals(",")){
                    data+=str.substring(i, i + 1);
                    if(!data.equals("nullf") || !data.equals("null,")){


                        Log.d("count : ",String.valueOf(count));
                        goodStr[count] = Float.valueOf(data);
                        count++;

                    }


                    data="";


                }
            }
            count =0;
            speed = goodStr;
            data="";


            //height
            for(int i=0; i<str2.length() ; i++){

                if(!str2.substring(i,i+1).equals("f") || !str2.substring(i,i+1).equals(",")){
                    data += str2.substring(i,i+1);


                } else if(str2.substring(i,i+1).equals("f") || str2.substring(i,i+1).equals(",")){
                    data+=str2.substring(i, i + 1);
                    if(!data.equals("nullf") || !data.equals("null,")){


                        Log.d("count : ", String.valueOf(count));
                        goodStr2[count] = Float.valueOf(data);
                        count++;

                    }


                    data="";


                }
            }
            height = goodStr2;
            Log.d("speed : ", dbManager.printSpeed(5)+ ", "+ dbManager.printSpeed(2));
            Log.d("height : ", dbManager.printHeight(5)+ ", "+ dbManager.printHeight(2));



        }

    }



}