package com.harshul.notify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.harshul.notify.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final Activity mActivity = MainActivity.this;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.buttonContinue.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

    }
}