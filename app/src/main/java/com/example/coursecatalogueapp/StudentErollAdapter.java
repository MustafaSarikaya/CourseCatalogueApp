package com.example.coursecatalogueapp;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursecatalogueapp.instructor.InstructorAssignActivity;
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
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class StudentErollAdapter extends ArrayAdapter<Course> {
    private Activity context;
    List<Course> courses;
    List<String> courseTimes;
    private FirebaseFirestore firestore;

    private CollectionReference coursesReference;
    private String userName;




    public StudentErollAdapter(@NonNull Activity context, @NonNull List<Course> courses, String userName, List<String> courseTimes) {
        super(context, R.layout.instructor_course_row, courses);
        this.context = context;
        this.courses = courses;
        this.courseTimes = courseTimes;
        firestore = FirebaseFirestore.getInstance();

        coursesReference = firestore.collection("courses");
        this.userName = userName;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listViewItem = layoutInflater.inflate(R.layout.instructor_course_row, null, true);

        TextView courseName = listViewItem.findViewById(R.id.courseName);
        TextView courseCode = listViewItem.findViewById(R.id.courseCode);

        ImageButton plusButton = listViewItem.findViewById(R.id.plusbutton);
        Course course = courses.get(position);

        courseName.setText(context.getString(R.string.course_name_label) + " " + course.getCourseName());
        courseCode.setText(context.getString(R.string.course_code_label) + " " + course.getCourseCode());

        course.addStudent(userName);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time1 = course.getLecture1Day() + course.getLecture1Time();
                String time2 = course.getLecture2Day() + course.getLecture2Time();

                boolean flag = false;

                for(String time: courseTimes){
                    if(time.equals(time1)){
                        flag = true;
                    }
                    if(time.equals(time2)){
                        flag = true;
                    }
                }


                if(flag){
                    Toast.makeText(context, "Time conflict!", Toast.LENGTH_LONG).show();
                }

                else {
                    Map<String, Object> courseInfo = new HashMap<>();
                    courseInfo.put("students", course.getStudents());
                    coursesReference.document(course.getId()).set(courseInfo, SetOptions.merge());
                }
            }
        });

        return listViewItem;
    }

}
