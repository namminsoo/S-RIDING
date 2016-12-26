package com.example.namsoo.s_riding_ui.fragment_menu;

import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.BarChartView;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.ElasticEase;
import com.db.chart.view.animation.style.DashAnimation;
import com.example.namsoo.s_riding_ui.R;
import com.example.namsoo.s_riding_ui.RecordFragment_menu.RecordPage1Activity;
import com.example.namsoo.s_riding_ui.db.DBManager;

/**
 * Created by 김나영 on 2015-10-16.
 */
public class RecordItemFragment extends Fragment {


    Context context;

    DBManager dbManager;

    /**
     * Third chart
     */
    private BarChartView mChartThree;
    private ImageButton mPlayThree;
    private boolean mUpdateThree;
    private final String[] mLabelsThree = {
            "", "", "", "", "", "", ""};
    private final float[] mValuesThree = new float[7];// = {12f, 6f, 7f, 8f, 9f, 8f, 9f};
    //private final float[] mValuesThree  = {12f, 6f, 7f, 8f, 9f, 8f, 9f};

    private  final String[] mValuesThreeDate = new String[7]; //해당 date저장하기


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

    private float[][] mValuesTwo = {{35f, 37f, 47f, 49f, 43f, 46f, 80f, 83f, 65f, 68f, 100f, 68f, 70f, 73f, 83f, 85f, 70f, 73f, 73f, 77f,
            33f, 15f, 18f, 25f, 28f, 25f, 28f, 40f, 43f, 25f, 28f, 55f, 58f, 50f, 53f, 53f, 57f, 48f, 50f, 53f, 54f,
            25f, 27f, 35f, 37f, 35f, 80f, 82f, 55f, 59f, 85f, 82f, 60f, 55f, 63f, 65f, 58f, 60f, 63f, 60f},
            {85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f,
                    85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f,
                    85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f, 85f}};

    //private float[][] mValuesTwo = new float[30][30];
    LinearLayout LineChart2Layout;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        context = getContext();


        dbManager = new DBManager(context, "TESTGRAPH.db", null, 1);
        LineChart2Layout = (LinearLayout) view.findViewById(R.id.lineChart2Layout);

        setBar3Date();
        CreateBar3Chart();




        // Init third chart Bar
        mUpdateThree = true;
        mChartThree = (BarChartView) view.findViewById(R.id.barchart3);

