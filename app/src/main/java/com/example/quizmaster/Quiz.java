package com.example.quizmaster;

import java.util.ArrayList;

public class Quiz {
    private long id;
    private String title;
    private ArrayList<Question> questions;
    private int pos = 0;

    public Quiz(String title) {
        this.id = -1;
        this.title = title;
        this.questions = new ArrayList<>();
        this.pos = 0;
    }

    public int getId() {
        return (int) id;
    }

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
}
