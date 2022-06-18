package com.example.coursecatalogueapp.admin;

import static android.app.ProgressDialog.show;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

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

public class AdminCourseListActivity extends AppCompatActivity {

    static AdminCourseListAdapter adapter;
    EditText input;
    ImageView enter, HomeButton, addCourseButton;
    Intent intent;

    List<Course> courses;
    static private ListView courseList;
    private FirebaseFirestore firestore;
    private static CollectionReference courseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_list);

        //Set up firestore
        firestore = FirebaseFirestore.getInstance();
        courseReference = firestore.collection("courses");
        //Set up the employee and customer lists
        courses = new ArrayList<>();

        courseList = findViewById(R.id.listview);
        input= findViewById(R.id.input);
        enter= findViewById(R.id.add);
        HomeButton = findViewById(R.id.Home);
        addCourseButton =findViewById(R.id.addUserButton);

        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCourseListActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCourseListActivity.this, AdminAddCourseActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
            courseReference.orderBy("courseCode").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                              Course course = new Course(courseCode, courseName, id);

                              //Add the course to the list
                              courses.add(course);
                          }
                      }
                      //Set up the list in the UI
                      setUpList(courses, courseList);
              }
          });
    }

    private void setUpList(final List<Course> courses, ListView listView) {
        //Create a list adapter
        adapter = new AdminCourseListAdapter(AdminCourseListActivity.this, courses);
//        for(int i = 0; i < adapter.getCount(); i++) {
//            //Get the list item from the adapter at the index
//            View view = adapter.getView(i, null, listView);
//        }
        listView.setAdapter(adapter);
    }

    public static void deleteCourse(Course course) {
        courseReference.document(course.getId()).delete();
    }

    public void onUpdateClick() {
        Intent updateIntent = new Intent(AdminCourseListActivity.this, AdminAddCourseActivity.class);
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