package com.example.myapplication.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LiveData<String> getText() {
        if(mText==null){
            mText = new MutableLiveData<String>("This is search fragment");
        }
        return mText;
    }
    public void setText(String value) {
        mText = new MutableLiveData<String>(value);
    }
}
