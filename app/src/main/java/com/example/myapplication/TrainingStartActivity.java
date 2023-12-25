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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.databinding.ActivityTrainingStartBinding;
import com.example.myapplication.exercise.Exercise;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TrainingStartActivity extends AppCompatActivity {
    private ActivityTrainingStartBinding binding;
    private DatabaseAdapter adapter;
    TextView timeView;
    ProgressBar timerProgressCircular;
    private long trainingId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainingStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TrainingTimerViewModel timerViewModel = new ViewModelProvider(this).get(TrainingTimerViewModel.class);

        timeView = binding.timeView;
        timerProgressCircular = binding.timerProgressCircular;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            trainingId = extras.getLong("id");
        }


        adapter = new DatabaseAdapter(this);
        adapter.open();
        ArrayList<Exercise> exercises = adapter.getExercisesByTrainingId(trainingId);
        adapter.close();
        ArrayList<Long> times = new ArrayList<>();
        for (int i = 0; i < exercises.size(); i++) {
            for (int set = 0; set < exercises.get(i).getSets(); set++) {
                times.add((long) exercises.get(i).getTimePerSet() * 1000);
                times.add((long) exercises.get(i).getRelaxTimeBetweenSets() * 1000);
            }
        }
        times.remove(times.size() - 1);
        timerViewModel.mArrayList = times;

        timerViewModel.getValue().observe(this, value -> {
            timeView.setText(value);
            System.out.println(timerViewModel.progress.getValue());
            timerProgressCircular.setProgress(timerViewModel.progress.getValue(), true);
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
            timerViewModel.resetTimer();
        });
    }
}