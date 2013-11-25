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
import java.util.Formatter;
import java.util.HashMap;

import io.github.mabdrabo.schoolassistant.R;
import io.github.mabdrabo.schoolassistant.objects.Course;
import io.github.mabdrabo.schoolassistant.objects.Quiz;

public class QuizzesActivity extends Activity {

    ArrayList<Quiz> quizzes;
    ArrayList<HashMap<String, String>> quizzesList;
    ListView quizzesListView;
    Quiz selectedQuiz;

    ArrayAdapter<String> courseSpinnerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes);

        quizzes = new ArrayList<Quiz>();
        quizzesList = new ArrayList<HashMap<String, String>>();
        quizzesListView = ((ListView) findViewById(R.id.quizzesListView));

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
        quizzes = MainActivity.database.getAllQuizzes();
        quizzesList = new ArrayList<HashMap<String, String>>();
        for (Quiz quiz : quizzes) {
            HashMap<String, String> datum = new HashMap<String, String>(2);
            Formatter formatter = new Formatter();
            datum.put("main", "" + formatter.format("%s - %tF", MainActivity.database.getCourse(quiz.get_courseId()).get_name(),  MainActivity.getDate(quiz.get_date())));
            formatter.close();
            formatter = new Formatter();
            datum.put("sub", "" + formatter.format("%s", quiz.get_place()));
            formatter.close();
            quizzesList.add(datum);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, quizzesList, android.R.layout.simple_list_item_2,
                new String[]{"main", "sub"},
                new int[]{android.R.id.text1, android.R.id.text2});
        quizzesListView.setAdapter(adapter);
        quizzesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view(quizzes.get(position));
            }
        });
    }


    private void add() {
        final Dialog addQuizDialog = new Dialog(this);
        addQuizDialog.setTitle("Add Quiz");
        addQuizDialog.setContentView(R.layout.add_quiz_dialog);
        addQuizDialog.show();

        final Spinner courseSpinner = (Spinner) addQuizDialog.findViewById(R.id.courseSpinner);
        courseSpinner.setAdapter(courseSpinnerAdapter);

        Button addQuizButton = (Button) addQuizDialog.findViewById(R.id.addQuizButton);
        addQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int course_id = MainActivity.courses.get(courseSpinner.getSelectedItemPosition()).get_id();
                String place = "" + ((EditText) addQuizDialog.findViewById(R.id.QuizPlaceEditText)).getText();
                DatePicker datePicker = (DatePicker) addQuizDialog.findViewById(R.id.datePicker);
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                long date = calendar.getTimeInMillis();

                Quiz quiz = new Quiz(place);
                quiz.set_courseId(course_id);
                quiz.set_date(date);

                MainActivity.database.addQuiz(quiz);
                addQuizDialog.dismiss();
                onResume();
            }
        });
    }


    private void view(Quiz quiz) {
        selectedQuiz = quiz;
        final Dialog quizDialog = new Dialog(this);
        quizDialog.setTitle("Quiz");
        quizDialog.setContentView(R.layout.quiz_dialog);

        ((TextView) quizDialog.findViewById(R.id.courseTextView)).setText("" + MainActivity.database.getCourse(selectedQuiz.get_courseId()).get_name());
        Formatter formatter = new Formatter();
        ((TextView) quizDialog.findViewById(R.id.dateTextView)).setText("" + formatter.format("%tF", MainActivity.getDate(quiz.get_date())));
        formatter.close();
        ((TextView) quizDialog.findViewById(R.id.placeTextView)).setText("" + selectedQuiz.get_place());

        quizDialog.show();

        quizDialog.findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        quizDialog.findViewById(R.id.deleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
    }


    private void edit() {
        final Dialog editQuizDialog = new Dialog(this);
        editQuizDialog.setTitle("Edit Quiz");
        editQuizDialog.setContentView(R.layout.add_quiz_dialog);
        editQuizDialog.show();

        final Spinner courseSpinner = (Spinner) editQuizDialog.findViewById(R.id.courseSpinner);
        courseSpinner.setAdapter(courseSpinnerAdapter);
        courseSpinner.setSelection(MainActivity.get_course_spinner_position(selectedQuiz.get_courseId()));

        ((EditText) editQuizDialog.findViewById(R.id.QuizPlaceEditText)).setText(selectedQuiz.get_place());
        Calendar date = MainActivity.getDate(selectedQuiz.get_date());
        ((DatePicker) editQuizDialog.findViewById(R.id.datePicker)).updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));

        Button addQuizButton = (Button) editQuizDialog.findViewById(R.id.addQuizButton);
        addQuizButton.setText("Update");
        addQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int course_id = MainActivity.courses.get(courseSpinner.getSelectedItemPosition()).get_id();
                String place = "" + ((EditText) editQuizDialog.findViewById(R.id.QuizPlaceEditText)).getText();
                DatePicker datePicker = (DatePicker) editQuizDialog.findViewById(R.id.datePicker);
                TimePicker timePicker = (TimePicker) editQuizDialog.findViewById(R.id.timePicker);
                Calendar calendar = Calendar.getInstance();
                calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                long date = calendar.getTimeInMillis();

                Quiz quiz = new Quiz(place);
                quiz.set_id(selectedQuiz.get_id());
                quiz.set_courseId(course_id);
                quiz.set_place(place);
                quiz.set_date(date);

                MainActivity.database.updateQuiz(quiz);
                editQuizDialog.dismiss();
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
                        MainActivity.database.deleteQuiz(selectedQuiz);
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
