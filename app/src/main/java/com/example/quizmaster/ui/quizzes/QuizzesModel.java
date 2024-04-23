package com.example.quizmaster.ui.quizzes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class QuizzesModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public QuizzesModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is quizzes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}