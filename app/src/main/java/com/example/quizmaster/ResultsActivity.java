package com.example.quizmaster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.backend.DbHelper;
import com.example.quizmaster.databinding.ActivityResultsBinding;
import com.example.quizmaster.ui.QuizAdapter;
import com.example.quizmaster.ui.ResultQuestionsAdapter;


public class ResultsActivity extends AppCompatActivity {

    private RecyclerView resultsView;
    private ResultQuestionsAdapter adapter;
    private long ResultId;
    private QuizResult quizResult;
    private DbHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WindowInsetsController controller = getWindow().getDecorView().getWindowInsetsController();
        if (controller != null) {
            controller.hide(WindowInsets.Type.navigationBars());
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityResultsBinding binding = ActivityResultsBinding.inflate(getLayoutInflater());
        View root = binding.getRoot();
        setContentView(root);  // Use the root view from your binding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new DbHelper(this);
        resultsView = root.findViewById(R.id.recycler_view_question_results);
        ResultId = getIntent().getLongExtra("ResultId", -1);
        quizResult = dbHelper.getQuizResultById(ResultId);
        setUpRecyclerView();

        Button restartQuizButton = findViewById(R.id.restart_quiz);
        Button resultsHomeButton = findViewById(R.id.results_home);

        restartQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuizTakingActivity.class);
                intent.putExtra("QuizId", (int) quizResult.getQuizId());
                v.getContext().startActivity(intent);
                finish();
            }
        });

        resultsHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private void setUpRecyclerView() {
        adapter = new ResultQuestionsAdapter(quizResult.getResults(), dbHelper);
        resultsView.setAdapter(adapter);
        resultsView.setLayoutManager(new LinearLayoutManager(resultsView.getContext()));
    }
}