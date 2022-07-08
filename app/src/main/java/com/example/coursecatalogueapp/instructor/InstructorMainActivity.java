package com.example.coursecatalogueapp.instructor;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.coursecatalogueapp.R;
//import com.example.coursecatalogueapp.admin.expandableListAdapter;
import com.example.coursecatalogueapp.instructor.adapters.InstructorMainAdapter;
import com.example.coursecatalogueapp.modules.Course;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InstructorMainActivity extends Activity {

    String userName;
    static InstructorMainAdapter adapter;
    ListView courseList;
    List<Course> courses;
    Button myCoursesBtn;
//   public static List<Course> myCourses;
    SearchView search;
    TextView pageTitle;

    private FirebaseFirestore firestore;
    private CollectionReference courseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_main);

        firestore = FirebaseFirestore.getInstance();
        courseReference = firestore.collection("courses");

        myCoursesBtn = findViewById(R.id.btn_myCourses);
        search = findViewById(R.id.searchView);
        courseList = findViewById(R.id.listview);
        pageTitle = findViewById(R.id.instructorMainTitle);

        courses = new ArrayList<>();
   //     myCourses = new ArrayList<>();

        SharedPreferences sharedPref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        userName = sharedPref.getString(getString(R.string.user_name_key), null);
        pageTitle.setText("Welcome " + userName);

        myCoursesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InstructorMainActivity.this, InstructorMyCourses.class);
                startActivity(i);
            }
        });
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
                for(QueryDocumentSnapshot doc: value) {
                    String courseName = doc.getString("courseName");
                    String courseCode = doc.getString("courseCode");
                    String courseInstructor = doc.getString("courseInstructor");
                    String id = doc.getId();
                    Course course = new Course(courseCode, courseName, id);
                    course.setCourseInstructor(courseInstructor);

                    courses.add(course);
                }
                setUpList(courses, courseList);
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

    private void setUpList(final List<Course> courses, ListView listView){
        adapter = new InstructorMainAdapter(InstructorMainActivity.this, courses);
        listView.setAdapter(adapter);
    }

}