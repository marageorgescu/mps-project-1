package com.example.qresent;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.qresent.databinding.FragmentCoursesBinding;
import com.example.qresent.databinding.FragmentLoginBinding;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class CoursesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private FragmentCoursesBinding binding;
        private ArrayList<String> courses = new ArrayList<String>(Arrays.asList("Managementul proiectelor software", "Sisteme multiprocesor", "Programare Web",
                "Proiectarea retellor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Managementul proiectelor software",
                "Managementul proiectelor software", "Managementul proiectelor software", "Managementul proiectelor software"));

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            binding = DataBindingUtil.inflate(
                    inflater,
                    R.layout.fragment_courses,
                container,
                false
        );

            Log.i("tagda", "dadadadad");

        binding.coursesLL.setOrientation(LinearLayout.VERTICAL);
        RecyclerView scrollCourses = binding.scrollCourses;
        scrollCourses.setLayoutManager(new LinearLayoutManager(this.getContext()));
        scrollCourses.setAdapter(new RecyclerAdapter(courses, this.getContext()));



        return binding.getRoot();
    }
}