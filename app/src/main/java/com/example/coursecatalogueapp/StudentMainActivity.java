package com.example.coursecatalogueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.coursecatalogueapp.modules.User;

public class StudentMainActivity extends Activity {

    TextView pageTitle, loginStatus;
    Button btn_vec, btn_ec;
    String userName, userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        pageTitle = findViewById(R.id.studentMainTitle);
        loginStatus = findViewById(R.id.loginStatusStudent);
        btn_vec = (Button)findViewById(R.id.btn_view_enrolled_courses);
        btn_ec = (Button)findViewById(R.id.btn_enroll_a_course);

        SharedPreferences sharedPref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        userName = sharedPref.getString(getString(R.string.user_name_key), null);
        userRole = sharedPref.getString(getString(R.string.user_role_key), null);

        pageTitle.setText("Welcome " + userName);
        loginStatus.setText("Logged in as " + userRole);

        btn_vec.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(StudentMainActivity.this, StudentViewCourseActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

        btn_ec.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(StudentMainActivity.this, StudentEnrollCourseActivity.class);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });

    }
}
