package com.example.schedulemanagerapplication.model;

import java.util.Date;

public class Appointment extends Schedule {
    private User fromUser;
    public Appointment(String description, Date date, String id, User fromUser) {
        super(description, date, id);
        this.fromUser = fromUser;
    }

    Appointment(){

    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }
}
