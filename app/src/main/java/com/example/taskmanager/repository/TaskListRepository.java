package com.example.taskmanager.repository;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.taskmanager.model.TaskModel;
import com.example.taskmanager.model.UserProfileModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskListRepository {
    public static final String TAG = "TodoListRepository";

    private final DatabaseReference myRef;
    private final MutableLiveData<List<TaskModel>> allTodos;
    private final MutableLiveData<String> currentUser;
    private final List<TaskModel> todos;

    public TaskListRepository(Application application) {
        myRef = FirebaseDatabase.getInstance().getReference("todos");
        todos = new ArrayList<>();
        allTodos = new MutableLiveData<>();
        currentUser = new MutableLiveData<>();
    }

    public LiveData<List<TaskModel>> getAllTodos() {
        return allTodos;
    }

    public LiveData<String> getCurrentUser() { return currentUser; }

    public void insertTask(String id, String date, TaskModel todo) {
        String key = myRef.push().getKey();
        todo.setKey(key);
        myRef.child(id).child(date).child(key).setValue(todo);
        todos.add(todo);
        allTodos.setValue(todos);
    }

    public void updateTask(String id, String date, TaskModel todo, int position) {
        Log.d(TAG, id);
        Log.d(TAG, date);
        Log.d(TAG, todo.getKey());
        myRef.child(id).child(date).child(todo.getKey()).setValue(todo);
        todos.get(position).setTitle(todo.getTitle());
        todos.get(position).setContent(todo.getContent());
        todos.get(position).setHour(todo.getHour());
        todos.get(position).setMinute(todo.getMinute());
        allTodos.setValue(todos);
    }

    public void deleteTask(String id, String date, TaskModel todo, int position) {
        myRef.child(id).child(date).child(todo.getKey()).removeValue();
        todos.remove(position);
        allTodos.setValue(todos);
    }

    public void getTasks(String id, String date) {
        myRef.child(id).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                todos.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    TaskModel todo = snapshot1.getValue(TaskModel.class);
                    todos.add(todo);
                }

                DatabaseReference profilesRef = FirebaseDatabase.getInstance().getReference("profiles");
                profilesRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        UserProfileModel userProfile = snapshot.getValue(UserProfileModel.class);
                        if (userProfile != null)
                            currentUser.setValue(userProfile.getUserName());
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

                allTodos.setValue(todos);
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
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    todos.add(snapshot1.getValue(TaskModel.class));
                }
                allTodos.setValue(todos);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}

