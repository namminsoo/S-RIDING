package com.example.namsoo.s_riding_ui.fragment_menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.namsoo.s_riding_ui.R;
import com.example.namsoo.s_riding_ui.RecordFragment_menu.MainRecordActivity;
import com.example.namsoo.s_riding_ui.RecordFragment_menu.RecordEntry;
import com.example.namsoo.s_riding_ui.RecordFragment_menu.RecordPage1Activity;
import com.example.namsoo.s_riding_ui.db.DBManager;

import java.util.ArrayList;

public class RecordFragment extends Fragment {

    //http://arsviator.tistory.com/168
    ListView recoreList;
    ArrayAdapter<RecordEntry> m_adapter;
    ArrayList<RecordEntry> arrayList = new ArrayList<RecordEntry>();
    RecordEntry[] recordEntries = new RecordEntry[100];


    Context context;

    int dbCount = 0;

    Fragment newFragment = null;

    DBManager dbManager;

    static  RecordFragment recordFragment;



    public static RecordFragment newinstance() {
        if (recordFragment == null) {
            recordFragment = new RecordFragment();
        }
        return recordFragment;

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_record_list, container, false);


    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        context = getContext();
        dbManager = new DBManager(context, "S_RidingTest2.db", null, 1);
        recoreList = (ListView) view.findViewById(R.id.list_record);


        try {
//y(String _mainAddress, String _subAddress, String _distance, String _time, String _kcal){
            for (int i = 1; i < dbManager.printCountData() + 1; i++) {
                // for(int i=1; i<11; i++){

                //  Log.d("RecordEntries : ",dbManager.printStartingLocation(i)+" > " + dbManager.printDestinationLocation(i)+", " + dbManager.printDistance(i)+", "+ dbManager.printTotalTime(i)+", " +dbManager.printKcal(i)) ;
                //  recordEntries[i] = new RecordEntry(dbManager.printStartingLocation(i)+" > " + dbManager.printDestinationLocation(i) , dbManager.printDistance(i), dbManager.printTotalTime(i), dbManager.printKcal(i));
                recordEntries[i] = new RecordEntry(dbManager.printMainStartingLocation(i) + " > " + dbManager.printMainDestinationLocation(i), dbManager.printDistance(i), dbManager.printTotalTime(i), dbManager.printKcal(i));
                //  recordEntries[i].setRecordEntry(dbManager.printStartingLocation(i)+" > " + dbManager.printDestinationLocation(i) , dbManager.printDistance(i), dbManager.printTotalTime(i), dbManager.printKcal(i));
                arrayList.add(recordEntries[i]);
            }

            //m_adapter = new ArrayAdapter<RecordEntry>(context, R.layout.record_entry, R.id.eName, arrayList);
            m_adapter = new ABArrayAdapter(context, R.layout.record_entry, R.id.eName, arrayList);
            recoreList.setAdapter(m_adapter);

            recoreList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //아이템 터치


                    Log.d("position : ", String.valueOf(position));

//              //다른 fragment로 넘기기
                    Intent intent = new Intent(getActivity(), MainRecordActivity.class);
                    RecordPage1Activity.db_index = position + 1;
                    startActivity(intent);


                }
            });
        } catch (Exception e) {
        }


 /*       view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if( keyCode == KeyEvent.KEYCODE_BACK ) {


                    return true;
                } else {
                    return false;
                }
            }
        });

*/


    }


    private class ABArrayAdapter extends ArrayAdapter<RecordEntry> {
        private ArrayList<RecordEntry> items;
        private int rsrc;

        public ABArrayAdapter(Context ctx, int rsrcId, int txtId, ArrayList<RecordEntry> data) {
            super(ctx, rsrcId, txtId, data);
            this.items = data;
            this.rsrc = rsrcId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(rsrc, null);
            }
            RecordEntry e = items.get(position);
            if (e != null) {
                ((TextView) v.findViewById(R.id.eName)).setText(e.getMainAddress());
                ((TextView) v.findViewById(R.id.ePhoneNo)).setText(e.getDistance() + "km | " + e.getTime() + "second | " + e.getKcal()+"kcal");

                ((ImageView) v.findViewById(R.id.ePhoto)).setImageResource(R.drawable.ridingtest2);

            }
            return v;
        }
    }




}
