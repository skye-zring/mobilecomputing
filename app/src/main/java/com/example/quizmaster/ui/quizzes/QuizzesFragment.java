package com.example.quizmaster.ui.quizzes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.Quiz;
import com.example.quizmaster.R;
import com.example.quizmaster.backend.DbHelper;
import com.example.quizmaster.databinding.FragmentQuizzesBinding;
import com.example.quizmaster.ui.QuestionAdapter;
import com.example.quizmaster.ui.QuizAdapter;

import java.util.List;

public class QuizzesFragment extends Fragment {

    private FragmentQuizzesBinding binding;
    private RecyclerView quizzesView;
    private QuizAdapter adapter;
    private List<Quiz> quizzes;
    private DbHelper dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentQuizzesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //setting dbhelper and getting all quizes
        dbHelper = new DbHelper(getContext());
        quizzes = dbHelper.getAllQuizzes();
        //recycler view setup
        quizzesView = root.findViewById(R.id.recycler_view_quizzes);
        setUpRecyclerView();

        return root;
    }

    //recycler view adapter
    private void setUpRecyclerView() {
        adapter = new QuizAdapter(quizzes);
        quizzesView.setAdapter(adapter);
        quizzesView.setLayoutManager(new LinearLayoutManager(quizzesView.getContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbHelper.close();
        binding = null;
    }
}

