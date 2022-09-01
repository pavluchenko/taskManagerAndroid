package com.example.taskmanager.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanager.R;
import com.example.taskmanager.databinding.ActivityFriendSearchBinding;
import com.example.taskmanager.model.UserProfile;
import com.example.taskmanager.view.ProfileViewModel;

import java.util.List;

public class FriendSearchActivity extends AppCompatActivity {

    private ActivityFriendSearchBinding binding;
    private String uid;
    private ProfileViewModel profileViewModel;
    private String userNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_up, R.anim.none);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        profileViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ProfileViewModel.class);
        profileViewModel.getAllProfiles().observe(this, new Observer<List<UserProfile>>() {
            @Override
            public void onChanged(List<UserProfile> userProfiles) {
                profileViewModel.addFriend(userProfiles, uid, userNickName);
            }
        });

        profileViewModel.addFriendCheck().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(FriendSearchActivity.this, "Friend added", Toast.LENGTH_SHORT).show();
                    Intent data = new Intent();
                    data.putExtra("check", true);
                    setResult(RESULT_OK, data);
                    finish();
                    overridePendingTransition(R.anim.none, R.anim.fadeout_up);
                } else {
                    Toast.makeText(FriendSearchActivity.this, "You are already registered friends.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNickName = binding.userNickName.getText().toString();
                profileViewModel.getUsersProfile();
            }
        });

        binding.closeButton.setColorFilter(Color.parseColor("#FFFFFF"));
        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("check", false);
                setResult(RESULT_OK, data);
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_up);
            }
        });
    }
}