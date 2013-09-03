package io.github.mabdrabo.schoolassistant.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;

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
//        getMenuInflater().inflate(R.menu.course, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
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
            System.out.println(section.get_id() + "SECTION_ID");
            String info = section.get_place() + "  " + section.get_day_short() + "  " + section.get_time();
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

}
