package com.harshul.notify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harshul.notify.databinding.ActivitySignUpBinding;
import com.harshul.notify.util.Constants;
import com.harshul.notify.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");
    private final Activity mActivity = SignUpActivity.this;
    private ActivitySignUpBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        binding.tvLogin.setText(Utils.getUnderlineColor(this, getString(R.string.already_joined), R.color.main_color, 18, 23));

        authStateListener = firebaseAuth -> {
            currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                //user already logged in
                startActivity(new Intent(mActivity, NotesActivity.class));
                finish();
            }
        };


        binding.tvLogin.setOnClickListener(view -> {
            startActivity(new Intent(mActivity, LoginActivity.class));
            finish();
        });

        binding.buttonCreate.setOnClickListener(view -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            String name = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            createUserAccount(name, email, password);
        });

    }

    private void createUserAccount(String name, String email, String password) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(mActivity, "Enter your name", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(mActivity, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(mActivity, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                currentUser = firebaseAuth.getCurrentUser();
                assert currentUser != null;
                String currentUserId = currentUser.getUid();

                Map<String, Object> data = new HashMap<>();
                data.put(Constants.USER_NAME, name);
                data.put(Constants.USER_ID, currentUserId);
                collectionReference.add(data).addOnSuccessListener(documentReference -> {
                            startActivity(new Intent(mActivity, NotesActivity.class));
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(mActivity, "Error", Toast.LENGTH_SHORT).show();
                            binding.progressBar.setVisibility(View.GONE);
                        });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}