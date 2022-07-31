package com.example.coursecatalogueapp.student.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.student.StudentCourseListActivity;
import com.example.coursecatalogueapp.modules.Course;

import java.util.List;

public class StudentCourseListAdapter extends ArrayAdapter<Course> {
    private Activity context;
    List<Course> courses;
    String studentID;

    public StudentCourseListAdapter(@NonNull Activity context, @NonNull List<Course> courses,@NonNull String  studentID) {
        super(context, R.layout.course_list_row, courses);
        this.context = context;
        this.courses = courses;
        this.studentID = studentID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listViewItem = layoutInflater.inflate(R.layout.student_course_list_row, null,true);

        TextView courseName = listViewItem.findViewById(R.id.courseName);
        TextView courseCode = listViewItem.findViewById(R.id.courseCode);
        TextView lecture1Day = listViewItem.findViewById(R.id.lecture1Day);
        TextView lecture1Time = listViewItem.findViewById(R.id.lecture1Time);
        TextView lecture2Day = listViewItem.findViewById(R.id.lecture2Day);
        TextView lecture2Time = listViewItem.findViewById(R.id.lecture2Time);

        Course course = courses.get(position);

        courseName.setText(course.getCourseName());
        courseCode.setText(course.getCourseCode());
        lecture1Day.setText(course.getLecture1Day());
        lecture1Time.setText(course.getLecture1Time());
        lecture2Day.setText(course.getLecture2Day());
        lecture2Time.setText(course.getLecture2Time());




        //Set delete button functions
        ImageButton deleteButton = (ImageButton) listViewItem.findViewById(R.id.courseDeleteBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentCourseListActivity.deleteCourse(course,studentID);
            }
        });

        return listViewItem;
    }
}

