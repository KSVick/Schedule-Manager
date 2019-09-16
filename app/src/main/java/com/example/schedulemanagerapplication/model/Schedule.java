package com.example.schedulemanagerapplication.model;

import java.util.Date;

public class Schedule {
    private String description;
    private Date date;
    private String id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Schedule(String description, Date date, String id) {
        this.description = description;
        this.date = date;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Schedule(){

    }
}
