package com.example.coursecatalogueapp.instructor;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.Utils.Utils;
import com.example.coursecatalogueapp.auth.LoginError;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InstructorAssignActivity extends Activity {
    private static final String TAG = "InstructorAssign";

    //Declare variables
    private String courseDescription, courseCapacity, courseId, lecture1Day, lecture2Day, lecture1Time, lecture2Time;
    private FirebaseFirestore db;
    private CollectionReference coursesReference;

    //Declare UI elements
    TimePickerDialog picker;
    EditText inputCourseDescription, inputCourseCapacity, inputTime1, inputTime2;
    Spinner spinnerLectureDay1, spinnerLectureDay2;
    TextView assignCourseTitle, courseCodeText, courseNameText;
    Button assignButton;
    Boolean isUpdate = false;
    String userId;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_assign);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        coursesReference = db.collection("courses");

        //Initialize spinners
        ArrayList weekDays = new ArrayList(Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, weekDays);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLectureDay1 = findViewById(R.id.firstLectureSpinnerInput);
        spinnerLectureDay1.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, weekDays);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLectureDay2 = findViewById(R.id.secondLectureSpinnerInput);
        spinnerLectureDay2.setAdapter(adapter2);
        //

        //Initialize UI elements
        inputCourseDescription = findViewById(R.id.courseDescriptionInput);
        inputCourseCapacity = findViewById(R.id.courseCapacityInput);
        assignButton = findViewById(R.id.assignBtn);
        assignCourseTitle = findViewById(R.id.instructorAssignTitle);
        inputTime1 = findViewById(R.id.timePickerInput1);
        inputTime2 = findViewById(R.id.timePickerInput2);
        courseCodeText = findViewById(R.id.courseCodeText);
        courseNameText = findViewById(R.id.courseNameText);
        //

        //Get userId
        SharedPreferences sharedPref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        userId = sharedPref.getString(getString(R.string.user_uid_key), null);

        //Time Picker Click Listener
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
                                lecture1Time = sHour + ":";// + sMinute
                                if(sHour<10){
                                    lecture1Time = "0"+lecture1Time;
                                }
                                if(sMinute<10){
                                    lecture1Time = lecture1Time+"0"+sMinute;
                                }else{
                                    lecture1Time = lecture1Time+sMinute;
                                }
                                inputTime1.setText(lecture1Time);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        //Time Picker Click Listener
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
                                lecture2Time = sHour + ":";// + sMinute
                                if(sHour<10){
                                    lecture2Time = "0"+lecture2Time;
                                }
                                if(sMinute<10){
                                    lecture2Time = lecture2Time+"0"+sMinute;
                                }else{
                                    lecture2Time = lecture2Time+sMinute;
                                }
                                inputTime2.setText(lecture2Time);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        // Check if we are updating a course
        i = getIntent();
        isUpdate = i.getStringExtra("TAG").equals("update");
        courseId = i.getStringExtra("courseId");
        courseCodeText.setText(i.getStringExtra("courseCode"));
        courseNameText.setText(i.getStringExtra("courseName"));
        if (isUpdate) {
            // fill input fields
            assignButton.setText(R.string.update);
            assignCourseTitle.setText(R.string.update_title);
            inputCourseCapacity.setText(i.getStringExtra("courseCapacity"));
            inputCourseDescription.setText(i.getStringExtra("courseDescription"));
            spinnerLectureDay1.setSelection(weekDays.indexOf(i.getStringExtra("lecture1Day")));
            spinnerLectureDay2.setSelection(weekDays.indexOf(i.getStringExtra("lecture2Day")));
            inputTime1.setText(i.getStringExtra("lecture1Time"));
            inputTime2.setText(i.getStringExtra("lecture2Time"));
        }

        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the submitted form data
                courseDescription = inputCourseDescription.getText().toString();
                courseCapacity = inputCourseCapacity.getText().toString();
                lecture1Day = spinnerLectureDay1.getSelectedItem().toString();
                lecture2Day = spinnerLectureDay2.getSelectedItem().toString();
                lecture1Time = inputTime1.getText().toString();
                lecture2Time = inputTime2.getText().toString();
                //Validates input and gets error message
                final String invalidFormData = validateInput(courseDescription, courseCapacity, lecture1Time,lecture2Time);

                //If there is an error
                if(!invalidFormData.equals("")) {
                    //Show a snackbar with the error message
                    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.instructorAssignPage), invalidFormData, BaseTransientBottomBar.LENGTH_SHORT);
                    //Add close button
                    mySnackbar.setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
                    //Show snackbar
                    mySnackbar.show();
                } else {
                    if (isUpdate) {
                        updateCourseInfo(
                                courseId,
                                courseDescription,
                                courseCapacity,
                                lecture1Day,
                                lecture2Day,
                                lecture1Time,
                                lecture2Time,
                                getCurrentFocus());
                    } else {
                        assignCourse(
                                courseId,
                                userId,
                                courseDescription,
                                courseCapacity,
                                lecture1Day,
                                lecture2Day,
                                lecture1Time,
                                lecture2Time,
                                getCurrentFocus());
                    }
                }
            }
        });
    }

    private void updateCourseInfo(
            final String courseId,
            final String courseDescription,
            final String courseCapacity,
            final String lecture1Day,
            final String lecture2Day,
            final String lecture1Time,
            final String lecture2Time,
            final View view
    ) {
        Map<String, Object> courseInfo = new HashMap<>();
        courseInfo.put("courseDescription", courseDescription);
        courseInfo.put("courseCapacity", courseCapacity);
        courseInfo.put("lecture1Day", lecture1Day);
        courseInfo.put("lecture1Time", lecture1Time);
        courseInfo.put("lecture2Day", lecture2Day);
        courseInfo.put("lecture2Time", lecture2Time);

        coursesReference.document(courseId).set(courseInfo, SetOptions.merge());
        back(view);
    }

    public void assignCourse(
            final String courseId,
            final String courseInstructor,
            final String courseDescription,
            final String courseCapacity,
            final String lecture1Day,
            final String lecture2Day,
            final String lecture1Time,
            final String lecture2Time,
            final View view
    ) {
        //Create a map with the data to write to cloud firestore
        Map<String, Object> courseInfo = new HashMap<>();
        courseInfo.put("courseDescription", courseDescription);
        courseInfo.put("courseCapacity", courseCapacity);
        courseInfo.put("courseInstructor", userId);
        courseInfo.put("lecture1Day", lecture1Day);
        courseInfo.put("lecture1Time", lecture1Time);
        courseInfo.put("lecture2Day", lecture2Day);
        courseInfo.put("lecture2Time", lecture2Time);

        coursesReference.document(courseId).set(courseInfo, SetOptions.merge());
        Toast.makeText(InstructorAssignActivity.this, "Course added to your schedule!", Toast.LENGTH_SHORT).show();
        back(getCurrentFocus());
    }


    private void reload() { }

    private void updateUI(FirebaseUser user) { }

    /**
     * Validates the inputs of login page
     * @param courseDescription
     * @param courseCapacity
     * @param lecture1Time
     * @param lecture2Time
     * @return
     */
    private String validateInput(
            String courseDescription,
            String courseCapacity,
            String lecture1Time,
            String lecture2Time
        ) {
        //Checks if any field is empty
        if(courseDescription.isEmpty() || courseCapacity.isEmpty() || lecture1Time.isEmpty() || lecture2Time.isEmpty()) {
            return "One or more required fields are empty. ";
        }
        //Validates Time
        boolean validTime1 = Utils.isTimeValid(lecture1Time);
        boolean validTime2 = Utils.isTimeValid(lecture2Time);
        if(!validTime1) {
            inputTime1.getText().clear();
            return "One or more time fields are entered incorrectly. ";
        }
        if(!validTime2) {
            inputTime2.getText().clear();
            return "One or more time fields are entered incorrectly. ";
        }
        //Validates Capacity
        boolean validInteger = Utils.isIntValid(courseCapacity);
        if(!validInteger) {
            inputCourseCapacity.getText().clear();
            return "Course capacity information must be a number";
        }
        boolean isUnderCapacity = Integer.parseInt(courseCapacity) >= 10 && Integer.parseInt(courseCapacity) <= 300;
        if (!isUnderCapacity) {
            inputCourseCapacity.getText().clear();
            return "Course capacity must be between 10 and 300";
        }
        //Returns no error message if inputs are valid.
        return "";
    }

    /**
     * Back button function
     * @param view the current view
     */
    public void back(View view) {
        //Go back to previous activity
        this.finish();
    }
}
