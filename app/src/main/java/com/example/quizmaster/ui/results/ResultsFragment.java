package com.example.quizmaster.ui.results;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.Quiz;
import com.example.quizmaster.QuizResult;
import com.example.quizmaster.R;
import com.example.quizmaster.backend.DbHelper;
import com.example.quizmaster.databinding.FragmentQuizzesBinding;
import com.example.quizmaster.databinding.FragmentResultsBinding;
import com.example.quizmaster.ui.QuizAdapter;
import com.example.quizmaster.ui.ResultQuizAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultsFragment extends Fragment {

    private FragmentResultsBinding binding;

    private RecyclerView resultsView;
    private ResultQuizAdapter adapter;
    private List<QuizResult> results;
    private DbHelper dbHelper;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentResultsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dbHelper = new DbHelper(getContext());
        results = dbHelper.getAllQuizResults();
        resultsView = root.findViewById(R.id.recycler_view_results);
        setUpRecyclerView();

        return root;
    }

    private void setUpRecyclerView() {
        List<QuizResult> reversedResults = new ArrayList<>(results);
        Collections.reverse(reversedResults);
        adapter = new ResultQuizAdapter(reversedResults, dbHelper);
        resultsView.setAdapter(adapter);
        resultsView.setLayoutManager(new LinearLayoutManager(resultsView.getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbHelper.close();
        binding = null;
    }
}