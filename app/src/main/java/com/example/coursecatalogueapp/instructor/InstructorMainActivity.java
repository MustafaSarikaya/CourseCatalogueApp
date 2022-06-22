package com.example.coursecatalogueapp.instructor;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
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

    TextView pageTitle;
    static String userName;
    static InstructorMainAdapter adapter;
    ListView CourseList;
    List<Course> courses;
    SearchView search;


     ExpandableListView expandableListView;
     ExpandableListAdapter expandableListAdapter;
     List<Course> expandableListDetail;
     String expendableListTitle;
    private FirebaseFirestore firestore;
    private static CollectionReference courseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_main);

        // expandable List

        expandableListDetail = new ArrayList<>();

        expendableListTitle = "Selim";
        expandableListAdapter = new exp_list_adapter(this,expandableListDetail,expendableListTitle);


        expandableListView = findViewById(R.id.ExpListView);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if(lastExpandedPosition != -1 && groupPosition != lastExpandedPosition){
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String selected = expandableListAdapter.getChild(groupPosition,childPosition).toString();
                Toast.makeText(InstructorMainActivity.this, "BOMBOM", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        firestore = FirebaseFirestore.getInstance();
        courseReference = firestore.collection("courses");

        search = findViewById(R.id.searchView);
        CourseList = findViewById(R.id.listview);
        courses = new ArrayList<>();
        getCourses();


        pageTitle = findViewById(R.id.instructorMainTitle);

        SharedPreferences sharedPref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        userName = sharedPref.getString(getString(R.string.user_name_key), null);


        pageTitle.setText("Welcome " + userName);

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
                    Course course = new Course(courseCode, courseName, id);
                    course.setCourseInstructor(courseInstructor);

                    courses.add(course);
                }
                setUpList(courses, CourseList);
            }

        });
    }
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
        adapter = new InstructorMainAdapter(InstructorMainActivity.this, courses);
        listView.setAdapter(adapter);
    }

}