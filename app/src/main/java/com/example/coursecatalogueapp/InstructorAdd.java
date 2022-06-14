package com.example.coursecatalogueapp;

import static com.example.coursecatalogueapp.InstructorsListActivity.addInstructor;
import static com.example.coursecatalogueapp.InstructorsListActivity.names;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.coursecatalogueapp.modules.Instructor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InstructorAdd extends AppCompatActivity {


    FloatingActionButton fabSave;
    EditText firstName;
    EditText lastName;
    EditText emailAddress;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.instructor_add);

        fabSave = findViewById(R.id.fabSave);
        firstName = findViewById(R.id.IAFirstName);
        lastName = findViewById(R.id.IALastName);
        emailAddress = findViewById(R.id.IAEmailAddress);
        password = findViewById(R.id.IAPassword);




        //fabSave click
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtFirstName = firstName.getText().toString();
                String txtLastName = lastName.getText().toString();
                String txtEmailAddress = emailAddress.getText().toString();
                String txtPassword = password.getText().toString();
                Bundle extras=getIntent().getExtras();
                if(extras ==null) {
                    // String value = extras.getString("TAG");


                    Intent intent = new Intent(v.getContext(), InstructorsListActivity.class);
                    intent.putExtra("name", txtFirstName);
                    addInstructor(txtFirstName,txtLastName,txtEmailAddress,txtPassword);
                    startActivity(intent);

                }else{
                    Intent intent = new Intent(v.getContext(), StudentListActivity.class);
                    intent.putExtra("name", txtFirstName);
                    addInstructor(txtFirstName,txtLastName,txtEmailAddress,txtPassword);
                    startActivity(intent);
                }
            }
        });
    }
}
