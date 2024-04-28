package com.example.quizmaster;

import java.util.ArrayList;

public class QuizResult {

    private long id;
    private ArrayList<QuestionResult> results;
    private long quizId;

    public QuizResult(long quizId){
        this.id = -1;
        this.results =  new ArrayList<>();
        this.quizId = quizId;
    }

    public void addResult(QuestionResult result){this.results.add(result);}
    public void setId(long id){this.id = id;}
    public long getId(){return this.id;}
    public ArrayList<QuestionResult> getResults(){return this.results;}
    public long getQuizId(){return this.quizId;}
    public void setQuizId(long quizId){this.quizId = quizId;}
}
