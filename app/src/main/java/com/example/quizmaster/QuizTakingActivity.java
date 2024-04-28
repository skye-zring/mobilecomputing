package com.example.quizmaster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.quizmaster.backend.DbHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizTakingActivity extends AppCompatActivity {

    private TextView quizTitle;
    private TextView questionField;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;
    private Button stopQuiz;
    private QuizResult quizResult;

    private Question current;
    private Quiz quiz;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DbHelper(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_taking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get the quiz ID from the intent
        int quizId = getIntent().getIntExtra("QuizId", -1);

        // Fetch the quiz data using the quiz I
        quiz = fetchQuiz(quizId);
        quizResult = new QuizResult(quiz.getId());

        // Get references to the UI elements
        quizTitle = findViewById(R.id.quizTitle);
        questionField = findViewById(R.id.questionField);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        stopQuiz = findViewById(R.id.stopQuiz);

        quizTitle.setText(quiz.getTitle());

        DisplayNextQuestion(null);

        // Set up the stop quiz button
        stopQuiz.setOnClickListener(v -> {

            new AlertDialog.Builder(v.getContext())
                    .setTitle("Stop Quiz")
                    .setMessage("Are you sure you want to abandon this quiz?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setIcon(R.drawable.ic_alert)
                    .show();
        });

        View.OnClickListener answerButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                String buttonText = b.getText().toString();
                DisplayNextQuestion(buttonText);
            }
        };

        // Set the same OnClickListener to all the buttons
        answer1.setOnClickListener(answerButtonClickListener);
        answer2.setOnClickListener(answerButtonClickListener);
        answer3.setOnClickListener(answerButtonClickListener);
        answer4.setOnClickListener(answerButtonClickListener);
    }

    private Quiz fetchQuiz(int quizId) {
        // Use the DbHelper instance to fetch the quiz
        Quiz quiz = dbHelper.getQuiz(quizId);
        return quiz;
    }

    private void DisplayNextQuestion(String answer){
        if(answer != null){
            QuestionResult qResult = new QuestionResult(quiz.getCurrentQuestion().getId(), answer);
            Log.d("QuestionId", String.valueOf(quiz.getCurrentQuestion().getId()));
            quizResult.addResult(qResult);
        }
        current = quiz.getNextQuestion();
        if(current == null) {
            saveResultsToDatabase();
            finish();
            return;
        }
        List<String> answers = new ArrayList<>();
        answers.add(current.getCorrectAnswer());
        answers.add(current.getWrongAnswerA());
        answers.add(current.getWrongAnswerB());
        answers.add(current.getWrongAnswerC());
        questionField.setText(current.getText());
        // Shuffle the list
        Collections.shuffle(answers);

        // Set the button texts from the shuffled list
        answer1.setText(answers.get(0));
        answer2.setText(answers.get(1));
        answer3.setText(answers.get(2));
        answer4.setText(answers.get(3));
    }

    private void saveResultsToDatabase(){
        long quizResultId = dbHelper.insertQuizResult(quizResult);
        quizResult.setId(quizResultId);
        for (QuestionResult questionResult : quizResult.getResults()){
            long questionResultId = dbHelper.insertQuestionResult(questionResult, quizResultId);
            questionResult.setId(questionResultId);
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
