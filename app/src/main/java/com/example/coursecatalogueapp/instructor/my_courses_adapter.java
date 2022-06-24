package com.example.coursecatalogueapp.instructor;

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

import java.util.List;

public class my_courses_adapter extends ArrayAdapter<Course> {
    private Activity context;
    List<Course> mycourses;

    public my_courses_adapter(Activity context, List<Course> mycourses) {
        super(context, R.layout.course_list_row, mycourses);
        this.context = context;
        this.mycourses = mycourses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listViewItem = layoutInflater.inflate(R.layout.course_list_row, null, true);
        TextView courseName = listViewItem.findViewById(R.id.courseName);
        TextView courseCode = listViewItem.findViewById(R.id.courseCode);
        Course course = mycourses.get(position);

        courseName.setText(course.getCourseName());
        courseCode.setText(course.getCourseCode());

        return listViewItem;

    }


}
