package com.example.cherry.complaintsystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

//To view the personal information and edit if needed

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link personal_info.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link personal_info#newInstance} factory method to
 * create an instance of this fragment.
 */
public class personal_info extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public personal_info() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment personal_info.
     */
    // TODO: Rename and change types and number of parameters
    public static personal_info newInstance(String param1, String param2) {
        personal_info fragment = new personal_info();
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
        return inflater.inflate(R.layout.fragment_personal_info, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CustomSharedPreference studentPreferences = new CustomSharedPreference(getContext());

        //Get student_info from the shared preferences
        student_info student = studentPreferences.getStudentInfo(getContext());


        TextView input_name = view.findViewById(R.id.input_name_info);
        input_name.setText(student.getName());

        TextView input_entry_no = view.findViewById(R.id.input_entry_no_info);
        input_entry_no.setText(student.getEntry_no());

        TextView input_branch = view.findViewById(R.id.input_branch_info);
        input_branch.setText(student.getBranch());

        TextView input_phone_no = view.findViewById(R.id.input_phone_no_info);
        input_phone_no.setText(student.getPhone_no());

        TextView input_year_join = view.findViewById(R.id.input_year_joining_info);
        input_year_join.setText(student.getYear_join());

        TextView input_hostel = view.findViewById(R.id.input_hostel_info);
        input_hostel.setText(student.getHostel());

        TextView input_room_no = view.findViewById(R.id.input_room_no_info);
        input_room_no.setText(student.getRoom_no());

        TextView input_floor = view.findViewById(R.id.input_floor_info);
        input_floor.setText(student.getFloor());

        TextView input_wing = view.findViewById(R.id.input_wing_info);
        input_wing.setText(student.getWing());

        final Activity activity = getActivity();

        Button edit_info = (Button) view.findViewById(R.id.pInfo_edit);
        edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), PersonalInfoInput.class);
                //intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                startActivity(intent);
                //activity.finish();


            }
        });


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
