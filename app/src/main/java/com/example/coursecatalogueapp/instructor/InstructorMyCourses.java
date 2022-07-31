package com.example.coursecatalogueapp.instructor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.instructor.adapters.InstructorMyCoursesAdapter;
import com.example.coursecatalogueapp.modules.Course;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InstructorMyCourses extends Activity {
    ListView listView;

    public List<Course> mycourses;
    static InstructorMyCoursesAdapter adapter;
    String userId;

    private FirebaseFirestore firestore;
    private CollectionReference courseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_my_courses);
        firestore = FirebaseFirestore.getInstance();
        courseReference = firestore.collection("courses");
        listView = findViewById(R.id.listView);
        mycourses=new ArrayList<>();

        SharedPreferences sharedPref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        userId = sharedPref.getString(getString(R.string.user_uid_key), null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        courseReference.whereEqualTo("courseInstructor", userId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mycourses.clear();
                listView.setAdapter(adapter);
                for(QueryDocumentSnapshot doc: value) {
                    String courseName = doc.getString("courseName");
                    String courseCode = doc.getString("courseCode");
                    String courseInstructor = doc.getString("courseInstructor");
                    String courseDescription = doc.getString("courseDescription");
                    String courseCapacity = doc.getString("courseCapacity");
                    String lecture1Day = doc.getString("lecture1Day");
                    String lecture1Time = doc.getString("lecture1Time");
                    String lecture2Day = doc.getString("lecture2Day");
                    String lecture2Time = doc.getString("lecture2Time");
                    String students = doc.getString("students");

                    String id = doc.getId();

                    Course course = new Course(courseCode, courseName, id, students);

                    course.setCourseInstructor(courseInstructor);
                    course.setCourseDescription(courseDescription);
                    course.setCourseCapacity(courseCapacity);
                    course.setLecture1Day(lecture1Day);
                    course.setLecture1Time(lecture1Time);
                    course.setLecture2Day(lecture2Day);
                    course.setLecture2Time(lecture2Time);

                    mycourses.add(course);
                }
                setUpList(mycourses, listView);
            }
        });
    }

    private void setUpList(final List<Course> courses, ListView listView){
        adapter = new InstructorMyCoursesAdapter(InstructorMyCourses.this,mycourses);
        listView.setAdapter(adapter);
    }

}
