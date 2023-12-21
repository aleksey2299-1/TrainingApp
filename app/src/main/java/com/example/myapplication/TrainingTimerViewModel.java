package com.example.myapplication;

import android.os.CountDownTimer;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TrainingTimerViewModel extends ViewModel {
    private Thread thread;
    private CountDownTimer countDownTimer;
    public MutableLiveData<Boolean> isStarted = new MutableLiveData<Boolean>(false);
    private MutableLiveData<Long> timeLeftMillis;
    private MutableLiveData<String> value;

    public LiveData<String> getValue() {
        if (value == null) {
            value = new MutableLiveData<String>("00:00");
        }
        return value;
    }

    public void startTimer() {
        if (timeLeftMillis == null) {
            timeLeftMillis = new MutableLiveData<Long>(new Long(6000));
        }
        countDownTimer = new CountDownTimer(timeLeftMillis.getValue(), 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis.postValue(millisUntilFinished);
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                isStarted.postValue(false);
            }
        }.start();

        isStarted.postValue(true);
    }

    public void resetTimer(int time) {
        timeLeftMillis.postValue(new Long(time));
    }

    public void pauseTimer() {
        countDownTimer.cancel();
        isStarted.postValue(false);
    }

    public void updateCountDownText() {
        int minutes = (int) (timeLeftMillis.getValue() / 1000) / 60;
        int seconds = (int) (timeLeftMillis.getValue() / 1000) % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        value.postValue(formattedTime);
    }
}
