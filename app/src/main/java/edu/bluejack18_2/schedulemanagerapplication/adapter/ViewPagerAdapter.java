package edu.bluejack18_2.schedulemanagerapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.schedulemanagerapplication.R;
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

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private String [] titles = {"yesterday","today","tomorrow"};
    private DatabaseReference databaseReference;
    private SharedPrefManager sharedPrefManager;
    private AgendaAdapter agendaAdapter;
    private ArrayList<Schedule> schedules = new ArrayList<>();
    private Date cdate;

    public ViewPagerAdapter(Context context) {
        this.context = context;
        sharedPrefManager = new SharedPrefManager(context);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+sharedPrefManager.getSPUserKey()+"/Schedules");
        agendaAdapter = new AgendaAdapter(context);
//        Date dt = new Date();
//        Calendar c = Calendar.getInstance();
//        c.setTime(dt);
//        c.add(Calendar.DATE, -1);
//        dt = c.getTime();

//        cdate = dt;



    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
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

    public void refreshScheduleData(final Date date, final AgendaAdapter agendaAdapter){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    schedules = new ArrayList<>();
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        Schedule schedule = userSnapshot.getValue(Schedule.class);
                        if(isSameDay(schedule.getDate(),date))
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

    private void filterDate(Date date){
        ArrayList<Schedule> newSchedules = new ArrayList<>();
        for (Schedule s: schedules
             ) {
            if(isSameDay(s.getDate(),date))
                newSchedules.add(s);
        }
        agendaAdapter.setSchedules(newSchedules);
        agendaAdapter.notifyDataSetChanged();

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout,null);

        TextView titleTxt = view.findViewById(R.id.custom_layout_title_txt);
        titleTxt.setText(titles[position]);

        RecyclerView recyclerView = view.findViewById(R.id.custom_layout_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        AgendaAdapter agendaAdapter = new AgendaAdapter(context);
        recyclerView.setAdapter(agendaAdapter);

        if(titles[position].equals("tomorrow")) {
            Date dt = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, 1);
            dt = c.getTime();
//            filterDate(dt);
            cdate = dt;
            refreshScheduleData(dt,agendaAdapter);
        }else if(titles[position].equals("yesterday")){
            Date dt = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(dt);
            c.add(Calendar.DATE, -1);
            dt = c.getTime();
//            filterDate(dt);
            cdate = dt;
            refreshScheduleData(dt,agendaAdapter);
        }else{
            Date dt = new Date();
//            filterDate(dt);
            cdate = dt;
            refreshScheduleData(dt,agendaAdapter);
        }

        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }


}