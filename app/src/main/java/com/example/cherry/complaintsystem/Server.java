package com.example.cherry.complaintsystem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Server {

    String server_url = "http://192.168.42.91/yo/";
    String push_php = "db_complaints.php";
    String sync_php = "db_sync.php";
    String hostel_info_php = "dp_hostel.php";
    Boolean posted = false;
    public JSONObject ReqResponse;

    public void push(final complaint_model complaints, student_info student, final Context context){

        //TODO: Add in functionality for multiple complaints if required.

        //TODO: Fix datetime error in PHP Script
        JSONObject jsonObj = convertToJSON(complaints, student, "insert");

        JsonObjectRequest req = new JsonObjectRequest(server_url+push_php, jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response_post", response.toString());
                        //ReqResponse = response;

                        int server_id;

                        //Update the table with the server id
                        try{
                            server_id = response.getInt("server_id");
                        }catch (JSONException e){
                            server_id = 0;
                            Log.d("Error", "Unable to get server_id");
                        }

                        //Update the database with that record
                        ContentValues values = new ContentValues();
                        values.put(DBContentProvider._SID, server_id);

                        //Update database content provider
                        int count = context.getContentResolver().update(DBContentProvider.CONTENT_URI_COMPLAINTS, values, DBContentProvider._ID+"=?", new String[] {String.valueOf(complaints.getComplaintID())});
                        Log.d("count", Integer.toString(count));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", "Its okay. keep trying");
                VolleyLog.e("Error: ", error.getMessage());

            }
        });

        Mysingleton.getInstance(context).addTorequestque(req);

    }

    private JSONObject convertToJSON(complaint_model complaint, student_info student, String action){

        JSONObject jsonObject = new JSONObject();

        JSONArray student_details = new JSONArray();
        student_details.put(student.getName());
        student_details.put(student.getEntry_no());
        student_details.put(student.getHostel());
        student_details.put(student.getRoom_no());

        JSONArray student_complaint = new JSONArray();
        if(complaint == null){
            student_complaint = null;
        }else{
            student_complaint.put(complaint.getComplaintID()); //ID in local database
            student_complaint.put(complaint.getComplaintClass()); //Class of the complaint
            student_complaint.put(complaint.getIssue()); //Issue
            student_complaint.put(complaint.getDate_time()); //Date_Time
            student_complaint.put(complaint.getStatus()); //Status -- Local Database

        }

        try{
            jsonObject.put("action", action);
            jsonObject.put("student", student_details);
            jsonObject.put("complaint", student_complaint);
        }catch (JSONException e){
            Log.d("Error", "JSONException student_details");
        }

        return jsonObject;
    }


     public void sync(final Context context, student_info student, int action, Activity act){

        //Check if there exists a null server id -- New complaint record
        Log.d("response_post", "sync");
        //Check if local database matches the information stored in the server.
        if(action == 1){
         synclocalDBwithGlobalDB(context, student, act);
        }
        else{
            updated_complaints(context, student, act);
        }


         //Check the updated column in the db -- return the json data of any updated columns

     }

     public void synclocalDBwithGlobalDB(final Context context, student_info student, final Activity act){

        //Retrieve the data from the servers -- and then do the heavy work updating within the app
         JSONObject jsonObj = convertToJSON(null, student, "sync");

         JsonObjectRequest req = new JsonObjectRequest(server_url + sync_php, jsonObj,
                 new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject response) {
                         Log.d("response_post", response.toString());
                         Boolean Success = true;
                         //Check if data is returned or not
                         try{
                             String status = response.getString("status").toString();
                             Log.d("response_post", status);
                             if(!status.equals("Success")){
                                 Success = false;
                                 Log.d("response_post", "No data received");
                             }


                         }catch(JSONException e){
                             Log.d("response_post", "JSON Error Exception");
                         }


                         if(Success){
                             //Delete the records in the local database and then insert the new data
                             //int count = context.getContentResolver().delete(DBContentProvider.CONTENT_URI_COMPLAINTS, "1", null);
                             //Log.d("response_post", "No. of Deleted Records "+Integer.toString(count));

                             //Insert all the new data into the table
                             try{
                                 JSONArray complaints = response.getJSONArray("complaints");

                                 for(int i=0; i<complaints.length(); i++){

                                     JSONArray complaint_row = complaints.getJSONArray(i);
                                     ContentValues values = new ContentValues();
                                     int _SID = Integer.parseInt(complaint_row.getString(0));
                                     String complaint_class = complaint_row.getString(1);
                                     String complaint_issue = complaint_row.getString(2);
                                     String complaint_date_time = complaint_row.getString(3);

                                     int complaint_status = Integer.parseInt(complaint_row.getString(4));

                                     Log.d("response_post", Integer.toString(_SID) + ", " + complaint_class + ", " + complaint_issue + ", " + complaint_date_time + ", " + complaint_status);

                                     values.put(DBContentProvider._SID, _SID);
                                     values.put(DBContentProvider.COMPLAINT_CLASS, complaint_class);
                                     values.put(DBContentProvider.COMPLAINT_ISSUE, complaint_issue);
                                     values.put(DBContentProvider.COMPLAINT_DATE, complaint_date_time);
                                     values.put(DBContentProvider.COMPLAINT_STATUS, complaint_status);

                                     //Update in database content provider
                                     int count = context.getContentResolver().update(DBContentProvider.CONTENT_URI_COMPLAINTS, values, DBContentProvider._SID+"=?", new String[] {String.valueOf(_SID)});

                                     Log.d("response_post", "count update: " + Integer.toString(count));

                                     if(count == 0) {
                                         //Update didn't work as there's no record with that _SID
                                         //Insert the new record
                                         Uri uri = context.getContentResolver().insert(DBContentProvider.CONTENT_URI_COMPLAINTS, values);
                                         Log.d("response_post", uri.toString());
                                     }
                                 }

                                 //Go to homepage activity
                                 Intent intent = new Intent(act, Homepage.class);
                                 context.startActivity(intent);
                                 act.finish();

                             }catch(JSONException e){
                                 Log.d("response_post", "Json Error Exception 2");
                                 Log.e("response_post", "JSON", e);
                             }



                         }

                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Log.d("Error", "Its okay. keep trying");
                 VolleyLog.e("Error: ", error.getMessage());

             }
         });

         Mysingleton.getInstance(context).addTorequestque(req);

     }

     public void updated_complaints(final Context context, final student_info student, final Activity act){
        Log.d("response_post", "checking for updated complaints");
         JSONObject jsonObj = convertToJSON(null, student, "updated");


         JsonObjectRequest req = new JsonObjectRequest(server_url + sync_php, jsonObj,
                 new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject response) {
                         Log.d("response_post", response.toString());

                         Boolean Success = true;

                         try{
                             String status = response.getString("status").toString();
                             Log.d("response_post", status);
                             if(!status.equals("Success")){
                                 Success = false;
                                 Log.d("response_post", "No data received");
                             }


                         }catch(JSONException e){
                             Log.d("response_post", "JSON Error Exception");
                         }


                         if(Success) {


                             try {
                                 JSONArray complaints = response.getJSONArray("complaints");
                                 Log.d("response_post", "here");
                                 for (int i = 0; i < complaints.length(); i++) {

                                     JSONArray complaint_row = complaints.getJSONArray(i);
                                     ContentValues values = new ContentValues();
                                     int _SID = Integer.parseInt(complaint_row.getString(0));
                                     String complaint_class = complaint_row.getString(1);
                                     String complaint_issue = complaint_row.getString(2);
                                     String complaint_date_time = complaint_row.getString(3);

                                     int complaint_status = Integer.parseInt(complaint_row.getString(4));

                                     Log.d("response_post", Integer.toString(_SID) + ", " + complaint_class + ", " + complaint_issue + ", " + complaint_date_time + ", " + complaint_status);

                                     values.put(DBContentProvider._SID, _SID);
                                     values.put(DBContentProvider.COMPLAINT_CLASS, complaint_class);
                                     values.put(DBContentProvider.COMPLAINT_ISSUE, complaint_issue);
                                     values.put(DBContentProvider.COMPLAINT_DATE, complaint_date_time);
                                     values.put(DBContentProvider.COMPLAINT_STATUS, complaint_status);

                                     //Update in database content provider
                                     int count = context.getContentResolver().update(DBContentProvider.CONTENT_URI_COMPLAINTS, values, DBContentProvider._SID + "=?", new String[]{String.valueOf(_SID)});

                                     Log.d("response_post", "count update: " + Integer.toString(count));

                                     if (count == 0) {
                                         //Update didn't work as there's no record with that _SID
                                         //Insert the new record
                                         Uri uri = context.getContentResolver().insert(DBContentProvider.CONTENT_URI_COMPLAINTS, values);
                                         Log.d("response_post", uri.toString());
                                     }
                                 }

                                 Log.d("response_post", "finished updateing database");

                                 //Send the confirmation that updating has been done and reset the updated columns in the server to zero
                                 sendUpdatedConfirmation(context, student);

                                 //Go to homepage activity
                                 Intent intent = new Intent(act, Homepage.class);
                                 context.startActivity(intent);
                                 act.finish();


                             } catch (JSONException e) {
                                 Log.d("response_post", "Json Error Exception 2");
                                 Log.e("response_post", "JSON", e);
                             }
                         }

                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Log.d("Error", "Its okay. keep trying");
                 VolleyLog.e("Error: ", error.getMessage());

             }
         });

         Mysingleton.getInstance(context).addTorequestque(req);



     }


     public void getHostelInfo(final Context context, student_info student){
         Log.d("Hostel", "getting hostel info");
         JSONObject jsonObj = convertToJSON(null, student, "hostel info");
         Log.d("Hostel", jsonObj.toString());

         JsonObjectRequest req = new JsonObjectRequest(server_url + hostel_info_php, jsonObj,
                 new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject response) {
                         Log.d("response_post", response.toString());

                         try{

                             String status = response.getString("status").toString();
                             Log.d("response_post", status);
                             if(status.equals("Success")){
                                 //Update the records
                                 CustomSharedPreference sharedPreference = new CustomSharedPreference(context);
                                 sharedPreference.addHostelInfoFromJSON(response);
                                 Log.d("Hostel", "Information added");
                             }

                         }catch (JSONException e){

                             Log.e("Error", "Hostel Info JSON", e);
                         }


                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Log.d("Error", "Its okay. keep trying");
                 VolleyLog.e("Error: ", error.getMessage());

             }
         });


         Mysingleton.getInstance(context).addTorequestque(req);

     }

     private void sendUpdatedConfirmation(final Context context, final student_info student){
        Log.d("response_post", "sending updated confirmation");
         JSONObject jsonObj = convertToJSON(null, student, "updated done");

         JsonObjectRequest req = new JsonObjectRequest(server_url + sync_php, jsonObj,
                 new Response.Listener<JSONObject>() {
                     @Override
                     public void onResponse(JSONObject response) {
                         Log.d("response_post", response.toString());

                         try{

                             String status = response.getString("status").toString();
                             Log.d("response_post", status);
                             if(status.equals("Success")) {
                                 //Update success
                                 Log.d("response_post", "Its a success");
                             }

                         }catch (JSONException e){

                             Log.e("Error", "Update confirmation JSON", e);
                         }


                     }
                 }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Log.d("Error", "Its okay. keep trying");
                 VolleyLog.e("Error: ", error.getMessage());

             }
         });


         Mysingleton.getInstance(context).addTorequestque(req);


     }




}
