package com.example.coursecatalogueapp.instructor;

import android.app.Activity;
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
import com.example.coursecatalogueapp.modules.Course;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class my_courses_adapter extends ArrayAdapter<Course> {
    private Activity context;
    static List<Course> mycourses;

    private FirebaseFirestore firestore;
    private static CollectionReference courseReference,courseReference2;

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
        TextView name = listViewItem.findViewById(R.id.courseName);
        TextView code = listViewItem.findViewById(R.id.courseCode);
        Course course = mycourses.get(position);
        ImageView delete = listViewItem.findViewById(R.id.courseDeleteBtn);

        firestore = FirebaseFirestore.getInstance();
        courseReference = firestore.collection("mycourses");
        courseReference2 = firestore.collection("courses");
        name.setText(course.getCourseName());
        code.setText(course.getCourseCode());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courseReference2.add(course);
                courseReference.document(course.getId()).delete();
                Toast.makeText(context, "Course deleted from your schedule", Toast.LENGTH_SHORT).show();

            }
        });




        return listViewItem;

    }



}
