package com.example.quizmaster;

public class QuestionResult {
    private long questionId;
    private long id;
    private String answer;


    public QuestionResult(long questionId, String answer){
        this.questionId = questionId;
        this.id = -1;
        this.answer = answer;
    }

    public long getQuestionId(){
        return questionId;
    }

    public void setQuestionId(long qId){
        this.questionId = qId;
    }

    public String getAnswer(){
        return answer;
    }

    public void setAnswer(String ans){
        this.answer = ans;
    }

    public long getId(){
        return id;
    }

    public void setId(long resultId){
        this.id = resultId;
    }
}
