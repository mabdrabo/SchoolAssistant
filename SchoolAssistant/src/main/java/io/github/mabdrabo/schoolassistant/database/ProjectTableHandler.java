package io.github.mabdrabo.schoolassistant.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import io.github.mabdrabo.schoolassistant.objects.Project;

/**
 * Created by mahmoud on 9/3/13.
 */
public class ProjectTableHandler {

    static DatabaseHandler dbHandler;

    // Projects table name
    public static final String TABLE_PROJECTS = "project";

    // Project Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_COURSE_ID = "course_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_DATE = "due_date";

    public static String CREATE_PROJECTS_TABLE = "CREATE TABLE " + TABLE_PROJECTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_COURSE_ID + " INTEGER,"
            + KEY_TITLE + " TEXT,"
            + KEY_NOTES + " TEXT,"
            + KEY_DATE + " INTEGER)";

    public ProjectTableHandler(DatabaseHandler databaseHandler) {
        this.dbHandler = databaseHandler;
    }

    public static void add(Project project) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_ID, project.get_courseId()); // project Course Id
        values.put(KEY_TITLE, project.get_title()); // project title
        values.put(KEY_NOTES, project.get_notes()); // project notes
        values.put(KEY_DATE, project.get_dueDate()); // project deadline date

        // Inserting Row
        db.insert(TABLE_PROJECTS, null, values);
        db.close(); // Closing database connection
    }

    public static Project get(int project_id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PROJECTS, null, KEY_ID + "=?", new String[]{String.valueOf(project_id)}, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;

        Project project = new Project(cursor.getString(2));
        project.set_id(Integer.parseInt(cursor.getString(0)));
        project.set_courseId(Integer.parseInt(cursor.getString(1)));
        project.set_notes(cursor.getString(3));
        project.set_dueDate(Long.parseLong(cursor.getString(4)));
        cursor.close();

        return project;
    }

    public static void update(Project project) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_ID, project.get_courseId()); // project Course Id
        values.put(KEY_TITLE, project.get_title()); // project title
        values.put(KEY_NOTES, project.get_notes()); // project notes
        values.put(KEY_DATE, project.get_dueDate()); // project deadline date
        // Inserting Row
        db.update(TABLE_PROJECTS, values, KEY_ID + "=?", new String[]{String.valueOf(project.get_id())});
        db.close(); // Closing database connection
    }

    // gets all Project with courseId = course_id
    // if course_id = -1, gets all Projects
    public static ArrayList<Project> getAll(int course_id) {
        ArrayList<Project> courseList = new ArrayList<Project>();
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selection = (course_id > -1)? KEY_COURSE_ID + "=" + course_id : null;
        Cursor cursor = db.query(TABLE_PROJECTS, null, selection, null, null, null, KEY_DATE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Project project = new Project(cursor.getString(2));
                project.set_id(Integer.parseInt(cursor.getString(0)));
                project.set_courseId(Integer.parseInt(cursor.getString(1)));
                project.set_notes(cursor.getString(3));
                project.set_dueDate(Long.parseLong(cursor.getString(4)));
                // Adding Project to list
                courseList.add(project);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return courseList;
    }
}
