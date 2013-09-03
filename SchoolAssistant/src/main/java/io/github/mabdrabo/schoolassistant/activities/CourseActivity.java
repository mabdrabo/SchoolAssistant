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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
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

    int selected_course_id;
    Course course;
    ArrayList<Section> course_sections;
    ArrayList<Note> course_notes;
    ArrayList<Assignment> course_assignments;
    ArrayList<Quiz> course_quizzes;
    ArrayList<Project> course_projects;

    TextView courseNameTextView;
    ExpandableListView courseExpandableListView;

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
                return true;
            case R.id.action_add_assignment:
                return true;
            case R.id.action_add_quiz:
                return true;
            case R.id.action_add_project:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        course_sections = MainActivity.database.getCourseSections(selected_course_id);
        ArrayList<String> course_sections_string = new ArrayList<String>();

        for (Section section : course_sections) {
            int[] time = MainActivity.toHourMinute(section.get_time());
            String info =  section.get_day_short() + "  " + section.get_place()
                    + "  " + time[0] + ":" + time[1]
                    + " " + ((time[2] == 0)? "am" : "pm");
            course_sections_string.add(info);
        }

        ArrayList<String> listDataHeader = new ArrayList<String>();
        HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Classes");
        listDataHeader.add("Notes");
        listDataHeader.add("Assignments");
        listDataHeader.add("Quizzes");
        listDataHeader.add("Projects");

        listDataChild.put(listDataHeader.get(0), course_sections_string);

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        courseExpandableListView.setAdapter(listAdapter);

        courseNameTextView.setText(course.get_name());

    }


    private void addSection() {
        final Dialog addSectionDialog = new Dialog(this);
        addSectionDialog.setTitle("Add Class");
        addSectionDialog.setContentView(R.layout.add_class_dialog);
        addSectionDialog.show();

        final ArrayList<Course> courses = MainActivity.database.getAllCourses();
        ArrayList<String> courses_names = new ArrayList<String>();
        int courseSpinnerDefaultPosition = 0;
        boolean flag = false;
        for (Course course : courses) {
            courses_names.add(course.get_name());
            if (course.get_id() == this.course.get_id())
                flag = true;
            else if (!flag)
                courseSpinnerDefaultPosition++;
        }

        final Spinner courseSpinner = (Spinner) addSectionDialog.findViewById(R.id.courseSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courses_names);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(dataAdapter);
        courseSpinner.setSelection(courseSpinnerDefaultPosition);

        final Spinner daySpinner = (Spinner) addSectionDialog.findViewById(R.id.daySpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MainActivity.DAY_OF_WEEK_LONG);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);

        final TimePicker classTimePicker = (TimePicker) addSectionDialog.findViewById(R.id.classTimePicker);

        Button addSectionButton = (Button) addSectionDialog.findViewById(R.id.addSectionButton);
        addSectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int course_id = courses.get(courseSpinner.getSelectedItemPosition()).get_id();
                String place = "" + ((EditText) addSectionDialog.findViewById(R.id.placeEditText)).getText();
                int day = daySpinner.getSelectedItemPosition();  // 0 is Saturday
                int time = MainActivity.toSeconds(classTimePicker.getCurrentHour(), classTimePicker.getCurrentMinute());

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
}
