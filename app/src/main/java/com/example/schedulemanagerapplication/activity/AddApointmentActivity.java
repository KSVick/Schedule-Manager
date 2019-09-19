package com.example.schedulemanagerapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.utility.Helper;

public class AddApointmentActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apointment);

//        Intent intent = getIntent();
//        String id = intent.getStringExtra("user_identifier");
        Toast.makeText(this, Helper.id, Toast.LENGTH_SHORT).show();

    }
}
