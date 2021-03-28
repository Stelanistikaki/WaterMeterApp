package com.example.watermeterapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {
    private String id;
    private ArrayList<Meter> meters;
    private String lat, lng;
    private Boolean found;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get the data in order to find lat and lng for the map
        Gson gson = new Gson();
        SharedPreferences sp = getSharedPreferences("shared preferences" , MODE_PRIVATE);
        String json = sp.getString("data", "");
        Type type = new TypeToken<ArrayList<Meter>>() {}.getType();
        meters = gson.fromJson(json, type);
        //get id to find it in all the meters
        id = sp.getString("id","");
        //variable to indicate if this meter id exists
        found = false;
        //get the lan and lng
        getMeter();
        //the id exists
        if(found) {
            try {
                // Assume that Google maps app exists. If not, exception will occur
                PackageManager pm = getPackageManager();
                pm.getPackageInfo("com.tranzmate", PackageManager.GET_ACTIVITIES);
                //open google maps in the specific lat and lng
                Uri gmmIntentUri = Uri.parse("geo: " + lat + ", " + lng + "?q= " + lat + "," + lng + "(Label+Name)");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            } catch (PackageManager.NameNotFoundException e) {
                // Google Maps not installed - send to store
                String url = "https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=el&gl=US";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
            //the meter id is not available
        } else {
            Toast.makeText(MapActivity.this, "This meter is not available. Try again.", Toast.LENGTH_SHORT).show();
        }
        finish();

    }

    private void getMeter() {
        //search for the meter with this id
        //if found get the lat and lng for map
        for(int i=0; i< meters.size(); i++){
            if(meters.get(i).getId().equals(id)){
                lat = meters.get(i).getLat();
                lng = meters.get(i).getLng();
                found = true;
            }
        }
    }
}