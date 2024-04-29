package com.example.quizmaster;

import java.util.ArrayList;

// class for storing a quiz
public class Quiz {
    private long id;
    private String title;
    private ArrayList<Question> questions;
    private int pos;


    public Quiz(String title) {
        // set id to -1, it will be updated when quiz is inserted into db
        this.id = -1;
        this.title = title;
        this.questions = new ArrayList<>();
        // used for quiz taking to go through questions
        this.pos = -1;
    }

    public int getId() {
        return (int) id;
    }

    //gets the question at pos in the questions list
    public Question getCurrentQuestion(){
        return questions.get(pos);
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public int getAmountOfQuestions(){
        return questions.size();
    }

    public int getPos(){
        return pos;
    }

    // gets next question in the list 
    public Question getNextQuestion(){
        pos += 1;
        if (pos != getAmountOfQuestions()){
            return questions.get(pos);
        }
        return null;
    }
}
