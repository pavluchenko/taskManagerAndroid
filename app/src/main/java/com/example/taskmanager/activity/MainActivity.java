package com.example.taskmanager.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taskmanager.R;
import com.example.taskmanager.helper.StringHelper;
import com.example.taskmanager.adapter.FriendAdapter;
import com.example.taskmanager.adapter.TaskAdapter;
import com.example.taskmanager.databinding.ActivityMainBinding;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.view.LoginViewModel;
import com.example.taskmanager.view.ProfileViewModel;
import com.example.taskmanager.view.TaskListViewModel;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private String selectedDate;
    private TaskListViewModel taskListViewModel;
    private LoginViewModel loginViewModel;
    private ProfileViewModel profileViewModel;
    private ActivityMainBinding binding;
    private FirebaseUser user;

    ActivityResultLauncher<Intent> addFirendLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        boolean addFriend = intent.getBooleanExtra("check", false);
                        if (addFriend)
                            profileViewModel.getFriends(user.getUid());
                    }
                }
            });

    ActivityResultLauncher<Intent> dateLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        String date = intent.getStringExtra("date");
                        Log.d(TAG, date);
                        binding.today.setText(date);
                        taskListViewModel.getTasks(user.getUid(), date);
                        binding.progressBar.setVisibility(View.VISIBLE);
                        selectedDate = date;
                    }
                }
            });

    ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        int position = intent.getIntExtra(StringHelper.EXTRA_POSITION, -1);
                        String key = intent.getStringExtra(StringHelper.EXTRA_KEY);
                        String email = intent.getStringExtra(StringHelper.EXTRA_EMAIL);
                        String date = intent.getStringExtra(StringHelper.EXTRA_DATE);
                        String title = intent.getStringExtra(StringHelper.EXTRA_TITLE);
                        String content = intent.getStringExtra(StringHelper.EXTRA_CONTENT);
                        String hour = intent.getStringExtra(StringHelper.EXTRA_HOUR);
                        String minute = intent.getStringExtra(StringHelper.EXTRA_MINUTE);

                        Task task = new Task(user.getUid(), key, title, email, date, content, hour, minute, false);

                        Log.d(TAG, user.getUid());
                        Log.d(TAG, key);
                        Log.d(TAG, date);
                        taskListViewModel.updateTask(user.getUid(), date, task, position);
                    }
                }
            });
    ActivityResultLauncher<Intent> addLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        String date = intent.getStringExtra(StringHelper.EXTRA_DATE);
                        String title = intent.getStringExtra(StringHelper.EXTRA_TITLE);
                        String content = intent.getStringExtra(StringHelper.EXTRA_CONTENT);
                        String hour = intent.getStringExtra(StringHelper.EXTRA_HOUR);
                        String minute = intent.getStringExtra(StringHelper.EXTRA_MINUTE);
                        taskListViewModel.insertTask(user.getUid(), date, new Task(user.getUid(), null, title, user.getEmail(), date, content, hour, minute, false));
                    }
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        loginViewModel.loginCheck();
        user = loginViewModel.getUser().getValue();

        binding.mainTodosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.mainTodosRecyclerView.setHasFixedSize(true);

        binding.mainFriendsRecyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.mainFriendsRecyclerview.setHasFixedSize(true);

        TaskAdapter taskAdapter = new TaskAdapter(user.getUid());
        binding.mainTodosRecyclerView.setAdapter(taskAdapter);

        FriendAdapter friendAdapter = new FriendAdapter();
        binding.mainFriendsRecyclerview.setAdapter(friendAdapter);

        taskAdapter.setOnItemClickListener((task, position) -> {
            if (task.getUid().equals(user.getUid())) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra(StringHelper.EXTRA_POSITION, position);
                intent.putExtra(StringHelper.EXTRA_KEY, task.getKey());
                intent.putExtra(StringHelper.EXTRA_EMAIL, task.getEmail());
                intent.putExtra(StringHelper.EXTRA_DATE, task.getDate());
                intent.putExtra(StringHelper.EXTRA_TITLE, task.getTitle());
                intent.putExtra(StringHelper.EXTRA_CONTENT, task.getContent());
                intent.putExtra(StringHelper.EXTRA_HOUR, task.getHour());
                intent.putExtra(StringHelper.EXTRA_MINUTE, task.getMinute());

                editLauncher.launch(intent);
            }
        });

        taskAdapter.setOnItemCheckedListener((task, position) -> taskListViewModel.updateTask(user.getUid(), task.getDate(), task, position));

        friendAdapter.setOnItemClickListener((userProfile, position) -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            taskListViewModel.getTasks(userProfile.getUid(), selectedDate);
        });

        taskListViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(TaskListViewModel.class);
        taskListViewModel.getAllTasks().observe(this, tasks -> {
            binding.progressBar.setVisibility(View.GONE);
            taskAdapter.setTasks(tasks);
        });

        profileViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ProfileViewModel.class);
        profileViewModel.getUserProfile().observe(this, profile -> {
            if (profile != null) {
                Glide.with(MainActivity.this)
                        .load(profile.getProfileUri())
                        .into(binding.userProfile);
                binding.userNickName.setText(profile.getNickName());
            } else {
                Glide.with(MainActivity.this)
                        .load(R.drawable.basic_profile)
                        .into(binding.userProfile);
                binding.userNickName.setText(user.getEmail());
            }

        });

        profileViewModel.getUser(user.getUid());

        taskListViewModel.getCurrentUser().observe(this, s -> binding.currentUserTodoList.setText(s + "Todo List"));

        profileViewModel.getAllFriends().observe(this, userProfiles -> friendAdapter.setFriends(userProfiles));

        profileViewModel.getFriends(user.getUid());

        binding.today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                dateLauncher.launch(intent);
            }
        });


        selectedDate = taskListViewModel.getToday();
        binding.today.setText(selectedDate);
        binding.progressBar.setVisibility(View.VISIBLE);
        taskListViewModel.getTasks(user.getUid(), selectedDate);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Task task = taskAdapter.getTaskAt(viewHolder.getAdapterPosition());
                if (task.getUid().equals(user.getUid())) {
                    taskListViewModel.deleteTask(user.getUid(), task.getDate(), task, position);
                }

            }
        }).attachToRecyclerView(binding.mainTodosRecyclerView);

        binding.addTodoButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            intent.putExtra(StringHelper.EXTRA_DATE, selectedDate);
            addLauncher.launch(intent);
        });

        binding.friendSearchButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FriendSearchActivity.class);
            intent.putExtra("uid", user.getUid());
            addFirendLauncher.launch(intent);
            //startActivity(intent);
        });

        binding.setting.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("uid", user.getUid());
            intent.putExtra("email", user.getEmail());
            startActivity(intent);
        });

    }
}