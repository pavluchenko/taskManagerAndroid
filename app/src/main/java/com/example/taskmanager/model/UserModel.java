package com.example.taskmanager.model;

public class UserModel {
    private String email;
    private final String password;

    public UserModel(String id, String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String name) {
        this.email = name;
    }

    public String getPassword() {
        return password;
    }

}
