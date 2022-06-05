package com.example.coursecatalogueapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminMainActivity extends Activity {

    private Button accountsButton;
    private Button Courses;



    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminmainpage);

        accountsButton = (Button)findViewById(R.id.Accounts);
        Courses = (Button)findViewById(R.id.Courses);

        accountsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminMainActivity.this, Accounts.class);
                startActivity(intent);
            }
        });

    }




}
