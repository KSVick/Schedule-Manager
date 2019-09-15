package com.example.schedulemanagerapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.utility.Helper;
import com.example.schedulemanagerapplication.utility.SharedPrefManager;

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

        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);

        //Ini Id Usernya : sharedPrefManager.getSPUserKey()
//        Toast.makeText(this, sharedPrefManager.getSPUserKey(),Toast.LENGTH_SHORT).show();


    }
}
