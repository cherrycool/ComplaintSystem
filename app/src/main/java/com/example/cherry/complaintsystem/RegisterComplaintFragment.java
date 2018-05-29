package com.example.cherry.complaintsystem;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//Activity XML : fragment_register_complaint.xml

//Currently has a bug -- lemme fix it

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterComplaintFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterComplaintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterComplaintFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String register_complaint_class;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public RegisterComplaintFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterComplaintFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterComplaintFragment newInstance(String param1, String param2) {
        RegisterComplaintFragment fragment = new RegisterComplaintFragment();
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
        return inflater.inflate(R.layout.fragment_register_complaint, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Create Class dropdown menu TODO: Get the classes from a db --> temporarily stored in shared preferences
        addClassesToSpinner(view);

        Button btn = (Button) view.findViewById(R.id.register_okay);

        final Spinner class_spinner = (Spinner) view.findViewById(R.id.register_class);
        final EditText issue_edit = (EditText) view.findViewById(R.id.register_issue);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 1. Save in local DB and display on list
                //TODO: 2. Sync with server

                CustomSharedPreference sharedPrefObj = new CustomSharedPreference(getContext());
                SharedPreferences sharedPref = sharedPrefObj.getSharedPref("complaints", getContext());

                int id = sharedPref.getAll().size();
                id++;


                String complaint_class = class_spinner.getSelectedItem().toString();


                String complaint_issue = issue_edit.getText().toString();

                //Create the complaint model
                complaint_model c_data = new complaint_model(id, register_complaint_class, complaint_issue, null, 1);

                // Add a new student record
                ContentValues values = new ContentValues();
                values.put(DBContentProvider.COMPLAINT_CLASS, register_complaint_class);

                values.put(DBContentProvider.COMPLAINT_ISSUE, complaint_issue);

                values.put(DBContentProvider.COMPLAINT_STATUS, 1);

                //Insert in database content provider
                Uri uri = getContext().getContentResolver().insert(DBContentProvider.CONTENT_URI_COMPLAINTS, values);

                //Check if inserted or not in debug
                Log.d("complaint_db", uri.toString());

                Toast.makeText(getContext(), "Complaint registered", Toast.LENGTH_LONG).show();

                //Send the data to the server.
                long long_id = ContentUris.parseId(uri); //id of the last inserted data.


                //Server serv = new Server();

                //serv.push(c_data, null, getContext());

                Fragment fragment = new Complaints();

                replaceFragment(fragment);
            }
        });

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
        //transaction.addToBackStack(null);
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

    public void addClassesToSpinner(View view){

        final Spinner spinner_class = (Spinner) view.findViewById(R.id.register_class);

        List<String> class_list = new ArrayList<String>();
        class_list.add("Washing Machine");
        class_list.add("Electrical");
        class_list.add("Plumbing");
        class_list.add("Carpentry");
        class_list.add("Others");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, class_list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_class.setAdapter(dataAdapter);

        spinner_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                register_complaint_class = spinner_class.getSelectedItem().toString();

                //Toast.makeText(getContext(), register_complaint_class, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
