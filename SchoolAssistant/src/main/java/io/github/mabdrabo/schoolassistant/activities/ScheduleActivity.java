package io.github.mabdrabo.schoolassistant.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import java.util.ArrayList;

import io.github.mabdrabo.schoolassistant.R;
import io.github.mabdrabo.schoolassistant.objects.Course;
import io.github.mabdrabo.schoolassistant.objects.Section;

public class ScheduleActivity extends Activity {

    ArrayAdapter<String> courseSpinnerAdapter;
    ArrayAdapter<String> daySpinnerAdapter;
    ArrayAdapter<String> timeSpinnerAdapter;

    int selected_course_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
    }


    @Override
    protected void onResume() {
        super.onResume();
        GridView gridView = (GridView) findViewById(R.id.gridView);

        String[] classes_names = new String[42];
        int classes_names_counter = 0;
        classes_names[classes_names_counter++] = " ";

        for (int i=0; i<5; i++)
            classes_names[classes_names_counter++] = MainActivity.SLOT_START_TIME[i];

        ArrayList<Section> allClasses = MainActivity.database.getAllSections();
        String[][] day_class = new String[6][5];
        for (Section section : allClasses) {
            day_class[section.get_day()][section.get_time()] = MainActivity.database.getCourse(section.get_courseId()).get_name();
            day_class[section.get_day()][section.get_time()] += " " + section.get_place();
        }

        for (int i=0; i<6; i++) {
            classes_names[classes_names_counter++] = MainActivity.DAY_OF_WEEK_SHORT[i];
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

        ArrayAdapter<String> classesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, classes_names);
        gridView.setAdapter(classesAdapter);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.master, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add:
                add();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void add() {
        final Dialog addSectionDialog = new Dialog(this);
        addSectionDialog.setTitle("Add Class");
        addSectionDialog.setContentView(R.layout.add_class_dialog);
        addSectionDialog.show();

        final Spinner courseSpinner = (Spinner) addSectionDialog.findViewById(R.id.courseSpinner);
        courseSpinner.setAdapter(courseSpinnerAdapter);

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
}
