package com.example.coursecatalogueapp.admin;

import static android.app.ProgressDialog.show;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.modules.Course;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminCourseListActivity extends AppCompatActivity {

    static AdminCourseListAdapter adapter;
    EditText input;
    ImageView enter, HomeButton, addCourseButton, delete, Update;
    String isCourse;
//    TextView courseCode;
    Intent intent;

//    private TextView isim2, numara2,email2;
//    private Button ok_pop2;
//
//    private AlertDialog.Builder dialogBuilder;
//    private AlertDialog dialog;

    static List<Course> courses;
    static private ListView courseList;
    private FirebaseFirestore firestore;
    private CollectionReference courseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_course_list);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set up firestore
        firestore = FirebaseFirestore.getInstance();
        courseReference = firestore.collection("courses");
        //Set up the employee and customer lists
        courses = new ArrayList<>();



        courseList = findViewById(R.id.listview);

        input= findViewById(R.id.input);
        enter= findViewById(R.id.add);
        HomeButton = findViewById(R.id.Home);
        addCourseButton =findViewById(R.id.addUserButton);
//        Update = findViewById(R.id.courseEditBtn);
//        delete = findViewById(R.id.courseDeleteBtn);


//        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                String name = names.get(position);
//
//
////                viewCard();
//                isim2.setText(name);
//               // String name = names.get(position);
//                //makeToast(name);
//            }
//        });

//        courseList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                deleteCourse(courses.get(position));
//                return false;
//            }
//        });

        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCourseListActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCourseListActivity.this, AdminAddCourseActivity.class);
                startActivity(intent);
            }
        });

//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String CourseCode = input.getText().toString();
//
//                int index = -1;
//                for(int i = 0; i < courses.size(); i++){
//                    if(courses.get(i).getCourseCode().equals(CourseCode)){
//                        index = i;
//                    }
//                }
//
//                deleteUser(courses.get(index));
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
                    courseReference.orderBy("courseCode").addSnapshotListener(new EventListener<QuerySnapshot>() {
                      @Override
                      public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                          if (error != null) {
                              Log.w("UsersActivity", "Listen failed.", error);
                              return;
                          }

                              //Clear the list to prepare for loading of new data
                              courses.clear();
                              courseList.setAdapter(adapter);
                              //Iterate through the documents read from firestore
                              for(QueryDocumentSnapshot doc : value) {
                                  //If the document exists
                                  if(doc.exists()) {
                                      //Get the basic user data from the document
                                      String courseName = doc.getString("courseName");
                                      String courseCode = doc.getString("courseCode");
                                      String id = doc.getId();
                                      //Create an course Object
                                      Course course = new Course(courseCode, courseName, id);

                                      //Add the course to the list
                                      courses.add(course);
                                  }
                              }
                              //Set up the list in the UI
                              setUpList(courses, courseList);

                      }
                  });
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    private void setUpList(final List<Course> courses, ListView listView) {
        //Create a list adapter
        adapter = new AdminCourseListAdapter(AdminCourseListActivity.this, courses);
        for(int i = 0; i < adapter.getCount(); i++) {
            //Get final version of index
//            final int finalI = i;
//            //Get the list item from the adapter at the index
//            View view = adapter.getView(i, null, listView);
//            //Open the user info dialog when the list item is clicked
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //courseInfoDialogue(courses.get(finalI));
//                }
//            });

//            ImageButton updateButton = (ImageButton) view.findViewById(R.id.courseEditBtn);
//            updateButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//           });
//
//            Set delete button functions
//
//            Add the list item to the list view

        }
        listView.setAdapter(adapter);
    }

    /**
     * Opens a dialog displaying the text format of the given user account
     * @param course the user account to display the info for
     */
    private void courseInfoDialogue(Course course) {
        //Create new AlertDialog
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(AdminCourseListActivity.this);
        alertDialogBuilder
                .setTitle(course.getCourseName()) //Set the title of the dialog to the account name
                .setMessage(course.toString()) //Set the message of the dialog to the account text
                .setCancelable(true)
                .setPositiveButton(
                        "CLOSE",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                );
        //Show AlertDialog
        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Prompts the admin if the admin truly wants to delete the given account
     * @param  course
     */
//    public void deleteCourseDialogue(final Course course) {
//        //Create new AlertDialog
//        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(AdminCourseListActivity.this);
//        alertDialogBuilder
//                .setTitle("Delete user account for " + course.getCourseName() + "?")
//                .setMessage("Are you sure you want to delete this user account? Any data associated with this user will be permanently deleted.")
//                .setCancelable(true)
//                .setPositiveButton(
//                        "YES",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //Delete the account
////
//                                dialog.cancel();
//                            }
//                        }
//                )
//                .setNegativeButton(
//                        "NO",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        }
//                );
//    }
//        //Show AlertDialog
//        androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }

        //   public void deleteCourse(Course course) {
        //       courseReference.document(course.getId()).delete();
        // }

//    // popup
//    public void viewCard(){
//            dialogBuilder = new AlertDialog.Builder(this);
//            final View popupWindow = getLayoutInflater().inflate(R.layout.popup,null);
//            isim2 = popupWindow.findViewById(R.id.isim);
//            numara2 = popupWindow.findViewById(R.id.numara);
//            email2= popupWindow.findViewById(R.id.email);
//            ok_pop2 = popupWindow.findViewById(R.id.ok_pop);
//
//            dialogBuilder.setView(popupWindow);
//            dialog = dialogBuilder.create();
//            dialog.show();
//
//            ok_pop2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//    }

//    // save changes
//    @Override
//    protected void onDestroy() {
//        File path = getApplicationContext().getFilesDir();
//        try {
//            FileOutputStream writer = new FileOutputStream(new File(path, "list.txt"));
//            writer.write(names.toString().getBytes());
//
//
//            writer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        super.onDestroy();
//    }

        /**
         * Back button function
         //  * @param view the current view
         */
    public void back(View view) {
        //Go back to previous activity
        this.finish();
    }

    public static void RemoveCourse(int remove){
        courses.remove(remove);
        courseList.setAdapter(adapter);

    }
}