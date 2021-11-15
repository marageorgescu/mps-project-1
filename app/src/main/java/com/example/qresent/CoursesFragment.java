package com.example.qresent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qresent.databinding.FragmentCoursesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;


public class CoursesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FragmentCoursesBinding binding;
    private final ArrayList<String> courses = new ArrayList<String>(Arrays.asList("Managementul proiectelor software", "Sisteme multiprocesor", "Programare Web",
            "Proiectarea retellor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Sisteme multiprocesor", "Managementul proiectelor software",
            "Managementul proiectelor software", "Managementul proiectelor software", "Managementul proiectelor software"));
    FirebaseAuth firebaseAuth;

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

            firebaseAuth = FirebaseAuth.getInstance();
            binding.coursesLL.setOrientation(LinearLayout.VERTICAL);
            RecyclerView scrollCourses = binding.scrollCourses;
            scrollCourses.setLayoutManager(new LinearLayoutManager(this.getContext()));
            RecyclerAdapter adapter = new RecyclerAdapter(courses, this.getContext());
            scrollCourses.setAdapter(adapter);


            binding.logoutBtn.setOnClickListener(v -> {
                firebaseAuth.signOut();
                checkIfUserIsLoggedIn(binding, v);
            });

            return binding.getRoot();
        }

    private void checkIfUserIsLoggedIn(FragmentCoursesBinding binding, View v) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Navigation.findNavController(v).navigate(R.id.action_coursesFragment_to_loginFragment);
        } else {

        }
    }
}