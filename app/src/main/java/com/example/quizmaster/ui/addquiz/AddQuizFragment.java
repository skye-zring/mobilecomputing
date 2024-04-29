package com.example.quizmaster.ui.addquiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.Question;
import com.example.quizmaster.Quiz;
import com.example.quizmaster.R;
import com.example.quizmaster.backend.DbHelper;
import com.example.quizmaster.databinding.FragmentAddquizBinding;
import com.example.quizmaster.ui.QuestionAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddQuizFragment extends Fragment {

    private FragmentAddquizBinding binding;
    private QuestionAdapter adapter;
    private RecyclerView questionsView;
    private List<Question> questions = new ArrayList<>();
    private LinearLayout parentLayout; // Parent layout where forms will be added

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddquizBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //setting up recycelr view
        questionsView = root.findViewById(R.id.recycler_view_questions);
        setUpRecyclerView();

        // Find the add question button and set its click listener
        Button addQuestionButton = root.findViewById(R.id.add_question);
        addQuestionButton.setOnClickListener(v -> addQuestionForm(inflater, container));

        // find the save button and set its click listener
        Button saveQuizButton = root.findViewById(R.id.save_quiz);
        saveQuizButton.setOnClickListener(v -> saveQuizToDatabase());


        return root;


    }

    // adding a new question form
    private void addQuestionForm(LayoutInflater inflater, ViewGroup container) {
        // create a new question object
        Question newQuestion = new Question();
        // add the new question to the questions list
        questions.add(newQuestion);

        // notify the adapter that an item has been inserted
        adapter.notifyItemInserted(questions.size() - 1);

        // scroll to the position of the new item
        questionsView.scrollToPosition(questions.size() - 1);
    }

    //setting up lsit of quiz forms
    private void setUpRecyclerView() {
        adapter = new QuestionAdapter(questions);
        questionsView.setAdapter(adapter);
        questionsView.setLayoutManager(new LinearLayoutManager(questionsView.getContext()));
    }

    //saving everythong to db with error handling
    private void saveQuizToDatabase() {
        DbHelper dbHelper = new DbHelper(requireContext());
        String quizTitle = binding.quizTitle.getText().toString();
        Quiz quiz = new Quiz(quizTitle);

        if (quizTitle.trim().isEmpty()){
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("You must provide a quiz title.")
                    .setTitle("Error")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        if (questions.isEmpty()){
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(requireContext());
            builder.setMessage("You must have at least one question.")
                    .setTitle("Error")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        for (Question question : questions) {
            // dheck if any fields are empty
            if (question.getText().trim().isEmpty() ||
                    question.getCorrectAnswer().trim().isEmpty() ||
                    question.getWrongAnswerA().trim().isEmpty() ||
                    question.getWrongAnswerB().trim().isEmpty() ||
                    question.getWrongAnswerC().trim().isEmpty()) {
                // raise an alert and cancel the save operation if any field is empty
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(requireContext());
                builder.setMessage("All question fields must be filled.")
                        .setTitle("Error")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }
        }

        long quizid = dbHelper.insertQuiz(quiz);
        quiz.setId(quizid);

        for (Question question : questions) {
            // create a new question object and associate it with quiz using the generated quizId
            question.setQuizId(quizid);
            Question newQuestion = new Question(
                    question.getText(),
                    question.getCorrectAnswer(),
                    question.getWrongAnswerA(),
                    question.getWrongAnswerB(),
                    question.getWrongAnswerC(),
                    quizid
            );

            long questionid = dbHelper.insertQuestion(newQuestion);
            question.setId(questionid);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Quiz saved successfully.")
                .setTitle("Success")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Go to QuizzesFragment after the user acknowledges the alert
                        NavHostFragment.findNavController(AddQuizFragment.this)
                                .navigate(R.id.action_AddQuizFragment_to_QuizzesFragment);
                    }
                })
                .show();
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
