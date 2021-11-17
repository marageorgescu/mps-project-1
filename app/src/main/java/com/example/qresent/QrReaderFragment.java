package com.example.qresent;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.qresent.databinding.FragmentQrReaderBinding;
import com.example.qresent.databinding.FragmentSignupBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class QrReaderFragment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 0;
    private CodeScanner mCodeScanner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentQrReaderBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_qr_reader,
                container,
                false
        );


        final Activity activity = getActivity();
        CodeScannerView scannerView = binding.scannerView;

        mCodeScanner = new CodeScanner(requireActivity(), scannerView);
        mCodeScanner.setDecodeCallback(result -> activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                /*while(result.getText().equals("")) {

                }*/
                Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
                PutUserAttendanceOnDB(result.getText().toString());
            }
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
        return binding.getRoot();
    }

    void PutUserAttendanceOnDB(String course)
    {
        DatabaseReference myRef = database.getReference("Attendance");
        myRef.child(course).child(mAuth.getCurrentUser().getUid()).setValue(Calendar.getInstance().getTime().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //Toast.makeText(getActivity(), "Introducere", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void GetUserNameFromDB(String course)
    {
        DatabaseReference myRef = database.getReference("Users");
        myRef.child(mAuth.getCurrentUser().getUid()).child("name").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null)
                    PutUserAttendanceOnDB(course);
                else
                    Toast.makeText(getActivity(), "Kaka", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }


}