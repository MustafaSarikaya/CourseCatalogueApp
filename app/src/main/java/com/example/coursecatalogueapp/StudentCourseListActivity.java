package com.example.coursecatalogueapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.coursecatalogueapp.admin.AdminCourseListActivity;
import com.example.coursecatalogueapp.admin.AdminMainActivity;
import com.example.coursecatalogueapp.admin.adapters.AdminCourseListAdapter;
import com.example.coursecatalogueapp.admin.adapters.StudentCourseListAdapter;
import com.example.coursecatalogueapp.modules.Course;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentCourseListActivity extends AppCompatActivity {

    static StudentCourseListAdapter adapter;
    ImageView  HomeButton;
    Intent intent;

    List<Course> courses;
    String studentID;
    List<String> studentEnrollments;
    static private ListView courseList;
    private FirebaseFirestore firestore;
    private static CollectionReference courseReference;
    private static CollectionReference studentEnrollmentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_list);


        //Set up firestore
        firestore = FirebaseFirestore.getInstance();
        courseReference = firestore.collection("courses");
        studentEnrollmentReference = firestore.collection("student_enrollments");

        //Set up the employee and customer lists
        courses = new ArrayList<>();
        studentEnrollments = new ArrayList<>();

        courseList = findViewById(R.id.listview);

        HomeButton = findViewById(R.id.Home);

        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentCourseListActivity.this, StudentMainActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        studentID = sharedPref.getString(getString(R.string.user_uid_key), null);


    }

    @Override
    protected void onStart() {
        super.onStart();


        studentEnrollmentReference
                .whereEqualTo("studentID",studentID)
                .addSnapshotListener(new EventListener<QuerySnapshot>(){
                    @Override
                    public void onEvent(@Nullable QuerySnapshot val, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            studentEnrollments.clear();

                            for (QueryDocumentSnapshot document :val) {
                                if(document.exists()) {
                                    //Get the basic user data from the document
                                    String courseName = document.getString("courseID");
                                    studentEnrollments.add(courseName);
                                }
                            }
                            if (!studentEnrollments.isEmpty()) {
                                courseReference
                                        .whereIn("courseCode", studentEnrollments)
                                        .orderBy("courseCode").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                if (error != null) {
                                                    Log.w("UsersActivity", "Listen failed.", error);
                                                    return;
                                                }

                                                //Clear the list to prepare for loading of new data
                                                courses.clear();
                                                courseList.setAdapter(adapter);
                                                //Iterate through the documents read from firestore
                                                for(QueryDocumentSnapshot doc : value) {
                                                    //If the document exists
                                                    if(doc.exists()) {
                                                        //Get the basic user data from the document
                                                        String courseName = doc.getString("courseName");
                                                        String courseCode = doc.getString("courseCode");
                                                        String id = doc.getId();
                                                        //Create an course Object
                                                        Course course = new Course(courseCode, courseName,id);
                                                        course.setLecture1Day(doc.getString("lecture1Day"));
                                                        course.setLecture1Time(doc.getString("lecture1Time"));
                                                        course.setLecture2Day(doc.getString("lecture2Day"));
                                                        course.setLecture2Time(doc.getString("lecture2Time"));

                                                        //Add the course to the list
                                                        courses.add(course);
                                                    }
                                                }
                                                //Set up the list in the UI
                                                setUpList(courses, courseList);
                                            }
                                        });

                            } else {
                                                courses.clear();
                                                courseList.setAdapter(adapter);
                                                setUpList(courses, courseList);

                            }



                        }
                    }


                });

    }

    private void setUpList(final List<Course> courses, ListView listView) {
        //Create a list adapter
        adapter = new StudentCourseListAdapter(StudentCourseListActivity.this, courses,studentID);

        listView.setAdapter(adapter);
    }

    public static void deleteCourse(Course course,String studentID) {
        studentEnrollmentReference
                .whereEqualTo("courseID",course.getCourseCode())
                .whereEqualTo("studentID",studentID)
                .get()
                .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                        }
                    }
                });
    }



    /**
     * Back button function
     * @param view the current view
     */
    public void back(View view) {
        //Go back to previous activity
        this.finish();
    }
}

