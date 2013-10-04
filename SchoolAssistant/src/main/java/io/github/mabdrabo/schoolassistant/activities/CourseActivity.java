package io.github.mabdrabo.schoolassistant.activities;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;

import io.github.mabdrabo.schoolassistant.ExpandableListAdapter;
import io.github.mabdrabo.schoolassistant.R;
import io.github.mabdrabo.schoolassistant.objects.Assignment;
import io.github.mabdrabo.schoolassistant.objects.Course;
import io.github.mabdrabo.schoolassistant.objects.Note;
import io.github.mabdrabo.schoolassistant.objects.Project;
import io.github.mabdrabo.schoolassistant.objects.Quiz;
import io.github.mabdrabo.schoolassistant.objects.Section;

public class CourseActivity extends Activity {

    Course course;
    int selected_course_id;

    ArrayList<Section> course_sections;
    ArrayList<Note> course_notes;
    ArrayList<Assignment> course_assignments;
    ArrayList<Quiz> course_quizzes;
    ArrayList<Project> course_projects;

    TextView courseNameTextView;
    ExpandableListView courseExpandableListView;

    ArrayAdapter<String> courseSpinnerAdapter;
    ArrayAdapter<String> daySpinnerAdapter;
    ArrayAdapter<String> timeSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        // Show the Up button in the action bar.
        setupActionBar();

        courseNameTextView = (TextView) findViewById(R.id.courseNameTextView);
        courseExpandableListView = (ExpandableListView) findViewById(R.id.courseExpandableListView);

        Bundle extras = getIntent().getExtras();
        selected_course_id = extras.getInt("selected_course_id");
        course = MainActivity.database.getCourse(selected_course_id);
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.course, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add_section:
                addSection();
                return true;
            case R.id.action_add_note:
                addNote();
                return true;
            case R.id.action_add_assignment:
                addAssignment();
                return true;
            case R.id.action_add_quiz:
                addQuiz();
                return true;
            case R.id.action_add_project:
                addProject();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<String> listDataHeader = new ArrayList<String>();
        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Classes");
        listDataHeader.add("Notes");
        listDataHeader.add("Assignments");
        listDataHeader.add("Quizzes");
        listDataHeader.add("Projects");

        course_sections = MainActivity.database.getCourseSections(selected_course_id);
        ArrayList<String> course_sections_string = new ArrayList<String>();
        for (Section section : course_sections) {
            String info =  section.get_day_short() + "  " + section.get_place() + section.get_time();
            course_sections_string.add(info);
        }

        course_notes = MainActivity.database.getCourseNotes(selected_course_id);
        ArrayList<String> course_notes_string = new ArrayList<String>();
        for (Note note : course_notes) {
            String info =  note.get_content();
            course_notes_string.add(info);
        }

        course_assignments = MainActivity.database.getCourseAssignments(selected_course_id);
        ArrayList<String> course_assignments_string = new ArrayList<String>();
        for (Assignment assignment : course_assignments) {
            String info =  assignment.get_description();
            Formatter formatter = new Formatter();
            info += "" + formatter.format("%tF", MainActivity.getDate(assignment.get_dueDate()));
            formatter.close();
            course_assignments_string.add(info);
        }

        course_quizzes = MainActivity.database.getCourseQuizzes(selected_course_id);
        ArrayList<String> course_quizzes_string = new ArrayList<String>();
        for (Quiz quiz : course_quizzes) {
            String info =  quiz.get_place();
            course_quizzes_string.add(info);
        }

        course_projects = MainActivity.database.getCourseProjects(selected_course_id);
        ArrayList<String> course_projects_string = new ArrayList<String>();
        for (Project project : course_projects) {
            String info =  project.get_title();
            course_projects_string.add(info);
        }

        listDataChild.put(listDataHeader.get(0), course_sections_string);
        listDataChild.put(listDataHeader.get(1), course_notes_string);
        listDataChild.put(listDataHeader.get(2), course_assignments_string);
        listDataChild.put(listDataHeader.get(3), course_quizzes_string);
        listDataChild.put(listDataHeader.get(4), course_projects_string);

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        courseExpandableListView.setAdapter(listAdapter);

        courseNameTextView.setText(course.get_name());

        ArrayList<String> courses_names = new ArrayList<String>();
        for (Course course : MainActivity.courses)
            courses_names.add(course.get_name());

        courseSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courses_names);
        courseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        daySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MainActivity.DAY_OF_WEEK_LONG);
        daySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        timeSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MainActivity.SLOT_START_TIME);
        timeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }


    private void addSection() {
        final Dialog addSectionDialog = new Dialog(this);
        addSectionDialog.setTitle("Add Class");
        addSectionDialog.setContentView(R.layout.add_class_dialog);
        addSectionDialog.show();

        final Spinner courseSpinner = (Spinner) addSectionDialog.findViewById(R.id.courseSpinner);
        courseSpinner.setAdapter(courseSpinnerAdapter);
        courseSpinner.setSelection(MainActivity.get_course_spinner_position(selected_course_id));

        final Spinner daySpinner = (Spinner) addSectionDialog.findViewById(R.id.daySpinner);
        daySpinner.setAdapter(daySpinnerAdapter);

        final Spinner timeSpinner = (Spinner) addSectionDialog.findViewById(R.id.timeSpinner);
        timeSpinner.setAdapter(timeSpinnerAdapter);

        Button addSectionButton = (Button) addSectionDialog.findViewById(R.id.addSectionButton);
        addSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int course_id = MainActivity.courses.get(courseSpinner.getSelectedItemPosition()).get_id();
                String place = "" + ((EditText) addSectionDialog.findViewById(R.id.placeEditText)).getText();
                int day = daySpinner.getSelectedItemPosition();  // 0 is Saturday
                int time = timeSpinner.getSelectedItemPosition();  // 0 is 8:30

                Section section = new Section(place);
                section.set_courseId(course_id);
                section.set_day(day);
                section.set_time(time);

                MainActivity.database.addSection(section);
                addSectionDialog.dismiss();
                onResume();
            }
        });
    }

    private void addNote() {
        final Dialog addNoteDialog = new Dialog(this);
        addNoteDialog.setTitle("Add Note");
        addNoteDialog.setContentView(R.layout.add_note_dialog);
        addNoteDialog.show();

        final Spinner courseSpinner = (Spinner) addNoteDialog.findViewById(R.id.courseSpinner);
        courseSpinner.setAdapter(courseSpinnerAdapter);
        courseSpinner.setSelection(MainActivity.get_course_spinner_position(selected_course_id));

        Button addNoteButton = (Button) addNoteDialog.findViewById(R.id.addNoteButton);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int course_id = MainActivity.courses.get(courseSpinner.getSelectedItemPosition()).get_id();
                String content = "" + ((EditText) addNoteDialog.findViewById(R.id.NoteEditText)).getText();
                Note note = new Note(content);
                note.set_courseId(course_id);

                MainActivity.database.addNote(note);
                addNoteDialog.dismiss();
                onResume();
            }
        });
    }

    private void addAssignment() {
        final Dialog addAssignmentDialog = new Dialog(this);
        addAssignmentDialog.setTitle("Add Assignment");
        addAssignmentDialog.setContentView(R.layout.add_assignment_dialog);
        addAssignmentDialog.show();

        final Spinner courseSpinner = (Spinner) addAssignmentDialog.findViewById(R.id.courseSpinner);
        courseSpinner.setAdapter(courseSpinnerAdapter);
        courseSpinner.setSelection(MainActivity.get_course_spinner_position(selected_course_id));

        Button addAssignmentButton = (Button) addAssignmentDialog.findViewById(R.id.addAssignmentButton);
        addAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int course_id = MainActivity.courses.get(courseSpinner.getSelectedItemPosition()).get_id();
                String description = "" + ((EditText) addAssignmentDialog.findViewById(R.id.AssignmentEditText)).getText();
                String notes = "" + ((EditText) addAssignmentDialog.findViewById(R.id.AssignmentNotesEditText)).getText();
                DatePicker datePicker = (DatePicker) addAssignmentDialog.findViewById(R.id.datePicker);
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                long deadline = calendar.getTimeInMillis();

                Assignment assignment = new Assignment(description);
                assignment.set_courseId(course_id);
                assignment.set_notes(notes);
                assignment.set_dueDate(deadline);

                MainActivity.database.addAssignment(assignment);
                addAssignmentDialog.dismiss();
                onResume();
            }
        });
    }

    private void addQuiz() {
        final Dialog addQuizDialog = new Dialog(this);
        addQuizDialog.setTitle("Add Quiz");
        addQuizDialog.setContentView(R.layout.add_quiz_dialog);
        addQuizDialog.show();

        final Spinner courseSpinner = (Spinner) addQuizDialog.findViewById(R.id.courseSpinner);
        courseSpinner.setAdapter(courseSpinnerAdapter);
        courseSpinner.setSelection(MainActivity.get_course_spinner_position(selected_course_id));

        Button addQuizButton = (Button) addQuizDialog.findViewById(R.id.addQuizButton);
        addQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int course_id = MainActivity.courses.get(courseSpinner.getSelectedItemPosition()).get_id();
                String place = "" + ((EditText) addQuizDialog.findViewById(R.id.QuizPlaceEditText)).getText();
                DatePicker datePicker = (DatePicker) addQuizDialog.findViewById(R.id.datePicker);
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                long date = calendar.getTimeInMillis();

                Quiz quiz = new Quiz(place);
                quiz.set_courseId(course_id);
                quiz.set_place(place);
                quiz.set_date(date);

                MainActivity.database.addQuiz(quiz);
                addQuizDialog.dismiss();
                onResume();
            }
        });
    }

    private void addProject() {
        final Dialog addProjectDialog = new Dialog(this);
        addProjectDialog.setTitle("Add Project");
        addProjectDialog.setContentView(R.layout.add_project_dialog);
        addProjectDialog.show();

        final Spinner courseSpinner = (Spinner) addProjectDialog.findViewById(R.id.courseSpinner);
        courseSpinner.setAdapter(courseSpinnerAdapter);
        courseSpinner.setSelection(MainActivity.get_course_spinner_position(selected_course_id));

        Button addProjectButton = (Button) addProjectDialog.findViewById(R.id.addProjectButton);
        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int course_id = MainActivity.courses.get(courseSpinner.getSelectedItemPosition()).get_id();
                String title  = "" + ((EditText) addProjectDialog.findViewById(R.id.projectTitleEditText)).getText();
                String note = "" + ((EditText) addProjectDialog.findViewById(R.id.projectNotesEditText)).getText();
                Project project = new Project(title);
                project.set_courseId(course_id);
                project.set_notes(note);

                MainActivity.database.addProject(project);
                addProjectDialog.dismiss();
                onResume();
            }
        });
    }

}
