package com.example.qresent;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qresent.databinding.FragmentEditCourseBinding;
import com.example.qresent.databinding.FragmentViewCourseBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ViewCourseFragment extends Fragment {

    FragmentViewCourseBinding binding;
    TextView courseName;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_view_course,
                container,
                false
        );


        database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
        courseName = binding.CourseNameTV;

        String title = getArguments().getString("courseTitle");
        courseName.setText(title);
        GetDescriptionFromDB(title);


        return binding.getRoot();
    }

    void GetDescriptionFromDB(String title)
    {
        DatabaseReference myRef = database.getReference("Courses");
        myRef.child(title).child("Description").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null)
                    binding.CourseDescriptionTV.setText(dataSnapshot.getValue().toString());
                else
                    binding.CourseDescriptionTV.setText("No description added");
            }
        });
    }



}