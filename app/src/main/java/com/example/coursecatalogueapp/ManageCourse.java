package com.example.coursecatalogueapp;

import androidx.appcompat.app.AppCompatActivity;



import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManageCourse extends AppCompatActivity {

    TextView Header, Description;
    EditText CourseId, CourseName;
    Button addBtn, findBtn, deleteBtn;
    ListView courseListView;
    ArrayList<String> courseList;
    ArrayAdapter adapter;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_course);

        courseList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //info Layout
        CourseId = findViewById(R.id.CourseId);
        CourseName = findViewById(R.id.CourseName);

        //buttons
        addBtn = findViewById(R.id.addBtn);
        findBtn = findViewById(R.id.findBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        //ListView
        courseListView = findViewById(R.id.CourseListView);

        //button listeners
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String name = CourseName.getText().toString();
                String id = CourseId.getText().toString();

                writeCourse(id, name);

                CourseName.setText("");
                CourseId.setText("");

                viewCourses();
            }
        });

        findBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(MainActivity.this, "Add product", Toast.LENGTH_SHORT).show();
                String name = CourseName.getText().toString();
                findCourse(name);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //Toast.makeText(MainActivity.this, "Add product", Toast.LENGTH_SHORT).show();
                String name = CourseName.getText().toString();

                //Delete the course from the firebase
                CourseName.setText("");
                CourseId.setText("");
                viewCourses();
            }
        });


    }

    public void writeCourse(String courseCode, String name){
        String id = mDatabase.child("courses").push().getKey();
        Course course = new Course(courseCode, name);
        mDatabase.child("courses").child(id).child("courseCode").setValue(course.getCourseCode());
        mDatabase.child("courses").child(id).child("courseName").setValue(course.getCourseName());
    }

    private void viewCourses(){
//        courseList.clear();
//        Cursor cursor = dbHandler.getData();
//        if (cursor.getCount() == 0){
//            Toast.makeText(ManageCourse.this, "Nothing to show", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            while(cursor.moveToNext()){
//                courseList.add(cursor.getString(1));
//            }
//        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseList);
        courseListView.setAdapter(adapter);


    }

    private void findCourse(String name){
        courseList.clear();
//        Cursor cursor = dbHandler.getData();
//
//        if (cursor.getCount() == 0){
//            Toast.makeText(ManageCourse.this, "No result", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            while(cursor.moveToNext()){
//                if(cursor.getString(1).equals(name)){
//                    courseList.add(cursor.getString(1));
//                }
//            }
//        }


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseList);
        courseListView.setAdapter(adapter);


    }
}
