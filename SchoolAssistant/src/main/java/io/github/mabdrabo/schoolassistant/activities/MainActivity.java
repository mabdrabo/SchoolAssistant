package io.github.mabdrabo.schoolassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import io.github.mabdrabo.schoolassistant.R;
import io.github.mabdrabo.schoolassistant.database.DatabaseHandler;
import io.github.mabdrabo.schoolassistant.objects.Course;
import io.github.mabdrabo.schoolassistant.objects.Section;

public class MainActivity extends Activity {

    public static String[] DAY_OF_WEEK_LONG = new String[]{"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
    public static String[] DAY_OF_WEEK_SHORT = new String[]{"Sat", "Sun", "Mon", "Tue", "Wed", "Thu"};
    public static String[] SLOT_START_TIME = new String[]{"8:30", "10:30", "12:15", "2:15", "4:00"};
    public static DatabaseHandler database;

    public static ArrayList<Course> courses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHandler(this);
        courses = new ArrayList<Course>();
        courses = MainActivity.database.getAllCourses();
    }


    @Override
    protected void onResume() {
        super.onResume();
        ListView upcomingListView = (ListView) findViewById(R.id.upcomingListView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void onClickScheduleButton(View view) {
        Intent intent = new Intent().setClass(this, ScheduleActivity.class);
        startActivity(intent);
    }

    public void onClickTomorrowButton(View view) {
        Calendar calendar = Calendar.getInstance();
        int day_month = calendar.get(Calendar.DAY_OF_MONTH);
        int day_week = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

    }

    public void onClickCoursesButton(View view) {
        Intent intent = new Intent().setClass(this, CoursesActivity.class);
        startActivity(intent);
    }

    public void onClickAssignmentsButton(View view) {
        Intent intent = new Intent().setClass(this, AssignmentsActivity.class);
        startActivity(intent);
    }

    public void onClickQuizzesButton(View view) {
        Intent intent = new Intent().setClass(this, QuizzesActivity.class);
        startActivity(intent);
    }

    public void onClickProjectsButton(View view) {
        Intent intent = new Intent().setClass(this, ProjectsActivity.class);
        startActivity(intent);
    }

    public static Calendar getDate(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar;
    }

    public static int get_course_spinner_position(int course_id) {
        boolean flag = false;
        int courseSpinnerDefaultPosition = 0;
        for (Course course : MainActivity.courses) {
            if (course.get_id() == course_id)
                flag = true;
            else if (!flag)
                courseSpinnerDefaultPosition++;
        }
        return courseSpinnerDefaultPosition;
    }
}
