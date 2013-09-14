package io.github.mabdrabo.schoolassistant.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import io.github.mabdrabo.schoolassistant.objects.Note;

/**
 * Created by mahmoud on 9/3/13.
 */
public class NoteTableHandler {

    static DatabaseHandler dbHandler;

    // Courses table name
    public static final String TABLE_NOTES = "notes";

    // Sections Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_COURSE_ID = "course_id";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE = "date";

    public static String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_COURSE_ID + " INTEGER,"
            + KEY_CONTENT + " TEXT,"
            + KEY_DATE + " INTEGER)";


    public NoteTableHandler(DatabaseHandler databaseHandler) {
        this.dbHandler = databaseHandler;
    }

    public static void add(Note note) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_ID, note.get_courseId()); // note Course Id
        values.put(KEY_CONTENT, note.get_content()); // note content
        values.put(KEY_DATE, note.get_date()); // note date

        // Inserting Row
        db.insert(TABLE_NOTES, null, values);
        db.close(); // Closing database connection
    }

    public static Note get(int note_id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTES, null, KEY_ID + "=?", new String[]{String.valueOf(note_id)}, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;

        Note note = new Note(cursor.getString(2));
        note.set_id(Integer.parseInt(cursor.getString(0)));
        note.set_courseId(Integer.parseInt(cursor.getString(1)));
        note.set_date(Long.parseLong(cursor.getString(3)));
        cursor.close();

        return note;
    }

    public static void update(Note note) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_ID, note.get_courseId()); // note Course Id
        values.put(KEY_CONTENT, note.get_content()); // note content
        values.put(KEY_DATE, note.get_date()); // note date

        // Inserting Row
        db.update(TABLE_NOTES, values, KEY_ID + "=?", new String[]{String.valueOf(note.get_id())});
        db.close(); // Closing database connection
    }


    // gets all Notes with courseId = course_id
    // if course_id = -1, gets all Sections
    public static ArrayList<Note> getAll(int course_id) {
        ArrayList<Note> noteList = new ArrayList<Note>();
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        String selection = (course_id > -1)? KEY_COURSE_ID + "=" + course_id : null;
        Cursor cursor = db.query(TABLE_NOTES, null, selection, null, null, null, KEY_DATE + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note(cursor.getString(2));
                note.set_id(Integer.parseInt(cursor.getString(0)));
                note.set_courseId(Integer.parseInt(cursor.getString(1)));

                // Adding Section to list
                noteList.add(note);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return noteList;
    }

}
