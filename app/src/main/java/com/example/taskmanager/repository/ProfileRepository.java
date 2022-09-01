package com.example.taskmanager.repository;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.taskmanager.model.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProfileRepository {

    private final DatabaseReference myRef;
    private final MutableLiveData<UserProfile> userProfile;
    private final MutableLiveData<List<UserProfile>> allProfiles;
    private final MutableLiveData<List<UserProfile>> allFriends;
    private final MutableLiveData<Boolean> addFriendCheck;
    List<UserProfile> userProfiles;
    List<UserProfile> friends;

    public ProfileRepository(Application qpp) {
        myRef = FirebaseDatabase.getInstance().getReference("profiles");
        userProfile = new MutableLiveData<>();
        allProfiles = new MutableLiveData<>();
        allFriends = new MutableLiveData<>();
        addFriendCheck = new MutableLiveData<>();
        userProfiles = new ArrayList<>();
        friends = new ArrayList<>();
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public LiveData<List<UserProfile>> getAllProfiles() {
        return allProfiles;
    }

    public LiveData<List<UserProfile>> getAllFriends() {
        return allFriends;
    }

    public LiveData<Boolean> addFirendCheck() {
        return addFriendCheck;
    }

    public void getFriends(String uid) {
        myRef.child(uid).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                friends.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserProfile profile = snapshot1.getValue(UserProfile.class);
                    friends.add(profile);
                }
                allFriends.setValue(friends);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    //TODO: fix
    public void getUsersProfile() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userProfiles.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    UserProfile profile = ds.getValue(UserProfile.class);
                    userProfiles.add(profile);
                }
                allProfiles.setValue(userProfiles);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void addFriend(List<UserProfile> usersProfile, String uid, String userNickName) {
        for (UserProfile userProfile : usersProfile) {
            if (userProfile.getNickName().equals(userNickName)) {
                myRef.child(uid).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        boolean overlapCheck = true;
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            UserProfile profile = snapshot1.getValue(UserProfile.class);
                            if (profile.getNickName().equals(userNickName))
                                overlapCheck = false;
                        }

                        if (overlapCheck) {
                            myRef.child(uid).child("friends").push().setValue(userProfile)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful())
                                            addFriendCheck.setValue(true);
                                    });
                        } else {
                            addFriendCheck.setValue(false);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }
        }
    }

    public void getUser(String uid) {
        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                UserProfile profile = snapshot.getValue(UserProfile.class);
                if (profile != null)
                    userProfile.setValue(profile);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void setUser(UserProfile profile) {
        String uid = profile.getUid();
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("profiles/" + uid + ".jpg");
        UploadTask uploadTask = storage.putFile(Uri.parse(profile.getTempPhotoUri()));
        uploadTask.addOnSuccessListener(taskSnapshot -> storage.getDownloadUrl().addOnSuccessListener(uri -> {
            Uri profileUri = uri;
            profile.setProfileUri(profileUri.toString());

            myRef.child(uid).setValue(profile).addOnCompleteListener(task -> {
                if (task.isSuccessful())
                    getUser(uid);
            });
        }));

    }


}
