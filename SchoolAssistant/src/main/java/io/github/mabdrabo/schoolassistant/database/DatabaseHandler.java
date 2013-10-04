package io.github.mabdrabo.schoolassistant.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import io.github.mabdrabo.schoolassistant.objects.Assignment;
import io.github.mabdrabo.schoolassistant.objects.Course;
import io.github.mabdrabo.schoolassistant.objects.Note;
import io.github.mabdrabo.schoolassistant.objects.Project;
import io.github.mabdrabo.schoolassistant.objects.Quiz;
import io.github.mabdrabo.schoolassistant.objects.Section;

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
        db.execSQL(NoteTableHandler.CREATE_NOTES_TABLE);
        db.execSQL(AssignmentTableHandler.CREATE_ASSIGNMENTS_TABLE);
        db.execSQL(QuizTableHandler.CREATE_QUIZZES_TABLE);
        db.execSQL(ProjectTableHandler.CREATE_PROJECTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CourseTableHandler.TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + SectionTableHandler.TABLE_SECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + NoteTableHandler.TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + AssignmentTableHandler.TABLE_ASSIGNMENT);
        db.execSQL("DROP TABLE IF EXISTS " + QuizTableHandler.TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS " + ProjectTableHandler.TABLE_PROJECTS);
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

    public void deleteCourse(Course course) {
        CourseTableHandler.delete(course);
        for (Section section : getCourseSections(course.get_id()))
            deleteSection(section);
        for (Assignment assignment : getCourseAssignments(course.get_id()))
            deleteAssignment(assignment);
        for (Project project : getCourseProjects(course.get_id()))
            deleteProject(project);
        for (Quiz quiz : getCourseQuizzes(course.get_id()))
            deleteQuiz(quiz);
        for (Note note : getCourseNotes(course.get_id()))
            deleteNote(note);
    }

    public void updateCourse(Course course) {
        CourseTableHandler.update(course);
    }
    //////////////////////////////////////

    // SECTIONS //
    public void addSection(Section section) {
        SectionTableHandler.add(section);
    }

    public ArrayList<Section> getCourseSections(int course_id) {
        return SectionTableHandler.getAll(course_id);
    }

    public ArrayList<Section> getAllSections() {
        return SectionTableHandler.getAll(-1);
    }

    public void deleteSection(Section section) {
        SectionTableHandler.delete(section);
    }

    public void updateSection(Section section) {
        SectionTableHandler.update(section);
    }
    ///////////////////////////////////////

    // NOTES //
    public void addNote(Note note) {
        NoteTableHandler.add(note);
    }

    public ArrayList<Note> getCourseNotes(int course_id) {
        return NoteTableHandler.getAll(course_id);
    }

    public ArrayList<Note> getAllNotes() {
        return NoteTableHandler.getAll(-1);
    }

    public void deleteNote(Note note) {
        NoteTableHandler.delete(note);
    }

    public void updateNote(Note note) {
        NoteTableHandler.update(note);
    }
    ///////////////////////////////////////

    // ASSIGNMENTS //
    public void addAssignment(Assignment assignment) {
        AssignmentTableHandler.add(assignment);
    }

    public ArrayList<Assignment> getCourseAssignments(int course_id) {
        return AssignmentTableHandler.getAll(course_id);
    }

    public ArrayList<Assignment> getAllAssignments() {
        return AssignmentTableHandler.getAll(-1);
    }

    public void deleteAssignment(Assignment assignment) {
        AssignmentTableHandler.delete(assignment);
    }

    public void updateAssignment(Assignment assignment) {
        AssignmentTableHandler.update(assignment);
    }
    ///////////////////////////////////////

    // QUIZZES //
    public void addQuiz(Quiz quiz) {
        QuizTableHandler.add(quiz);
    }

    public ArrayList<Quiz> getCourseQuizzes(int course_id) {
        return QuizTableHandler.getAll(course_id);
    }

    public ArrayList<Quiz> getAllQuizzes() {
        return QuizTableHandler.getAll(-1);
    }

    public void deleteQuiz(Quiz quiz) {
        QuizTableHandler.delete(quiz);
    }

    public void updateQuiz(Quiz quiz) {
        QuizTableHandler.update(quiz);
    }
    ///////////////////////////////////////

    // PROJECTS //
    public void addProject(Project project) {
        ProjectTableHandler.add(project);
    }

    public ArrayList<Project> getCourseProjects(int course_id) {
        return ProjectTableHandler.getAll(course_id);
    }

    public ArrayList<Project> getAllProjects() {
        return ProjectTableHandler.getAll(-1);
    }

    public void deleteProject(Project project) {
        ProjectTableHandler.delete(project);
    }

    public void updateProject(Project project) {
        ProjectTableHandler.update(project);
    }
    ///////////////////////////////////////
}
