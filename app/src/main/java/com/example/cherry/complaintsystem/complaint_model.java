package com.example.cherry.complaintsystem;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Cherry on 4/23/2018.
 */

public class complaint_model {

    int complaint_id;
    String complaint_class;
    String issue;
    String date_time;
    int status;

    public complaint_model(int complaint_id, String complaint_class, String issue, String date_time, int status){

        this.complaint_id = complaint_id;
        this.complaint_class = complaint_class;
        this.issue = issue;
        this.date_time = date_time;
        this.status = status;
    }

    public int getComplaintID(){
        return complaint_id;
    }

    public String getComplaintClass(){
        return complaint_class;
    }

    public String getIssue(){
        return issue;
    }

    public String getDate_time(){
        return date_time;
    }

    public int getStatus(){
        return status;
    }


}
