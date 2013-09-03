package io.github.mabdrabo.schoolassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by mahmoud on 9/3/13.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SchoolAssistantDB";

    CourseTableHandler courseTableHandler;
    SectionTableHandler sectionTableHandler;
    NoteTableHandler noteTableHandler;
    AssignmentTableHandler assignmentTableHandler;
    QuizTableHandler quizTableHandler;
    ProjectTableHandler projectTableHandler;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        courseTableHandler = new CourseTableHandler(this);
        sectionTableHandler = new SectionTableHandler(this);
        noteTableHandler = new NoteTableHandler(this);
        assignmentTableHandler = new AssignmentTableHandler(this);
        quizTableHandler = new QuizTableHandler(this);
        projectTableHandler = new ProjectTableHandler(this);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CourseTableHandler.CREATE_COURSES_TABLE);
        db.execSQL(SectionTableHandler.CREATE_SECTIONS_TABLE);
//        db.execSQL(NoteTableHandler.CREATE_NOTES_TABLE);
//        db.execSQL(AssignmentTableHandler.CREATE_ASSIGNMENTS_TABLE);
//        db.execSQL(QuizTableHandler.CREATE_QUIZES_TABLE);
//        db.execSQL(ProjectTableHandler.CREATE_PROJECTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CourseTableHandler.TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + SectionTableHandler.TABLE_SECTIONS);
//        db.execSQL("DROP TABLE IF EXISTS " + NoteTableHandler.TABLE_NOTES);
//        Create tables again
        onCreate(db);
    }


    // COURSES //
    public void addCourse(Course course) {
        CourseTableHandler.add(course);
    }

    public Course getCourse(int course_id) {
        return CourseTableHandler.get(course_id);
    }

    public ArrayList<Course> getAllCourses() {
        return CourseTableHandler.getAll();
    }
    //////////////////////////////////////

    // SECTIONS //
    public void addSection(Section section) {
        SectionTableHandler.add(section);
    }

    public ArrayList<Section> getCourseSections(int course_id) {
        return SectionTableHandler.getAll(course_id);
    }

    public ArrayList<Section> getAllSections(int course_id) {
        return SectionTableHandler.getAll(-1);
    }
    ///////////////////////////////////////
}
