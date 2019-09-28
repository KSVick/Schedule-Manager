package com.example.schedulemanagerapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemanagerapplication.R;
import com.example.schedulemanagerapplication.fragment.ManageAppointmentFragment;
import com.example.schedulemanagerapplication.model.Appointment;
import com.example.schedulemanagerapplication.model.Schedule;
import com.example.schedulemanagerapplication.utility.Converter;
import com.example.schedulemanagerapplication.utility.SharedPrefManager;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter
        .ViewHolder>{
    private ManageAppointmentFragment manageAppointmentFragment;
    private ArrayList<Appointment> appointments;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceSchedule;
    private SharedPrefManager sharedPrefManager;

    public AppointmentAdapter(ManageAppointmentFragment manageAppointmentFragment){
        this.manageAppointmentFragment = manageAppointmentFragment;
        sharedPrefManager = new SharedPrefManager(manageAppointmentFragment.getContext());
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+sharedPrefManager.getSPUserKey()+"/Appointments");
        databaseReferenceSchedule = FirebaseDatabase.getInstance().getReference("Users/"+sharedPrefManager.getSPUserKey()+"/Schedules");
        appointments = new ArrayList<>();
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(manageAppointmentFragment.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtDate.setText(appointments.get(position).getDate().toString());
        holder.txtDescription.setText(appointments.get(position).getDescription());
        holder.txtUser.setText(appointments.get(position).getFromUser().getFullname());

        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Appointment appointment = appointments.get(position);
                databaseReference.child(appointment.getId()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        String scheduleId = databaseReferenceSchedule.push().getKey();
                        Schedule schedule = new Schedule(appointment.getDescription(), appointment.getDate(), scheduleId);
                        schedule.setCollaborator(appointment.getFromUser());
                        schedule.setLocation(appointment.getLocation());
                        databaseReferenceSchedule.child(scheduleId).setValue(schedule);

                        Toast.makeText(manageAppointmentFragment.getContext(), "Accepted", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Appointment appointment = appointments.get(position);
                databaseReference.child(appointment.getId()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        Toast.makeText(manageAppointmentFragment.getContext(), "Rejected", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        if(appointments.get(position).getLocation() != null)
            holder.txtLocation.setText(appointments.get(position).getLocation());

    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }


    protected class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtDate, txtDescription, txtUser, txtLocation;
        Button btnAccept, btnReject;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDate = itemView.findViewById(R.id.item_appointment_txtdate);
            txtDescription = itemView.findViewById(R.id.item_appointment_txtdescription);
            txtUser = itemView.findViewById(R.id.item_appointment_txtname);
            btnAccept = itemView.findViewById(R.id.item_appointment_btnAccept);
            btnReject = itemView.findViewById(R.id.item_appointment_btnReject);
            txtLocation = itemView.findViewById(R.id.item_appointment_txtLocation);
        }
    }
}
