package com.example.quizmaster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
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
        //hiding the built in android nav
        WindowInsetsController controller = getWindow().getDecorView().getWindowInsetsController();
        if (controller != null) {
            controller.hide(WindowInsets.Type.navigationBars());
        }
        super.onCreate(savedInstanceState);
        //creating dbhelper instance to interact with db
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

        //populating the fileds with the first question
        DisplayNextQuestion(null);

        // Set up the stop quiz button
        stopQuiz.setOnClickListener(v -> {

            //asking the user if they are sure
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Stop Quiz")
                    .setMessage("Are you sure you want to abandon this quiz?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        //finishing the intent if they choose to close
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setIcon(R.drawable.ic_alert)
                    .show();
        });

        // onlicks for the answer buttons
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

    //displays the next question from the quiz
    private void DisplayNextQuestion(String answer){
        //add question result for this answer to the quiz result object
        if(answer != null){
            QuestionResult qResult = new QuestionResult(quiz.getCurrentQuestion().getId(), answer);
            quizResult.addResult(qResult);
        }
        //go to next question
        current = quiz.getNextQuestion();
        //if next question is null, it means quiz is finished so save result to db.
        if(current == null) {
            long id = saveResultsToDatabase();
            finish();
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("ResultId", id);
            startActivity(intent);
            return;
        }
        //get the possible answers from the current question
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

    // saving quiz results to db
    private long saveResultsToDatabase(){
        long quizResultId = dbHelper.insertQuizResult(quizResult);
        quizResult.setId(quizResultId);
        for (QuestionResult questionResult : quizResult.getResults()){
            long questionResultId = dbHelper.insertQuestionResult(questionResult, quizResultId);
            questionResult.setId(questionResultId);
        }
        return quizResultId;
    }

    //close dbhelper when done
    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
