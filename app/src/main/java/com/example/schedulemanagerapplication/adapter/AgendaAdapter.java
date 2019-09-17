package com.example.schedulemanagerapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.model.Schedule;

import java.util.ArrayList;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Schedule> schedules;

    public AgendaAdapter(Context context){
        this.context = context;
        schedules = new ArrayList<>();
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_agenda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtDate.setText(schedules.get(position).getDate().toString());
        holder.txtDescription.setText(schedules.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtDate, txtDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.item_agenda_txtDate);
            txtDescription = itemView.findViewById(R.id.item_agenda_txtDescription);
        }
    }

}
