package com.example.cherry.complaintsystem;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

//Currently not in use properly -- WIll see later

//Shared Preferences -- To store the student information such as name, email etc

public class CustomSharedPreference {

    static SharedPreferences StudentPreferences;
    String FILENAME = "StudentPrefs";
    static SharedPreferences.Editor editor;
    static Boolean signedUp = false;

    public CustomSharedPreference(Context context){

        StudentPreferences = context.getSharedPreferences(FILENAME, 0);
        editor = StudentPreferences.edit();
    }

    public SharedPreferences getSharedPref(String filename, Context context) {
        StudentPreferences = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        return StudentPreferences;
    }

    public void addLoginData(String entry_no_username, String password){

        editor.putString("entry_no_username", entry_no_username);
        editor.putString("password", password);

        editor.commit();
    }

    public void addPersonalInfo(String name, String entry_no, String branch, String phone_no, String year_join ){

        editor.putString("name", name);
        editor.putString("branch", branch);
        editor.putString("entry_no", entry_no);
        editor.putString("phone_no", phone_no);
        editor.putString("year_join", year_join);

        signedUp = true;

        editor.commit();
    }

    public void addHostelInfo(String hostel, String room_no, String floor, String wing){

        editor.putString("hostel", hostel);
        editor.putString("room_no", room_no);
        editor.putString("floor", floor);
        editor.putString("wing", wing);
        editor.putBoolean("signedUp", true);

        editor.commit();
    }

    public student_info getStudentInfo(Context context){

        //Get the info from the shared preferences

        String name = StudentPreferences.getString("name", null);
        String entry_no = StudentPreferences.getString("entry_no", null);
        String branch = StudentPreferences.getString("branch", null);
        String phone_no = StudentPreferences.getString("phone_no", null);
        String year_join = StudentPreferences.getString("year_join", null);

        String hostel = StudentPreferences.getString("hostel", null);
        String room_no = StudentPreferences.getString("room_no", null);
        String floor = StudentPreferences.getString("floor", null);
        String wing = StudentPreferences.getString("wing", null);


        student_info student = new student_info(name, entry_no, branch, phone_no, year_join, hostel, room_no, floor, wing);

        return student;

    }

    public Boolean getSignedUp(){


        Boolean val = false;

        try{
            val = StudentPreferences.getBoolean("signedUp", false);
        }catch (NullPointerException e){
            return false;
        }

        return val;
    }

    public void addHostelInfoFromJSON(JSONObject response){

        try {
            String caretaker = response.getString("caretaker");
            String designation = response.getString("designation");
            String phone_no = response.getString("phone_no");

            //Add to the preferences.
            editor.putString("caretaker", caretaker);
            editor.putString("designation", designation);
            editor.putString("hostel_phone_no", phone_no);

            editor.commit();

        }catch (JSONException e){
            Log.e("Error", "Extracting hostel info from json", e);
        }

    }

    public String [] getHostelInfo(Context context){

        String [] info = new String [4];

        info[0] = StudentPreferences.getString("hostel", null);
        info[1] = StudentPreferences.getString("caretaker", null);
        info[2] = StudentPreferences.getString("designation", null);
        info[3] = StudentPreferences.getString("hostel_phone_no", null);

        return info;

    }

}
