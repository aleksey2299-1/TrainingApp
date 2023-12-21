package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.myapplication.databinding.ActivityTrainingStartBinding;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TrainingStartActivity extends AppCompatActivity {
    private ActivityTrainingStartBinding binding;
    TextView timeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainingStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TrainingTimerViewModel timerViewModel = new ViewModelProvider(this).get(TrainingTimerViewModel.class);

        timeView = binding.timeView;
        timerViewModel.getValue().observe(this, value -> {
            timeView.setText(value);
        });

        ImageButton startButton = binding.startButton;
        ImageButton stopButton = binding.stopButton;
        ImageButton nextButton = binding.nextButton;
        ImageButton prevButton = binding.prevButton;
        startButton.setOnClickListener( v -> {
            if (timerViewModel.isStarted.getValue()){
                timerViewModel.pauseTimer();
                startButton.setImageResource(R.drawable.baseline_play_circle_24);
                stopButton.setVisibility(View.VISIBLE);
            } else{
                timerViewModel.startTimer();
                startButton.setImageResource(R.drawable.baseline_pause_circle_24);
                stopButton.setVisibility(View.INVISIBLE);
            }
        });
        stopButton.setOnClickListener( v -> {
            timerViewModel.resetTimer(90000);
        });
    }
}