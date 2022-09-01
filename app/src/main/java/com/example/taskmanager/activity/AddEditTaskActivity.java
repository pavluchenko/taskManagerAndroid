package com.example.taskmanager.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.helper.StringHelper;
import com.example.taskmanager.R;
import com.example.taskmanager.databinding.ActivityAddEditTaskBinding;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String TAG = "EditTaskActivity";

    private String hour;
    private String minute;

    private ActivityAddEditTaskBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        Intent intent = getIntent();

        if (intent.hasExtra(StringHelper.EXTRA_KEY)) {
            binding.roomName.setText("Task Manager");
            binding.date.setText(intent.getStringExtra(StringHelper.EXTRA_DATE));
            binding.title.setText(intent.getStringExtra(StringHelper.EXTRA_TITLE));
            binding.content.setText(intent.getStringExtra(StringHelper.EXTRA_CONTENT));
            String getHour = intent.getStringExtra(StringHelper.EXTRA_HOUR);
            String getMinute = intent.getStringExtra(StringHelper.EXTRA_MINUTE);

            if (Build.VERSION.SDK_INT < 23) {
                binding.time.setCurrentHour(Integer.parseInt(getHour));
                binding.time.setCurrentMinute(Integer.parseInt(getMinute));

                Log.d(TAG + "1", getHour);
                Log.d(TAG, getMinute);
            } else {
                binding.time.setHour(Integer.parseInt(getHour));
                binding.time.setMinute(Integer.parseInt(getMinute));

                Log.d(TAG + "2", getHour);
                Log.d(TAG, getMinute);
            }


        } else {
            binding.roomName.setText("Task Manager");
            binding.date.setText(intent.getStringExtra(StringHelper.EXTRA_DATE));
        }

        binding.cancelButton.setOnClickListener(view -> {
            finish();
            overridePendingTransition(R.anim.none, R.anim.fadeout_left);
        });

        binding.okButton.setOnClickListener(view -> {
            String date = binding.date.getText().toString();
            String title = binding.title.getText().toString();
            String content = binding.content.getText().toString();

            if (Build.VERSION.SDK_INT < 23) {
                hour = String.valueOf(binding.time.getCurrentHour());
                minute = String.valueOf(binding.time.getCurrentMinute());
            } else {
                hour = String.valueOf(binding.time.getHour());
                minute = String.valueOf(binding.time.getMinute());
            }


            Intent data = new Intent();
            data.putExtra(StringHelper.EXTRA_DATE, date);
            data.putExtra(StringHelper.EXTRA_TITLE, title);
            data.putExtra(StringHelper.EXTRA_CONTENT, content);
            data.putExtra(StringHelper.EXTRA_HOUR, hour);
            data.putExtra(StringHelper.EXTRA_MINUTE, minute);
            Log.d(TAG, hour + " " + minute);

            String key = getIntent().getStringExtra(StringHelper.EXTRA_KEY);
            String email = getIntent().getStringExtra(StringHelper.EXTRA_EMAIL);
            int position = getIntent().getIntExtra(StringHelper.EXTRA_POSITION, -1);


            if (key != null && email != null && position != -1) {
                Log.d(TAG, key);
                Log.d(TAG, email);
                Log.d(TAG, String.valueOf(position));
                data.putExtra(StringHelper.EXTRA_KEY, key);
                data.putExtra(StringHelper.EXTRA_EMAIL, email);
                data.putExtra(StringHelper.EXTRA_POSITION, position);
            }


            setResult(RESULT_OK, data);
            finish();
            overridePendingTransition(R.anim.none, R.anim.fadeout_left);
        });

    }
}