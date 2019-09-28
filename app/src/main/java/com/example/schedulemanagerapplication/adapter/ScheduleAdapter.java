package com.example.schedulemanagerapplication.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private String m_Text = "";

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
        if(schedules.get(position).getLocation() != null)
            holder.txtLocation.setText(schedules.get(position).getLocation());
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
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Edit Schedule");

// Set up the input
                final EditText input = new EditText(context);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT );
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();

                        int pos = Converter.findPositionOfSchedule(master, schedules.get(position));
                        Schedule newSchedule =master.get(pos);
                        newSchedule.setDescription(m_Text);
                        databaseReference.child(master.get(pos).getId()).setValue(newSchedule);
                        Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtDate, txtDescription, txtLocation;
        Button btnDelete, btnUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.item_schedule_txtDate);
            txtDescription = itemView.findViewById(R.id.item_schedule_txtDescription);
            btnDelete = itemView.findViewById(R.id.item_schedule_buttonDelete);
            txtLocation = itemView.findViewById(R.id.item_schedule_txtLocation);
            btnUpdate = itemView.findViewById(R.id.item_schedule_buttonUpdate);
        }
    }
}
