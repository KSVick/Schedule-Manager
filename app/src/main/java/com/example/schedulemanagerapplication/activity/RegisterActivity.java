package com.example.schedulemanagerapplication.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtUsername,txtPassword,txtConfirmPassword,txtEmail,txtFullname;
    private RadioGroup radioGroupGender;
    private CheckBox checkBoxAgreement;
    private TextView lblError,lblLogin;
    private RadioButton radioMale,radioFemale;
    DatabaseReference databaseReference;

    public boolean validateEmail(String email){

        int countAt = 0;
        int countDot = 0;

        for (int i = 0; i < email.length(); i++) {
            if(email.charAt(i) == '@'){
                countAt++;
            }
            if(email.charAt(i) == '.'){
                countDot++;
            }
        }

        if(email.length() < 15){
            lblError.setText("Email length must more than 14");
            txtEmail.setError("Email length must more than 14");
            txtEmail.requestFocus();
            return true;
        }

        else if(email.indexOf("@") + 1 == email.indexOf(".")){
            lblError.setText("Character ‘@’ must not be next to’.’");
            txtEmail.setError("Character ‘@’ must not be next to’.’");
            txtEmail.requestFocus();
            return true;
        }

        else if(email.startsWith(".") || email.startsWith("@")){
            lblError.setText("Must not starts with ‘@’ and ‘.’");
            txtEmail.setError("Must not starts with ‘@’ and ‘.’");
            txtEmail.requestFocus();
            return true;
        }

        else if(countAt > 1){
//            Log.d("CREATION",email);
//            Log.d("MYINT", "value: " + countAt);
            lblError.setText("Must not contains more than 1 ‘@’");
            txtEmail.setError("Must not contains more than 1 ‘@’");
            txtEmail.requestFocus();
            return true;
        }

        else if(countAt == 0 || countDot == 0){
            lblError.setText("Must be valid email address format");
            txtEmail.setError("Must be valid email address format");
            txtEmail.requestFocus();
            return true;
        }

        else if(!email.endsWith(".com")){
            lblError.setText("Must end with ‘.com’");
            txtEmail.setError("Must end with ‘.com’");
            txtEmail.requestFocus();
            return true;
        }
        else{
            lblError.setText("");
            return false;
        }
    }

    boolean validateName(String fullname, String username){
        if(fullname.length() < 5 || fullname.length() > 30){
            txtFullname.setError("Fullname must be between 5 and 30 characters");
            txtFullname.requestFocus();
            lblError.setText("Fullname must be between 5 and 30 characters");
            return true;
        }
        else if(username.length() < 5 || username.length() > 10){
            txtUsername.setError("Username must be between 5 and 10 characters");
            txtUsername.requestFocus();
            lblError.setText("Username must be between 5 and 10 characters");
            return true;
        }
        else if(username.contains(" ")){
            txtUsername.setError("Username can only be 1 word");
            txtUsername.requestFocus();
            lblError.setText("Username can only be 1 word");
            return true;
        }
        lblError.setText("");
        return false;
    }

    boolean validatePassword(String password,String confirmPassword){
        int number = 0;
        int alphabet = 0;

        for (int i=0; i<password.length(); i++){
            if(Character.isDigit(password.charAt(i)) == true){
                number++;
            }
            if(Character.isLetter(password.charAt(i)) == true){
                alphabet++;
            }
        }

        if(password.length() < 5 || password.length() > 20){
            txtPassword.setError("Password must be between 5 and 10 characters");
            txtPassword.requestFocus();
            lblError.setText("Password must be between 5 and 10 characters");
            return true;
        }

        else if(number == 0){
            txtPassword.setError("Password must contain number");
            txtPassword.requestFocus();
            lblError.setText("Password must contain number");
            return true;
        }

        else if(alphabet == 0){
            txtPassword.setError("Password must contain alphabet");
            txtPassword.requestFocus();
            lblError.setText("Password must contain alphabet");
            return true;
        }

        else if(alphabet + number != password.length()){
            txtPassword.setError("Password must be alphabetic");
            txtPassword.requestFocus();
            lblError.setText("Password must be alphabetic");
            return true;
        }

        else if(password.equals(confirmPassword) == false){
            txtConfirmPassword.setError("Password and confirm password must be same");
            txtConfirmPassword.requestFocus();
            lblError.setText("Password and confirm password must be same");
            return true;
        }
        lblError.setText("");
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        txtEmail = findViewById(R.id.txtEmail);
        txtUsername = findViewById(R.id.txtUsername);
        txtFullname = findViewById(R.id.txtFullname);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        radioGroupGender = findViewById(R.id.radio_group_gender);
        checkBoxAgreement = findViewById(R.id.checkBox);
        lblError = findViewById(R.id.lblError);
        lblLogin = findViewById(R.id.lblLogin);
        final Button btnRegister = findViewById(R.id.btnRegister);
        radioMale = findViewById(R.id.radioMale);
        radioFemale = findViewById(R.id.radioFemale);

        lblLogin.setOnClickListener(this);
        radioGroupGender.setOnClickListener(this);
        checkBoxAgreement.setOnClickListener(this);
        radioMale.setOnClickListener(this);
        radioFemale.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:
                final String email = txtEmail.getText().toString();
                final String username = txtUsername.getText().toString();
                final String fullname = txtFullname.getText().toString();
                final String password = txtPassword.getText().toString();
                final String confirmPassword = txtConfirmPassword.getText().toString();
                final boolean checkGender = radioGroupGender.getCheckedRadioButtonId() != -1;
                final boolean checkAgree = checkBoxAgreement.isChecked();
                String gender = "";

                if(validateEmail(email)){

                }
                else if(validateName(fullname,username)){

                }

                else if(validatePassword(password,confirmPassword)){

                }

                else if(!checkGender){
                    lblError.setText("Gender Must be selected");
                }

                else if(!checkAgree){
                    checkBoxAgreement.setError("Check Agree Terms & Condition");
                    checkBoxAgreement.requestFocus();
                    lblError.setText("Check Agree Terms & Condition");
                }
                else{
                    if(radioMale.isChecked()){
                        gender = "Male";
                    }
                    else{
                        gender = "Female";
                    }

                    final String genderF = gender;

                    String id = databaseReference.push().getKey();

                    User user = new User(email,fullname,username,password,gender);

                    databaseReference.child(id).setValue(user);

                    lblError.setTextColor(Color.GREEN);
                    lblError.setText("Register Success");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }
                    },1500);
                }
                break;
            case R.id.lblLogin:
                Intent loginIntent = new Intent(this,LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
    }
}
