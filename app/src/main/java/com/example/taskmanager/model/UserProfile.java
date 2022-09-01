package com.example.taskmanager.model;

public class UserProfile {
    private final String uid;
    private final String email;
    private final String nickName;
    private final String tempPhotoUri;
    private String profileUri;

    public UserProfile(String uid, String email, String nickName, String profileUri, String tempPhotoUri) {
        this.uid = uid;
        this.email = email;
        this.nickName = nickName;
        this.profileUri = profileUri;
        this.tempPhotoUri = tempPhotoUri;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickName;
    }

    public String getProfileUri() {
        return profileUri;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    public String getTempPhotoUri() {
        return tempPhotoUri;
    }
}
