package io.github.mabdrabo.schoolassistant.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import io.github.mabdrabo.schoolassistant.objects.Quiz;

/**
 * Created by mahmoud on 9/3/13.
 */
public class QuizTableHandler {

    static DatabaseHandler dbHandler;

    // Quizzes table name
    public static final String TABLE_QUIZ = "quiz";

    // Quiz Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_COURSE_ID = "course_id";
    private static final String KEY_PLACE = "description";
    private static final String KEY_DATE = "date";

    public static String CREATE_QUIZZES_TABLE = "CREATE TABLE " + TABLE_QUIZ + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_COURSE_ID + " INTEGER,"
            + KEY_PLACE + " TEXT,"
            + KEY_DATE + " INTEGER)";

    public QuizTableHandler(DatabaseHandler databaseHandler) {
        this.dbHandler = databaseHandler;
    }

    public static void add(Quiz quiz) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_ID, quiz.get_courseId()); // quiz Course Id
        values.put(KEY_PLACE, quiz.get_place()); // quiz description
        values.put(KEY_DATE, quiz.get_date()); // quiz deadline date

        // Inserting Row
        db.insert(TABLE_QUIZ, null, values);
        db.close(); // Closing database connection
    }

    public static Quiz get(int quiz_id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(TABLE_QUIZ, null, KEY_ID + "=?", new String[]{String.valueOf(quiz_id)}, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;

        Quiz quiz = new Quiz(cursor.getString(2));
        quiz.set_id(Integer.parseInt(cursor.getString(0)));
        quiz.set_courseId(Integer.parseInt(cursor.getString(1)));
        quiz.set_date(Long.parseLong(cursor.getString(3)));
        cursor.close();

        return quiz;
    }

    public static void update(Quiz quiz) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_ID, quiz.get_courseId()); // quiz Course Id
        values.put(KEY_PLACE, quiz.get_place()); // quiz place
        values.put(KEY_DATE, quiz.get_date()); // quiz date
        // Inserting Row
        db.update(TABLE_QUIZ, values, KEY_ID + "=?", new String[]{String.valueOf(quiz.get_id())});
        db.close(); // Closing database connection
    }

    // gets all Assignment with courseId = course_id
    // if course_id = -1, gets all Assignments
    public static ArrayList<Quiz> getAll(int course_id) {
        ArrayList<Quiz> quizList = new ArrayList<Quiz>();
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selection = (course_id > -1)? KEY_COURSE_ID + "=" + course_id : null;
        Cursor cursor = db.query(TABLE_QUIZ, null, selection, null, null, null, KEY_DATE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Quiz quiz = new Quiz(cursor.getString(2));
                quiz.set_id(Integer.parseInt(cursor.getString(0)));
                quiz.set_courseId(Integer.parseInt(cursor.getString(1)));
                quiz.set_date(Long.parseLong(cursor.getString(3)));
                // Adding Quiz to list
                quizList.add(quiz);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return quizList;
    }
}
