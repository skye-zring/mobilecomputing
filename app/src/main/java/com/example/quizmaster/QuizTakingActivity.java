package com.example.quizmaster;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizmaster.backend.DbHelper;

public class QuizTakingActivity extends AppCompatActivity {

    private TextView quizTitle;
    private TextView questionField;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;
    private Button stopQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_taking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the quiz ID from the intent
        // Assuming you are in the receiving activity
        int quizId = getIntent().getIntExtra("QuizId", -1);


        // Fetch the quiz data using the quiz ID
        // This will depend on how you are storing your quizzes
        Quiz quiz = fetchQuiz(quizId);

        // Get references to the UI elements
        quizTitle = findViewById(R.id.quizTitle);
        questionField = findViewById(R.id.questionField);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        stopQuiz = findViewById(R.id.stopQuiz);

        // Populate the UI elements with the quiz data
        quizTitle.setText(quiz.getTitle());
        questionField.setText(quiz.getCurrentQuestion().getText());
        answer1.setText(quiz.getCurrentQuestion().getCorrectAnswer());
        answer2.setText(quiz.getCurrentQuestion().getWrongAnswerA());
        answer3.setText(quiz.getCurrentQuestion().getWrongAnswerB());
        answer4.setText(quiz.getCurrentQuestion().getWrongAnswerC());

        // Set up the stop quiz button
        stopQuiz.setOnClickListener(v -> {
            // Handle stopping the quiz
        });
    }

    private Quiz fetchQuiz(int quizId) {
        // Get a DbHelper instance
        DbHelper dbHelper = new DbHelper(this);

        // Fetch the quiz data using the quiz ID
        Quiz quiz = dbHelper.getQuiz(quizId);

        return quiz;
    }

}
