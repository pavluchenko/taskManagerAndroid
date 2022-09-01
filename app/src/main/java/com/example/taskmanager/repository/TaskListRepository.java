package com.example.taskmanager.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.UserProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskListRepository {

    public static final String TAG = "TodoListRepository";

    private final DatabaseReference database;
    private final MutableLiveData<List<Task>> allTasks;
    private final MutableLiveData<String> currentUser;
    private final List<Task> tasks;

    public TaskListRepository(Application qpp) {
        database = FirebaseDatabase.getInstance().getReference("tasks");
        tasks = new ArrayList<>();
        allTasks = new MutableLiveData<>();
        currentUser = new MutableLiveData<>();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public LiveData<String> getCurrentUser() {
        return currentUser;
    }

    public void insertTask(String id, String date, Task task) {
        String key = database.push().getKey();
        task.setKey(key);
        database.child(id).child(date).child(key).setValue(task);
        tasks.add(task);
        allTasks.setValue(tasks);
    }

    public void updateTask(String id, String date, Task task, int position) {
        Log.d(TAG, id);
        Log.d(TAG, date);
        Log.d(TAG, task.getKey());
        database.child(id).child(date).child(task.getKey()).setValue(task);
        tasks.get(position).setTitle(task.getTitle());
        tasks.get(position).setContent(task.getContent());
        tasks.get(position).setHour(task.getHour());
        tasks.get(position).setMinute(task.getMinute());
        allTasks.setValue(tasks);
    }

    public void deleteTask(String id, String date, Task task, int position) {
        database.child(id).child(date).child(task.getKey()).removeValue();
        tasks.remove(position);
        allTasks.setValue(tasks);
    }

    public void getTasks(String id, String date) {
        database.child(id).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                tasks.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Task task = snapshot1.getValue(Task.class);
                    tasks.add(task);
                }

                DatabaseReference profilesRef = FirebaseDatabase.getInstance().getReference("profiles");
                profilesRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        UserProfile userProfile = snapshot.getValue(UserProfile.class);
                        if (userProfile != null)
                            currentUser.setValue(userProfile.getNickName());
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    }
                });
                allTasks.setValue(tasks);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getToday() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String today = now.format(formatter);
        return today;
    }

    public void init() {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    tasks.add(snapshot1.getValue(Task.class));
                }
                allTasks.setValue(tasks);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
