package com.example.qresent;

import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.qresent.databinding.FragmentLoginBinding;
import com.example.qresent.databinding.FragmentSignupBinding;

public class SignupFragment extends Fragment {

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
}