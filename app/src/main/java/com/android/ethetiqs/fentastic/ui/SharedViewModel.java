package com.android.ethetiqs.fentastic.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<String> mInputDataId;

    public SharedViewModel() {
        mInputDataId = new MutableLiveData<>();
    }

    public LiveData<String> getInputDataId() {
        return mInputDataId;
    }

    public void setInputDataId(String id) {
        mInputDataId.setValue(id);
    }

}