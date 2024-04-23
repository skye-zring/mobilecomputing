package com.example.quizmaster.ui.addquiz;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.quizmaster.Question;
import com.example.quizmaster.Quiz;
import com.example.quizmaster.R;
import com.example.quizmaster.backend.DbHelper;
import com.example.quizmaster.databinding.FragmentAddquizBinding;

public class AddQuizFragment extends Fragment {

    private FragmentAddquizBinding binding;
    private LinearLayout parentLayout; // Parent layout where forms will be added

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddQuizViewModel addQuizViewModel =
                new ViewModelProvider(this).get(AddQuizViewModel.class);

        binding = FragmentAddquizBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Find the parent layout where forms will be added
        parentLayout = root.findViewById(R.id.linear_layout);

        // Find the "Add Question" button and set its click listener
        Button addQuestionButton = root.findViewById(R.id.add_question);
        addQuestionButton.setOnClickListener(v -> addQuestionForm(inflater, container));

        Button saveQuizButton = root.findViewById(R.id.save_quiz);
        saveQuizButton.setOnClickListener(v -> saveQuizToDatabase());


        return root;


    }

    // Method to add the question form
    private void addQuestionForm(LayoutInflater inflater, ViewGroup container) {
        // Inflate the question_form.xml layout
        View questionFormView = inflater.inflate(R.layout.question_form, parentLayout, false);

        // Find the delete button in the question form
        Button deleteButton = questionFormView.findViewById(R.id.delete_question);



        // Set click listener for the delete button
        deleteButton.setOnClickListener(v -> {
            // Remove the question form from the parent layout
            parentLayout.removeView(questionFormView);
        });

        // Add the inflated view to the parent layout
        parentLayout.addView(questionFormView);
    }

    // Method to save the quiz and questions to the database
    // Method to save the quiz and questions to the database using DbHelper
    private void saveQuizToDatabase() {
        DbHelper dbHelper = new DbHelper(requireContext());
        String quizTitle = binding.quizTitle.getText().toString();
        Quiz quiz = new Quiz(quizTitle);

        long quizid = dbHelper.insertQuiz(quiz);

        quiz.setId(quizid);

        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            View childView = parentLayout.getChildAt(i);
            if (childView instanceof LinearLayout) {
                EditText questionText = childView.findViewById(R.id.question_text);
                EditText correctAnswer = childView.findViewById(R.id.edit_correct_answer);
                EditText wrong1 = childView.findViewById(R.id.edit_wrong1);
                EditText wrong2 = childView.findViewById(R.id.edit_wrong2);
                EditText wrong3 = childView.findViewById(R.id.edit_wrong3);

                // Create a new Question object and associate it with the quiz using the generated quizId
                Question question = new Question(
                        questionText.getText().toString(),
                        correctAnswer.getText().toString(),
                        wrong1.getText().toString(),
                        wrong2.getText().toString(),
                        wrong3.getText().toString(),
                        quizid
                );

                long questionid = dbHelper.insertQuestion(question);

                question.setId(questionid);

            }
        }

    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
