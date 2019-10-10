package edu.bluejack18_2.schedulemanagerapplication.model;

public class User {
    private String email;
    private String fullname;
    private String username;
    private String password;
    private String gender;

    public User(String email, String fullname, String username, String password, String gender) {
        this.email = email;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.gender = gender;
    }

    public User(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
