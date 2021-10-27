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
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QrReaderFragment extends Fragment {

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
                Toast.makeText(activity, result.getText(), Toast.LENGTH_SHORT).show();
            }
        }));
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
        return binding.getRoot();
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