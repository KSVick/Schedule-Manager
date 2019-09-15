package com.example.schedulemanagerapplication.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.adapter.ScheduleAdapter;
import com.example.schedulemanagerapplication.model.Schedule;
import com.example.schedulemanagerapplication.utility.Converter;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateActivity extends AppCompatActivity {
    Context currentContext;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat actionDateFormat = new SimpleDateFormat("MMMM yyyy");
    final ScheduleAdapter scheduleAdapter = new ScheduleAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        currentContext = this;

        RecyclerView recyclerView = findViewById(R.id.task_recycleview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(scheduleAdapter);

        final CompactCalendarView compactCalendarView = findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(actionDateFormat.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        Date date = null;
        try{
            date = dateFormat.parse("20/09/2019");
        }catch (Exception e){}
        final Event ev1 = new Event(Color.GRAY, date.getTime(), "Kumpul TPA Mobile");
        compactCalendarView.addEvent(ev1);


        Event ev2 = new Event(Color.GREEN, date.getTime(), "Kumpul Lagi TPA Mobile");
        compactCalendarView.addEvent(ev2);
        Event ev3 = new Event(Color.GREEN, date.getTime(), "Kumpul Lagi Lagi TPA Mobile");
        compactCalendarView.addEvent(ev3);

        try {
            Event ev4 = new Event(Color.GREEN, dateFormat.parse("22/09/2019").getTime(), "ASD ");
            compactCalendarView.addEvent(ev4);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);

                ArrayList<Schedule> schedules = Converter.eventsToSchedules(events);
                scheduleAdapter.setSchedules(schedules);
                scheduleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionBar.setTitle(actionDateFormat.format(firstDayOfNewMonth));
            }
        });
    }


}
