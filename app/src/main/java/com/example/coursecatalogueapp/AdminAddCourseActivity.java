package com.example.coursecatalogueapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.coursecatalogueapp.Utils.Function;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class AdminAddCourseActivity extends Activity {

    private static final String TAG = "Register";

    //Declare variables
    private String courseName;
    private String courseCode;
    private FirebaseFirestore db;
    //Declare UI elements
    EditText inputCourseName, inputCourseCode;
    Spinner spinnerRoles;
    Button addButton;
    TextView loginLink, spinnerText, pageTitle;

    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();

        //Initialize UI elements
//        inputCourseName = findViewById(R.id.);
//        inputCourseCode = findViewById(R.id.);
//        addButton = findViewById(R.id.);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the submitted form data
                courseName = inputCourseName.getText().toString();
                courseCode = inputCourseCode.getText().toString();

            }
        });
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            reload();
//        }
//    }

    public void createAccount(
            final String name,
            final String email,
            final String role,
            final String password,
            final View view
    ) {
        // Create user on Firebase auth
        UserController.getInstance().signUp(name, email, role, password, getCurrentFocus(), new Function() {
            @Override
            public void f(Object... params) {
                writeToSharedPrefs(params);

                Intent intent;
                if (i.getExtras() == null) {
                    intent = new Intent(AdminAddCourseActivity.this, MainActivity.class);
                    //set the new task and clear flags, so that the user can't go back here
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    back(getCurrentFocus());
                }
            }
        });
    }

    private void writeToSharedPrefs(Object... params) {
        Log.d("LOGIN DEBUG", "Writing data to shared preferences...");
        //Writing data to shared preferences after everything has succeeded.
        //Get shared preferences
        SharedPreferences prefs = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        //Get the editor of the shared preferences
        SharedPreferences.Editor editor = prefs.edit();
        //Write login data to shared preferences
        editor.putString(getString(R.string.user_name_key), (String) params[0]);
        editor.putString(getString(R.string.user_email_key), (String) params[1]);
        editor.putString(getString(R.string.user_role_key), (String) params[2]);
        editor.putString(getString(R.string.user_uid_key), (String) params[3]);
        //Apply shared preferences changes
        editor.apply();
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) { }

    /**
     * Back button function
     * @param view the current view
     */
    public void back(View view) {
        //Go back to previous activity
        this.finish();
    }
}
