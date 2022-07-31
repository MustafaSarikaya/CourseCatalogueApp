package com.example.coursecatalogueapp;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursecatalogueapp.instructor.InstructorAssignActivity;
import com.example.coursecatalogueapp.instructor.InstructorViewActivity;
import com.example.coursecatalogueapp.instructor.adapters.InstructorMyCoursesAdapter;
import com.example.coursecatalogueapp.modules.Course;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentViewAdapter extends ArrayAdapter<Course> {
    private Activity context;
    static List<Course> mycourses;
    private String userName;
    private FirebaseFirestore firestore;
    private static CollectionReference courseReference;

    public StudentViewAdapter(Activity context, List<Course> mycourses, String n) {
        super(context, R.layout.list_row, mycourses);
        this.context = context;
        this.mycourses = mycourses;
        userName = n;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listViewItem = layoutInflater.inflate(R.layout.list_row, null, true);
        TextView name = listViewItem.findViewById(R.id.username);
        TextView code = listViewItem.findViewById(R.id.email);
        Course course = mycourses.get(position);
        ImageButton delete = listViewItem.findViewById(R.id.userDeleteBtn);



        firestore = FirebaseFirestore.getInstance();
        courseReference = firestore.collection("courses");
        name.setText(course.getCourseName());
        code.setText(course.getCourseCode());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                course.removeStudent(userName);
                Map<String, Object> courseInfo = new HashMap<>();
                courseInfo.put("students", course.getStudents());

                courseReference.document(course.getId()).set(courseInfo, SetOptions.merge());
                Toast.makeText(context, "Course deleted from your schedule", Toast.LENGTH_SHORT).show();
            }
        });

        return listViewItem;
    }
}
