package com.example.qresent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.qresent.databinding.FragmentForgotPasswordBinding;
import com.example.qresent.databinding.FragmentSignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.net.PasswordAuthentication;

public class ForgotPasswordFragment extends Fragment {

    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentForgotPasswordBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_forgot_password,
                container,
                false
        );

        mAuth = FirebaseAuth.getInstance();

        binding.resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.forgotEmailET.getText().toString();
                Log.d("info", email + "da");
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d("reset", "task is successful");
                        } else {
                            Log.d("reset", "task failed");
                        }

                    }
                });

            }
        });

        return binding.getRoot();
    }

}
