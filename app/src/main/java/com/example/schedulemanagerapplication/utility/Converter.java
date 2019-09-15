package com.example.schedulemanagerapplication.utility;

import com.example.schedulemanagerapplication.model.Schedule;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Converter {
    public static ArrayList<Schedule> eventsToSchedules(List<Event> events) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        for (Event e:
                events) {
            schedules.add(new Schedule(e.getData().toString(),new Date(e.getTimeInMillis())));
        }
        return schedules;
    }
}
