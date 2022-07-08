package com.example.coursecatalogueapp.instructor.adapters;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.instructor.InstructorAssignActivity;
import com.example.coursecatalogueapp.modules.Course;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class InstructorMainAdapter extends ArrayAdapter<Course> {
    private Activity context;
    List<Course> courses;
    private FirebaseFirestore firestore;
    private static CollectionReference userReference;

    public InstructorMainAdapter(@NonNull Activity context,@NonNull List<Course> courses) {
        super(context, R.layout.instructor_course_row, courses);
        this.context = context;
        this.courses= courses;
        firestore = FirebaseFirestore.getInstance();
        userReference = firestore.collection("users");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listViewItem = layoutInflater.inflate(R.layout.instructor_course_row, null, true);

        TextView courseName = listViewItem.findViewById(R.id.courseName);
        TextView courseCode = listViewItem.findViewById(R.id.courseCode);
        TextView courseInstructor = listViewItem.findViewById(R.id.courseInstructor);
        ImageView imageView = listViewItem.findViewById(R.id.imageView);

        ImageButton plusButton = listViewItem.findViewById(R.id.plusbutton);
        Course course = courses.get(position);

        courseName.setText(context.getString(R.string.course_name_label) + " " + course.getCourseName());
        courseCode.setText(context.getString(R.string.course_code_label) + " " + course.getCourseCode());
        String courseInst = course.getCourseInstructor();
        boolean isInstructorAssigned = !courseInst.equals("");

        if (isInstructorAssigned) {
            DocumentReference docRef = userReference.document(courseInst);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String instructorName = (String) document.get("name");
                            courseInstructor.setText(context.getString(R.string.instructor_label) + " " + instructorName);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInstructorAssigned) {
                    Toast.makeText(context, "Can't assign to a course that aready has an instructor", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(context, InstructorAssignActivity.class);
                    i.putExtra("TAG", "assign");
                    i.putExtra("courseId", course.getId());
                    i.putExtra("courseName", course.getCourseName());
                    i.putExtra("courseCode", course.getCourseCode());
                    context.startActivity(i);
                }
            }
        });

        return listViewItem;
    }
}
