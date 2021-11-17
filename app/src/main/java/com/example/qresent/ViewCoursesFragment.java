package com.example.qresent;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.example.qresent.databinding.FragmentViewCoursesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.ArrayList;


public class ViewCoursesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FragmentViewCoursesBinding binding;
    private ArrayList<String> courses = new ArrayList<String>(Arrays.asList("Managementul proiectelor software", "Sisteme multiprocesor", "Programare Web",
            "Proiectarea retellor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Managementul proiectelor software",
            "Managementul proiectelor software", "Managementul proiectelor software", "Managementul proiectelor software"));
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_view_courses,
                container,
                false
        );

        Log.i("tagda", "dadadadad");

        firebaseAuth = FirebaseAuth.getInstance();
        binding.coursesLL.setOrientation(LinearLayout.VERTICAL);
        RecyclerView scrollCourses = binding.scrollCourses;
        scrollCourses.setLayoutManager(new LinearLayoutManager(this.getContext()));
        RecyclerAdapterViewCourse adapter = new RecyclerAdapterViewCourse(courses, this.getContext());
        scrollCourses.setAdapter(adapter);



        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkIfUserIsLoggedIn(binding, v);
            }
        });

        return binding.getRoot();
    }

    private void checkIfUserIsLoggedIn(FragmentViewCoursesBinding binding, View v) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Navigation.findNavController(v).navigate(R.id.action_viewCoursesFragment_to_loginFragment);
        } else {

        }
    }
}