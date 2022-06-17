package com.example.coursecatalogueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.coursecatalogueapp.modules.User;

public class StudentMainActivity extends AppCompatActivity {

    TextView pageTitle, loginStatus;

    User account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        pageTitle = findViewById(R.id.studentMainTitle);
        loginStatus = findViewById(R.id.loginStatusStudent);

        account = UserController.getInstance().getUserAccount();

        pageTitle.setText("Welcome " + account.getName());
        loginStatus.setText("Logged in as " + account.getRole());

    }
}
