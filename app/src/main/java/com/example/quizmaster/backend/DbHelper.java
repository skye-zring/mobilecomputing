package com.example.quizmaster.backend;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizmaster.Question;
import com.example.quizmaster.Quiz;
import com.example.quizmaster.ui.addquiz.AddQuizFragment;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "quiz.db";

    // Table names
    public static final String TABLE_QUIZ = "quiz";
    public static final String TABLE_QUESTION = "question";

    // Common column names
    public static final String COLUMN_ID = "_id";

    // Quiz table columns
    public static final String COLUMN_QUIZ_TITLE = "title";

    // Question table columns
    public static final String COLUMN_QUESTION_TEXT = "text";
    public static final String COLUMN_CORRECT_ANSWER = "correct_answer";
    public static final String COLUMN_WRONG_ANSWER_A = "wrong_answer_a";
    public static final String COLUMN_WRONG_ANSWER_B = "wrong_answer_b";
    public static final String COLUMN_WRONG_ANSWER_C = "wrong_answer_c";
    public static final String COLUMN_QUIZ_ID = "quiz_id";


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating Quiz table
        String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUIZ + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_QUIZ_TITLE + " TEXT"
                + ")";
        db.execSQL(CREATE_QUIZ_TABLE);

        // Creating Question table
        String CREATE_QUESTION_TABLE = "CREATE TABLE " + TABLE_QUESTION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_QUESTION_TEXT + " TEXT,"
                + COLUMN_CORRECT_ANSWER + " TEXT,"
                + COLUMN_WRONG_ANSWER_A + " TEXT,"
                + COLUMN_WRONG_ANSWER_B + " TEXT,"
                + COLUMN_WRONG_ANSWER_C + " TEXT,"
                + COLUMN_QUIZ_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_QUIZ_ID + ") REFERENCES " + TABLE_QUIZ + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_QUESTION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        // Create tables again
        onCreate(db);
    }

    public long insertQuiz(Quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUIZ_TITLE, quiz.getTitle());
        long id = db.insert(TABLE_QUIZ, null, values);
        db.close(); // Closing database connection
        return id;
    }

    public long insertQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_TEXT, question.getText());
        values.put(COLUMN_CORRECT_ANSWER, question.getCorrectAnswer());
        values.put(COLUMN_WRONG_ANSWER_A, question.getWrongAnswerA());
        values.put(COLUMN_WRONG_ANSWER_B, question.getWrongAnswerB());
        values.put(COLUMN_WRONG_ANSWER_C, question.getWrongAnswerC());
        values.put(COLUMN_QUIZ_ID, question.getQuizId());
        long id = db.insert(TABLE_QUESTION, null, values);
        db.close(); // Closing database connection
        return id;
    }

    @SuppressLint("Range")
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        String selectQuizQuery = "SELECT  * FROM " + TABLE_QUIZ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor quizCursor = db.rawQuery(selectQuizQuery, null);

        if (quizCursor.moveToFirst()) {
            do {
                Quiz quiz = new Quiz(quizCursor.getString(quizCursor.getColumnIndex(COLUMN_QUIZ_TITLE)));
                quiz.setId(quizCursor.getInt(quizCursor.getColumnIndex(COLUMN_ID)));

                String selectQuestionQuery = "SELECT  * FROM " + TABLE_QUESTION + " WHERE " + COLUMN_QUIZ_ID + " = " + quiz.getId();
                Cursor questionCursor = db.rawQuery(selectQuestionQuery, null);

                if (questionCursor.moveToFirst()) {
                    do {
                        Question question = new Question();
                        question.setText(questionCursor.getString(questionCursor.getColumnIndex(COLUMN_QUESTION_TEXT)));
                        question.setCorrectAnswer(questionCursor.getString(questionCursor.getColumnIndex(COLUMN_CORRECT_ANSWER)));
                        question.setWrongAnswerA(questionCursor.getString(questionCursor.getColumnIndex(COLUMN_WRONG_ANSWER_A)));
                        question.setWrongAnswerB(questionCursor.getString(questionCursor.getColumnIndex(COLUMN_WRONG_ANSWER_B)));
                        question.setWrongAnswerC(questionCursor.getString(questionCursor.getColumnIndex(COLUMN_WRONG_ANSWER_C)));
                        question.setQuizId(questionCursor.getLong(questionCursor.getColumnIndex(COLUMN_QUIZ_ID)));
                        quiz.addQuestion(question);
                    } while (questionCursor.moveToNext());
                }

                questionCursor.close();
                quizzes.add(quiz);
            } while (quizCursor.moveToNext());
        }

        quizCursor.close();
        db.close();
        return quizzes;
    }


    public void deleteQuizAndQuestions(int quizId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete all questions associated with the quiz
        db.delete(TABLE_QUESTION, COLUMN_QUIZ_ID + " = ?", new String[]{String.valueOf(quizId)});

        // Delete the quiz
        db.delete(TABLE_QUIZ, COLUMN_ID + " = ?", new String[]{String.valueOf(quizId)});

        db.close();
    }

}
