package com.example.myapplication;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TrainingTimerViewModel extends ViewModel {
    public ArrayList<Long> mArrayList;
//    private Thread thread;
//    private CountDownTimer countDownTimer;
    public MutableLiveData<Boolean> isStarted = new MutableLiveData<Boolean>(false);
    private MutableLiveData<Long> timeLeftMillis = new MutableLiveData<>(-1L);
    MutableLiveData<Integer> progress = new MutableLiveData<>(100);
    private MutableLiveData<String> value;
    public int index = 0;
    private long time;
    private Handler timerHandler;
    private Runnable timerRunnable;

    public long getTimeLeft() {
        return timeLeftMillis.getValue();
    }

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
                if (timeLeftMillis.getValue()<0) {
                    time = mArrayList.get(index);
                    timeLeftMillis = new MutableLiveData<>(time);
                    index++;
                }
                long millis = timeLeftMillis.getValue();
                timeLeftMillis.postValue(millis - 1000);
                float m = (float) millis;
                float t = (float) time;
                progress = new MutableLiveData<>((int)(m/t * 100));
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                value.postValue(String.format("%02d:%02d", minutes, seconds));

                timerHandler.postDelayed(this, 1000);
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
    public void resetTimer() {
        timeLeftMillis.postValue(-1L);
        progress.postValue(100);
        index = 0;
        long millis = mArrayList.get(index);
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        value.postValue(String.format("%02d:%02d", minutes, seconds));
    }


//    public void startTimer() {
//        timeLeftMillis = new MutableLiveData<Long>(mArrayList.get(index));
//        countDownTimer = new CountDownTimer(timeLeftMillis.getValue(), 1000) {
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                timeLeftMillis.postValue(millisUntilFinished);
//                updateCountDownText();
//            }
//
//            @Override
//            public void onFinish() {
//                timeLeftMillis.postValue(0L);
//                updateCountDownText();
//                isStarted.postValue(false);
//            }
//        };
//        index++;
//        while (index!=mArrayList.size()){
//            countDownTimer.start();
//            wait(mArrayList.get(index));
//            startTimer();
//        }
//
//        isStarted.postValue(true);
//    }
//
//    public void resetTimer(int time) {
//        timeLeftMillis.postValue((long) time);
//    }
//
//    public void pauseTimer() {
//        countDownTimer.cancel();
//        isStarted.postValue(false);
//    }
//
//    public void updateCountDownText() {
//        int minutes = (int) (timeLeftMillis.getValue() / 1000) / 60;
//        int seconds = (int) (timeLeftMillis.getValue() / 1000) % 60;
//        String formattedTime = String.format("%02d:%02d", minutes, seconds);
//        value.postValue(formattedTime);
//    }
}
