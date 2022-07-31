package com.example.coursecatalogueapp.instructor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.instructor.adapters.InstructorMainAdapter;
import com.example.coursecatalogueapp.instructor.adapters.InstructorViewAdapter;
import com.example.coursecatalogueapp.modules.Course;
import com.example.coursecatalogueapp.modules.Student;
import com.example.coursecatalogueapp.modules.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class InstructorViewActivity extends Activity {

    private ArrayList<User> students;
    private ArrayList<String> studentList;
    private Intent i;
    private InstructorViewAdapter adapter;
    private ListView studentListView;
    private String[] s;
    private String courseCode;
    private FirebaseFirestore firestore;
    private static CollectionReference studentEnrollmentReference;
    private static CollectionReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_view);

        firestore = FirebaseFirestore.getInstance();
        studentEnrollmentReference = firestore.collection("student_enrollments");
        usersReference = firestore.collection("users");

        studentListView = findViewById(R.id.listView);
        students = new ArrayList<>();
        studentList = new ArrayList<>();
        i = getIntent();
        courseCode = i.getStringExtra("courseCode");
//        students = i.getStringExtra("students");
//        System.out.println(students);
//
//        s = students.split(",");
//        for(int i = 0; i < s.length; i++){
//            if(!s[i].equals("")){
//                studentList.add(s[i]);
//            }
//        }
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
//        studentListView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        studentEnrollmentReference
                .whereEqualTo("courseCode",courseCode)
                .addSnapshotListener(new EventListener<QuerySnapshot>(){
                    @Override
                    public void onEvent(@Nullable QuerySnapshot val, @Nullable FirebaseFirestoreException error) {
                        if (error == null) {
                            studentList.clear();

                            for (QueryDocumentSnapshot document :val) {
                                if(document.exists()) {
                                    //Get the basic user data from the document
                                    String studentId = document.getString("studentId");
                                    studentList.add(studentId);
                                }
                            }
                            if (!studentList.isEmpty()) {
                                usersReference
                                        .whereIn("uid", studentList)
                                        .orderBy("name").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                if (error != null) {
                                                    Log.w("UsersActivity", "Listen failed.", error);
                                                    return;
                                                }

                                                //Clear the list to prepare for loading of new data
                                                students.clear();
                                                studentListView.setAdapter(adapter);
                                                //Iterate through the documents read from firestore
                                                for(QueryDocumentSnapshot doc : value) {
                                                    //If the document exists
                                                    if(doc.exists()) {
                                                        //Get the basic user data from the document
                                                        String user_name = doc.getString("name");
                                                        String user_email = doc.getString("email");
                                                        String id = doc.getId();
                                                        //Create an course Object
                                                        Student student = new Student(user_name, user_email,id);

                                                        //Add the course to the list
                                                        students.add(student);
                                                    }
                                                }
                                                //Set up the list in the UI
                                                setUpList(students, studentListView);
                                            }
                                        });

                            } else {
                                students.clear();
                                setUpList(students, studentListView);
                            }
                        }
                    }
                });
    }

    private void setUpList(final List<User> students, ListView listView){
        adapter = new InstructorViewAdapter(InstructorViewActivity.this, students);
        listView.setAdapter(adapter);
    }
}