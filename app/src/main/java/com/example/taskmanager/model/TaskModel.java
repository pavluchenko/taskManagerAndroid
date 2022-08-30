package com.example.taskmanager.model;

public class TaskModel {

    private String key;
    private String title;
    private String description;
    private String email;
    private String date;
    private String content;
    private String hour;
    private String minute;
    private boolean complete;

    public TaskModel(String key, String title, String description, String email, String date, String content,
                     String hour, String minute, boolean complete) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.email = email;
        this.date = date;
        this.content = content;
        this.hour = hour;
        this.minute = minute;
        this.complete = complete;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
