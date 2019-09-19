package com.example.schedulemanagerapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.utility.Helper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView lblUsername,lblGender,lblFollowers,lblFollowing;
    private EditText txtEmail,txtFullname;
    private ImageView imgSchedule;
    Button btnFollow,btnAddApointment;
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
        btnFollow = findViewById(R.id.userDetails_btnFollow);
        btnAddApointment = findViewById(R.id.userDetails_btnAddAppointment);
        lblFollowing = findViewById(R.id.userDetails_lblFollowing);
        lblFollowers = findViewById(R.id.userDetails_lblFollowers);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        lblGender.setText(Helper.searchUser.getGender());
        lblUsername.setText(Helper.searchUser.getUsername());
        txtEmail.setText(Helper.searchUser.getEmail());
        txtFullname.setText(Helper.searchUser.getFullname());
        imgSchedule.setOnClickListener(this);

        followCount();
    }

    public void followCount(){
        Query query = databaseReference.orderByChild("Schedules");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("CREATION",dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.userDetails_imgSchedule:
                Intent scheduleIntent = new Intent(this, DateActivity.class);
                startActivity(scheduleIntent);
                break;
            case R.id.userDetails_btnFollow:

                break;
        }
    }
}
