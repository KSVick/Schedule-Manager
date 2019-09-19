package com.example.schedulemanagerapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.model.Appointment;
import com.example.schedulemanagerapplication.model.Schedule;
import com.example.schedulemanagerapplication.utility.Helper;
import com.example.schedulemanagerapplication.utility.SharedPrefManager;
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

public class AddApointmentActivity extends AppCompatActivity implements View.OnClickListener {
    private Date currentDate = null;
    DatabaseReference databaseReference;
    DatabaseReference mydatabaseReference;
    DatabaseReference databaseReferenceAppointment;
    private SharedPrefManager sharedPrefManager;
    private CompactCalendarView compactCalendarView;
    private ArrayList<Schedule> schedules = new ArrayList<>();
    private ArrayList<Schedule> myschedules = new ArrayList<>();
    private Button btnCompare, btnInsert;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat actionDateFormat = new SimpleDateFormat("MMMM yyyy");

    public void refreshScheduleData(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    compactCalendarView.removeAllEvents();
                    schedules = new ArrayList<>();
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        Schedule schedule = userSnapshot.getValue(Schedule.class);
                        String key = userSnapshot.getKey();
                        Event ev = new Event(Color.GREEN, schedule.getDate().getTime(), schedule.getDescription());
                        compactCalendarView.addEvent(ev);
                        schedules.add(schedule);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getMyScheduleData(){
        mydatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
//                    compactCalendarView.removeAllEvents();
//                    compactCalendarView.removeEvent();
                    myschedules = new ArrayList<>();
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        Schedule schedule = userSnapshot.getValue(Schedule.class);
                        String key = userSnapshot.getKey();

                        Event ev;
                        List<Event> temp = compactCalendarView.getEvents(schedule.getDate());
                        if(temp.size() >0){
                            compactCalendarView.removeEvent(temp.get(0));
                            ev = new Event(Color.YELLOW, schedule.getDate().getTime(), schedule.getDescription());
                        }else{
                            ev = new Event(Color.BLUE, schedule.getDate().getTime(), schedule.getDescription());
                        }

                        compactCalendarView.addEvent(ev);
                        myschedules.add(schedule);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apointment);

//        Intent intent = getIntent();
//        String id = intent.getStringExtra("user_identifier");
        sharedPrefManager = new SharedPrefManager(this);
        mydatabaseReference = FirebaseDatabase.getInstance().getReference("Users/"+sharedPrefManager.getSPUserKey()+"/Schedules");

        btnCompare = findViewById(R.id.add_apointment_button_compare_schedule);
        btnInsert = findViewById(R.id.activity_add_apointment_btnInsertSchedule);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+Helper.id+"/Schedules");
        databaseReferenceAppointment = FirebaseDatabase.getInstance().getReference("Users/"+Helper.id+"/Appointments");

        compactCalendarView = findViewById(R.id.activity_add_appointment_compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setEventIndicatorStyle(CompactCalendarView.FILL_LARGE_INDICATOR);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(actionDateFormat.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        refreshScheduleData();

        btnCompare.setOnClickListener(this);
        btnInsert.setOnClickListener(this);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                TextView textViewDate = findViewById(R.id.activity_add_apointment_txtViewDate);
                textViewDate.setText(dateFormat.format(dateClicked));
                currentDate = dateClicked;

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(actionDateFormat.format(firstDayOfNewMonth));
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_apointment_button_compare_schedule:
                getMyScheduleData();
                btnCompare.setEnabled(false);
                break;
            case R.id.activity_add_apointment_btnInsertSchedule:
                TextInputEditText textInputEditText = findViewById(R.id.activity_add_apointment_txtInputDescription);
                String description = textInputEditText.getText().toString();

                String userId = sharedPrefManager.getSPUserKey();
                String scheduleId = databaseReferenceAppointment.push().getKey();
                Appointment schedule = new Appointment(description, currentDate, scheduleId, userId);
                databaseReferenceAppointment.child(scheduleId).setValue(schedule);

                textInputEditText.setText("");
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                refreshScheduleData();

                refreshScheduleData();
                btnCompare.setEnabled(true);
                break;
        }
    }
}
