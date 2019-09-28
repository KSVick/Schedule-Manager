package com.example.schedulemanagerapplication.model;

import java.util.Date;

public class Schedule {
    private String description;
    private Date date;
    private String id;
    private User collaborator;
    private String location;

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

    public void setCollaborator(User collaborator) {
        this.collaborator = collaborator;
    }

    public User getCollaborator() {
        return collaborator;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
