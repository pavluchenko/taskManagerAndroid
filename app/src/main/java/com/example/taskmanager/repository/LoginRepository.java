package com.example.taskmanager.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.taskmanager.model.UserModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class LoginRepository {

    public final static String TAG = "LoginRepository";

    private final Application application;
    private final FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private final MutableLiveData<FirebaseUser> userData;
    private final MutableLiveData<Boolean> loginValid;

    public LoginRepository(Application application) {
        this.application = application;
        mAuth = FirebaseAuth.getInstance();
        userData = new MutableLiveData<>();
        loginValid = new MutableLiveData<>();
    }

    public LiveData<FirebaseUser> getUser() {return userData;}

    public LiveData<Boolean> getLoginValid() { return loginValid;}

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void login(UserModel user) {
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor() , task -> {
                    if (task.isSuccessful()) {
                        mUser = mAuth.getCurrentUser();
                        Log.d(TAG, "Login successfully");
                        Toast.makeText(application, "Login successfully", Toast.LENGTH_SHORT).show();
                        loginValid.setValue(true);
                    } else {
                        Log.d(TAG, "Authorization failed");
                        Toast.makeText(application, "Authorization failed", Toast.LENGTH_SHORT).show();
                        loginValid.setValue(false);
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void register(UserModel user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor(), task -> {
                    if (task.isSuccessful()) {
                        mUser = mAuth.getCurrentUser();
                        Log.d(TAG, "Successful registration");
                        Toast.makeText(application, "Successful registration", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "Registration failed");
                        Toast.makeText(application, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public void checkLogin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userData.setValue(currentUser);
            loginValid.setValue(true);
            Log.d(TAG, "Log in");
        } else {
            Log.d(TAG, "Login failed");
            loginValid.setValue(false);
        }
    }
}


