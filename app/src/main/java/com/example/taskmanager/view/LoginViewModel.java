package com.example.taskmanager.view;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.taskmanager.model.User;
import com.example.taskmanager.repository.LoginRepository;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel {
    private final LoginRepository repository;
    private final LiveData<FirebaseUser> userData;
    private final LiveData<Boolean> loginValid;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new LoginRepository(application);
        userData = repository.getUser();
        loginValid = repository.getLoginValid();
    }

    public LiveData<FirebaseUser> getUser() {
        return userData;
    }

    public LiveData<Boolean> getLoginValid() {
        return loginValid;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void login(User user) {
        repository.login(user);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void register(User user) {
        repository.register(user);
    }

    public void logout() {
        repository.logout();
    }

    public void loginCheck() {
        repository.loginCheck();
    }

}

