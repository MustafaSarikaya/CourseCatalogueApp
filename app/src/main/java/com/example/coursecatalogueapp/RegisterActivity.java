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
import com.example.coursecatalogueapp.modules.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class RegisterActivity extends Activity {

    private static final String TAG = "Register";

    //Declare variables
    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private String fullName;
    private String userRole;
    private FirebaseFirestore db;
    //Declare UI elements
    EditText inputEmail, inputPassword, inputFullname;
    Spinner spinnerRoles;
    Button registerButton;
    TextView loginLink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Initialize UI elements
        inputEmail = findViewById(R.id.usernameInput);
        inputPassword = findViewById(R.id.passwordInput);
        inputFullname = findViewById(R.id.fullNameInput);
        spinnerRoles = findViewById(R.id.roleSpinnerInput);
        registerButton = findViewById(R.id.registerBtn);
        loginLink = findViewById(R.id.loginLink);


        setRegisterClickListener();
    }

    private void setRegisterClickListener() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the submitted form data
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                fullName = inputFullname.getText().toString();
                userRole = spinnerRoles.getSelectedItem().toString();

                createAccount(fullName,email,userRole, password, getCurrentFocus());
                // TODO add Role check functionality when the admin, instructor, student classes created
                // TODO Create an intent to navigate to the appropriate page (student, instructor, admin)
            }
        });
    }

    //loginLink click
    public void onLinkClick(View view) {
        Intent i = new Intent(RegisterActivity.this, LogInActivity.class);
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

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

                //Navigate to Main Activity when successful
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                //set the new task and clear flags, so that the user can't go back here
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
}
