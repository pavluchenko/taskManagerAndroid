package com.example.taskmanager.view;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskListRepository;

import java.util.List;

public class TaskListViewModel extends AndroidViewModel {

    private final TaskListRepository repository;
    private final LiveData<List<Task>> allTasks;
    private final LiveData<String> currentUser;

    public TaskListViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskListRepository(application);
        allTasks = repository.getAllTasks();
        currentUser = repository.getCurrentUser();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<String> getCurrentUser() {
        return currentUser;
    }

    public void insertTask(String id, String date, Task task) {
        repository.insertTask(id, date, task);
    }

    public void updateTask(String id, String date, Task task, int position) {
        repository.updateTask(id, date, task, position);
    }

    public void deleteTask(String id, String date, Task task, int position) {
        repository.deleteTask(id, date, task, position);
    }

    public void getTasks(String id, String date) {
        repository.getTasks(id, date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getToday() {
        return repository.getToday();
    }
}
