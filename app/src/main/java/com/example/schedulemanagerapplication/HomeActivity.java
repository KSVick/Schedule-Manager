package com.example.schedulemanagerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    TextView lblEmail,lblFullname,lblUsername,lblPassword,lblGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        lblEmail = findViewById(R.id.lblEmail);
        lblFullname = findViewById(R.id.lblFullname);
        lblPassword = findViewById(R.id.lblPassword);
        lblUsername = findViewById(R.id.lblUsername);
        lblGender = findViewById(R.id.lblGender);

        lblEmail.setText(Helper.user.getEmail());
        lblFullname.setText(Helper.user.getFullname());
        lblUsername.setText(Helper.user.getUsername());
        lblPassword.setText(Helper.user.getPassword());
        lblGender.setText(Helper.user.getGender());
    }
}
