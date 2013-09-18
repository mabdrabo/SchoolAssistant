package io.github.mabdrabo.schoolassistant.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import io.github.mabdrabo.schoolassistant.objects.Assignment;

/**
 * Created by mahmoud on 9/3/13.
 */
public class AssignmentTableHandler {

    static DatabaseHandler dbHandler;

    // Assignments table name
    public static final String TABLE_ASSIGNMENT = "assignment";

    // Assignment Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_COURSE_ID = "course_id";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DATE = "due_date";
    private static final String KEY_NOTE = "notes";

    public static String CREATE_ASSIGNMENTS_TABLE = "CREATE TABLE " + TABLE_ASSIGNMENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_COURSE_ID + " INTEGER,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_DATE + " INTEGER,"
            + KEY_NOTE + " TEXT)";

    public AssignmentTableHandler(DatabaseHandler databaseHandler) {
        this.dbHandler = databaseHandler;
    }

    public static void add(Assignment assignment) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_ID, assignment.get_courseId()); // assignment Course Id
        values.put(KEY_DESCRIPTION, assignment.get_description()); // assignment description
        values.put(KEY_DATE, assignment.get_dueDate()); // assignment deadline date
        values.put(KEY_NOTE, assignment.get_notes()); // assignment notes

        // Inserting Row
        db.insert(TABLE_ASSIGNMENT, null, values);
        db.close(); // Closing database connection
    }

    public static Assignment get(int assignment_id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ASSIGNMENT, null, KEY_ID + "=?", new String[]{String.valueOf(assignment_id)}, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;

        Assignment assignment = new Assignment(cursor.getString(2));
        assignment.set_id(Integer.parseInt(cursor.getString(0)));
        assignment.set_courseId(Integer.parseInt(cursor.getString(1)));
        assignment.set_dueDate(Long.parseLong(cursor.getString(3)));
        assignment.set_notes((cursor.getString(4)));
        cursor.close();

        return assignment;
    }

    public static void update(Assignment assignment) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_ID, assignment.get_courseId()); // assignment Course Id
        values.put(KEY_DESCRIPTION, assignment.get_description()); // assignment description
        values.put(KEY_DATE, assignment.get_dueDate()); // assignment deadline date
        values.put(KEY_NOTE, assignment.get_notes());
        // Inserting Row
        db.update(TABLE_ASSIGNMENT, values, KEY_ID + "=?", new String[]{String.valueOf(assignment.get_id())});
        db.close(); // Closing database connection
    }

    // gets all Assignment with courseId = course_id
    // if course_id = -1, gets all Assignments
    public static ArrayList<Assignment> getAll(int course_id) {
        ArrayList<Assignment> assignmentList = new ArrayList<Assignment>();
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selection = (course_id > -1)? KEY_COURSE_ID + "=" + course_id : null;
        Cursor cursor = db.query(TABLE_ASSIGNMENT, null, selection, null, null, null, KEY_DATE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Assignment assignment = new Assignment(cursor.getString(2));
                assignment.set_id(Integer.parseInt(cursor.getString(0)));
                assignment.set_courseId(Integer.parseInt(cursor.getString(1)));
                assignment.set_notes((cursor.getString(4)));
                // Adding Assignment to list
                assignmentList.add(assignment);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return assignmentList;
    }
}
