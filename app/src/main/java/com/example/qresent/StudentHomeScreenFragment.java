package com.example.qresent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.qresent.databinding.FragmentStudentHomeScreenBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Iterator;

public class StudentHomeScreenFragment extends Fragment {

    FragmentStudentHomeScreenBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_student_home_screen,
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
                String accountType = ((DataSnapshot) it.next()).getValue().toString();
                String email = ((DataSnapshot) it.next()).getValue().toString();
                String faculty = ((DataSnapshot) it.next()).getValue().toString();
                String group = ((DataSnapshot) it.next()).getValue().toString();
                String name = ((DataSnapshot) it.next()).getValue().toString();

                binding.infoTv.setText(accountType + "\nName: " + name + "\nEmail: " + email + "\nFaculty: " + faculty + "\nGroup: " + group);
            }
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show());

        binding.gotocoursesBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_studentHomeScreenFragment_to_coursesFragment));

        binding.gotoScheduleBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_studentHomeScreenFragment_to_calendarFragment);
        });

        binding.gotoscanqrBtn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_studentHomeScreenFragment_to_qrReaderFragment));

        binding.gotologoutBtn.setOnClickListener(v -> {
            firebaseAuth.signOut();
            checkIfUserIsLoggedIn(binding, v);
        });

        return binding.getRoot();
    }

    private void checkIfUserIsLoggedIn(FragmentStudentHomeScreenBinding binding, View v) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            Navigation.findNavController(v).navigate(R.id.action_studentHomeScreenFragment_to_loginFragment);
        } else {

        }
    }
}