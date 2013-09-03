package io.github.mabdrabo.schoolassistant.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import io.github.mabdrabo.schoolassistant.objects.Section;

/**
 * Created by mahmoud on 9/3/13.
 */
public class SectionTableHandler {

    static DatabaseHandler dbHandler;

    // Courses table name
    public static final String TABLE_SECTIONS = "sections";

    // Sections Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_COURSE_ID = "course_id";
    private static final String KEY_PLACE = "place";
    private static final String KEY_DAY = "day";
    private static final String KEY_TIME = "time";

    public static String CREATE_SECTIONS_TABLE = "CREATE TABLE " + TABLE_SECTIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_COURSE_ID + " INTEGER,"
            + KEY_PLACE + " TEXT,"
            + KEY_DAY + " INTEGER,"
            + KEY_TIME + " INTEGER,"
            + "UNIQUE(" + KEY_DAY + ", " + KEY_TIME + ")"
            + " ON CONFLICT REPLACE)";


    public SectionTableHandler(DatabaseHandler databaseHandler) {
        this.dbHandler = databaseHandler;
    }


    public static void add(Section section) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_ID, section.get_courseId()); // Section Course Id
        values.put(KEY_PLACE, section.get_place()); // Section place
        values.put(KEY_DAY, section.get_day()); // Section day
        values.put(KEY_TIME, section.get_time()); // Section time

        // Inserting Row
        System.out.println(db.insert(TABLE_SECTIONS, null, values) + " inserttt");
        db.close(); // Closing database connection
    }

    public static Section get(int section_id) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SECTIONS, null, KEY_ID + "=?", new String[]{String.valueOf(section_id)}, null, null, null);

        if (cursor != null && cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            return null;

        Section section = new Section(cursor.getString(2));
        section.set_id(Integer.parseInt(cursor.getString(0)));
        section.set_courseId(Integer.parseInt(cursor.getString(1)));
        section.set_day(Integer.parseInt(cursor.getString(3)));
        section.set_time(Integer.parseInt(cursor.getString(4)));
        cursor.close();

        return section;
    }

    public static void update(Section section) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_ID, section.get_courseId()); // Section Course Id
        values.put(KEY_PLACE, section.get_place()); // Section place
        values.put(KEY_DAY, section.get_day()); // Section day
        values.put(KEY_TIME, section.get_time()); // Section time

        // updating row
        db.update(TABLE_SECTIONS, values, KEY_ID + "=?", new String[]{String.valueOf(section.get_id())});
    }

    // gets all Sections with courseId = course_id
    // if course_id = -1, gets all Sections
    public static ArrayList<Section> getAll(int course_id) {
        ArrayList<Section> sectionList = new ArrayList<Section>();
        SQLiteDatabase db = dbHandler.getReadableDatabase();
//        String selection = (course_id > -1)? KEY_COURSE_ID + "=" + course_id : null;
        Cursor cursor = db.query(TABLE_SECTIONS, null, KEY_COURSE_ID + "=" + course_id, null, null, null, KEY_DAY + " ASC");

        if (cursor.moveToFirst()) {
            do {
                Section section = new Section(cursor.getString(2));
                section.set_id(Integer.parseInt(cursor.getString(0)));
                section.set_courseId(Integer.parseInt(cursor.getString(1)));
                section.set_day(Integer.parseInt(cursor.getString(3)));
                section.set_time(Integer.parseInt(cursor.getString(4)));

                // Adding Section to list
                sectionList.add(section);
            } while (cursor.moveToNext());
            cursor.close();
        }
        System.out.println(sectionList.size() + "ddddddddd");
        return sectionList;
    }
}
