package com.example.watermeterapp;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class TaskClass extends AsyncTask<Void, Void, ArrayList<Meter>> {
    private String access;
    //constructor to get the access token
    public TaskClass(String access) {
        this.access = access;
    }

    @Override
    protected ArrayList<Meter> doInBackground(Void... voids) {
        OkHttpClient client = new OkHttpClient();
        ArrayList<Meter> meterList = new ArrayList();
        //prepare the request to get the meters from api
        Request meterRequest = new Request.Builder().url("https://test-api.gwf.ch/reports/measurements/")
                .method("GET", null)
                .addHeader("Authorization", "Bearer "+ access)
                .build();

        client.newCall(meterRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //get the response from the call
                ResponseBody responseBody = response.body();
                JSONArray array = null;
                try {
                    //get all the items to manipulate them
                    array = new JSONArray(responseBody.string());
                    for(int i=0; i< array.length(); i++){
                        JSONObject meter = array.getJSONObject(i);
                        Meter item = new Meter();
                        //get values from json response
                        item.setId(meter.getString("meter_id"));
                        item.setSerialNum(meter.getString("comm_mod_serial"));
                        item.setType(meter.getString("comm_mod_type"));
                        item.setVolume(meter.getString("volume"));
                        item.setLat(meter.getString("lat"));
                        item.setLng(meter.getString("lng"));
                        String batteryText = meter.getString("battery_lifetime");
                        //since battery is not a string if it is null I manually make it 0
                        if(batteryText.equals("null"))
                            item.setBattery(0);
                        else
                            item.setBattery(Integer.valueOf(batteryText));
                        //prepare the arraylist with the data
                        meterList.add(item);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        //return the list
        return meterList;
    }

}
