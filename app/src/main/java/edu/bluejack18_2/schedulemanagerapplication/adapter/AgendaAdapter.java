package edu.bluejack18_2.schedulemanagerapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemanagerapplication.R;
import edu.bluejack18_2.schedulemanagerapplication.model.Schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Schedule> schedules;
    SimpleDateFormat actionDateFormat = new SimpleDateFormat("dd MMMM yyyy");

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
        holder.txtDate.setText(actionDateFormat.format(schedules.get(position).getDate()));
        holder.txtDescription.setText(schedules.get(position).getDescription());
        if(schedules.get(position).getCollaborator() != null){
            holder.txtCollaborator.setText(schedules.get(position).getCollaborator().getFullname());
        }
        if(schedules.get(position).getLocation() != null)
         holder.txtLocation.setText(schedules.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtDate, txtDescription, txtCollaborator, txtLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.item_agenda_txtDate);
            txtDescription = itemView.findViewById(R.id.item_agenda_txtDescription);
            txtCollaborator = itemView.findViewById(R.id.item_agenda_txtCollaborator);
            txtLocation = itemView.findViewById(R.id.item_agenda_txtLocation);
        }
    }

}
