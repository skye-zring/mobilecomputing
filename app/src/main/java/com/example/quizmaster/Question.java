package com.example.quizmaster;

//class for storing questions
public class Question {
    private long id;
    private String text;
    private String correctAnswer;
    private String wrongAnswerA;
    private String wrongAnswerB;
    private String wrongAnswerC;
    private long quizId;

    public Question(String text, String correctAnswer, String wrongAnswerA, String wrongAnswerB, String wrongAnswerC, long quizId) {
        // id is updated when question is inserted into the db
        this.id = -1;
        this.text = text;
        this.correctAnswer = correctAnswer;
        this.wrongAnswerA = wrongAnswerA;
        this.wrongAnswerB = wrongAnswerB;
        this.wrongAnswerC = wrongAnswerC;
        this.quizId = quizId;
    }

    //second constructor for making blank question
    public Question() {
        this.id = -1;
        this.text = "";
        this.correctAnswer = "";
        this.wrongAnswerA = "";
        this.wrongAnswerB = "";
        this.wrongAnswerC = "";
        this.quizId = -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getWrongAnswerA() {
        return wrongAnswerA;
    }

    public void setWrongAnswerA(String wrongAnswerA) {
        this.wrongAnswerA = wrongAnswerA;
    }

    public String getWrongAnswerB() {
        return wrongAnswerB;
    }

    public void setWrongAnswerB(String wrongAnswerB) {
        this.wrongAnswerB = wrongAnswerB;
    }

    public String getWrongAnswerC() {
        return wrongAnswerC;
    }

    public void setWrongAnswerC(String wrongAnswerC) {
        this.wrongAnswerC = wrongAnswerC;
    }

    public long getQuizId() {
        return quizId;
    }

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }

    public String getQuestionDetails(){
        return String.format("%s %s %s %s %s %s %s", id, text, correctAnswer, wrongAnswerA, wrongAnswerB, wrongAnswerC, quizId);
    }
}
