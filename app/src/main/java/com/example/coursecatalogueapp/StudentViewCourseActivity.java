package com.example.coursecatalogueapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.coursecatalogueapp.modules.Course;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentViewCourseActivity extends AppCompatActivity {


    ListView listView;
    public List<Course> mycourses;
    static StudentViewAdapter adapter;
    private FirebaseFirestore firestore;
    private CollectionReference courseReference;
    String userName;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_course);
        firestore = FirebaseFirestore.getInstance();
        courseReference = firestore.collection("courses");
        listView = findViewById(R.id.listView);
        mycourses = new ArrayList<>();
        i = getIntent();
        userName = i.getStringExtra("userName");

    }

    @Override
    protected void onStart() {
        super.onStart();
        courseReference.orderBy("courseInstructor").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mycourses.clear();
                listView.setAdapter(adapter);
                for (QueryDocumentSnapshot doc : value) {
                    String courseName = doc.getString("courseName");
                    String courseCode = doc.getString("courseCode");
                    String students = doc.getString("students");
                    String id = doc.getId();

                    if(isEnrolled(userName, students)){
                        Course course = new Course(courseCode, courseName, id, students);
                        mycourses.add(course);
                    }
                }
                System.out.println("The size of mycourses: " + mycourses.size());
                setUpList(mycourses, listView, userName);
            }
        });
    }

    private void setUpList(final List<Course> courses, ListView listView, String userName){
        adapter = new StudentViewAdapter(StudentViewCourseActivity.this,mycourses, userName);
        listView.setAdapter(adapter);
    }

    private boolean isEnrolled(String userName, String studentList){

        if(studentList != null) {
            String[] s = studentList.split(",");
            for (int i = 0; i < s.length; i++) {
                if (s[i].equals(userName)) {
                    return true;
                }
            }
        }
        return false;
    }
}