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
import com.example.coursecatalogueapp.modules.Course;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class AdminAddCourseActivity extends Activity {

    private static final String TAG = "Register";

    //Declare variables
    private String courseName;
    private String courseCode;
    private FirebaseFirestore db;
    private CollectionReference coursesReference;
    //Declare UI elements
    EditText inputCourseName, inputCourseCode;
    Button addButton;



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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the submitted form data
                courseName = inputCourseName.getText().toString();
                courseCode = inputCourseCode.getText().toString();

                createCourse(courseName,courseCode, getCurrentFocus());
            }
        });
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            reload();
//        }
//    }

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
