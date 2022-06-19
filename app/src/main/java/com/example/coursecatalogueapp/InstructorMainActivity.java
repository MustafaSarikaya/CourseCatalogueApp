package com.example.coursecatalogueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.coursecatalogueapp.modules.User;

public class InstructorMainActivity extends Activity {

    TextView pageTitle, loginStatus;

    String userName, userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_main);

        pageTitle = findViewById(R.id.instructorMainTitle);
        loginStatus = findViewById(R.id.loginStatusInstructor);

        SharedPreferences sharedPref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        userName = sharedPref.getString(getString(R.string.user_name_key), null);
        userRole = sharedPref.getString(getString(R.string.user_role_key), null);

        pageTitle.setText("Welcome " + userName);
        loginStatus.setText("Logged in as " + userRole);

    }
}