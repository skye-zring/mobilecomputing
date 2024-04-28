package com.example.quizmaster.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.Question;
import com.example.quizmaster.R;
import com.example.quizmaster.QuestionResult;
import com.example.quizmaster.backend.DbHelper;

import java.util.List;

public class ResultQuestionsAdapter extends RecyclerView.Adapter<ResultQuestionsAdapter.ViewHolder> {

    private List<QuestionResult> questionResults;
    private DbHelper dbHelper;

    public ResultQuestionsAdapter(List<QuestionResult> questionResults, DbHelper dbHelper) {
        this.questionResults = questionResults;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_question_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionResult questionResult = questionResults.get(position);
        Question question = dbHelper.getQuestionById(questionResult.getQuestionId());
        holder.resultQuestion.setText(question.getText());
        holder.providedAnswer.setText(questionResult.getAnswer());
        holder.correctAnswer.setText(question.getCorrectAnswer());
        int borderColor;
        if(question.getCorrectAnswer().equals(questionResult.getAnswer())){
            borderColor = Color.parseColor("#2ECC71"); // Green
        }else{
            borderColor = Color.parseColor("#EC7063"); // Red
        }
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setStroke(8, borderColor);
        borderDrawable.setCornerRadius(4);
        holder.itemView.setBackground(borderDrawable);
    }


    @Override
    public int getItemCount() {
        return questionResults.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView resultQuestion;
        TextView providedAnswer;
        TextView correctAnswer;
        TextView resultStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            resultQuestion = itemView.findViewById(R.id.result_question);
            providedAnswer = itemView.findViewById(R.id.provided_answer);
            correctAnswer = itemView.findViewById(R.id.correct_answer);
        }
    }
}
