package com.example.qresent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<String> coursesList;

    public RecyclerAdapter(ArrayList<String> coursesList, Context context) {
        this.coursesList = coursesList;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_row, parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        holder.text.setText(coursesList.get(position).toString());
        holder.button.setOnClickListener((view) -> Log.i("button clk", holder.text.getText().toString()));
    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView text;
        protected Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text_id);
            button = (Button) itemView.findViewById(R.id.row_button_id);
        }
    }
}