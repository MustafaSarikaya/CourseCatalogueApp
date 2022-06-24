package com.example.coursecatalogueapp.instructor;

import static com.example.coursecatalogueapp.instructor.Instructor_MyCourses.mycourses;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.modules.Course;

import java.util.ArrayList;
import java.util.List;

public class InstructorMainAdapter extends ArrayAdapter<Course> {
    private Activity context;
    List<Course> courses;


    public InstructorMainAdapter(Activity context, List<Course> courses) {
        super(context, R.layout.instructor_course_row, courses);
        this.context = context;
        this.courses = courses;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listViewItem = layoutInflater.inflate(R.layout.instructor_course_row, null, true);

        TextView courseName = listViewItem.findViewById(R.id.courseName);
        TextView courseCode = listViewItem.findViewById(R.id.courseCode);

      //  ImageView addButton= listViewItem.findViewById(R.id.plusbutton);
        Course course = courses.get(position);



       courseName.setText(course.getCourseName());
       courseCode.setText(course.getCourseCode());
//       addButton.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               String coursename = (String) courseName.getText();
//               String coursecode = (String) courseCode.getText();
//               String courseInstructor = course.getCourseInstructor();
//               String id = course.getId();
//               Course course = new Course(coursecode,coursename,id,courseInstructor);
//               mycourses.add(course);
//
//
//           }
//       });






        return listViewItem;
    }
    public void addCourse(Course course){
        mycourses.add(course);


    }







}
