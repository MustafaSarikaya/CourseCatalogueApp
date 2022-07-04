package com.example.coursecatalogueapp.admin.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.admin.AdminAddCourseActivity;
import com.example.coursecatalogueapp.admin.AdminCourseListActivity;
import com.example.coursecatalogueapp.auth.RegisterActivity;
import com.example.coursecatalogueapp.modules.Course;
import com.example.coursecatalogueapp.modules.User;

import java.util.List;

public class AdminCourseListAdapter extends ArrayAdapter<Course> {
    private Activity context;
    List<Course> courses;

    public AdminCourseListAdapter(@NonNull Activity context, @NonNull List<Course> courses) {
        super(context, R.layout.course_list_row, courses);
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listViewItem = layoutInflater.inflate(R.layout.course_list_row, null,true);

        TextView courseName = listViewItem.findViewById(R.id.courseName);
        TextView courseCode = listViewItem.findViewById(R.id.courseCode);

        Course course = courses.get(position);

        courseName.setText(course.getCourseName());
        courseCode.setText(course.getCourseCode());


        ImageButton updateButton = (ImageButton) listViewItem.findViewById(R.id.courseEditBtn);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, AdminAddCourseActivity.class);
                i.putExtra("TAG", "update");
                i.putExtra("courseName", course.getCourseName());
                i.putExtra("courseCode",course.getCourseCode());
                i.putExtra("courseId", course.getId());
                i.putExtra("courseInstructor",course.getCourseInstructor());
                context.startActivity(i);
            }
        });

        //Set delete button functions
        ImageButton deleteButton = (ImageButton) listViewItem.findViewById(R.id.courseDeleteBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminCourseListActivity.deleteCourse(course);
            }
        });

        return listViewItem;
    }
}

