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

    ArrayList<String> coursesList;

    public ScheduleCoursesAdapter(ArrayList<String> coursesList, Context context) {
        this.coursesList = coursesList;
    }

    @Override
    public ScheduleCoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_courses_row, parent, false);
        return new ScheduleCoursesViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleCoursesViewHolder holder, int position) {
        Log.d("TAG", coursesList.get(position));

        holder.text.setText(coursesList.get(position));
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

}