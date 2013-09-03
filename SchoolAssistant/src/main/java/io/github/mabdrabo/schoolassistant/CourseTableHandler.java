package io.github.mabdrabo.schoolassistant;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by mahmoud on 9/3/13.
 */
public class CourseTableHandler {

    static DatabaseHandler dbHandler;

    // Courses table name
    public static final String TABLE_COURSES = "courses";

    // Courses Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";

    public static String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + "UNIQUE(" + KEY_NAME + ")"
            + " ON CONFLICT REPLACE)";


    public CourseTableHandler(DatabaseHandler databaseHandler) {
        this.dbHandler = databaseHandler;
    }


    public static void add(Course course) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, course.get_name()); // Category name

        // Inserting Row
        db.insert(TABLE_COURSES, null, values);
        db.close(); // Closing database connection
    }

    public static Course get(int course_id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSES, null, KEY_ID + "=?", new String[]{String.valueOf(course_id)}, null, null, null);
        ArrayList<Course> courseList = new ArrayList<Course>();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            Course course = new Course(cursor.getString(1));
            course.set_id(Integer.parseInt(cursor.getString(0)));

            cursor.close();
            return course;
        } else {
            cursor.close();
            return null;
        }
    }

    public static void update(Course course) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, course.get_name());

        // updating row
        db.update(TABLE_COURSES, values, KEY_ID + "=?", new String[]{String.valueOf(course.get_id())});
    }

    public static ArrayList<Course> getAll() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSES, null, null, null, null, null, null);
        ArrayList<Course> courseList = new ArrayList<Course>();

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Course course = new Course(cursor.getString(1));
                course.set_id(Integer.parseInt(cursor.getString(0)));

                // Adding Course to list
                courseList.add(course);
            } while (cursor.moveToNext());
            cursor.close();
            return courseList;
        } else {
            cursor.close();
            return null;
        }
    }

    public static ArrayList<Section> getSections(int course_id) {
        return null;
    }

    public static ArrayList<Note> getNotes(int course_id) {
        return null;
    }

    public static ArrayList<Assignment> getAssignments(int course_id) {
        return null;
    }

    public static ArrayList<Quiz> getQuizes(int course_id) {
        return null;
    }

    public static ArrayAdapter<Project> getProjects(int course_id) {
        return null;
    }
}