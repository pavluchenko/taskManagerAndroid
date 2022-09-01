package com.example.taskmanager.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.taskmanager.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginRepository {

    public final static String TAG = "LoginRepository";

    private final Application application;
    private final FirebaseAuth mAuth;
    private final MutableLiveData<FirebaseUser> userData;
    private final MutableLiveData<Boolean> loginValid;
    private FirebaseUser mUser;

    public LoginRepository(Application application) {
        this.application = application;
        mAuth = FirebaseAuth.getInstance();
        userData = new MutableLiveData<>();
        loginValid = new MutableLiveData<>();
    }

    public LiveData<FirebaseUser> getUser() {
        return userData;
    }

    public LiveData<Boolean> getLoginValid() {
        return loginValid;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void login(User user) {
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    if (task.isSuccessful()) {
                        mUser = mAuth.getCurrentUser();
                        Log.d(TAG, "Login is success");
                        Toast.makeText(application, "Login is success", Toast.LENGTH_SHORT).show();
                        loginValid.setValue(true);
                    } else {
                        Log.d(TAG, "Authorization failed");
                        Toast.makeText(application, "Authorization failed", Toast.LENGTH_SHORT).show();
                        loginValid.setValue(false);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void register(User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    if (task.isSuccessful()) {
                        mUser = mAuth.getCurrentUser();
                        Log.d(TAG, "Registration is success");
                        Toast.makeText(application, "Registration is success", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Registration failed");
                        Toast.makeText(application, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public void loginCheck() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userData.setValue(currentUser);
            loginValid.setValue(true);
            Log.d(TAG, "Login");
        } else {
            Log.d(TAG, "Failed");
            loginValid.setValue(false);
        }
    }
}

