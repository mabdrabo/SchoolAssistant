package io.github.mabdrabo.schoolassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

import io.github.mabdrabo.schoolassistant.R;
import io.github.mabdrabo.schoolassistant.database.DatabaseHandler;
import io.github.mabdrabo.schoolassistant.objects.Course;
import io.github.mabdrabo.schoolassistant.objects.Section;

public class MainActivity extends Activity {

    public static String[] DAY_OF_WEEK_LONG = new String[]{"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday"};
    public static String[] DAY_OF_WEEK_SHORT = new String[]{"Sat", "Sun", "Mon", "Tue", "Wed", "Thu"};
    public static DatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHandler(this);
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

    public static int toSeconds(int hour, int minute) {
        return (hour * 60 * 60) + (minute * 60);
    }

    public static int[] toHourMinute(int seconds) {
        int AmPm = 0;  // AM is 0, PM is 1
        int hour = (int) (seconds / (60 * 60));
        int minute = (int) ((seconds % (60 * 60)) / (60));
        if (hour > 12)
            hour += -12;
        if (hour >= 12)
            AmPm = 1;
        return new int[]{hour, minute, AmPm};
    }
}
