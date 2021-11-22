package com.example.qresent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qresent.databinding.FragmentStatisticsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class StatisticsFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_WRITE_STORAGE = 3;
    FragmentStatisticsBinding binding;

    private final HashMap<String, String> abrevieri = new HashMap<String, String>() {
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
    private final File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo.xls");
    private final ArrayList<Integer> stats = new ArrayList<Integer>();
    private final ArrayList<String> courses = new ArrayList<String>();
    int nrCourses;
    RecyclerView scrollStats;
    FirebaseDatabase database;
    private Button exportButton;
    private Spinner spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_statistics,
                container,
                false
        );

        checkAndRequestPermissions();

//        if (Build.VERSION.SDK_INT >= 30){
//            if (!Environment.isExternalStorageManager()){
//                Intent getpermission = new Intent();
//                getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
//                startActivity(getpermission);
//            }
//        }

        binding.statsLL.setOrientation(LinearLayout.VERTICAL);
        scrollStats = binding.scrollStats;
        scrollStats.setLayoutManager(new LinearLayoutManager(this.getContext()));
        initCoursesArray(scrollStats);
        initspinnerfooter();

        binding.exportBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
                        DatabaseReference attendanceRef = database.getReference("Attendance");
                        DatabaseReference usersRef = database.getReference("Users");
                        String selectedCourse = binding.attendanceSpinner.getSelectedItem().toString();
                        ArrayList<String[]> studentsData = new ArrayList<>();
                        final CSVWriter[] writer = {null};

                        attendanceRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(selectedCourse)) {
                                    if (dataSnapshot.child(selectedCourse).hasChildren()) {
                                        Iterator iterator = dataSnapshot.child(selectedCourse).getChildren().iterator();
                                        int usersSize = Math.toIntExact(dataSnapshot.child(selectedCourse).getChildrenCount());

                                        while (iterator.hasNext()) {
                                            String encondingName = ((DataSnapshot) iterator.next()).getKey();
                                            final String[] name = {""};
                                            final String[] grupa = {""};
                                            final String[] facultate = {""};
                                            final String[] email = {""};

                                            usersRef.child(encondingName).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                @Override
                                                public void onSuccess(DataSnapshot usersDataSnapshot) {

                                                    for (DataSnapshot item_snapshot : usersDataSnapshot.getChildren()) {

                                                        if (item_snapshot.getKey().equals("name")) {
                                                            name[0] = item_snapshot.getValue().toString();
                                                            String[] tempData = {name[0], facultate[0], grupa[0], email[0]};
                                                            if (studentsData.size() < usersSize) {
                                                                studentsData.add(tempData);
                                                            }

                                                            if (studentsData.size() == usersSize) {
                                                                String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                                                                String fileName = "AnalysisData.csv";
                                                                String filePath = baseDir + File.separator + fileName;

                                                                try {
                                                                    writer[0] = new CSVWriter(new FileWriter(filePath, true));
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                String[] data = {"Name", "Faculty", "Group", "Email"};
                                                                writer[0].writeNext(data);
                                                                for (String[] tempString : studentsData) {
                                                                    Toast.makeText(getContext(), Arrays.toString(tempString), Toast.LENGTH_SHORT).show();
                                                                    writer[0].writeNext(tempString);
                                                                }

                                                                try {
                                                                    writer[0].close();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }

                                                        if (item_snapshot.getKey().equals("grupa")) {
                                                            grupa[0] = item_snapshot.getValue().toString();
                                                        }
                                                        if (item_snapshot.getKey().equals("facultate")) {
                                                            facultate[0] = item_snapshot.getValue().toString();
                                                        }
                                                        if (item_snapshot.getKey().equals("email")) {
                                                            email[0] = item_snapshot.getValue().toString();
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
        );

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
                    String course = ((DataSnapshot) iterator.next()).getKey();
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
                                    String course = ((DataSnapshot) it.next()).getKey();
                                    materii.add(course);
                                }

                                Log.i("coursessize", String.valueOf(courses.size()));

                                ArrayList<String> copie = (ArrayList<String>) courses.clone();

                                for (String course: copie) {
                                    int bec = 0;
                                    for (String materie: materii) {
                                        Log.i("ceapamasii", course + " " + abrevieri.get(materie) + "Size" + courses.size());
                                        if (course.contains(abrevieri.get(materie)) == true) {
                                            bec = 1;
                                        }
                                    }
                                    if (bec == 0) {
                                        courses.remove(course);
                                    }
                                }

                                nrCourses = courses.size();
                                DatabaseReference reference = database.getReference("Attendance");

                                for(String course: courses) {
                                    reference.child(course).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            Log.i("count", course + " " + dataSnapshot.getChildrenCount());
                                            waitForRetrieveData();
                                            stats.add((int) dataSnapshot.getChildrenCount());
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

    void waitForRetrieveData()
    {
        nrCourses--;
        if(nrCourses == 0)
        {
            RecyclerViewStatistics adapter = new RecyclerViewStatistics(stats, courses, getActivity());
            scrollStats.setAdapter(adapter);
        }

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
                    String course = ((DataSnapshot) it.next()).getKey();
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

    private boolean checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
        return true;
    }

}