package com.example.quizmaster.ui.quizzes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.quizmaster.databinding.FragmentQuizzesBinding;

public class QuizzesFragment extends Fragment {

    private FragmentQuizzesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        QuizzesModel quizzesModel =
                new ViewModelProvider(this).get(QuizzesModel.class);

        binding = FragmentQuizzesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textQuizzes;
        quizzesModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}