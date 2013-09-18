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


    public void onClickCoursesButton(View view) {
        Intent intent = new Intent().setClass(this, CoursesActivity.class);
        startActivity(intent);
    }

    public void onClickScheduleButton(View view) {
        setContentView(R.layout.activity_schedule);
        GridView gridView = (GridView) findViewById(R.id.gridView);

        String[] classes_names = new String[42];
        int classes_names_counter = 0;
        classes_names[classes_names_counter++] = " ";

        for (int i=0; i<5; i++)
            classes_names[classes_names_counter++] = SLOT_START_TIME[i];

        ArrayList<Section> allClasses = MainActivity.database.getAllSections();
        String[][] day_class = new String[6][5];
        for (Section section : allClasses) {
            day_class[section.get_day()][section.get_time()] = MainActivity.database.getCourse(section.get_courseId()).get_name();
            day_class[section.get_day()][section.get_time()] += " " + section.get_place();
        }

        for (int i=0; i<6; i++) {
            classes_names[classes_names_counter++] = DAY_OF_WEEK_SHORT[i];
            for (int j=0; j<5; j++) {
                if (day_class[i][j] != null)
                    classes_names[classes_names_counter++] = day_class[i][j];
                else
                    classes_names[classes_names_counter++] = "free";
            }
        }

        for (int i=0; i<42; i++)
            if (classes_names[i] == null)
                classes_names[i] = "x";

        ArrayAdapter<String> xadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classes_names);
        gridView.setAdapter(xadapter);
    }

    public void onClickTodayButton(View view) {
        Calendar calendar = Calendar.getInstance();
        int day_month = calendar.get(Calendar.DAY_OF_MONTH);
        int day_week = calendar.get(Calendar.DAY_OF_WEEK);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

    }

    public void onClickAssignmentsButton(View view) {
        Intent intent = new Intent().setClass(this, AssignmentsActivity.class);
        startActivity(intent);
    }

    public void onClickQuizzesButton(View view) {

    }

    public void onClickProjectsButton(View view) {

    }

    public static int[] getDate(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return new int[] {calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)};
    }
}
