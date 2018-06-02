package com.example.cherry.complaintsystem;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//Activity: fragment_fragment_complaint_details.xml -- Excuse the stupid name, but android automatically did it. NOT ME!

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_complaint_details.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_complaint_details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_complaint_details extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public fragment_complaint_details() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_complaint_details.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_complaint_details newInstance(String param1, String param2) {
        fragment_complaint_details fragment = new fragment_complaint_details();
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
        return inflater.inflate(R.layout.fragment_fragment_complaint_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO: Make Layout Proper.

        String complaint_class=getArguments().getString("complaint_class");
        String complaint_date=getArguments().getString("complaint_date");
        String complaint_issue = getArguments().getString("complaint_issue");
        String complaint_status = getArguments().getString("complaint_status");

        TextView complaint_class_text = (TextView) view.findViewById(R.id.details_class);
        complaint_class_text.setText(complaint_class);

        TextView complaint_date_text = (TextView) view.findViewById(R.id.details_date);
        complaint_date_text.setText(complaint_date);

        TextView complaint_issue_text = (TextView) view.findViewById(R.id.details_issue);
        complaint_issue_text.setText(complaint_issue);

        String[] statuses = new String [] {"Registered", "In Progress", "Complete"};

        String str_complaint_status = statuses[Integer.parseInt(complaint_status) - 1];

        TextView complaint_status_text = (TextView) view.findViewById(R.id.details_status);
        complaint_status_text.setText(str_complaint_status);



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
}
