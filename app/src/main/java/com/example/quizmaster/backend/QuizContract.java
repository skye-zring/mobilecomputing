package com.example.quizmaster.backend;
import android.provider.BaseColumns;

public final class QuizContract {
    // Private constructor to prevent instantiation of the class
    private QuizContract() {}

    // Inner class that defines the table contents for quizzes
    public static class QuizEntry implements BaseColumns {
        // Table name for quizzes
        public static final String TABLE_NAME = "quizzes";

        // Column names for quizzes
        public static final String COLUMN_TITLE = "title";
    }

    // Inner class that defines the table contents for questions
    public static class QuestionEntry implements BaseColumns {
        // Table name for questions
        public static final String TABLE_NAME = "questions";

        // Column names for questions
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_CORRECT_ANSWER = "correct_answer";
        public static final String COLUMN_WRONG_ANSWER_A = "wrong_answer_a";
        public static final String COLUMN_WRONG_ANSWER_B = "wrong_answer_b";
        public static final String COLUMN_WRONG_ANSWER_C = "wrong_answer_c";
        public static final String COLUMN_QUIZ_ID = "quiz_id";
    }
}

