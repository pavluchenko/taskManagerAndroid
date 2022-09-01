package com.example.taskmanager.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.inputmethod.EditorInfo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.taskmanager.R;
import com.example.taskmanager.databinding.ActivityProfileBinding;
import com.example.taskmanager.model.UserProfile;
import com.example.taskmanager.view.LoginViewModel;
import com.example.taskmanager.view.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {

    private final static String TAG = "ProfileActivity";

    private String uid;
    private Uri tempPhotoUri;
    private String userNickName;
    private String userEmail;
    private ActivityProfileBinding binding;

    private ProfileViewModel profileViewModel;
    private LoginViewModel loginViewModel;

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        tempPhotoUri = intent.getData();

                        if (tempPhotoUri != null) {
                            Glide.with(ProfileActivity.this)
                                    .load(tempPhotoUri)
                                    .into(binding.userProfile);
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        overridePendingTransition(R.anim.fadein_up, R.anim.none);
        if (ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
            }
        }

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        userEmail = intent.getStringExtra("email");

        binding.userEmail.setText(userEmail);

        profileViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ProfileViewModel.class);
        profileViewModel.getUserProfile().observe(this, profile -> {
            Log.d(TAG, "Hello!!!");
            Log.d(TAG, profile.getNickName());
            Glide.with(ProfileActivity.this)
                    .load(profile.getProfileUri())
                    .into(binding.userProfile);
            binding.userEmail.setText(profile.getEmail());
            binding.userNickName.setText(profile.getNickName());
        });

        loginViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        binding.logoutButton.setOnClickListener(view -> loginViewModel.logout());


        profileViewModel.getUser(uid);

        binding.userProfile.setOnClickListener(view -> {
            Intent imageProfile = new Intent(Intent.ACTION_PICK);
            imageProfile.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imageProfile.setType("image/*");
            launcher.launch(imageProfile);
        });

        binding.userNickName.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                userNickName = textView.getText().toString();
                if (tempPhotoUri == null)
                    return true;
                UserProfile profile = new UserProfile(uid, userEmail, userNickName, null, tempPhotoUri.toString());
                profileViewModel.setUser(profile);
                return true;
            }
            return false;
        });

        binding.closeButton.setOnClickListener(view -> {
            finish();
            overridePendingTransition(R.anim.none, R.anim.fadeout_up);
        });
    }
}