package com.example.taskmanager.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanager.databinding.ActivityLoginBinding;
import com.example.taskmanager.model.User;
import com.example.taskmanager.view.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        loginViewModel.getLoginValid().observe(this, aBoolean -> {
            if (aBoolean) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
        binding.registerButton.setOnClickListener(view -> {
            String id = binding.userId.getText().toString();
            String pw = binding.userPassword.getText().toString();

            if (id.length() <= 0 || pw.length() <= 0) {
                Toast.makeText(LoginActivity.this, "Please check your entries.", Toast.LENGTH_SHORT).show();
                return;
            }

            loginViewModel.register(new User(id, pw));

        });

        binding.loginButton.setOnClickListener(view -> {
            String id = binding.userId.getText().toString();
            String pw = binding.userPassword.getText().toString();

            if (id.length() <= 0 || pw.length() <= 0) {
                Toast.makeText(LoginActivity.this, "Please check your entries.", Toast.LENGTH_SHORT).show();
                return;
            }

            loginViewModel.login(new User(id, pw));

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loginViewModel.loginCheck();

    }
}