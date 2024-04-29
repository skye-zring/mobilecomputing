package com.example.quizmaster;

import java.util.ArrayList;

// class for storing results from a quiz
public class QuizResult {

    private long id;
    private ArrayList<QuestionResult> results;
    private long quizId;

    public QuizResult(long quizId){
        //id is updated when result is inserted into db
        this.id = -1;
        this.results =  new ArrayList<>();
        this.quizId = quizId;
    }

    //add question result to the list
    public void addResult(QuestionResult result){this.results.add(result);}
    public void setId(long id){this.id = id;}
    public long getId(){return this.id;}
    public ArrayList<QuestionResult> getResults(){return this.results;}
    public long getQuizId(){return this.quizId;}
    public void setQuizId(long quizId){this.quizId = quizId;}
}
