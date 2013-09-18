package io.github.mabdrabo.schoolassistant.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.mabdrabo.schoolassistant.R;
import io.github.mabdrabo.schoolassistant.objects.Course;

public class CoursesActivity extends Activity {


    ArrayList<HashMap<String, String>> coursesList;
    ListView coursesListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        // Show the Up button in the action bar.
        setupActionBar();

        coursesList = new ArrayList<HashMap<String, String>>();
        coursesListView = ((ListView) findViewById(R.id.coursesListView));
        final Intent intent = new Intent().setClass(this, CourseActivity.class);

        coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selected_course_id = MainActivity.courses.get(position).get_id();
                intent.putExtra("selected_course_id", selected_course_id);
                startActivity(intent);
            }
        });
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
        getMenuInflater().inflate(R.menu.courses, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add_course:
                addCourse();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        coursesList = new ArrayList<HashMap<String, String>>();
        for (Course course : MainActivity.courses) {
            HashMap<String, String> datum = new HashMap<String, String>(2);
            datum.put("main", course.get_name());
//            datum.put("sub", );
            coursesList.add(datum);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, coursesList, android.R.layout.simple_list_item_2,
                new String[]{"main", "sub"},
                new int[]{android.R.id.text1, android.R.id.text2});
        coursesListView.setAdapter(adapter);
    }

    private void addCourse() {
        final Dialog addCourseDialog = new Dialog(this);
        addCourseDialog.setTitle("Add Course");
        addCourseDialog.setContentView(R.layout.add_course_dialog);
        addCourseDialog.show();

        Button addCourseButton = (Button) addCourseDialog.findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "" + ((EditText) addCourseDialog.findViewById(R.id.courseNameEditText)).getText();
//                String code = "";
                Course course = new Course(name);
                MainActivity.database.addCourse(course);
                addCourseDialog.dismiss();
                MainActivity.courses = MainActivity.database.getAllCourses();
                onResume();
            }
        });
    }
}
