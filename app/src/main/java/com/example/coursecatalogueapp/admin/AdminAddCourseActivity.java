package com.example.coursecatalogueapp.admin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.Utils.Function;
import com.example.coursecatalogueapp.Utils.Utils;
import com.example.coursecatalogueapp.modules.Course;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class AdminAddCourseActivity extends Activity {

    private static final String TAG = "Register";

    //Declare variables
    private String courseName;
    private String courseCode, courseInstructor;
    private FirebaseFirestore db;
    private CollectionReference coursesReference;
    //Declare UI elements
    EditText inputCourseName, inputCourseCode;
    TextView addCourseTitle;
    Button addButton;
    Boolean isUpdate = false;
    String courseId;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_course);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        coursesReference = db.collection("courses");

        //Initialize UI elements
        inputCourseName = findViewById(R.id.addCourseName);
        inputCourseCode = findViewById(R.id.addCourseCode);
        addButton = findViewById(R.id.addCourseBtn);
        addCourseTitle = findViewById(R.id.addCourseTitle);

        i = getIntent();
        if (i.getExtras() != null) {
             isUpdate = i.getStringExtra("TAG").equals("update");
             addButton.setText(R.string.update);
             addCourseTitle.setText(R.string.update_title);
             inputCourseCode.setText(i.getStringExtra("courseCode"));
             inputCourseName.setText(i.getStringExtra("courseName"));
             courseId = i.getStringExtra("courseId");
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the submitted form data
                courseName = inputCourseName.getText().toString();
                courseCode = inputCourseCode.getText().toString();
                final String loginError = validateInput(courseName, courseCode);

                //If there is an error
                if(!loginError.equals("")) {
                    //Show a snackbar with the error message
                    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.adminAddCourseLayout), loginError, BaseTransientBottomBar.LENGTH_SHORT);
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
                        updateCourse(courseCode, courseName, courseId, getCurrentFocus());
                    } else {
                        createCourse(courseName, courseCode, getCurrentFocus());
                    }
                }
            }
        });
    }

    private void updateCourse(
            final String courseCode,
            final String courseName,
            final String courseId,
            final View view
    ) {
        Map<String, Object> courseInfo = new HashMap<>();
        courseInfo.put("courseName", courseName);
        courseInfo.put("courseCode", courseCode);

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
        courseInfo.put("courseInstructor", "");
        courseInfo.put("students", new LinkedList<String>());

        coursesReference.add(courseInfo);
        back(view);
    }


    private void reload() { }

    private void updateUI(FirebaseUser user) { }

    /**
     *
     * @param courseName
     * @param courseCode
     * @return
     */
    private String validateInput(String courseName, String courseCode) {
        //Checks if any field is empty
        if(courseName.isEmpty() || courseCode.isEmpty()) {
            return "One or more required fields are empty. ";
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
