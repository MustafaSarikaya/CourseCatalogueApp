package com.example.coursecatalogueapp.instructor;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.modules.Course;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Instructor_MyCourses extends Activity {
    ListView listView;

    public static List<Course> mycourses;
    static my_courses_adapter adapter;



    private FirebaseFirestore firestore;
    private static CollectionReference courseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);
        firestore = FirebaseFirestore.getInstance();
        courseReference = firestore.collection("mycourses");
        listView = findViewById(R.id.listView);
        mycourses=new ArrayList<>();

      //  setUpList(mycourses, listView);
        getMyCourses();


    }

    public void getMyCourses(){
        courseReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mycourses.clear();
                listView.setAdapter(adapter);
                for(QueryDocumentSnapshot doc: value) {
                    String courseName = doc.getString("courseName");
                    String courseCode = doc.getString("courseCode");
                    String courseInstructor = doc.getString("courseInstructor");
                    String id = doc.getId();
                    Course course = new Course(courseCode, courseName, id,courseInstructor);
                    course.setCourseInstructor(courseInstructor);
                    String username = InstructorMainActivity.getuserName();

                    if(courseInstructor.contains(username)) {

                        mycourses.add(course);
                    }
                }

                setUpList(mycourses, listView);
            }

        });
    }
//    public static void addCourse(Course course) {
//        mycourses.add(course);
//
//
//    }
    private void setUpList(final List<Course> courses, ListView listView){
        adapter = new my_courses_adapter(Instructor_MyCourses.this,mycourses);
        listView.setAdapter(adapter);
    }
}
