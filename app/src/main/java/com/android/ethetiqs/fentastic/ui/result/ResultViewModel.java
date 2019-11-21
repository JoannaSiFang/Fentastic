package com.android.ethetiqs.fentastic.ui.result;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ResultViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ResultViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is yatusa fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}