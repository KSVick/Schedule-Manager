package com.example.schedulemanagerapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.model.User;
import com.example.schedulemanagerapplication.utility.Helper;
import com.example.schedulemanagerapplication.utility.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView lblUsername,lblGender;
    private EditText txtEmail,txtFullname;
    DatabaseReference databaseReference;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        lblGender = findViewById(R.id.profile_lblGender);
        lblUsername = findViewById(R.id.profile_lblUsername);
        txtEmail = findViewById(R.id.profile_txtEmail);
        txtFullname = findViewById(R.id.profile_txtFullname);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        lblGender.setText(Helper.user.getGender());
        lblUsername.setText(Helper.user.getUsername());
        txtEmail.setText(Helper.user.getEmail());
        txtFullname.setText(Helper.user.getFullname());

        final Button btnSave = findViewById(R.id.profile_btnSave);

        btnSave.setOnClickListener(this);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
//        Ini Id Usernya : sharedPrefManager.getSPUserKey()
//        Toast.makeText(this, sharedPrefManager.getSPUserKey(), Toast.LENGTH_SHORT).show();
        id = sharedPrefManager.getSPUserKey();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.profile_btnSave:
//                databaseReference.child(id).child("fullname").setValue(txtFullname.getText());
                Intent homeIntent = new Intent(this, HomeActivity.class);
                startActivity(homeIntent);
                break;
        }
    }
}
