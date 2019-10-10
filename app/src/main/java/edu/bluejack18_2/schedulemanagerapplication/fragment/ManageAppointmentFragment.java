package edu.bluejack18_2.schedulemanagerapplication.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.schedulemanagerapplication.R;

import edu.bluejack18_2.schedulemanagerapplication.adapter.AppointmentAdapter;
import edu.bluejack18_2.schedulemanagerapplication.model.Appointment;
import edu.bluejack18_2.schedulemanagerapplication.utility.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ManageAppointmentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ManageAppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageAppointmentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference databaseReference;
    private SharedPrefManager sharedPrefManager;
    private AppointmentAdapter appointmentAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ManageAppointmentFragment() {
        // Required empty public constructor
    }

    public void refreshScheduleData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Appointment> appointments = new ArrayList<>();
                if(dataSnapshot.exists()){
//                    appointments = new ArrayList<>();
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        Appointment appointment = userSnapshot.getValue(Appointment.class);
//                        String key = userSnapshot.getKey();
//                        Toast.makeText(getContext(),appointment.getDescription(),Toast.LENGTH_SHORT).show();
                        appointments.add(appointment);
                    }
                    appointmentAdapter.setAppointments(appointments);
                    appointmentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageAppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageAppointmentFragment newInstance(String param1, String param2) {
        ManageAppointmentFragment fragment = new ManageAppointmentFragment();
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
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_manage_appointment, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.fragment_manage_appointment_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        sharedPrefManager = new SharedPrefManager(root.getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+sharedPrefManager.getSPUserKey()+"/Appointments");
        appointmentAdapter = new AppointmentAdapter(this);
        recyclerView.setAdapter(appointmentAdapter);
        refreshScheduleData();

        return root;
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
