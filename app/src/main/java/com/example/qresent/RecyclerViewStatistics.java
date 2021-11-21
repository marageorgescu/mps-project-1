package com.example.qresent;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewStatistics extends RecyclerView.Adapter<RecyclerViewStatistics.ViewHolder> {

    ArrayList<Integer> statsList;
    ArrayList<String> coursesList;
    AdapterView.OnItemClickListener onItemClickListener;
    public RecyclerViewStatistics(ArrayList<Integer> statsList, ArrayList<String> coursesList, Context context) {
        this.statsList = statsList;
        this.coursesList = coursesList;
        Log.i("recycler view stats", coursesList.get(0));
    }

    @Override
    public RecyclerViewStatistics.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_row, parent,false);
        RecyclerViewStatistics.ViewHolder viewHolder = new RecyclerViewStatistics.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewStatistics.ViewHolder holder, int position) {
        holder.text.setText(coursesList.get(position).toString());
        int presentPercent = (int) ((((double)statsList.get(position) / 25.0)) * 100);
        int absentPercent = 100 - presentPercent;

        Log.i("Procentele", "PROC" + presentPercent);
        LinearLayout.LayoutParams presentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                absentPercent
        );
        holder.presenceLayout.setLayoutParams(presentParams);

        LinearLayout.LayoutParams absentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                presentPercent
        );
        holder.absenceLayout.setLayoutParams(absentParams);
        holder.textPresence.setText(statsList.get(position).toString() + "/25");

    }

    @Override
    public int getItemCount() {
        return statsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView text;
        protected TextView textPresence;
        protected View presenceLayout;
        protected View absenceLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text_id);
            presenceLayout = (View) itemView.findViewById(R.id.presentStudents);
            absenceLayout = (View) itemView.findViewById(R.id.absentStudents);
            textPresence = (TextView) itemView.findViewById(R.id.textPresence);

        }
    }
}
