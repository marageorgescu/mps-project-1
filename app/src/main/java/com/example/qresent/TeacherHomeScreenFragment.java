package com.example.qresent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.qresent.databinding.FragmentStudentHomeScreenBinding;
import com.example.qresent.databinding.FragmentTeacherHomeScreenBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;

public class TeacherHomeScreenFragment extends Fragment {

    FragmentTeacherHomeScreenBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_teacher_home_screen,
                container,
                false
        );

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
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

                binding.infoTv.setText(accountType + "\nName: " + name + "\nEmail: " + email + "\nFaculty: " + faculty);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.gotocoursesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_teacherHomeScreenFragment_to_coursesFragment);
            }
        });

        binding.gotoScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_teacherHomeScreenFragment_to_calendarFragment);
            }
        });

        binding.gotogenerateqrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_teacherHomeScreenFragment_to_qrGeneratorFragment);
            }
        });

        binding.gotostatisticsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_teacherHomeScreenFragment_to_statisticsFragment);
            }
        });



        binding.gotologoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                checkIfUserIsLoggedIn(binding, v);
            }
        });

        return binding.getRoot();
    }

    private void checkIfUserIsLoggedIn(FragmentTeacherHomeScreenBinding binding, View v) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Navigation.findNavController(v).navigate(R.id.action_teacherHomeScreenFragment_to_loginFragment);
        } else {

        }
    }
}