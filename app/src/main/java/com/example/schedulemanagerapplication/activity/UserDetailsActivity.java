package com.example.schedulemanagerapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.utility.Helper;
import com.example.schedulemanagerapplication.utility.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView lblUsername,lblGender,lblFollower,lblFollowing;
    private EditText txtEmail,txtFullname;
    private ImageView imgSchedule;
    private Button addAppointmentButton,btnFollow;
    DatabaseReference databaseReference;
    SharedPrefManager sharedPrefManager;
    Context contextInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        lblGender = findViewById(R.id.userDetails_lblGender);
        lblUsername = findViewById(R.id.userDetails_lblUsername);
        lblFollowing = findViewById(R.id.userDetails_lblFollowing);
        lblFollower = findViewById(R.id.userDetails_lblFollowers);
        txtEmail = findViewById(R.id.userDetails_txtEmail);
        txtFullname = findViewById(R.id.userDetails_txtFullname);
        imgSchedule = findViewById(R.id.userDetails_imgSchedule);
        addAppointmentButton = findViewById(R.id.userDetails_btnAddAppointment);
        btnFollow = findViewById(R.id.userDetails_btnFollow);

        lblGender.setText(Helper.searchUser.getGender());
        lblUsername.setText(Helper.searchUser.getUsername());
        txtEmail.setText(Helper.searchUser.getEmail());
        txtFullname.setText(Helper.searchUser.getFullname());

        imgSchedule.setOnClickListener(this);
        addAppointmentButton.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        sharedPrefManager = new SharedPrefManager(this);

//        Toast.makeText(this, Helper.id, Toast.LENGTH_SHORT).show();
        countFollowing();
        countFollowers();
        checkfollow();
    }

    public void checkfollow(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.child(sharedPrefManager.getSPUserKey()).child("Following").child(Helper.id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    btnFollow.setText("Follow");
                }
                else{
                    btnFollow.setText("Unfollow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void countFollowing(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.child(Helper.id).child("Following");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.d("CREATION",dataSnapshot.getChildrenCount()+"");
                lblFollowing.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void countFollowers(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.child(Helper.id).child("Followers");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lblFollower.setText(dataSnapshot.getChildrenCount()+"");
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
            case R.id.userDetails_btnAddAppointment:
                Intent appointment = new Intent(this, AddApointmentActivity.class);
                startActivity(appointment);
                break;
            case R.id.userDetails_btnFollow:
                if(btnFollow.getText().equals("Follow")){
                    btnFollow.setText("Unfollow");
                    btnFollow.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.unfollow));
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+Helper.id+"/Followers/"+sharedPrefManager.getSPUserKey());
                    databaseReference.setValue(sharedPrefManager.getSPUserKey());
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+sharedPrefManager.getSPUserKey()+"/Following/"+Helper.id);
                    databaseReference.setValue(Helper.id);
                    countFollowers();
                    countFollowing();
                }
                else{
                    btnFollow.setText("Follow");
                    btnFollow.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.following));
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+Helper.id+"/Followers");
                    databaseReference.child(sharedPrefManager.getSPUserKey()).removeValue();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+sharedPrefManager.getSPUserKey()+"/Following");
                    databaseReference.child(Helper.id).removeValue();
                    countFollowers();
                    countFollowing();
                }
                break;
        }
    }
}
