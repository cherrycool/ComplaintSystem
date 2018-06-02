package com.example.cherry.complaintsystem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//To get the complaint details information and load into the listview adapter in fragment_complaint_details.java

/**
 * Created by Cherry on 4/23/2018.
 */

public class CustomAdapter extends ArrayAdapter<complaint_model> implements View.OnClickListener {

    private List<complaint_model> dataSet;
    Context mContext;
    private String[] statuses = new String [] {"Registered", "In Progress", "Complete"};

    //View Lookup Cache
    private static class ViewHolder{
        TextView txtClass;
        TextView txtDate_time;
        TextView txtStatus;

    }

    public CustomAdapter(Context context, List<complaint_model> data){
        super(context, R.layout.complaint_row, data);

        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public void onClick(View v){
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        complaint_model complaintModel = (complaint_model) object;

        Log.d("Complaint_info", Integer.toString(complaintModel.getComplaintID()) + ", " + complaintModel.getComplaintClass() + ", " + complaintModel.getDate_time());

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //Get the data item for this position
        complaint_model ComplaintModel = getItem(position);

        //check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; //view lookup cache stored in tag

        final View result;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.complaint_row, parent, false);
            viewHolder.txtClass = (TextView) convertView.findViewById(R.id.complaint_class);
            viewHolder.txtDate_time = (TextView) convertView.findViewById(R.id.complaint_date);
            viewHolder.txtStatus = (TextView) convertView.findViewById(R.id.complaint_status_value);

            result = convertView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
            result = convertView;

        }

        lastPosition = position;

        viewHolder.txtClass.setText(ComplaintModel.getComplaintClass());
        viewHolder.txtDate_time.setText(ComplaintModel.getDate_time());

        String text_status = statuses[ComplaintModel.getStatus() - 1];

        viewHolder.txtStatus.setText(text_status);

        return convertView;
    }
}
