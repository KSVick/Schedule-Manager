package edu.bluejack18_2.schedulemanagerapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulemanagerapplication.R;
import edu.bluejack18_2.schedulemanagerapplication.model.User;
import edu.bluejack18_2.schedulemanagerapplication.utility.Helper;
import edu.bluejack18_2.schedulemanagerapplication.utility.SharedPrefManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Semaphore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    private TextView lblRegister,lblError;
    private EditText txtUsername,txtPassword;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Context current;
    DatabaseReference databaseReference;
    final Semaphore semaphore = new Semaphore(0);

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("2666118073419091");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        current = this;

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        lblRegister = findViewById(R.id.lblRegister);
        lblError = findViewById(R.id.lblError);
        final Button btnLogin = findViewById(R.id.btnLogin);
        final com.google.android.gms.common.SignInButton btnGoogle = findViewById(R.id.google_button);
        LoginButton facebookLoginButton = findViewById(R.id.loginFacebookButton);
        mCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setReadPermissions("email", "public_profile");

        facebookLoginButton.registerCallback(mCallbackManager,  new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
//                Toast.makeText(current, "test test", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(current, "test test1", Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(current, error.toString(), Toast.LENGTH_SHORT).show();
                // ...
            }

        });


        lblRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());


        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "signInWithCredential:success : "+user.getEmail());
                            updateCurrentUser(user);
                            updateUI(user);
                            signOut();
                            LoginManager.getInstance().logOut();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(current, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            signOut();
                            LoginManager.getInstance().logOut();



                        }

                        // ...
                    }
                });
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
//        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(current,"Sign "+user.getEmail(),Toast.LENGTH_LONG).show();

                            updateUI(user);
                            updateCurrentUser(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(current,"Failed"+task.getException().toString(),Toast.LENGTH_LONG).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });

    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

    }

    private void updateCurrentUser(FirebaseUser user) {
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    lblError.setText("Email doesn't exist!");
                } else {
                    User user = null;
                    String key = "";
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        user = userSnapshot.getValue(User.class);
                        key = userSnapshot.getKey();
                    }
                    if (user != null) {
                        Helper.user = user;
                        SharedPrefManager sharedPrefManager = new SharedPrefManager(current);
                        sharedPrefManager.saveSPString(SharedPrefManager.SP_USER_KEY, key);
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

    public void changeActivity(){
        Intent homeIntent = new Intent(this,HomeActivity.class);
        startActivity(homeIntent);
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
                    Query query = databaseReference.orderByChild("username").equalTo(username);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                lblError.setText("Username doesn't exist!");
                            }
                            else{
                                User user = new User();
                                String key = "";
                                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                                    user = userSnapshot.getValue(User.class);
                                    key = userSnapshot.getKey();
                                }
                                Helper.user = user;
                                if(!password.equals(Helper.user.getPassword())){
                                    lblError.setText("Invalid Username / Password!");
                                }
                                else{
                                    SharedPrefManager sharedPrefManager = new SharedPrefManager(current);
                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_USER_KEY, key);
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
            case R.id.lblRegister:
                Intent registerIntent = new Intent(this,RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.google_button:
                signIn();
                break;

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        
    }
}
