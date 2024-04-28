package com.example.quizmaster.backend;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.quizmaster.Question;
import com.example.quizmaster.QuestionResult;
import com.example.quizmaster.Quiz;
import com.example.quizmaster.QuizResult;
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

    // Result tables
    public static final String TABLE_QUIZ_RESULT = "quiz_result";
    public static final String TABLE_QUESTION_RESULT = "question_result";

    // QuizResult table columns
    public static final String COLUMN_QUIZ_ID_REF = "quiz_id_ref";

    // QuestionResult table columns
    public static final String COLUMN_QUESTION_ID_REF = "question_id_ref";
    public static final String COLUMN_QUIZ_RESULT_ID_REF = "quiz_result_id_ref";
    public static final String COLUMN_ANSWER_PROVIDED = "answer_provided";


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

        // Creating QuizResult table
        String CREATE_QUIZ_RESULT_TABLE = "CREATE TABLE " + TABLE_QUIZ_RESULT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_QUIZ_ID_REF + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_QUIZ_ID_REF + ") REFERENCES " + TABLE_QUIZ + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_QUIZ_RESULT_TABLE);

        // Creating QuestionResult table
        String CREATE_QUESTION_RESULT_TABLE = "CREATE TABLE " + TABLE_QUESTION_RESULT + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_QUESTION_ID_REF + " INTEGER,"
                + COLUMN_QUIZ_RESULT_ID_REF + " INTEGER,"
                + COLUMN_ANSWER_PROVIDED + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_QUESTION_ID_REF + ") REFERENCES " + TABLE_QUESTION + "(" + COLUMN_ID + "),"
                + "FOREIGN KEY(" + COLUMN_QUIZ_RESULT_ID_REF + ") REFERENCES " + TABLE_QUIZ_RESULT + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_QUESTION_RESULT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_RESULT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION_RESULT);
        // Create tables again
        onCreate(db);
    }

    public Question getQuestionById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUESTION,
                new String[]{COLUMN_ID, COLUMN_QUESTION_TEXT, COLUMN_CORRECT_ANSWER, COLUMN_WRONG_ANSWER_A, COLUMN_WRONG_ANSWER_B, COLUMN_WRONG_ANSWER_C, COLUMN_QUIZ_ID},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Question question = new Question();
        question.setId(cursor.getLong(0));
        question.setText(cursor.getString(1));
        question.setCorrectAnswer(cursor.getString(2));
        question.setWrongAnswerA(cursor.getString(3));
        question.setWrongAnswerB(cursor.getString(4));
        question.setWrongAnswerC(cursor.getString(5));
        question.setQuizId(cursor.getLong(6));

        cursor.close();
        db.close();
        return question;
    }

    public long insertQuiz(Quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUIZ_TITLE, quiz.getTitle());
        long id = db.insert(TABLE_QUIZ, null, values);
        db.close();
        return id;
    }

    public QuizResult getQuizResultById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the QuizResult table
        Cursor quizResultCursor = db.query(TABLE_QUIZ_RESULT,
                new String[]{COLUMN_ID, COLUMN_QUIZ_ID_REF},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (quizResultCursor != null)
            quizResultCursor.moveToFirst();

        QuizResult quizResult = new QuizResult(
                quizResultCursor.getLong(1));
        quizResult.setId(quizResultCursor.getLong(0));

        Cursor questionResultCursor = db.query(TABLE_QUESTION_RESULT,
                new String[]{COLUMN_ID, COLUMN_QUESTION_ID_REF, COLUMN_ANSWER_PROVIDED},
                COLUMN_QUIZ_RESULT_ID_REF + "=?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (questionResultCursor.moveToFirst()) {
            do {
                QuestionResult questionResult = new QuestionResult(
                        questionResultCursor.getLong(1),
                        questionResultCursor.getString(2));
                questionResult.setId(questionResultCursor.getLong(0));
                quizResult.addResult(questionResult);
            } while (questionResultCursor.moveToNext());
        }

        quizResultCursor.close();
        questionResultCursor.close();
        db.close();
        return quizResult;
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
        db.close();
        return id;
    }

    public long insertQuizResult(QuizResult quizResult) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUIZ_ID_REF, quizResult.getQuizId());
        long id = db.insert(TABLE_QUIZ_RESULT, null, values);
        db.close();
        return id;
    }

    // Method to insert a QuestionResult
    public long insertQuestionResult(QuestionResult questionResult, long quizResultId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION_ID_REF, questionResult.getQuestionId());
        values.put(COLUMN_QUIZ_RESULT_ID_REF, quizResultId);
        values.put(COLUMN_ANSWER_PROVIDED, questionResult.getAnswer());
        long id = db.insert(TABLE_QUESTION_RESULT, null, values);
        db.close();
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
                        question.setId(questionCursor.getLong(questionCursor.getColumnIndex(COLUMN_QUIZ_ID)));
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

    @SuppressLint("Range")
    public Quiz getQuiz(int quizId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUIZ, new String[] { COLUMN_ID,
                        COLUMN_QUIZ_TITLE }, COLUMN_ID + "=?",
                new String[] { String.valueOf(quizId) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Quiz quiz = new Quiz(cursor.getString(1));
        quiz.setId(Integer.parseInt(cursor.getString(0)));

        // Fetch the questions for this quiz
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
                question.setQuizId(questionCursor.getInt(questionCursor.getColumnIndex(COLUMN_QUIZ_ID)));
                question.setId(questionCursor.getLong(questionCursor.getColumnIndex(COLUMN_ID)));
                quiz.addQuestion(question);
            } while (questionCursor.moveToNext());
        }

        // don't forget to close the cursors
        questionCursor.close();
        cursor.close();
        db.close();
        return quiz;
    }

}
