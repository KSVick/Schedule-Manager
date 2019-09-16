package com.example.schedulemanagerapplication.utility;

import com.example.schedulemanagerapplication.model.Schedule;
import com.github.sundeepk.compactcalendarview.domain.Event;

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

}
