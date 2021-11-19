package com.example.qresent;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qresent.databinding.FragmentAddToCourseBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class AddToCourseFragment extends Fragment {

    FragmentAddToCourseBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_to_course,
                container,
                false
        );

        firebaseAuth = FirebaseAuth.getInstance();

        initspinnerfooter();
        initcoursespinnerfooter();

        binding.addteacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.teacherSpinner.getSelectedItem().toString();
                String course = binding.courseSpinner.getSelectedItem().toString();
                String type = "Teacher";
                if (binding.teacherRadio.isChecked()) {
                    type = "Teacher";
                } else if (binding.laborantRadio.isChecked()) {
                    type = "Lab Assistant";
                }

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference("Teachers");
                myRef.child(name).child(course).setValue(type);
            }
        });

        return binding.getRoot();
    }

    private void initspinnerfooter() {

        ArrayList<String> teacherList = new ArrayList<>();

        database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot children: snapshot.getChildren()) {
                    String str = children.child("accountType").getValue().toString();
                    if (str.equals("Teacher")) {
                        teacherList.add(children.child("name").getValue().toString());
                    }
                }

                String[] teachers = new String[teacherList.size()];
                teacherList.toArray(teachers);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, teachers);
                binding.teacherSpinner.setAdapter(adapter);
                binding.teacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getActivity(), (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initcoursespinnerfooter() {

        ArrayList<String> courseList = new ArrayList<>();

        database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Courses");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot children: snapshot.getChildren()) {
                    courseList.add(children.getKey().toString());
                }

                String[] courses = new String[courseList.size()];
                courseList.toArray(courses);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, courses);
                binding.courseSpinner.setAdapter(adapter);
                binding.courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getActivity(), (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}