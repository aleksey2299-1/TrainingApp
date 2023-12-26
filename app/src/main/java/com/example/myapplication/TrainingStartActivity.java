package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
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
    ImageButton stopButton, startButton;
    Animation moveLeftAnim, moveRightAnim;
    TextView timeView;
    ProgressBar timerProgressCircular;
    private long trainingId = 0;
    @SuppressLint("UseCompatLoadingForDrawables")
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

        startButton = binding.startButton;
        stopButton = binding.stopButton;
        ImageButton nextButton = binding.nextButton;
        ImageButton prevButton = binding.prevButton;

        moveLeftAnim = AnimationUtils.loadAnimation(getApplication(), R.anim.move_left_anim);
        moveRightAnim = AnimationUtils.loadAnimation(getApplication(), R.anim.move_right_anim);

        timerViewModel.getValue().observe(this, value -> {
            if (value.equals("Done!")) {
                startButton.setImageResource(R.drawable.baseline_replay_24);
                hideStopButton();
                timerViewModel.resetFinishedTimer();
            }
            timeView.setText(value);
            if (timerViewModel.index > 1&&timerViewModel.progress.getValue() == 100) {
                if (timerViewModel.index % 2 == 0) {
                    timerProgressCircular.setProgressDrawable(getDrawable(R.drawable.timer_progress_circular_relax));
                } else {
                    timerProgressCircular.setProgressDrawable(getDrawable(R.drawable.timer_progress_circular_active));
                }
            }
            timerProgressCircular.setProgress(timerViewModel.progress.getValue(), true);
        });

        startButton.setOnClickListener( v -> {
            if (timerViewModel.isStarted.getValue()){
                timerViewModel.pauseTimer();
                startButton.setImageResource(R.drawable.baseline_play_circle_24);
            } else{
                timerViewModel.startTimer();
                startButton.setImageResource(R.drawable.baseline_pause_circle_24);
            }
            if (!stopButton.isActivated()) {
                showStopButton();
            }
        });

        stopButton.setOnClickListener( v -> {
            timerProgressCircular.setProgressDrawable(getDrawable(R.drawable.timer_progress_circular_active));
            timerViewModel.resetTimer();
            hideStopButton();
            if (timerViewModel.isStarted.getValue()) {
                startButton.setImageResource(R.drawable.baseline_play_circle_24);
            }
        });
    }

    public void moveLeft(ImageButton imageButton) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageButton.getLayoutParams();
        layoutParams.rightMargin += (int) (imageButton.getWidth() * 0.5);
        imageButton.setLayoutParams(layoutParams);
    }

    public void moveRight(ImageButton imageButton) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) imageButton.getLayoutParams();
        layoutParams.rightMargin -= (int) (imageButton.getWidth() * 0.5);
        imageButton.setLayoutParams(layoutParams);
    }

    public void hideStopButton() {
        startButton.startAnimation(moveRightAnim);
        moveRight(startButton);
        stopButton.startAnimation(moveLeftAnim);
        moveLeft(stopButton);
        stopButton.setActivated(false);
    }

    public void showStopButton() {
        startButton.startAnimation(moveLeftAnim);
        moveLeft(startButton);
        stopButton.startAnimation(moveRightAnim);
        moveRight(stopButton);
        stopButton.setActivated(true);
    }
}