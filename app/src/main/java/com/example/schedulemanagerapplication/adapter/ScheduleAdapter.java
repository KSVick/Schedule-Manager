package com.example.schedulemanagerapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.activity.DateActivity;
import com.example.schedulemanagerapplication.model.Schedule;
import com.example.schedulemanagerapplication.utility.Converter;
import com.example.schedulemanagerapplication.utility.SharedPrefManager;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private DateActivity context;
    private ArrayList<Schedule> schedules;
    private ArrayList<Schedule> master;
    private DatabaseReference databaseReference;
    private SharedPrefManager sharedPrefManager;

    public ScheduleAdapter(DateActivity context){
        this.context = context;
        schedules = new ArrayList<>();
        sharedPrefManager = new SharedPrefManager(context);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+sharedPrefManager.getSPUserKey()+"/Schedules");
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }
    public void setMaster(ArrayList<Schedule> schedules) {
        this.master = schedules;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtDate.setText(schedules.get(position).getDate().toString());
        holder.txtDescription.setText(schedules.get(position).getDescription());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = Converter.findPositionOfSchedule(master, schedules.get(position));
                databaseReference.child(master.get(pos).getId()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                        context.refreshScheduleData();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtDate, txtDescription;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.item_schedule_txtDate);
            txtDescription = itemView.findViewById(R.id.item_schedule_txtDescription);
            btnDelete = itemView.findViewById(R.id.item_schedule_buttonDelete);
        }
    }
}
