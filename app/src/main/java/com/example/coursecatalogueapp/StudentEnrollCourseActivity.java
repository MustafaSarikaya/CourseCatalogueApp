package com.example.coursecatalogueapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class StudentEnrollCourseActivity extends AppCompatActivity {

    static StudentErollAdapter adapter;
    ListView courseList;
    List<Course> courses;
    List<String> courseTimes;
    SearchView search;
    Intent i;
    String userName;


    private FirebaseFirestore firestore;
    private CollectionReference courseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_enroll_course);


        firestore = FirebaseFirestore.getInstance();
        courseReference = firestore.collection("courses");
        search = findViewById(R.id.searchView);
        courseList = findViewById(R.id.listview);
        courses = new ArrayList<>();
        courseTimes = new ArrayList<>();
        i = getIntent();
        userName = i.getStringExtra("userName");

    }

    // Search bar
    protected void onStart(){
        super.onStart();
        if (search!= null){
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    find(newText);
                    return true;
                }
            });
        }
        courseReference.orderBy("courseInstructor").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                courses.clear();
                courseList.setAdapter(adapter);
                for(QueryDocumentSnapshot doc: value) {
                    String courseName = doc.getString("courseName");
                    String courseCode = doc.getString("courseCode");
                    String lecture1Day = doc.getString("lecture1Day");
                    String lecture1Time = doc.getString("lecture1Time");
                    String lecture2Day = doc.getString("lecture2Day");
                    String lecture2Time = doc.getString("lecture2Time");
                    String students = doc.getString("students");
                    String id = doc.getId();

                    Course course = new Course(courseCode, courseName, id);
                    course.setStudents(students);
                    course.setLecture1Day(lecture1Day);
                    course.setLecture1Time(lecture1Time);
                    course.setLecture2Day(lecture2Day);
                    course.setLecture2Time(lecture2Time);
                    if(isEnrolled(userName, students)) {
                        courseTimes.add(lecture1Day + lecture1Time);
                        courseTimes.add(lecture2Day + lecture2Time);
                    }
                    courses.add(course);
                }
                setUpList(courses, courseList, userName, courseTimes);
            }
        });
    }

    //Search bar
    private void find(String str){
        List<Course> myList = new ArrayList<>();
        for(Course object: courses){
            if(object.getCourseName().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
            else if(object.getCourseCode().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
            setUpList(myList, courseList, userName, courseTimes);
        }
    }

    private void setUpList(final List<Course> courses, ListView listView, String n, List<String> courseTimes){
        adapter = new StudentErollAdapter(StudentEnrollCourseActivity.this, courses, n, courseTimes);
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