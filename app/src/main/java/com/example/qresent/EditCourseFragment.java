package com.example.qresent;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.qresent.databinding.FragmentEditCourseBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EditCourseFragment extends Fragment {

    FragmentEditCourseBinding binding;
    TextView courseName;
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_edit_course,
                container,
                false
        );


        database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
        courseName = binding.CourseNameTV;

        String title = getArguments().getString("courseTitle");
        courseName.setText(title);
        UpdateDescriptionFromDB(title);

        binding.saveDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendNewDescriptionToDB();
            }
        });
        return binding.getRoot();
    }

    void UpdateDescriptionFromDB(String title)
    {
        DatabaseReference myRef = database.getReference("Courses");
        myRef.child(title).child("Description").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null)
                    binding.descriptionET.setText(dataSnapshot.getValue().toString());
                else
                    binding.descriptionET.setText("No description added");
            }
        });
    }

    void SendNewDescriptionToDB()
    {
        DatabaseReference myRef = database.getReference("Courses");
        myRef.child(courseName.getText().toString()).child("Description").setValue(binding.descriptionET.getText().toString());
    }

}