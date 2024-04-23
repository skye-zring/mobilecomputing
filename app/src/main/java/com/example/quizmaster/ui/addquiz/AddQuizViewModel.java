package com.example.quizmaster.ui.addquiz;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddQuizViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AddQuizViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is addquiz fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}