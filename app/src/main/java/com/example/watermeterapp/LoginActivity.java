package com.example.watermeterapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    // UI references.
    private EditText mPasswordView, mUsernameView;
    private Button loginButton;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        loginButton = findViewById(R.id.sign_in_button);

        //shared boolean reference
        sp = getSharedPreferences("shared preferences" , MODE_PRIVATE);

        //false is the default value for booleans
        //if the user is logged then go to Main Activity
        if(sp.getBoolean("logged",false)) {
            goToMainActivity();
            finish();
        }
        //when the button is clicked do the authentication
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    //get the email and password from the user screen
                    String email = mUsernameView.getText().toString();
                    String password = mPasswordView.getText().toString();
                    //call the authenticate method
                    authenticateUser(email, password);
                }else
                    Toast.makeText(LoginActivity.this, "No Internet connection!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //method to go to Main Activity
    public void goToMainActivity(){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void authenticateUser(String email, String password){
        //get the email and password from UI and compare them with the correct values *in file not uploaded to git*
        if(email.equals(getString(R.string.email)) && password.equals(getString(R.string.password))){
            String username;
            //get from the email the id to use for the user
            username = email.split("@")[0];
            //share it with the app
            sp.edit().putString("username", username).apply();
            sp.edit().putBoolean("logged", true).apply();
            goToMainActivity();
            finish();
        }
    }

    //method to check if the user has internet connection
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
