package com.example.taskmanager.repository;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.taskmanager.model.UserProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ProfileRepository {
    public static final String TAG = "ProfileRepository";
    List<UserProfileModel> userProfiles;
    List<UserProfileModel> friends;
    private final DatabaseReference myRef;
    private final MutableLiveData<UserProfileModel> userProfile;
    private final MutableLiveData<List<UserProfileModel>> allProfiles;
    private final MutableLiveData<List<UserProfileModel>> allFriends;
    private final MutableLiveData<Boolean> addFriendCheck;

    public ProfileRepository(Application application) {
        myRef = FirebaseDatabase.getInstance().getReference("profiles");
        userProfile = new MutableLiveData<>();
        allProfiles = new MutableLiveData<>();
        allFriends = new MutableLiveData<>();
        addFriendCheck = new MutableLiveData<>();
        userProfiles = new ArrayList<>();
        friends = new ArrayList<>();
    }

    public LiveData<UserProfileModel> getUserProfile() {
        return userProfile;
    }

    public LiveData<List<UserProfileModel>> getAllProfiles() {
        return allProfiles;
    }

    public LiveData<List<UserProfileModel>> getAllFriends() {
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
                    UserProfileModel profile = snapshot1.getValue(UserProfileModel.class);
                    friends.add(profile);
                }
                allFriends.setValue(friends);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getUsersProfile() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                userProfiles.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    UserProfileModel profile = snapshot1.getValue(UserProfileModel.class);
                    userProfiles.add(profile);
                }
                allProfiles.setValue(userProfiles);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void addFriend(List<UserProfileModel> usersProfile, String uid, String userNickName) {
        for (UserProfileModel userProfile : usersProfile) {
            if (userProfile.getUserName().equals(userNickName)) {
                myRef.child(uid).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        boolean overlapCheck = true;
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            UserProfileModel profile = snapshot1.getValue(UserProfileModel.class);
                            if (profile.getUserName().equals(userNickName))
                                overlapCheck = false;
                        }

                        if (overlapCheck) {
                            myRef.child(uid).child("friends").push().setValue(userProfile)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                addFriendCheck.setValue(true);
                                        }
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
                UserProfileModel profile = snapshot.getValue(UserProfileModel.class);
                if (profile != null)
                    userProfile.setValue(profile);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void setUser(UserProfileModel profile) {
        String uid = profile.getUid();
        StorageReference storage = FirebaseStorage.getInstance().getReference().child("profiles/" + uid + ".jpg");
        UploadTask uploadTask = storage.putFile(Uri.parse(profile.getTempPhotoUri()));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri profileUri = uri;
                        profile.setProfileUri(profileUri.toString());

                        myRef.child(uid).setValue(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful())
                                    getUser(uid);
                            }
                        });
                    }
                });
            }
        });
    }
}

