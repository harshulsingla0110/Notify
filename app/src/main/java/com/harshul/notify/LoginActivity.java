package com.harshul.notify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harshul.notify.databinding.ActivityLoginBinding;
import com.harshul.notify.util.Utils;

public class LoginActivity extends AppCompatActivity {

    private final Activity mActivity = LoginActivity.this;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        binding.tvRegister.setText(Utils.getUnderlineColor(this, getString(R.string.new_to_notify_register), R.color.main_color, 15, 23));

        binding.tvRegister.setOnClickListener(view -> {
            startActivity(new Intent(mActivity, SignUpActivity.class));
            finish();
        });

        binding.buttonLogin.setOnClickListener(view -> loginWithEmailPassword(binding.etEmail.getText().toString().trim(), binding.etPassword.getText().toString().trim()));
    }

    private void loginWithEmailPassword(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(mActivity, "Enter your email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(mActivity, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    startActivity(new Intent(mActivity, NotesActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseAuthInvalidCredentialsException)
                        Toast.makeText(mActivity, "Wrong password", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mActivity, "Error. Please try again", Toast.LENGTH_SHORT).show();

                });


    }
}