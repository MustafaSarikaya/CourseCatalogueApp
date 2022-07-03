package com.example.coursecatalogueapp.instructor;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.coursecatalogueapp.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InstructorAssignActivity extends Activity {
    private static final String TAG = "InstructorAssign";

    //Declare variables
    private String courseDescription;
    private String courseCapacity;
    private FirebaseFirestore db;
    private CollectionReference coursesReference;
    //Declare UI elements
    TimePickerDialog picker;
    EditText inputCourseDescription, inputCourseCapacity, inputTime1, inputTime2;
    Spinner spinnerLectureDay1, spinnerLectureDay2;
    TextView assignCourseTitle;
    Button assignButton;
    Boolean isUpdate;
    String courseId;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_assign);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        coursesReference = db.collection("courses");

        //Initialize UI elements
        inputCourseDescription = findViewById(R.id.courseDescriptionInput);
        inputCourseCapacity = findViewById(R.id.courseCapacityInput);
        assignButton = findViewById(R.id.assignBtn);
        assignCourseTitle = findViewById(R.id.instructorAssignTitle);
        spinnerLectureDay1 = findViewById(R.id.firstLectureSpinnerInput);
        spinnerLectureDay2 = findViewById(R.id.secondLectureSpinnerInput);
        inputTime1 = findViewById(R.id.timePickerInput1);
        inputTime2 = findViewById(R.id.timePickerInput2);

        i = getIntent();
        if (i.getExtras() != null) {
            isUpdate = i.getStringExtra("TAG").equals("update");
            assignButton.setText(R.string.update);
            assignCourseTitle.setText(R.string.update_title);
            inputCourseCapacity.setText(i.getStringExtra("courseCode"));
            inputCourseDescription.setText(i.getStringExtra("courseName"));
            courseId = i.getStringExtra("courseId");
        }

        inputTime1.setInputType(InputType.TYPE_NULL);
        inputTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(InstructorAssignActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                inputTime1.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        inputTime2.setInputType(InputType.TYPE_NULL);
        inputTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(InstructorAssignActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                inputTime1.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the submitted form data
                courseDescription = inputCourseDescription.getText().toString();
                courseCapacity = inputCourseCapacity.getText().toString();
                String Lecture1Day = spinnerLectureDay1.getSelectedItem().toString();
                String Lecture2Day = spinnerLectureDay2.getSelectedItem().toString();

                if (isUpdate) {
                    updateCourseInfo(
                            courseDescription,
                            courseCapacity,
                            courseId,
                            Lecture1Day,
                            Lecture2Day,
                            getCurrentFocus());
                } else {
                    createCourse(courseDescription, courseCapacity, getCurrentFocus());
                }
            }
        });
    }

    private void updateCourseInfo(
            final String courseId,
            final String courseDescription,
            final String courseCapacity,
            final String Lecture1Day,
            final String Lecture2Day,

            final View view
    ) {
        Map<String, Object> courseInfo = new HashMap<>();
        courseInfo.put("courseName", courseDescription);
        courseInfo.put("courseCode", courseCapacity);

        coursesReference.document(courseId).set(courseInfo);
        back(view);
    }

    public void createCourse(
            final String courseName,
            final String courseCode,
            final View view
    ) {
        //Create a map with the data to write to cloud firestore
        Map<String, Object> courseInfo = new HashMap<>();
        courseInfo.put("courseName", courseName);
        courseInfo.put("courseCode", courseCode);

        coursesReference.add(courseInfo);
        back(view);
    }


    private void reload() { }

    private void updateUI(FirebaseUser user) { }

    /**
     * Back button function
     * @param view the current view
     */
    public void back(View view) {
        //Go back to previous activity
        this.finish();
    }
}
