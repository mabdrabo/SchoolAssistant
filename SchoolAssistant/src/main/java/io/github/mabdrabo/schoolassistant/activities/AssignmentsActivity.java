package io.github.mabdrabo.schoolassistant.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
    Assignment selectedAssignment;

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
            case R.id.action_settings:
                startActivity(new Intent().setClass(this, SettingsActivity.class));
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
        assignmentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Assignment assignment = assignments.get(position);
                view(assignment);
            }
        });
    }

    private void add() {
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

    private void view(Assignment assignment) {
        selectedAssignment = assignment;
        final Dialog assignmentDialog = new Dialog(this);
        assignmentDialog.setTitle("Assignment");
        assignmentDialog.setContentView(R.layout.assignment_dialog);

        ((TextView) assignmentDialog.findViewById(R.id.courseTextView)).setText("" + MainActivity.database.getCourse(selectedAssignment.get_courseId()).get_name());
        ((TextView) assignmentDialog.findViewById(R.id.descriptionTextView)).setText("" + selectedAssignment.get_description());
        ((TextView) assignmentDialog.findViewById(R.id.notesTextView)).setText("" + selectedAssignment.get_notes());

        assignmentDialog.show();

        assignmentDialog.findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        assignmentDialog.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    private void edit() {
        final Dialog editAssignmentDialog = new Dialog(this);
        editAssignmentDialog.setTitle("Edit Assignment");
        editAssignmentDialog.setContentView(R.layout.add_assignment_dialog);
        editAssignmentDialog.show();

        final Spinner courseSpinner = (Spinner) editAssignmentDialog.findViewById(R.id.courseSpinner);
        courseSpinner.setAdapter(courseSpinnerAdapter);
        courseSpinner.setSelection(MainActivity.get_course_spinner_position(selectedAssignment.get_courseId()));

        ((EditText) editAssignmentDialog.findViewById(R.id.AssignmentEditText)).setText(selectedAssignment.get_description());
        ((EditText) editAssignmentDialog.findViewById(R.id.AssignmentNotesEditText)).setText(selectedAssignment.get_notes());

        Button addAssignmentButton = (Button) editAssignmentDialog.findViewById(R.id.addAssignmentButton);
        addAssignmentButton.setText("Update");
        addAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int course_id = MainActivity.courses.get(courseSpinner.getSelectedItemPosition()).get_id();
                String description = "" + ((EditText) editAssignmentDialog.findViewById(R.id.AssignmentEditText)).getText();
                String notes = "" + ((EditText) editAssignmentDialog.findViewById(R.id.AssignmentNotesEditText)).getText();
                DatePicker datePicker = (DatePicker) editAssignmentDialog.findViewById(R.id.datePicker);
                TimePicker timePicker = (TimePicker) editAssignmentDialog.findViewById(R.id.timePicker);

                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                long deadline = calendar.getTimeInMillis();

                Assignment assignment = new Assignment(description);
                assignment.set_id(selectedAssignment.get_id());
                assignment.set_courseId(course_id);
                assignment.set_notes(notes);
                assignment.set_dueDate(deadline);

                MainActivity.database.addAssignment(assignment);
                editAssignmentDialog.dismiss();
                onResume();
            }
        });
    }

    private void delete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.database.deleteAssignment(selectedAssignment);
                        dialog.dismiss();
                        onResume();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
