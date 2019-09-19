package com.example.schedulemanagerapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class SearchUserActivity extends AppCompatActivity implements View.OnClickListener {

    TextView lblError;
    Button btnSave;
    EditText txtUsername;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        lblError = findViewById(R.id.searchUser_lblError);
        btnSave = findViewById(R.id.searchUser_btnSearch);
        txtUsername = findViewById(R.id.searchUser_txtUsername);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        btnSave.setOnClickListener(this);
    }

    public void changeActivity(){
        Intent userDetailsIntent = new Intent(this,UserDetailsActivity.class);
        startActivity(userDetailsIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.searchUser_btnSearch:
                String username = txtUsername.getText().toString();
                if(username.equals("")){
                    lblError.setText("Username Cannot Empty");
                }
                else {
                    Query query = databaseReference.orderByChild("username").equalTo(username);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                lblError.setText("Username doesn't exist!");
                            } else {
                                User user = null;
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    user = userSnapshot.getValue(User.class);
                                }
                                if (user != null) {
                                    Helper.searchUser = user;
                                    changeActivity();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            lblError.setText("Check your internet connection!");
                        }
                    });
                }
                break;
        }
    }
}
