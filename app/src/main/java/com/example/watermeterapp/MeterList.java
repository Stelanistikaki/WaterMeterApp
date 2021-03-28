package com.example.watermeterapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.Gson;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MeterList extends Fragment {
    private ListView list;
    private ArrayList<Meter> meters;
    //constructor to get the data arraylist for all meters
    public MeterList(ArrayList<Meter> meters) {
        this.meters = meters;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meter_item_list, container, false);

        list = view.findViewById(R.id.list);

        //set ,the adapter of the list
        MeterListViewAdapter adapter = new MeterListViewAdapter(getContext(), R.layout.meter_item, meters);
        //not available in fragment so get the activity
        getActivity().runOnUiThread(new Runnable() {
            @Override public void run() {
                list.setAdapter(adapter);
            }
        });
        //onclick listener for each list item
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get the id of the clicked item and open the map on it
                String meterId = adapter.getItem(position).getId();
                Intent i = new Intent(getActivity(), MapActivity.class);
                Gson gson = new Gson();
                //this is because this can be clicked before the Search id in Main activity and those data will not be available
                SharedPreferences sp = getActivity().getSharedPreferences("shared preferences" , MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                String json = gson.toJson(meters);
                editor.putString("data", json);
                editor.apply();
                sp.edit().putString("id", meterId).apply();
                startActivity(i);
            }
        });

        return view;
    }


}