        mPlayThree = (ImageButton) view.findViewById(R.id.play3);
        mPlayThree.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mUpdateThree)
                    updateBar3Chart(2, mChartThree, mPlayThree);
                    // Log.d("Click : ","1");
                else
                    dismissBar3Chart(2, mChartThree, mPlayThree);
                // Log.d("Click : ","2");
                mUpdateThree = !mUpdateThree;
            }
        });

        showBar3Chart(2, mChartThree, mPlayThree);



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


        mChartThree.setOnEntryClickListener(new OnEntryClickListener() {
            @Override
            public void onClick(int i, int i1, Rect rect) {


                Log.d("onClick : ", i + ", " + i1); //click entry
                //showLine2Chart(1, mChartTwo, mPlayTwo);
                // LineChart2Layout.setVisibility(View.VISIBLE);


                //LineChart데이터 가져오기

                switch (i1) {
                    case 0:
                        //dbManager.printSpeed("10/08");
                        CreateLine2Chart("10/08", mChartTwo);
                        // showLine2Chart(1, mChartTwo, mPlayTwo);

                        //updateLine2Chart(1, mChartTwo, mPlayTwo);
                        break;
                    case 1:
                        CreateLine2Chart("10/09", mChartTwo);

                        break;
                    case 2:
                        CreateLine2Chart("10/10", mChartTwo);

                        break;
                    case 3:
                        CreateLine2Chart("10/11", mChartTwo);

                        break;
                    case 4:
                        CreateLine2Chart("10/12", mChartTwo);

                        break;
                    case 5:
                        CreateLine2Chart("10/13", mChartTwo);

                        break;
                    case 6:
                        CreateLine2Chart("10/14", mChartTwo);

                        break;

                    default:
                        break;
                }


            }
        });




    }




    //Bar의 해당 날짜
    public void setBar3Date(){

        String str = dbManager.printData();
        // Log.d("Date :" ,str);
        String data ="";
        int count=0;



        for(int i=0; i<str.length() ; i++){

            if(!str.substring(i,i+1).equals("\n")){
                data += str.substring(i,i+1);


            } else if(str.substring(i,i+1).equals("\n")){
                //data+=str.substring(i,i+1);

                mValuesThreeDate[count]= data;
                //Log.d("Date[count] : ", mValuesThreeDate[count]);


                count++;
                data="";
            }
        }

        //  Log.d("mValuesThreeDate: ",mValuesThreeDate.toString());

    }


    //Bar3 데이터 설정
    public void CreateBar3Chart(){
        //12f7f8f5f10f11f8f
        String str =   dbManager.printDistance(1);
        String data="";
        int count=0;



        for(int i=0; i<str.length() ; i++){

            if(!str.substring(i,i+1).equals("f")){
                data += str.substring(i,i+1);


            } else if(str.substring(i,i+1).equals("f")){
                data+=str.substring(i,i+1);
                // Log.d("Data를 뿌려보겠다 : ", data);
                mValuesThree[count]= Float.valueOf(data);
                count++;
                data="";
            }
        }

        // Log.d("mValuesThree: ",mValuesThree.toString());


    }

    //LineChart Data 설정
    public void CreateLine2Chart(String date, final LineChartView chart){

        String str =   dbManager.printSpeed(1);
        String str2 = dbManager.printHeight(RecordPage1Activity.db_index);
        float[] goodStr = new float[60];
        float[] goodStr2 = new float[60];
        String data="";
        int count=0;



        // chart.updateValues(0, mValuesTwo[1]);
        //chart.updateValues(1, mValuesTwo[0]);

        //   Log.d("printStr.length", str);

        //speed
        for(int i=0; i<str.length() ; i++){

            if(!str.substring(i,i+1).equals("f")){
                data += str.substring(i,i+1);


            } else if(str.substring(i,i+1).equals("f")){
                data+=str.substring(i, i + 1);
                // Log.d("Data를 뿌려보겠다 : ", data);
                goodStr[count] = Float.valueOf(data);
                // mValuesThree[count]= Float.valueOf(data);
                //   Log.d("printSpeed ", data);
                count++;
                data="";
            }
        }

        count =0;
        //height
        for(int i=0; i<str2.length() ; i++){

            if(!str2.substring(i,i+1).equals("f")){
                data += str2.substring(i,i+1);


            } else if(str2.substring(i,i+1).equals("f")){
                data+=str2.substring(i,i+1);
                // Log.d("Data를 뿌려보겠다 : ", data);
                goodStr2[count] = Float.valueOf(data);
                //  Log.d("printHeight ", data);
                //mValuesThree[count]= Float.valueOf(data);
                count++;
                data="";
            }
        }

        // mValuesTwo = new float[][]{goodStr, goodStr2};


        chart.updateValues(0,  goodStr);
        chart.updateValues(1, goodStr2);
        mChartTwo.notifyDataUpdate();

        //   Log.d("mValuesThree: ",mValuesThree.toString());

    }


    /**
     * Show a CardView chart
     *
     * @param tag   Tag specifying which chart should be dismissed
     * @param chart Chart view
     * @param btn   Play button
     */
    private void showBar3Chart(final int tag, final ChartView chart, final ImageButton btn) {
        dismissBar3Play(btn);
        Runnable action = new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        showBar3Play(btn);
                    }
                }, 500);
            }
        };

        switch (tag) {
            case 0:
                //    produceOne(chart, action); break;
            case 1:
                //   produceTwo(chart, action); break;
            case 2:
                produceThree(chart, action);
                break;

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
    private void updateBar3Chart(final int tag, final ChartView chart, ImageButton btn) {

        dismissBar3Play(btn);

        switch (tag) {
            case 0:
                //   updateOne(chart); break;
            case 1:
                // updateTwo(chart); break;
            case 2:
                updateThree(chart);
                break;
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
    private void dismissBar3Chart(final int tag, final ChartView chart, final ImageButton btn) {

        dismissBar3Play(btn);

        Runnable action = new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        showBar3Play(btn);
                        showBar3Chart(tag, chart, btn);
                    }
                }, 500);
            }
        };

        switch (tag) {
            case 0:
                // dismissOne(chart, action); break;
            case 1:
                //  dismissTwo(chart, action); break;
            case 2:
                dismissThree(chart, action);
                break;
            default:
        }
    }


    /**
     * Show CardView play button
     *
     * @param btn Play button
     */
    private void showBar3Play(ImageButton btn) {
        btn.setEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            btn.animate().alpha(1).scaleX(1).scaleY(1);

            showLine2Chart(1, mChartTwo, mPlayTwo);
        } else {

            btn.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Dismiss CardView play button
     *
     * @param btn Play button
     */
    private void dismissBar3Play(ImageButton btn) {
        btn.setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            btn.animate().alpha(0).scaleX(0).scaleY(0);

        else
            btn.setVisibility(View.INVISIBLE);
    }


    /**
     * Chart 3
     */

    public void produceThree(ChartView chart, Runnable action) {
        BarChartView barChart = (BarChartView) chart;



        Tooltip tip = new Tooltip(context, R.layout.barchart_three_tooltip);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1);
            tip.setEnterAnimation(alpha);

            alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0);
            tip.setExitAnimation(alpha);
        }

        barChart.setTooltips(tip);

        BarSet dataset = new BarSet(mLabelsThree, mValuesThree);
        dataset.setColor(Color.parseColor("#eb993b")); //보여주는 bar Color
        //dataset.getEntry(1).setColor(0x000000);




        barChart.addData(dataset);

        barChart.setBarSpacing(Tools.fromDpToPx(3));

        barChart.setXLabels(AxisController.LabelPosition.NONE)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setXAxis(false)
                .setYAxis(false);



        Animation anim = new Animation()
                .setEasing(new ElasticEase())
                .setEndAction(action);


        chart.show(anim);
    }


    public void updateThree(ChartView chart) {

        chart.dismissAllTooltips();

        float[] values = {
                11f, 7f, 6f, 7f, 10f, 11f, 12f, 9f, 8f, 7f,
                6f, 5f, 4f, 3f, 6f, 7f, 8f, 9f, 10f, 12f,
                13f, 11, 13f, 10f, 8f, 7f, 5f, 4f, 3f, 7f};
        chart.updateValues(0, values);
        chart.notifyDataUpdate();
    }

    public void dismissThree(ChartView chart, Runnable action) {

        chart.dismissAllTooltips();
        chart.dismiss(new Animation()
                .setEasing(new ElasticEase())
                .setEndAction(action));
    }


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

        LineSet dataset = new LineSet(mLabelsTwo, mValuesTwo[0]);
        dataset.setColor(Color.WHITE)
                .setThickness(Tools.fromDpToPx(3))
                .beginAt(4).endAt(55);
        chart.addData(dataset);

        dataset = new LineSet(mLabelsTwo, mValuesTwo[1]);
        dataset.setColor(Color.parseColor("#97b867"))
                .setThickness(Tools.fromDpToPx(3))
                        //      .beginAt(4).endAt(55);
                .setDashed(new float[]{10, 10});
        chart.addData(dataset);

        Paint gridPaint = new Paint(); //눈금선
        gridPaint.setColor(Color.parseColor("#7F97B867"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        chart.setBorderSpacing(Tools.fromDpToPx(0)) //start, end
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
        chart.updateValues(0, mValuesTwo[1]);
        chart.updateValues(1, mValuesTwo[0]);
        chart.notifyDataUpdate();
    }

    public static void dismissTwo(LineChartView chart, Runnable action) {
        chart.dismiss(new Animation().setStartPoint(1, .5f).setEndAction(action));
    }









}
