package com.example.quizmaster.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.Quiz;
import com.example.quizmaster.QuizResult;
import com.example.quizmaster.QuizTakingActivity;
import com.example.quizmaster.R;
import com.example.quizmaster.ResultsActivity;
import com.example.quizmaster.backend.DbHelper;

import java.util.List;

public class ResultQuizAdapter extends RecyclerView.Adapter<ResultQuizAdapter.ResultQuizViewHolder> {
    private List<QuizResult> results;
    private DbHelper dbHelper;

    public ResultQuizAdapter(List<QuizResult> results, DbHelper dbHelper) {
        this.results = results;
        this.dbHelper = dbHelper;
    }

    @Override
    public ResultQuizViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_quiz_card, parent, false);
        return new ResultQuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultQuizViewHolder holder, @SuppressLint("RecyclerView") int position) {
        QuizResult result = results.get(position);
        holder.resultIdentifier.setText("ResultID: " + result.getId());

        // Get the quiz using the dbHelper and set the title
        Quiz quiz = dbHelper.getQuiz((int) result.getQuizId());
        if (quiz != null) {
            holder.resultTitle.setText(quiz.getTitle());
        }

        holder.viewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ResultsActivity.class);
                intent.putExtra("ResultId", result.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ResultQuizViewHolder extends RecyclerView.ViewHolder {
        public TextView resultIdentifier;
        public TextView resultTitle;
        public Button viewResult;

        public ResultQuizViewHolder(View itemView) {
            super(itemView);
            resultIdentifier = itemView.findViewById(R.id.result_identifer);
            resultTitle = itemView.findViewById(R.id.result_title);
            viewResult = itemView.findViewById(R.id.view_result);
        }
    }
}
