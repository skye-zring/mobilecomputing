package com.example.quizmaster.backend;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizmaster.Question;
import com.example.quizmaster.Quiz;
import com.example.quizmaster.ui.addquiz.AddQuizFragment;

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

}
