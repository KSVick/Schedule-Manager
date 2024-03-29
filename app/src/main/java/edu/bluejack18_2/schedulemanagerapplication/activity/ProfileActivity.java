package edu.bluejack18_2.schedulemanagerapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.schedulemanagerapplication.R;

import edu.bluejack18_2.schedulemanagerapplication.utility.Helper;
import edu.bluejack18_2.schedulemanagerapplication.utility.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView lblUsername,lblGender,lblError,lblFollower,lblFollowing;
    private EditText txtEmail,txtFullname;
    private ImageView imgSchedule;
    DatabaseReference databaseReference;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        lblGender = findViewById(R.id.profile_lblGender);
        lblUsername = findViewById(R.id.profile_lblUsername);
        lblFollowing =  findViewById(R.id.profile_lblFollowing);
        lblFollower =  findViewById(R.id.profile_lblFollowers);
        txtEmail = findViewById(R.id.profile_txtEmail);
        txtFullname = findViewById(R.id.profile_txtFullname);
        lblError = findViewById(R.id.profile_lblError);
        imgSchedule = findViewById(R.id.profile_imgSchedule);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        lblGender.setText(Helper.user.getGender());
        lblUsername.setText(Helper.user.getUsername());
        txtEmail.setText(Helper.user.getEmail());
        txtFullname.setText(Helper.user.getFullname());

        final Button btnSave = findViewById(R.id.profile_btnSave);

        btnSave.setOnClickListener(this);
        imgSchedule.setOnClickListener(this);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
//        Ini Id Usernya : sharedPrefManager.getSPUserKey()
//        Toast.makeText(this, sharedPrefManager.getSPUserKey(), Toast.LENGTH_SHORT).show();
        id = sharedPrefManager.getSPUserKey();
        countFollowers();
        countFollowing();
    }

    public void countFollowing(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.child(id).child("Following");
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
        Query query = databaseReference.child(id).child("Followers");
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
            case R.id.profile_btnSave:
                String fullname = txtFullname.getText().toString();

                if(fullname.length() < 5 || fullname.length() > 30){
                    txtFullname.setError("Fullname must be between 5 and 30 characters");
                    txtFullname.requestFocus();
                    lblError.setText("Fullname must be between 5 and 30 characters");
                }
                else {
                    databaseReference.child(id).child("fullname").setValue(fullname);
                    Helper.user.setFullname(fullname);
                    Intent homeIntent = new Intent(this, HomeActivity.class);
                    startActivity(homeIntent);
                    break;
                }
            case R.id.profile_imgSchedule:
                Intent scheduleIntent = new Intent(this, DateActivity.class);
                startActivity(scheduleIntent);
                break;
        }
    }
}
