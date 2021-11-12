package com.example.qresent;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qresent.databinding.FragmentHomeScreenBinding;
import com.example.qresent.databinding.FragmentLoginBinding;

public class HomeScreenFragment extends Fragment {

    private FragmentHomeScreenBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home_screen,
                container,
                false
        );



        return binding.getRoot();
    }
}