package com.example.watermeterapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MeterListViewAdapter extends ArrayAdapter<Meter> {
    private ArrayList<Meter> dataSet;
    Context mContext;
    int mResource;
    //constructor for the adapter
    public MeterListViewAdapter(Context context, int resource, ArrayList<Meter> data) {
        super(context, R.layout.meter_item_list, data);
        this.dataSet = data;
        this.mContext=context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //set up the listview
        Meter meter = dataSet.get(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        //setup the UI components and fill them with the values
        TextView itemId = (TextView) convertView.findViewById(R.id.item_id);
        TextView itemType = (TextView) convertView.findViewById(R.id.type);
        TextView itemSerialNum = (TextView) convertView.findViewById(R.id.serial);
        TextView itemBattery = (TextView) convertView.findViewById(R.id.battery);
        TextView itemVolume = (TextView) convertView.findViewById(R.id.volume);
        itemId.setText(meter.getId());
        itemType.setText("Type: "+meter.getType());
        itemSerialNum.setText(meter.getSerialNum());
        itemBattery.setText("Battery Life: "+meter.getBattery().toString());
        itemVolume.setText("Volume: "+meter.getVolume());

        return convertView;
    }
}