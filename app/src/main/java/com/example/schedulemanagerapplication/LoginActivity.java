package com.example.schedulemanagerapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView lblRegister,lblError;
    private EditText txtUsername,txtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        lblRegister = findViewById(R.id.lblRegister);
        lblError = findViewById(R.id.lblError);
        final Button btnLogin = findViewById(R.id.btnLogin);

        lblRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                final String username = txtUsername.getText().toString();
                final String password = txtPassword.getText().toString();

                if(username.equals("")){
                    lblError.setText("Username must be filled");
                    txtUsername.setError("Username must be filled");
                    txtUsername.requestFocus();
                }
                else if(password.equals("")){
                    lblError.setText("Password must be filled");
                    txtPassword.setError("Password must be filled");
                    txtPassword.requestFocus();
                }
                else{
                    lblError.setTextColor(Color.GREEN);
                    lblError.setText("Login Success");
                }
                break;
            case R.id.lblRegister:
                Intent registerIntent = new Intent(this,RegisterActivity.class);
                startActivity(registerIntent);
                break;
        }
    }
}
