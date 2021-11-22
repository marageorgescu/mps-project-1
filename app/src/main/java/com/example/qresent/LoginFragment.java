package com.example.qresent;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.qresent.databinding.FragmentLoginBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class LoginFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int RC_SIGN_IN = 2;
    private final String TAG = "TAG";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FragmentLoginBinding binding;
    FirebaseDatabase database;

    private CallbackManager callbackManager;

    View view_global;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);


        //AppEventsLogger.activateApp(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_login,
                container,
                false
        );
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        checkAndRequestPermissions();
        binding.forgotpasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });
        binding.signUpTV.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_signupFragment));
        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(binding.emailET.getText()).toString().trim();
                String password = Objects.requireNonNull(binding.passwordET.getText()).toString().trim();
                if (checkValidityEmailAndPassword(email, password)) {
                    firebaseAuthWithEmailAndPassword(email, password, v);
                }


            }
        });

        createRequest();

        binding.googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_global = v;
                signIn();
            }
        });

        binding.facebookSignInBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "succes!" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void handleFacebookToken(AccessToken accessToken) {
        Log.d(TAG, "Access token" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "task is successful");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user, view_global);
                } else {
                    Log.d(TAG, "task failed");
                }
            }
        });

    }

    private boolean checkValidityEmailAndPassword(String email, String password) {
        if (email.isEmpty()) {
            binding.emailET.setError("Email is required");
            binding.emailET.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailET.setError("Please provide a valid email");
            binding.emailET.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            binding.passwordET.setError("Password is required");
            binding.passwordET.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            binding.passwordET.setError("Min password length should be 6 characters");
            binding.passwordET.requestFocus();
            return false;
        }

        return true;
    }

    private void firebaseAuthWithEmailAndPassword(String email, String password, View view) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user, view);

                        } else {
                            // If sign in fails, try to create a new account

                            /*createAccount(email, password);
                            FirebaseUser user = mAuth.getCurrentUser();

                            updateUI(user, view);*/

                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment);

                        }
                    }
                });
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
        });
    }

    private void updateUI(FirebaseUser user, View view) {
        if (user != null) {

            database = FirebaseDatabase.getInstance("https://qresent-926c3-default-rtdb.europe-west1.firebasedatabase.app/");
            DatabaseReference myRef = database.getReference("Users");
            myRef.child(user.getUid()).child("accountType").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    String type = dataSnapshot.getValue(String.class);
                    if (type.equals("Student")) {
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_studentHomeScreenFragment);
                    } else if (type.equals("Teacher")) {
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_teacherHomeScreenFragment);
                    } else if (type.equals("Administrator")) {
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_adminHomeScreenFragment);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private  boolean checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_CAMERA);
        }
        return true;
    }

    private void createRequest() {
        // Configure Google Sign in
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id_key))
                .requestEmail()
                .build();

        // Build a GoogleSignClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.i(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getActivity(), "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                        //updateUI(user, null);
                        Navigation.findNavController(view_global).navigate(R.id.action_loginFragment_to_studentHomeScreenFragment);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.i(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(getActivity(), "signInWithCredential:FAIL", Toast.LENGTH_SHORT).show();

                        //updateUI(null, null);
                    }
                });
    }
}