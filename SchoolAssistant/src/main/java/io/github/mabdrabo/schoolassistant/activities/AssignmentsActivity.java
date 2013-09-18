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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import io.github.mabdrabo.schoolassistant.R;
import io.github.mabdrabo.schoolassistant.objects.Assignment;
import io.github.mabdrabo.schoolassistant.objects.Course;

public class AssignmentsActivity extends Activity {

    ArrayList<Assignment> assignments;
    ArrayList<HashMap<String, String>> assignmentsList;
    ListView assignmentsListView;

    ArrayAdapter<String> courseSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        assignments = new ArrayList<Assignment>();
        assignmentsList = new ArrayList<HashMap<String, String>>();
        assignmentsListView = ((ListView) findViewById(R.id.assignmentsListView));

        ArrayList<Course> courses;
        courses = MainActivity.database.getAllCourses();
        ArrayList<String> courses_names = new ArrayList<String>();
        for (Course course : courses)
            courses_names.add(course.get_name());
        courseSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courses_names);
        courseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.assignments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add_assignment:
                addAssignment();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        assignments = MainActivity.database.getAllAssignments();
        assignmentsList = new ArrayList<HashMap<String, String>>();
        for (Assignment assignment : assignments) {
            HashMap<String, String> datum = new HashMap<String, String>(2);
            datum.put("main", assignment.get_description());
//            datum.put("sub", assignment.get_dueDate());  // Calendar get month, day
            assignmentsList.add(datum);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, assignmentsList, android.R.layout.simple_list_item_2,
                new String[]{"main", "sub"},
                new int[]{android.R.id.text1, android.R.id.text2});
        assignmentsListView.setAdapter(adapter);
    }

    private void addAssignment() {
        final Dialog addAssignmentDialog = new Dialog(this);
        addAssignmentDialog.setTitle("Add Assignment");
        addAssignmentDialog.setContentView(R.layout.add_assignment_dialog);

        final Spinner courseSpinner = (Spinner) addAssignmentDialog.findViewById(R.id.courseSpinner);
        courseSpinner.setAdapter(courseSpinnerAdapter);

        addAssignmentDialog.show();

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

    private void viewAssignment() {

    }
    
}
