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
        TextView instructorName = listViewItem.findViewById(R.id.ProfName);



        ImageView Unassign = (ImageView) listViewItem.findViewById(R.id.courseUnassign);
        ImageView Assign = (ImageView) listViewItem.findViewById(R.id.courseAssign);

        Course course = courses.get(position);

        courseName.setText(course.getCourseName());
        courseCode.setText(course.getCourseCode());


        String profName = InstructorMainActivity.getuserName();


        Assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(instructorName.getText().length()== 0) {
                    //    Course.setCourseProf(profName);
                    instructorName.setText("Prof." + profName);
                }

                else{
                    Toast.makeText(getContext(),"Course already has an instructor",Toast.LENGTH_LONG).show();
                }
                //      Intent myintent = new Intent(v.getContext(),Instructor_courses.class);
                //     myintent.putExtra("key",instructorName.getText());
                //    context.startActivity(myintent);
            }
        });
        Unassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instructorName.setText(null);
            }
        });

        return listViewItem;
    }

}
