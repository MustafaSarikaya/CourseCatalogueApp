package com.example.coursecatalogueapp.instructor.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.instructor.InstructorAssignActivity;
import com.example.coursecatalogueapp.instructor.InstructorMainActivity;
import com.example.coursecatalogueapp.instructor.InstructorMyCourses;
import com.example.coursecatalogueapp.modules.Course;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class InstructorMainAdapter extends ArrayAdapter<Course> {
    private Activity context;
    List<Course> courses;

    public InstructorMainAdapter(@NonNull Activity context,@NonNull List<Course> courses) {
        super(context, R.layout.instructor_course_row, courses);
        this.context = context;
        this.courses= courses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listViewItem = layoutInflater.inflate(R.layout.instructor_course_row, null, true);

        TextView courseName = listViewItem.findViewById(R.id.courseName);
        TextView courseCode = listViewItem.findViewById(R.id.courseCode);

        ImageView addButton = listViewItem.findViewById(R.id.plusbutton);
        Course course = courses.get(position);

        courseName.setText(course.getCourseName());
        courseCode.setText(course.getCourseCode());

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InstructorAssignActivity.class);
                i.putExtra("courseId", course.getId());
                context.startActivity(i);
            }
        });

        return listViewItem;
    }



}
