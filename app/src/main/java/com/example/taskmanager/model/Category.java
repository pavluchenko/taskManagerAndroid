package com.example.taskmanager.model;

public class Category {
    private int amountTask;
    private String categoryName;

    public Category(String categoryName, int amountTask) {
        this.amountTask = amountTask;
        this.categoryName = categoryName;
    }

    public int getAmountTask() {
        return amountTask;
    }

    public void setAmountTask(int amountTask) {
        this.amountTask = amountTask;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}