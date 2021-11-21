package com.example.qresent;

import android.icu.text.Edits;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.qresent.databinding.FragmentStatisticsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class StatisticsFragment extends Fragment {

    FragmentStatisticsBinding binding;

    private HashMap<String, String> abrevieri = new HashMap<String, String>() {
        {
            put("Integrarea sistemelor informatice", "isi");
            put("Managementul proiectelor software", "mps");
            put("Programare Web", "pw");
            put("Sisteme multiprocesor", "sm");
            put("Proiectarea retelelor", "pr");
            put("Utilizarea bazelor de date", "ubd");
            put("E-commerce", "ecomm");
        }
    };
    private ArrayList<Integer> stats = new ArrayList<Integer>();
    private ArrayList<String> courses = new ArrayList<String>();
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_statistics,
                container,
                false
        );

        binding.statsLL.setOrientation(LinearLayout.VERTICAL);
        RecyclerView scrollStats = binding.scrollStats;
        scrollStats.setLayoutManager(new LinearLayoutManager(this.getContext()));
        initCoursesArray(scrollStats);
        initspinnerfooter();

        return binding.getRoot();
    }

    private void initCoursesArray(RecyclerView scrollStats) {
        database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("CourseQRs");
        myRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    String course = ((DataSnapshot)iterator.next()).getKey().toString();
                    courses.add(course);
                }

                DatabaseReference ref = database.getReference("Users");
                ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("name").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.getValue().toString();

                        DatabaseReference reference = database.getReference("Teachers");
                        reference.child(name).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                ArrayList<String> materii = new ArrayList<>();
                                Iterator it = dataSnapshot.getChildren().iterator();
                                while (it.hasNext()) {
                                    String course = ((DataSnapshot)it.next()).getKey().toString();
                                    materii.add(course);
                                }

                                Log.i("coursessize", String.valueOf(courses.size()));

                                ArrayList<String> copie = (ArrayList<String>) courses.clone();

                                for (String course: copie) {
                                    int bec = 0;
                                    for (String materie: materii) {
                                        Log.i("ceapamasii", course + " " + abrevieri.get(materie) + "Size" + String.valueOf(courses.size()));
                                        if (course.contains(abrevieri.get(materie)) == true) {
                                            bec = 1;
                                        }
                                    }
                                    if (bec == 0) {
                                        courses.remove(course);
                                    }
                                }

                                for(String course: courses) {
                                    stats.add(10);
                                }

                                RecyclerViewStatistics adapter = new RecyclerViewStatistics(stats, courses, getActivity());
                                scrollStats.setAdapter(adapter);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initspinnerfooter() {

        ArrayList<String> courseList = new ArrayList<>();

        database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("Attendance");
        myRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Iterator it = dataSnapshot.getChildren().iterator();
                while (it.hasNext()) {
                    String course = ((DataSnapshot)it.next()).getKey().toString();
                    courseList.add(course);
                }

                String[] courses = new String[courseList.size()];
                courseList.toArray(courses);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, courses);
                binding.attendanceSpinner.setAdapter(adapter);
                binding.attendanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getActivity(), (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}