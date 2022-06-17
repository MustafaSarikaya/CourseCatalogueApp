package com.example.coursecatalogueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.coursecatalogueapp.modules.User;

public class InstructorMainActivity extends AppCompatActivity {

    TextView pageTitle, loginStatus;

    User account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_main);

        pageTitle = findViewById(R.id.instructorMainTitle);
        loginStatus = findViewById(R.id.loginStatusInstructor);

        account = UserController.getInstance().getUserAccount();

        pageTitle.setText("Welcome " + account.getName());
        loginStatus.setText("Logged in as " + account.getRole());

    }
}