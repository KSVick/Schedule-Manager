package edu.bluejack18_2.schedulemanagerapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.schedulemanagerapplication.R;
import edu.bluejack18_2.schedulemanagerapplication.activity.NewsActivity;
import edu.bluejack18_2.schedulemanagerapplication.adapter.AgendaAdapter;
import edu.bluejack18_2.schedulemanagerapplication.adapter.AgendaViewPagerAdapter;
import edu.bluejack18_2.schedulemanagerapplication.model.Schedule;
import edu.bluejack18_2.schedulemanagerapplication.utility.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodayAgendaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodayAgendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayAgendaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference databaseReference;
    private SharedPrefManager sharedPrefManager;
    private AgendaAdapter agendaAdapter;
    private ViewPager viewPager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TodayAgendaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayAgendaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayAgendaFragment newInstance(String param1, String param2) {
        TodayAgendaFragment fragment = new TodayAgendaFragment();
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

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public void refreshScheduleData(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Schedule> schedules;
                if(dataSnapshot.exists()){
                    schedules = new ArrayList<>();
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        Schedule schedule = userSnapshot.getValue(Schedule.class);
//                        String key = userSnapshot.getKey();
                        if(isSameDay(schedule.getDate(),new Date()))
                            schedules.add(schedule);
                    }
                    agendaAdapter.setSchedules(schedules);
                    agendaAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_today_agenda, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.fragment_today_agenda_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
        sharedPrefManager = new SharedPrefManager(root.getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+sharedPrefManager.getSPUserKey()+"/Schedules");
        agendaAdapter = new AgendaAdapter(root.getContext());
        Button button = root.findViewById(R.id.fragment_today_agenda_detailBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewsActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(agendaAdapter);
        refreshScheduleData();

//        viewPager = root.findViewById(R.id.news_viewPager);

//        AgendaViewPagerAdapter viewPagerAdapter = new AgendaViewPagerAdapter();

//        viewPager.setAdapter(buildAdapter());

        // Inflate the layout for this fragment
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
    private PagerAdapter buildAdapter() {
        return(new AgendaViewPagerAdapter(getContext(), getChildFragmentManager()));
    }
}
