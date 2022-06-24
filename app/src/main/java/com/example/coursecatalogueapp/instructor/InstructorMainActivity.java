package com.example.coursecatalogueapp.instructor;

import static com.example.coursecatalogueapp.instructor.Instructor_MyCourses.mycourses;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursecatalogueapp.R;
//import com.example.coursecatalogueapp.admin.expandableListAdapter;
import com.example.coursecatalogueapp.modules.Course;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class InstructorMainActivity extends Activity {

    static String userName;
    static InstructorMainAdapter adapter;
    ListView CourseList;
    List<Course> courses;
    Button addCourses;
   public static List<Course> myCourses;
    SearchView search;


    private FirebaseFirestore firestore;
    private static CollectionReference courseReference, myCourseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_main);


        firestore = FirebaseFirestore.getInstance();
        courseReference = firestore.collection("courses");
        myCourseReference = firestore.collection("mycourses");
        addCourses = findViewById(R.id.btn_myCourses);
        search = findViewById(R.id.searchView);
        CourseList = findViewById(R.id.listview);

        courses = new ArrayList<>();
        myCourses = new ArrayList<>();

        getCourses();



        SharedPreferences sharedPref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        userName = sharedPref.getString(getString(R.string.user_name_key), null);

        addCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InstructorMainActivity.this,Instructor_MyCourses.class);
                startActivity(i);

            }
        });



    }
    public static List<Course> getMyCourses(){
        return myCourses;
    }



    public static String getuserName(){
        return userName;
    }

    public void getCourses(){
        courseReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                courses.clear();
                CourseList.setAdapter(adapter);
                for(QueryDocumentSnapshot doc: value) {
                    String courseName = doc.getString("courseName");
                    String courseCode = doc.getString("courseCode");
                    String courseInstructor = doc.getString("courseInstructor");
                    String id = doc.getId();
                    Course course = new Course(courseCode, courseName, id,courseInstructor);
                    course.setCourseInstructor(courseInstructor);

                    courses.add(course);
                }
                setUpList(courses, CourseList);
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
            setUpList(myList, CourseList);
        }


    }
    private void setUpList(final List<Course> courses, ListView listView){
        adapter = new InstructorMainAdapter(InstructorMainActivity.this,courses);
        listView.setAdapter(adapter);
    }

}