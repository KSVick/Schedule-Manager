package com.example.schedulemanagerapplication.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schedulemanagerapplication.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public TextView lblUsername,lblGender;
    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        lblUsername = (TextView) itemView.findViewById(R.id.userdetail_lblUsername);
        lblGender = (TextView) itemView.findViewById(R.id.userdetail_lblGender);

    }

}
