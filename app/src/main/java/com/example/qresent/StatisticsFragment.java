package com.example.qresent;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.qresent.databinding.FragmentStatisticsBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class StatisticsFragment extends Fragment {

    FragmentStatisticsBinding binding;

    private ArrayList<Integer> stats = new ArrayList<Integer>(Arrays.asList(10,23,16,17,25));
    private ArrayList<String> courses = new ArrayList<String>(Arrays.asList("sm curs 1", "sm curs 2", "pweb curs 3", "app curs 2", "mp curs 4"));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_statistics,
                container,
                false
        );

        binding.statsLL.setOrientation(LinearLayout.VERTICAL);
        RecyclerView scrollStats = binding.scrollStats;
        scrollStats.setLayoutManager(new LinearLayoutManager(this.getContext()));
        RecyclerViewStatistics adapter = new RecyclerViewStatistics(stats, courses, this.getContext());
        scrollStats.setAdapter(adapter);


        return binding.getRoot();
    }
}