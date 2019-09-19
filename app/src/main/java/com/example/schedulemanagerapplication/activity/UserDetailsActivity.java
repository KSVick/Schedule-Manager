package com.example.schedulemanagerapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.utility.Helper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView lblUsername,lblGender;
    private EditText txtEmail,txtFullname;
    private ImageView imgSchedule;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        lblGender = findViewById(R.id.userDetails_lblGender);
        lblUsername = findViewById(R.id.userDetails_lblUsername);
        txtEmail = findViewById(R.id.userDetails_txtEmail);
        txtFullname = findViewById(R.id.userDetails_txtFullname);
        imgSchedule = findViewById(R.id.userDetails_imgSchedule);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        lblGender.setText(Helper.searchUser.getGender());
        lblUsername.setText(Helper.searchUser.getUsername());
        txtEmail.setText(Helper.searchUser.getEmail());
        txtFullname.setText(Helper.searchUser.getFullname());

        imgSchedule.setOnClickListener(this);
        Toast.makeText(this, Helper.id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.userDetails_imgSchedule:
                Intent scheduleIntent = new Intent(this, DateActivity.class);
                startActivity(scheduleIntent);
                break;
        }
    }
}
