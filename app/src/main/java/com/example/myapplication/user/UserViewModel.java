package com.example.myapplication.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<User> loggedUser = new MutableLiveData<User>();
    public void loggedUser(User user) {
        loggedUser.setValue(user);
    }
    public LiveData<User> getLoggedUser() {
        return loggedUser;
    }
}
