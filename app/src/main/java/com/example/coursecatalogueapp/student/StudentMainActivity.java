package com.example.coursecatalogueapp.student;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coursecatalogueapp.R;

public class StudentMainActivity extends Activity {

    TextView pageTitle, loginStatus;
    String userName, userRole;
    private Button Courses,Enroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        pageTitle = findViewById(R.id.studentMainTitle);
        loginStatus = findViewById(R.id.loginStatusStudent);

        SharedPreferences sharedPref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        userName = sharedPref.getString(getString(R.string.user_name_key), null);
        userRole = sharedPref.getString(getString(R.string.user_role_key), null);

        pageTitle.setText("Welcome " + userName);
        loginStatus.setText("Logged in as " + userRole);
        Courses = (Button)findViewById(R.id.MyCourses);
        Courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(StudentMainActivity.this, StudentCourseListActivity.class);
                startActivity(intent);
            }
        });

        Enroll = (Button)findViewById(R.id.Enroll);
        Enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(StudentMainActivity.this, StudentEnrollListActivity.class);
                startActivity(intent);
            }
        });

    }
}