package com.example.coursecatalogueapp.student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.student.adapters.StudentEnrollListAdapter;
import com.example.coursecatalogueapp.modules.Course;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentEnrollListActivity extends AppCompatActivity {

    static StudentEnrollListAdapter adapter;
    ImageView  HomeButton;
    SearchView search;
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
        setContentView(R.layout.activity_student_enroll_list);


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
                Intent intent = new Intent(StudentEnrollListActivity.this, StudentMainActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        studentID = sharedPref.getString(getString(R.string.user_uid_key), null);

    }

    @Override
    protected void onStart() {
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
        makelist();


    }

    private void makelist(){

        firestore.collection("student_enrollments")
                .whereEqualTo("studentID",studentID)
                .addSnapshotListener(new EventListener<QuerySnapshot>(){
                    @Override
                    public void onEvent(@Nullable QuerySnapshot val, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            studentEnrollments.clear();

                            for (QueryDocumentSnapshot document : val) {
                                if(document.exists()) {
                                    //Get the basic user data from the document
                                    String courseName = document.getString("courseID");
                                    studentEnrollments.add(courseName);
                                }
                            }
                            if (!studentEnrollments.isEmpty()) {
                                courseReference
                                        .whereNotIn("courseCode", studentEnrollments)
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
                                courseReference
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
                            }
                        }
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
            setUpList(myList, courseList);
        }
    }

    private static void enroll(final List<Course> enrolledCourses, Course course, String studentID) {
        boolean update=true;
        for (Course c: enrolledCourses) {
            if (c.getLecture1Day().equals(course.getLecture1Day()) && c.getLecture1Time().equals(course.getLecture1Time())) {
                update =false;
            } else if (c.getLecture2Day().equals(course.getLecture2Day()) && c.getLecture2Time().equals(course.getLecture2Time())) {
                update =false;
            }
        }

        if (update) {
            Map<String, Object> enrollInfo = new HashMap<>();
            enrollInfo.put("courseID", course.getCourseCode());
            enrollInfo.put("studentID", studentID);


            studentEnrollmentReference.add(enrollInfo);

        }
    }

    private void setUpList(final List<Course> courses, ListView listView) {
        //Create a list adapter
        adapter = new StudentEnrollListAdapter(StudentEnrollListActivity.this, courses,studentID);

        listView.setAdapter(adapter);
    }

    public static void enrollCourse(Course course,String studentID) {
        Map<String, Object> enrollInfo = new HashMap<>();
        enrollInfo.put("courseID", course.getCourseCode());
        enrollInfo.put("studentID", studentID);


        studentEnrollmentReference.add(enrollInfo);

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

