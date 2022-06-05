package com.example.coursecatalogueapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminMainActivity extends AppCompatActivity {

    private Button Accounts;
    private Button Courses;



    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminmainpage);

        Accounts = (Button)findViewById(R.id.Accounts);
        Courses = (Button)findViewById(R.id.Courses);

        Accounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAccounts();
            }
        });

    }
    public void openAccounts(){
        Intent intent = new Intent(this, Accounts.class);
        startActivity(intent);
        

    }



}
