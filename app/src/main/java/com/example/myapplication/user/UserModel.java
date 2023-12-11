package com.example.myapplication.user;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserModel extends ViewModel {

    public final MutableLiveData<User> mUserLiveData = new MutableLiveData<>();
    /*
    public UserModel() {
        // Моделируют пользовательскую информацию из сети загрузки из сети
        mUserLiveData.postValue(new User(1, "name1"));
    }
    */

    // Моделируют некоторые операции по показателям данных
    public void doSomething(User user) {
        if (user != null) {
            user.setUsername("hey");
            user.setEmail("gg@gg.com");
            mUserLiveData.setValue(user);
        }
    }
}

