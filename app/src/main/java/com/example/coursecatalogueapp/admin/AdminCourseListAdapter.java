package com.example.coursecatalogueapp.admin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.modules.Course;
import com.example.coursecatalogueapp.modules.User;

import java.util.List;

class AdminCourseListAdapter extends ArrayAdapter<Course> {
    private Activity context;
    List<Course> courses;

    public AdminCourseListAdapter(@NonNull Activity context, @NonNull List<Course> courses) {
        super(context, R.layout.course_list_row, courses);
        this.context = context;
        this.courses = courses;
    }

    @NonNull
    @Override
    public  View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listViewItem = layoutInflater.inflate(R.layout.course_list_row, null,true);

//        TextView number = listViewItem.findViewById(R.id.number);
//        number.setText(position + 1+ ".");
        TextView courseName = listViewItem.findViewById(R.id.courseName);
        TextView courseCode = listViewItem.findViewById(R.id.courseCode);

        Course course = courses.get(position);

        courseName.setText(course.getCourseName());
        courseCode.setText(course.getCourseCode());

        return listViewItem;
    }
}

