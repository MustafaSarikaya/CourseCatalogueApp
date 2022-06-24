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
        // firestore = FirebaseFirestore.getInstance();
        //courseReference = firestore.collection("courses");
        listView = findViewById(R.id.listView);

        mycourses = new ArrayList<>();
        setUpList(mycourses, listView);


    }
    private void setUpList(final List<Course> courses, ListView listView){
        adapter = new my_courses_adapter(Instructor_MyCourses.this,mycourses);
        listView.setAdapter(adapter);
    }
}
