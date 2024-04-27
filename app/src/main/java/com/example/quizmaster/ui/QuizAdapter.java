package com.example.quizmaster.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.Question;
import com.example.quizmaster.Quiz;
import com.example.quizmaster.R;
import com.example.quizmaster.backend.DbHelper;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {
    private List<Quiz> quizzes;

    public QuizAdapter(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    @Override
    public QuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_card, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuizViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Quiz quiz = quizzes.get(position);
        holder.quizTitle.setText(quiz.getTitle());
        holder.numberOfQuestions.setText(String.valueOf(quiz.getQuestions().size()));

        holder.deleteQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper dbHelper = new DbHelper(v.getContext());
                dbHelper.deleteQuizAndQuestions(quiz.getId());
                int currentPosition = holder.getAdapterPosition();
                quizzes.remove(currentPosition);
                notifyItemRemoved(currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizzes.size();
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder {
        public TextView quizTitle;
        public TextView numberOfQuestions;
        public Button startQuiz;
        public Button deleteQuiz;

        public QuizViewHolder(View itemView) {
            super(itemView);
            quizTitle = itemView.findViewById(R.id.quiz_title);
            numberOfQuestions = itemView.findViewById(R.id.number_of_questions);
            startQuiz = itemView.findViewById(R.id.start_quiz);
            deleteQuiz = itemView.findViewById(R.id.delete_quiz);
        }
    }
}
