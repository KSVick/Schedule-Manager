package com.example.schedulemanagerapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.fragment.ManageAppointmentFragment;
import com.example.schedulemanagerapplication.fragment.TodayAgendaFragment;
import com.example.schedulemanagerapplication.utility.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TodayAgendaFragment.OnFragmentInteractionListener, ManageAppointmentFragment.OnFragmentInteractionListener{
    private TextView lblFullname,lblEmail;
    DatabaseReference databaseReference;

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_layout_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        replaceFragment(new TodayAgendaFragment());


        // KO KE ini ak udah pusing banget
        // entah kenapa si nav header homenya gk bisa ke panggil
        // Kalau setContentView(R.layout.nav_header_home); di uncomment di si navigation drawernya ketimpa
//        setContentView(R.layout.nav_header_home);
//        lblFullname = findViewById(R.id.activity_home_lblFullname);
//        lblEmail = findViewById(R.id.activity_home_lblEmail);
////
//        lblEmail.setText(Helper.user.getEmail());
//        lblFullname.setText(Helper.user.getFullname());

//        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
////
////        Ini Id Usernya : sharedPrefManager.getSPUserKey()
//        Toast.makeText(this, sharedPrefManager.getSPUserKey(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_logout){
            SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
            sharedPrefManager.clearSP();
            Toast.makeText(this,"Log out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            replaceFragment(new TodayAgendaFragment());
        } else if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(this,ProfileActivity.class);
            startActivity(profileIntent);
        }   else if (id == R.id.nav_search) {
            Intent searchUserIntent = new Intent(this,SearchUserActivity.class);
            startActivity(searchUserIntent );
        } else if (id == R.id.nav_news) {
            Intent newsIntent = new Intent(this,NewsActivity.class);
            startActivity(newsIntent );
        } else if (id == R.id.nav_manage_appointment) {
            replaceFragment(new ManageAppointmentFragment());
        } else if (id == R.id.nav_manage_schedule) {
            Intent intent = new Intent(this, DateActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
