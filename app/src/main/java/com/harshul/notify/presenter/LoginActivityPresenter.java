package com.harshul.notify.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivityPresenter implements LoginActivityContract.Presenter {

    LoginActivityContract.View view;

    public LoginActivityPresenter(LoginActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void doLogin(String email, String password) {

        if (TextUtils.isEmpty(email)) {
            Toast.makeText((Context) view, "Enter your email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText((Context) view, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        view.onShowProgressBar();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> view.startActivity())
                .addOnFailureListener(e -> view.onError(e));
    }

    @Override
    public void forgotPassword(String email) {
        if (TextUtils.isEmpty(email))
            Toast.makeText((Context) view, "Please enter email", Toast.LENGTH_SHORT).show();
        else {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnSuccessListener(unused -> {
                Snackbar.make((View) view, "Email sent to: " + email, Snackbar.LENGTH_SHORT).show();
            });
        }

    }
}
