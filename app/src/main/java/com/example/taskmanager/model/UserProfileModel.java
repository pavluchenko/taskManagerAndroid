package com.example.taskmanager.model;

public class UserProfileModel {
    private final String uid;
    private final String email;
    private final String userName;
    private String profileUri;
    private final String photoUri;

    public UserProfileModel(String uid, String email, String nickName, String profileUri, String tempPhotoUri) {
        this.uid = uid;
        this.email = email;
        this.userName = nickName;
        this.profileUri = profileUri;
        this.photoUri = tempPhotoUri;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfileUri() {
        return profileUri;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    public String getTempPhotoUri() {
        return photoUri;
    }
}
