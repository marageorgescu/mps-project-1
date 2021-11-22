package com.example.qresent.schedule;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qresent.R;

import java.util.ArrayList;

public class ScheduleCoursesAdapter extends RecyclerView.Adapter<ScheduleCoursesViewHolder> {

    private final ArrayList<String> coursesList;
    private final ArrayList<Integer> timings;

    public ScheduleCoursesAdapter(ArrayList<String> coursesList, ArrayList<Integer> timings, Context context) {
        this.coursesList = coursesList;
        this.timings = timings;
    }

    @Override
    public ScheduleCoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_courses_row, parent, false);
        return new ScheduleCoursesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleCoursesViewHolder holder, int position) {
        Log.d("TAG", coursesList.get(position));
        if (coursesList.get(position).equals("Liber")) {
            holder.text.setText(coursesList.get(position));
            holder.text.setTextSize(32);
        } else {
            holder.text.setText(coursesList.get(position));
            holder.hour.setText(String.valueOf(timings.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

}