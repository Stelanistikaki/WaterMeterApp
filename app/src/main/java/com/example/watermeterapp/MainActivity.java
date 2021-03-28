package com.example.watermeterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private Button viewAll, searchId;
    private EditText idInput;
    private TextView username, logout;
    private TaskClass task ;
    private ArrayList<Meter> meters = new ArrayList<>();
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get the views from layout
        viewAll = findViewById(R.id.viewAllButton);
        searchId = findViewById(R.id.searchIdButton);
        idInput = findViewById(R.id.meterIdText);
        username = findViewById(R.id.username);
        logout = findViewById(R.id.logout);
        //get the username from login page to display it in the title
        sp = getSharedPreferences("shared preferences" , MODE_PRIVATE);
        String usernameText = sp.getString("username","");
        username.setText(usernameText);
        //set the listener to logout from app
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update the shared preferences to store that the user is logged out
                sp.edit().putBoolean("logged",false).apply();
                //if the user logs out he is directed to the log in page
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //get the access token from a hidden file *not uploaded in git*
        task = new TaskClass(getString(R.string.access));
        //get all the meters to use them later
        try {
            meters = task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //when the user inputs an id and clicks search he will be directed to the map view
        searchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the id from view
                String id = idInput.getText().toString();
                if(id.isEmpty()){
                    //show an error that there is not an id
                    Toast.makeText(MainActivity.this, "You have to write an id..", Toast.LENGTH_SHORT).show();
                }else{
                    //the user should be logged in to perform this
                    if(isOnline()) {
                        Intent i = new Intent(MainActivity.this, MapActivity.class);
                        Gson gson = new Gson();
                        SharedPreferences.Editor editor = sp.edit();
                        String json = gson.toJson(meters);
                        //put the data in the shared preferences to transfer them to Map Activity
                        editor.putString("data", json);
                        editor.apply();
                        //also take the id
                        sp.edit().putString("id", id).apply();
                        startActivity(i);
                    }else
                        Toast.makeText(MainActivity.this, "No Internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //the user can see all the meters in a list
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //the user needs internet connection for this
                if(isOnline()) {
                    //transfer the data and start the new fragment
                    Fragment fragment = new MeterList(meters);
                    FragmentTransaction mTransactiont = getSupportFragmentManager().beginTransaction();
                    mTransactiont.replace(R.id.list_frame, fragment, fragment.getClass().getName());
                    mTransactiont.commit();
                }else
                    Toast.makeText(MainActivity.this, "No Internet connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //method to check if the user has internet connection
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}