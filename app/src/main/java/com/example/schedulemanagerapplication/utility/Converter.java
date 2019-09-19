package com.example.schedulemanagerapplication.utility;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.example.schedulemanagerapplication.model.Schedule;
import com.example.schedulemanagerapplication.model.User;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converter
{
    @Deprecated
    public static ArrayList<Schedule> eventsToSchedules(List<Event> events) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        for (Event e:
                events) {
            schedules.add(new Schedule(e.getData().toString(),new Date(e.getTimeInMillis()),"NIL"));
        }
        return schedules;
    }

    public static int findPositionOfSchedule(ArrayList<Schedule> schedules, Schedule schedule){
        int i=0;
        for (Schedule s: schedules
             ) {
            if(s.getDate().equals(schedule.getDate()) && s.getDescription().equals(s.getDescription()))return i;
            i++;
        }
        return -1;
    }

    public static ArrayList<Schedule> getUserSchedulesByKey(String key){
        DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+key+"/Schedules");
        final ArrayList<Schedule> schedules = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        Schedule schedule = userSnapshot.getValue(Schedule.class);
                        String key = userSnapshot.getKey();

                        schedules.add(schedule);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return schedules;
    }

}
