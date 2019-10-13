package edu.bluejack18_2.schedulemanagerapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.schedulemanagerapplication.R;
import edu.bluejack18_2.schedulemanagerapplication.fragment.TodayAgendaFragment;

public class AgendaViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private String [] titles = {"Yesterday","Today"};

    public AgendaViewPagerAdapter(Context context, FragmentManager mgr){
        super(mgr);
        this.context = context;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return(TodayAgendaFragment.newInstance("",""));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_agenda_layout,null);
        TextView title = view.findViewById(R.id.fragment_today_agenda_textView);
        title.setText(titles[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
