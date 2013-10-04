package io.github.mabdrabo.schoolassistant.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.mabdrabo.schoolassistant.R;
import io.github.mabdrabo.schoolassistant.objects.Course;
import io.github.mabdrabo.schoolassistant.objects.Project;

public class ProjectsActivity extends Activity {

    ArrayList<Project> projects;
    ArrayList<HashMap<String, String>> projectsList;
    ListView projectsListView;
    Project selectedProject;

    ArrayAdapter<String> courseSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        projects = new ArrayList<Project>();
        projectsList = new ArrayList<HashMap<String, String>>();
        projectsListView = ((ListView) findViewById(R.id.projectsListView));

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
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        projects = MainActivity.database.getAllProjects();
        projectsList = new ArrayList<HashMap<String, String>>();
        for (Project project : projects) {
            HashMap<String, String> datum = new HashMap<String, String>(2);
            datum.put("main", project.get_title());
//            datum.put("sub", assignment.get_dueDate());  // Calendar get month, day
            projectsList.add(datum);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, projectsList, android.R.layout.simple_list_item_2,
                new String[]{"main", "sub"},
                new int[]{android.R.id.text1, android.R.id.text2});
        projectsListView.setAdapter(adapter);
        projectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Project project = projects.get(position);
                viewProject(project);
            }
        });
    }


    private void add() {
        final Dialog addProjectDialog = new Dialog(this);
        addProjectDialog.setTitle("Add Project");
        addProjectDialog.setContentView(R.layout.add_project_dialog);
        addProjectDialog.show();

        final Spinner courseSpinner = (Spinner) addProjectDialog.findViewById(R.id.courseSpinner);
        courseSpinner.setAdapter(courseSpinnerAdapter);

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

    private void viewProject(Project project) {
        selectedProject = project;
        final Dialog projectDialog = new Dialog(this);
        projectDialog.setTitle("Project");
        projectDialog.setContentView(R.layout.project_dialog);

        ((TextView) projectDialog.findViewById(R.id.courseTextView)).setText("" + MainActivity.database.getCourse(selectedProject.get_courseId()).get_name());
        ((TextView) projectDialog.findViewById(R.id.titleTextView)).setText("" + selectedProject.get_title());
        ((TextView) projectDialog.findViewById(R.id.notesTextView)).setText("" + selectedProject.get_notes());

        projectDialog.show();

        projectDialog.findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        projectDialog.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }

    private void edit() {
        final Dialog editProjectDialog = new Dialog(this);
        editProjectDialog.setTitle("Edit Project");
        editProjectDialog.setContentView(R.layout.add_project_dialog);
        editProjectDialog.show();

        final Spinner courseSpinner = (Spinner) editProjectDialog.findViewById(R.id.courseSpinner);
        courseSpinner.setAdapter(courseSpinnerAdapter);
        courseSpinner.setSelection(MainActivity.get_course_spinner_position(selectedProject.get_courseId()));

        ((EditText) editProjectDialog.findViewById(R.id.projectTitleEditText)).setText(selectedProject.get_title());
        ((EditText) editProjectDialog.findViewById(R.id.projectNotesEditText)).setText(selectedProject.get_notes());

        Button addProjectButton = (Button) editProjectDialog.findViewById(R.id.addProjectButton);
        addProjectButton.setText("Update");
        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int course_id = MainActivity.courses.get(courseSpinner.getSelectedItemPosition()).get_id();
                String title  = "" + ((EditText) editProjectDialog.findViewById(R.id.projectTitleEditText)).getText();
                String note = "" + ((EditText) editProjectDialog.findViewById(R.id.projectNotesEditText)).getText();
                Project project = new Project(title);
                project.set_courseId(course_id);
                project.set_notes(note);

                MainActivity.database.updateProject(project);
                editProjectDialog.dismiss();
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
                        MainActivity.database.deleteProject(selectedProject);
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
