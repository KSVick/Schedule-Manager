package edu.bluejack18_2.schedulemanagerapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.schedulemanagerapplication.R;
import edu.bluejack18_2.schedulemanagerapplication.adapter.ViewPagerAdapter;

public class NewsActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        viewPager = (ViewPager) findViewById(R.id.news_viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);
    }
}
