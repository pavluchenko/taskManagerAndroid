package com.example.taskmanager.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.R;
import com.example.taskmanager.databinding.ActivityCalendarBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarActivity extends AppCompatActivity {

    public final static String TAG = "CalendarActivity";

    private ActivityCalendarBinding binding;

    private String date = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_up, R.anim.none);

        binding.calendarView.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            String year = String.valueOf(i);
            String month = String.valueOf(i1 + 1);
            String day = String.valueOf(i2);
            if (i1 < 10) {
                month = "0" + (i1 + 1);
            }

            date = year + "-" + month + "-" + day;

        });


        binding.backButton.setOnClickListener(view -> {

            if (date == null || date.equals("")) {
                LocalDate now = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String today = now.format(formatter);

                date = today;
            }

            Intent data = new Intent();
            data.putExtra("date", date);
            Log.d(TAG, date);

            setResult(RESULT_OK, data);
            finish();
            overridePendingTransition(R.anim.none, R.anim.fadeout_up);
        });


    }
}