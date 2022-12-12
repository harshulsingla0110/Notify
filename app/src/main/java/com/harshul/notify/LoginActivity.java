package com.harshul.notify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.harshul.notify.databinding.ActivityLoginBinding;
import com.harshul.notify.presenter.LoginActivityContract;
import com.harshul.notify.presenter.LoginActivityPresenter;
import com.harshul.notify.util.Utils;

public class LoginActivity extends AppCompatActivity implements LoginActivityContract.View {

    private final Activity mActivity = LoginActivity.this;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");
    private ActivityLoginBinding binding;
    private LoginActivityContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        presenter = new LoginActivityPresenter(this);

        binding.tvRegister.setText(Utils.getUnderlineColor(this, getString(R.string.new_to_notify_register), R.color.main_color, 15, 23));

        binding.tvRegister.setOnClickListener(view -> {
            startActivity(new Intent(mActivity, SignUpActivity.class));
            finish();
        });

        binding.buttonLogin.setOnClickListener(view -> {
            presenter.doLogin(binding.etEmail.getText().toString().trim(), binding.etPassword.getText().toString().trim());
        });

        binding.tvForgotPassword.setOnClickListener(view -> presenter.forgotPassword(binding.etEmail.getText().toString().trim()));

    }

    @Override
    public void onShowProgressBar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(Exception e) {
        if (e instanceof FirebaseAuthInvalidCredentialsException)
            Toast.makeText(mActivity, "Wrong password", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mActivity, "Error. Please try again", Toast.LENGTH_SHORT).show();
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void startActivity() {
        startActivity(new Intent(mActivity, NotesActivity.class));
        finish();
    }
}