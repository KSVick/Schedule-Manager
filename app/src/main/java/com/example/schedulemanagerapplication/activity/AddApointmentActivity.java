package com.example.schedulemanagerapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.schedulemanagerapplication.R;

public class AddApointmentActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apointment);

        Intent intent = getIntent();
        String message = intent.getStringExtra("user_identifier");


        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
