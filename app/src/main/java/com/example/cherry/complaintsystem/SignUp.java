package com.example.cherry.complaintsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Map;
import java.util.Set;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        //When okay is clicked --  get info from textboxes

        //Check if information of student is stored -- That is already signed in

        CustomSharedPreference student_preferences = new CustomSharedPreference(getApplicationContext());
        if(student_preferences.getSignedUp()){

            Intent intent = new Intent(getApplicationContext(), Homepage.class);
            //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
            startActivity(intent);
            this.finish();
        }


        Button login = (Button) findViewById(R.id.login_btn);

        final Activity activity = this;

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Send data to kerberos login -- depending onResponse -- go to the next intent
                //Store in sharedpreferences -- first initialize the file -- since new user
                CustomSharedPreference studentPreferences = new CustomSharedPreference(getApplicationContext());

                Intent intent = new Intent(getApplicationContext(), PersonalInfoInput.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                startActivity(intent);
                activity.finish();

            }
        });

    }


}
