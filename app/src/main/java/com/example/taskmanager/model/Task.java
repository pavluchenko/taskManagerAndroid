package com.example.taskmanager.model;

public class Task {
    private final String email;
    private final String date;
    private String uid;
    //TODO: need?
    private String key;
    private String title;
    private String content;
    private String hour;
    private String minute;
    private boolean complete;

    public Task(String uid, String key, String title, String email, String date, String content, String hour, String minute, boolean complete) {
        this.uid = uid;
        this.key = key;
        this.title = title;
        this.email = email;
        this.date = date;
        this.content = content;
        this.hour = hour;
        this.minute = minute;
        this.complete = complete;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }


}
