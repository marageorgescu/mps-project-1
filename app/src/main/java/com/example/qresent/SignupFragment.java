package com.example.qresent;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.qresent.databinding.FragmentLoginBinding;
import com.example.qresent.databinding.FragmentSignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupFragment extends Fragment {

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSignupBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_signup,
                container,
                false
        );

        changeHighlightButton(binding.studentBtn, binding);

        binding.studentBtn.setOnClickListener(v -> changeHighlightButton(binding.studentBtn, binding));
        binding.teacherBtn.setOnClickListener(v -> changeHighlightButton(binding.teacherBtn, binding));
        binding.adminBtn.setOnClickListener(v -> changeHighlightButton(binding.adminBtn, binding));

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(binding.emailSignUpET.getText().toString(), binding.passwordSignUpET.getText().toString(), v);
            }
        });

        return binding.getRoot();
    }

    public void changeHighlightButton(ImageButton btn, FragmentSignupBinding binding) {
        if (btn.equals(binding.studentBtn)) {

            btn.setBackgroundColor(Color.parseColor("#65E0E5"));
            binding.teacherBtn.setBackgroundColor(0);
            binding.adminBtn.setBackgroundColor(0);

        } else if (btn.equals(binding.teacherBtn)) {

            btn.setBackgroundColor(Color.parseColor("#65E0E5"));
            binding.studentBtn.setBackgroundColor(0);
            binding.adminBtn.setBackgroundColor(0);

        } else if (btn.equals(binding.adminBtn)) {

            btn.setBackgroundColor(Color.parseColor("#65E0E5"));
            binding.studentBtn.setBackgroundColor(0);
            binding.teacherBtn.setBackgroundColor(0);

        }
    }

    private void createAccount(String email, String password, View v) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
        }).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Navigation.findNavController(v)
                        .navigate(R.id.action_signupFragment_to_qrGeneratorFragment);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}