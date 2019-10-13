package edu.bluejack18_2.schedulemanagerapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedulemanagerapplication.R;
import edu.bluejack18_2.schedulemanagerapplication.adapter.ScheduleAdapter;
import edu.bluejack18_2.schedulemanagerapplication.model.Schedule;
import edu.bluejack18_2.schedulemanagerapplication.utility.Converter;
import edu.bluejack18_2.schedulemanagerapplication.utility.SharedPrefManager;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateActivity extends AppCompatActivity implements View.OnClickListener {
    Context currentContext;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat actionDateFormat = new SimpleDateFormat("MMMM yyyy");
    private ScheduleAdapter scheduleAdapter = null;
    private Date currentDate = null;
    TextView textViewDate = null;
    private DatabaseReference databaseReference;
    private SharedPrefManager sharedPrefManager;
    private CompactCalendarView compactCalendarView;
    private ArrayList<Schedule> schedules = new ArrayList<>();

    private void refreshRecycleViewData(){
        List<Event> events = compactCalendarView.getEvents(currentDate);
        ArrayList<Schedule> schedules = Converter.eventsToSchedules(events);
        scheduleAdapter.setSchedules(schedules);
        scheduleAdapter.setMaster(this.schedules);
        scheduleAdapter.notifyDataSetChanged();
    }

    public void refreshScheduleDatas(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    compactCalendarView.removeAllEvents();
                    schedules = new ArrayList<>();
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        Schedule schedule = userSnapshot.getValue(Schedule.class);
                        Event ev = new Event(Color.BLUE, schedule.getDate().getTime(), schedule.getDescription());
                        compactCalendarView.addEvent(ev);
                        schedules.add(schedule);
                    }
                }
                refreshRecycleViewData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        scheduleAdapter = new ScheduleAdapter(this);
        currentContext = this;
        currentDate = new Date();
        textViewDate = findViewById(R.id.activity_date_txtViewDate);
        textViewDate.setText(dateFormat.format(currentDate));

        RecyclerView recyclerView = findViewById(R.id.task_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(scheduleAdapter);

        compactCalendarView = findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setEventIndicatorStyle(CompactCalendarView.FILL_LARGE_INDICATOR);

        sharedPrefManager = new SharedPrefManager(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+sharedPrefManager.getSPUserKey()+"/Schedules");

        refreshScheduleDatas();

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(actionDateFormat.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                TextView textViewDate = findViewById(R.id.activity_date_txtViewDate);
                textViewDate.setText(dateFormat.format(dateClicked));
                currentDate = dateClicked;
                refreshRecycleViewData();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(actionDateFormat.format(firstDayOfNewMonth));
            }
        });

        findViewById(R.id.activity_date_btnInsertSchedule).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_date_btnInsertSchedule:
                TextInputEditText textInputEditText = findViewById(R.id.activity_date_txtInputDescription);
                String description = textInputEditText.getText().toString();

                String userId = sharedPrefManager.getSPUserKey();
                String scheduleId = databaseReference.push().getKey();
                Schedule schedule = new Schedule(description, currentDate, scheduleId);
                databaseReference.child(scheduleId).setValue(schedule);

                textInputEditText.setText("");
                Toast.makeText(currentContext, "Success", Toast.LENGTH_SHORT).show();
//                refreshScheduleData();
                break;
        }

    }
}
