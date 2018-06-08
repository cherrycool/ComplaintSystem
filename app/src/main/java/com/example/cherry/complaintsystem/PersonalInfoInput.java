package com.example.cherry.complaintsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class PersonalInfoInput extends AppCompatActivity {

    private String spinner_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_input);
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

        //Retrieve Hostel information from server
        //Hardcoded for now
        addhostelToSpinner();

        //Put in text in edittext if it exists.
        CustomSharedPreference studentPref = new CustomSharedPreference(getApplicationContext());
        if(studentPref.getSignedUp()){

            //Get student_info from the shared preferences
            student_info student = studentPref.getStudentInfo(getApplicationContext());

            EditText name_text = findViewById(R.id.input_name);
            name_text.setText(student.getName());

            EditText entry_no_text = findViewById(R.id.input_entry_no);
            entry_no_text.setText(student.getEntry_no());

            EditText branch_text = findViewById(R.id.input_branch);
            branch_text.setText(student.getBranch());

            EditText year_join_text = findViewById(R.id.input_year_joining);
            year_join_text.setText(student.getYear_join());

            EditText phone_no_text = findViewById(R.id.input_phone_no);
            phone_no_text.setText(student.getPhone_no());

            //Spinner hostel_spinner = findViewById(R.id.input_hostel);
            //String hostel = spinner_selected; //hostel_spinner.getSelectedItem().toString();

            EditText room_no_text = findViewById(R.id.input_room_no);
            room_no_text.setText(student.getRoom_no());

            EditText floor_text = findViewById(R.id.input_floor);
            floor_text.setText(student.getFloor());


            EditText wing_text = findViewById(R.id.input_wing);
            wing_text.setText(student.getWing());

        }



        final Activity activity = this;

        Button enter = (Button) findViewById(R.id.pInfo_btn);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get the information from the edittexts and then add to shared preferences
                EditText name_text = findViewById(R.id.input_name);
                String name = name_text.getText().toString();

                EditText entry_no_text = findViewById(R.id.input_entry_no);
                String entry_no = entry_no_text.getText().toString();

                EditText branch_text = findViewById(R.id.input_branch);
                String branch = branch_text.getText().toString();

                EditText year_join_text = findViewById(R.id.input_year_joining);
                String year_join = year_join_text.getText().toString();

                EditText phone_no_text = findViewById(R.id.input_phone_no);
                String phone_no = phone_no_text.getText().toString();

                //Spinner hostel_spinner = findViewById(R.id.input_hostel);
                String hostel = spinner_selected; //hostel_spinner.getSelectedItem().toString();

                EditText room_no_text = findViewById(R.id.input_room_no);
                String room_no = room_no_text.getText().toString();

                EditText floor_text = findViewById(R.id.input_floor);
                String floor = floor_text.getText().toString();

                EditText wing_text = findViewById(R.id.input_wing);
                String wing = wing_text.getText().toString();

                CustomSharedPreference studentPreferences = new CustomSharedPreference(getApplicationContext());
                studentPreferences.addPersonalInfo(name, entry_no, branch, phone_no, year_join);
                studentPreferences.addHostelInfo(hostel, room_no, floor, wing);

                student_info student = studentPreferences.getStudentInfo(getApplicationContext());

                //Add the hostel info in shared preferences.
                Server server = new Server();
                server.getHostelInfo(getApplicationContext(), student);

                Intent intent = new Intent(getApplicationContext(), Homepage.class);

                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag

                startActivity(intent);
                activity.finish();

            }
        });



    }

    private void addhostelToSpinner(){

        final Spinner spinner_class = (Spinner) findViewById(R.id.input_hostel);

        List<String> class_list = new ArrayList<String>();
        class_list.add("Himadri");
        class_list.add("Kailash");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, class_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_class.setAdapter(dataAdapter);

        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_selected = spinner_class.getSelectedItem().toString();

                //Toast.makeText(getContext(), register_complaint_class, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}
