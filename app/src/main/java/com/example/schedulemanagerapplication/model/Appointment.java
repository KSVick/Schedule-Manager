package com.example.schedulemanagerapplication.model;

import java.util.Date;

public class Appointment extends Schedule {
    private String fromUserId;
    public Appointment(String description, Date date, String id, String fromUserId) {
        super(description, date, id);
        this.fromUserId = fromUserId;
    }
}
