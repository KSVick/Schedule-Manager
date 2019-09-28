package com.example.schedulemanagerapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.model.User;
import com.example.schedulemanagerapplication.utility.Helper;
import com.example.schedulemanagerapplication.utility.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    Context currentContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentContext = this;
        ProgressBar spinner =(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);


//                        Intent intent = new Intent(currentContext, AddApointmentActivity.class);



//        CEK UDAH LOGIN ATAU BELUM
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        if(sharedPrefManager.getSPUserKey().equals("")){
            Intent intent = new Intent(currentContext, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            databaseReference.child(sharedPrefManager.getSPUserKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);
                    if(!dataSnapshot.exists() || user == null ){
                        Intent intent = new Intent(currentContext, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        Helper.user = user;
                        Toast.makeText(currentContext,"Welcome, "+user.getFullname(),Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(currentContext, HomeActivity.class);

//                        Intent intent = new Intent(currentContext, AddApointmentActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Intent intent = new Intent(currentContext, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }


    }
}
