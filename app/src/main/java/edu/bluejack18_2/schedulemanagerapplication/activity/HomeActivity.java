package edu.bluejack18_2.schedulemanagerapplication.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.schedulemanagerapplication.R;
import edu.bluejack18_2.schedulemanagerapplication.fragment.ManageAppointmentFragment;
import edu.bluejack18_2.schedulemanagerapplication.fragment.TodayAgendaFragment;
import edu.bluejack18_2.schedulemanagerapplication.utility.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TodayAgendaFragment.OnFragmentInteractionListener, ManageAppointmentFragment.OnFragmentInteractionListener {
    private SharedPrefManager sharedPrefManager;
    private boolean notFirstFetchFollow = false;

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_layout_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addNotification(String title, String content, Class context) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "A")
                        .setSmallIcon(R.drawable.schedule_manager_logo)
                        .setContentTitle(title)
                        .setContentText(content);

        Intent notificationIntent = new Intent(this, context);
        if(context == HomeActivity.class){
            notificationIntent.putExtra("menuFragment", "ManageAppointment");
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    |Intent.FLAG_ACTIVITY_CLEAR_TASK);

        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void refreshScheduleData(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+sharedPrefManager.getSPUserKey()+"/Appointments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    addNotification("New Appointment", "You has gain a new appointment", HomeActivity.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void countFollowers(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReference.child(sharedPrefManager.getSPUserKey()).child("Followers");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(notFirstFetchFollow && dataSnapshot.exists()){
                    addNotification("New Follower", "You has gain a new follower", ProfileActivity.class);
                }else{
                    notFirstFetchFollow = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
        sharedPrefManager = new SharedPrefManager(this);
        String menuExtra = getIntent().getStringExtra("menuFragment");
        if(menuExtra != null && menuExtra.equals("ManageAppointment")){
            replaceFragment(new ManageAppointmentFragment());
        }else {
            replaceFragment(new TodayAgendaFragment());
        }

        refreshScheduleData();
        countFollowers();

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
            Intent newsIntent = new Intent(this,NewsAPIActivity.class);
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
