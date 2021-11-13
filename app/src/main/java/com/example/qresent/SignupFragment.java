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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupFragment extends Fragment {

    private FirebaseAuth mAuth;
    String accountType = new String("Student");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSignupBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_signup,
                container,
                false
        );

        mAuth = FirebaseAuth.getInstance();
        changeHighlightButton(binding.studentBtn, binding);

        binding.studentBtn.setOnClickListener(v -> changeHighlightButton(binding.studentBtn, binding));
        binding.teacherBtn.setOnClickListener(v -> changeHighlightButton(binding.teacherBtn, binding));
        binding.adminBtn.setOnClickListener(v -> changeHighlightButton(binding.adminBtn, binding));

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(binding.emailSignUpET.getText().toString(),
                        binding.passwordSignUpET.getText().toString(),
                        binding.nameET.getText().toString(),
                        binding.grupaET.getText().toString(),
                        binding.facultateET.getText().toString(),v);
            }
        });

        return binding.getRoot();
    }

    public void changeHighlightButton(ImageButton btn, FragmentSignupBinding binding) {
        if (btn.equals(binding.studentBtn)) {

            btn.setBackgroundColor(Color.parseColor("#65E0E5"));
            binding.teacherBtn.setBackgroundColor(0);
            binding.adminBtn.setBackgroundColor(0);
            accountType = "Student";
        } else if (btn.equals(binding.teacherBtn)) {

            btn.setBackgroundColor(Color.parseColor("#65E0E5"));
            binding.studentBtn.setBackgroundColor(0);
            binding.adminBtn.setBackgroundColor(0);
            accountType = "Teacher";
        } else if (btn.equals(binding.adminBtn)) {

            btn.setBackgroundColor(Color.parseColor("#65E0E5"));
            binding.studentBtn.setBackgroundColor(0);
            binding.teacherBtn.setBackgroundColor(0);
            accountType = "Administrator";
        }
    }

    private void createAccount(String email, String password,String name,String group,
                               String faculty,View v) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Navigation.findNavController(v)
                                .navigate(R.id.action_signupFragment_to_qrGeneratorFragment);

                        HashMap<Object, String> hashMap = new HashMap<>();
                        hashMap.put("uid", mAuth.getCurrentUser().getUid());
                        hashMap.put("name", name);
                        hashMap.put("grupa", group);
                        hashMap.put("grupa", faculty);
                        hashMap.put("email", email);
                        hashMap.put("accountType", accountType);



                        //Firebase database instance
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
                        DatabaseReference myRef = database.getReference("Users");
                        //put data within hashmap in database
                        myRef.child(mAuth.getCurrentUser().getUid()).setValue(hashMap);


                    }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}