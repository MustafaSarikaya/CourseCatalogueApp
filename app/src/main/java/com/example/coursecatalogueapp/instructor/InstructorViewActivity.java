package com.example.coursecatalogueapp.instructor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.coursecatalogueapp.R;

import java.util.ArrayList;

public class InstructorViewActivity extends Activity {

    private String students;
    private ArrayList<String> studentList;
    private Intent i;
    private ArrayAdapter adapter;
    private ListView studentListView;
    private String[] s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_view);
        studentListView = findViewById(R.id.listView);
        studentList = new ArrayList<>();
        i = getIntent();
        students = i.getStringExtra("students");
        System.out.println(students);

        s = students.split(",");
        for(int i = 0; i < s.length; i++){
            studentList.add(s[i]);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        studentListView.setAdapter(adapter);
    }
}