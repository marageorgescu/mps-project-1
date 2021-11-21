package com.example.qresent;

import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Toast;


import com.example.qresent.databinding.FragmentViewCoursesBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;


public class ViewCoursesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FragmentViewCoursesBinding binding;
    private ArrayList<String> courses = new ArrayList<String>(Arrays.asList("Integrarea sistemelor informatice",
            "Managementul proiectelor software", "Programare Web", "Sisteme multiprocesor", "Proiectarea retelelor",
            "Utilizarea bazelor de date", "E-commerce"));
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

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

        database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        binding.coursesLL.setOrientation(LinearLayout.VERTICAL);
        RecyclerView scrollCourses = binding.scrollCourses;
        scrollCourses.setLayoutManager(new LinearLayoutManager(this.getContext()));
        RecyclerAdapterViewCourse adapter = new RecyclerAdapterViewCourse(courses, this.getContext());
        scrollCourses.setAdapter(adapter);

        DatabaseReference myRef = database.getReference("Users");
        myRef.child(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Iterator it = dataSnapshot.getChildren().iterator();
                String accountType = ((DataSnapshot)it.next()).getValue().toString();
                String email = ((DataSnapshot)it.next()).getValue().toString();
                String faculty = ((DataSnapshot)it.next()).getValue().toString();
                String group = ((DataSnapshot)it.next()).getValue().toString();
                String name = ((DataSnapshot)it.next()).getValue().toString();
                String uid = ((DataSnapshot)it.next()).getValue().toString();
                if (accountType.equals("Student")) {
                    DatabaseReference studentsRef = database.getReference("Students");
                    studentsRef.child(name).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            Iterator iterator = dataSnapshot.getChildren().iterator();
                            ArrayList<String> coursesList = new ArrayList<>();
                            while(iterator.hasNext()) {
                                String course = ((DataSnapshot)iterator.next()).getKey().toString();
                                coursesList.add(course);
                            }
                            RecyclerAdapter adapter = new RecyclerAdapter(coursesList, getActivity());
                            scrollCourses.setAdapter(adapter);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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