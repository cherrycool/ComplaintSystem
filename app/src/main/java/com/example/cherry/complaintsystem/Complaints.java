package com.example.cherry.complaintsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//XML File: fragment_complaints.xml

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Complaints.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Complaints#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Complaints extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Variables for setting up the Listview
    List<complaint_model> ComplaintModel;
    ListView listView;
    private static CustomAdapter ComplaintAdapter;

    public Complaints() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Complaints.
     */
    // TODO: Rename and change types and number of parameters
    public static Complaints newInstance(String param1, String param2) {
        Complaints fragment = new Complaints();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_complaints, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listView = (ListView) view.findViewById(R.id.complaint_list);

        //Get the data stored in the local database.
        Cursor c = extract_local_complaints();

        ComplaintModel = new ArrayList<>();

        if (c.moveToFirst()) {
            do{
                int _ID = Integer.parseInt(c.getString(c.getColumnIndex(DBContentProvider._ID)));
                String _CLASS = c.getString(c.getColumnIndex(DBContentProvider.COMPLAINT_CLASS));
                String _ISSUE = c.getString(c.getColumnIndex(DBContentProvider.COMPLAINT_ISSUE));
                String _DATE = c.getString(c.getColumnIndex(DBContentProvider.COMPLAINT_DATE));
                int _STATUS = Integer.parseInt(c.getString(c.getColumnIndex(DBContentProvider.COMPLAINT_STATUS)));



                complaint_model complaint = new complaint_model(_ID, _CLASS, _ISSUE,_DATE, _STATUS);

                //To sync and push to server -- If server id is null, means its a new record, so its inserted in the database.
                if(TextUtils.isEmpty(c.getString(c.getColumnIndex(DBContentProvider._SID)))){
                    Server server = new Server();

                    Log.d("unsynced", c.getString(c.getColumnIndex(DBContentProvider._ID)));

                    //Dummy Student Info
                    //student_info student = new student_info("Ash", "2013CS5", "971777", "Himadri", "ED-14");
                    CustomSharedPreference student_preferences = new CustomSharedPreference(getContext());
                    student_info student = student_preferences.getStudentInfo(getContext());
                    //Insert into database on server.
                    server.push(complaint, student, getContext());
                }

                ComplaintModel.add(complaint);

            } while (c.moveToNext());


        }else{

            Server server = new Server();
            CustomSharedPreference student_preferences = new CustomSharedPreference(getContext());
            student_info student = student_preferences.getStudentInfo(getContext());
            server.sync(getContext(), student, 1, getActivity());

        }

        ComplaintAdapter = new CustomAdapter(getContext(), ComplaintModel);

        listView.setAdapter(ComplaintAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                complaint_model c_model = ComplaintModel.get(position);

                Bundle bundle = new Bundle();
                bundle.putString("complaint_class", c_model.getComplaintClass());
                bundle.putString("complaint_date", c_model.getDate_time());
                bundle.putString("complaint_issue", c_model.getIssue());
                bundle.putString("complaint_status", Integer.toString(c_model.getStatus()));

                Fragment fragment = new fragment_complaint_details();
                fragment.setArguments(bundle);

                replaceFragment(fragment);


                //Snackbar.make(view, c_model.getComplaintClass() + ", " + c_model.getIssue(), Snackbar.LENGTH_LONG).setAction("No action", null).show();
            }
        });

        //Check for updates
        Server server = new Server();
        CustomSharedPreference student_preferences = new CustomSharedPreference(getContext());
        student_info student = student_preferences.getStudentInfo(getContext());
        server.sync(getContext(), student, 2, getActivity());



    }


        // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private Cursor extract_local_complaints(){
        // Retrieve student records
        String URL = "content://com.example.cherry.complaintsystem.DBContentProvider/complaints_uri";
        Uri local_complaints_uri = Uri.parse(URL);

        Cursor c = getActivity().managedQuery(local_complaints_uri, null, null, null, null);

        return c;

    }
}
