package com.example.qresent.schedule;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qresent.R;

public class ScheduleCoursesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    protected TextView text;
    protected TextView hour;

    public ScheduleCoursesViewHolder(@NonNull View itemView) {
        super(itemView);
        text = (TextView) itemView.findViewById(R.id.text_id);
        hour = (TextView) itemView.findViewById(R.id.row_hour_id);

    }

    @Override
    public void onClick(View v) {

    }
}
