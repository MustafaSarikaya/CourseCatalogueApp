package com.example.coursecatalogueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class Accounts extends AppCompatActivity {

    ArrayList<String> Instructors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        Instructors = new ArrayList<>();

    }
}