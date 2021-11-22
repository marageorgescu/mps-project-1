package com.example.qresent.schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qresent.R;
import com.example.qresent.databinding.FragmentScheduleCoursesBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class ScheduleCoursesFragment extends Fragment {

    //    private final ArrayList<String> courses = new ArrayList<String>(Arrays.asList("Managementul proiectelor software", "Sisteme multiprocesor", "Programare Web",
//            "Proiectarea retellor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Managementul proiectelor software",
//            "Managementul proiectelor software", "Managementul proiectelor software", "Managementul proiectelor software"));
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> courses;
    private ArrayList<Integer> timings;
    private FragmentScheduleCoursesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_schedule_courses,
                container,
                false
        );

        courses = (ArrayList<String>) getArguments().getSerializable("courses");
        timings = (ArrayList<Integer>) getArguments().getSerializable("timings");

        firebaseAuth = FirebaseAuth.getInstance();
        binding.coursesLL.setOrientation(LinearLayout.VERTICAL);
        RecyclerView scrollCourses = binding.scrollCourses;
        scrollCourses.setLayoutManager(new LinearLayoutManager(this.getContext()));
        ScheduleCoursesAdapter adapter = new ScheduleCoursesAdapter(courses, timings, this.getContext());
        scrollCourses.setAdapter(adapter);

        return binding.getRoot();
    }
}