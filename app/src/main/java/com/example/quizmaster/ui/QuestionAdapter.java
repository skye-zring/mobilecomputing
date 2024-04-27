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


import androidx.recyclerview.widget.RecyclerView;

import com.example.quizmaster.Question;
import com.example.quizmaster.R;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private List<Question> questions;

    public QuestionAdapter(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_form, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Question question = questions.get(position);
        holder.questionText.setText(question.getText());
        holder.correctAnswer.setText(question.getCorrectAnswer());
        holder.wrongAnswerA.setText(question.getWrongAnswerA());
        holder.wrongAnswerB.setText(question.getWrongAnswerB());
        holder.wrongAnswerC.setText(question.getWrongAnswerC());
        // Set an OnClickListener for the deleteQuestion button
        holder.deleteQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete Question")
                        .setMessage("Are you sure you want to delete this question?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                questions.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, questions.size());
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .setIcon(R.drawable.ic_alert)
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        public EditText questionText;
        public EditText correctAnswer;
        public EditText wrongAnswerA;
        public EditText wrongAnswerB;
        public EditText wrongAnswerC;
        public Button deleteQuestion;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.question_text);
            correctAnswer = itemView.findViewById(R.id.edit_correct_answer);
            wrongAnswerA = itemView.findViewById(R.id.edit_wrong1);
            wrongAnswerB = itemView.findViewById(R.id.edit_wrong2);
            wrongAnswerC = itemView.findViewById(R.id.edit_wrong3);
            deleteQuestion = itemView.findViewById(R.id.delete_question);

            questionText.addTextChangedListener(new CustomTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    questions.get(getAdapterPosition()).setText(s.toString());
                    Log.d("Question:",questions.get(getAdapterPosition()).getText());
                }
            });

            correctAnswer.addTextChangedListener(new CustomTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    questions.get(getAdapterPosition()).setCorrectAnswer(s.toString());
                }
            });

            wrongAnswerA.addTextChangedListener(new CustomTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    questions.get(getAdapterPosition()).setWrongAnswerA(s.toString());
                }
            });

            wrongAnswerB.addTextChangedListener(new CustomTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    questions.get(getAdapterPosition()).setWrongAnswerB(s.toString());
                }
            });

            wrongAnswerC.addTextChangedListener(new CustomTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    questions.get(getAdapterPosition()).setWrongAnswerC(s.toString());
                }
            });
        }

        private abstract class CustomTextWatcher implements TextWatcher {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        }
    }
}
