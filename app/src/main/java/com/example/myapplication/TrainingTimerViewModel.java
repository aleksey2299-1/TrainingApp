package com.example.myapplication;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class TrainingTimerViewModel extends ViewModel {
    public ArrayList<Long> mArrayList;
    public MutableLiveData<Boolean> isStarted = new MutableLiveData<Boolean>(false);
    private MutableLiveData<Long> timeLeftMillis = new MutableLiveData<>(-1L);
    MutableLiveData<Integer> progress = new MutableLiveData<>(100);
    private MutableLiveData<String> value;
    public int index = 0;
    private long time;
    private Handler timerHandler;
    private Runnable timerRunnable;

    public LiveData<String> getValue() {
        if (value == null) {
            long millis = mArrayList.get(index);
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            value = new MutableLiveData<>(String.format("%02d:%02d", minutes, seconds));
        }
        return value;
    }

    public void startTimer() {

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (timeLeftMillis.getValue() < 0) {
                    time = mArrayList.get(index);
                    timeLeftMillis = new MutableLiveData<>(time);
                    index++;
                }
                long millis = timeLeftMillis.getValue();
                timeLeftMillis.postValue(millis - 1000);
                float m = (float) millis;
                float t = (float) time;
                progress = new MutableLiveData<>((int) (m / t * 100));
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                value.postValue(String.format("%02d:%02d", minutes, seconds));
                if (index < mArrayList.size()||timeLeftMillis.getValue() > 0) {
                    timerHandler.postDelayed(this, 1000);
                } else {
                    progress = new MutableLiveData<>(100);
                    value.postValue("Done!");
                    isStarted.postValue(false);
                }
            }
        };
        timerHandler = new Handler(Looper.getMainLooper());
        timerHandler.postDelayed(timerRunnable, 0);
        isStarted.postValue(true);
    }

    public void pauseTimer() {
        timerHandler.removeCallbacks(timerRunnable);
        isStarted.postValue(false);
    }

    public void resetFinishedTimer() {
        pauseTimer();
        timeLeftMillis.postValue(-1L);
        progress.postValue(100);
        index = 0;
    }

    public void resetTimer() {
        resetFinishedTimer();
        long millis = mArrayList.get(index);
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        value.postValue(String.format("%02d:%02d", minutes, seconds));
    }
}
