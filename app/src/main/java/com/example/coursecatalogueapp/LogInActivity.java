package com.example.coursecatalogueapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.coursecatalogueapp.Utils.Function;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInActivity extends Activity {

    private static final String TAG = "Login";

    private FirebaseAuth mAuth;
    private String username;
    private String password;
    private FirebaseFirestore db;

    EditText inputUsername, inputPassword;
    TextView registerLink;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        inputUsername = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        loginButton = findViewById(R.id.login);
        registerLink = findViewById(R.id.registerLink);

        setLoginClickListener();
    }

    private void setLoginClickListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = inputUsername.getText().toString();
                password = inputPassword.getText().toString();
                signIn(username,password);
                // TODO add Role check functionality when the admin, instructor, student classes created
                // TODO Create an intent to navigate to the appropriate page (student, instructor, admin)
            }
        });
    }

    //registerLink click
    public void onLinkClick(View view) {
        Intent i = new Intent(LogInActivity.this, RegisterActivity.class);
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

    private void signIn(String email, String password) {
        final boolean admin = email.compareTo("admin") == 0 && password.compareTo("admin123") == 0;
        //If logging in as admin
        if(admin) {
            //Show message to user to notify that they are logging into the admin account.
            email = "admin@gmail.com";
            Toast.makeText(LogInActivity.this, "Logging in as admin", Toast.LENGTH_SHORT).show();

            //Sign in as admin
            UserController.getInstance().signInAsAdmin(getCurrentFocus(), new Function() {
                @Override
                public void f(Object... params) {
                    writeToSharedPrefs("admin", "admin@gmail.com", "admin", "Vj5naf9QKLQcELivHXEWTY8lily2");

                    //Navigate to Main Activity
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            //Validates input and gets error message
//            final LoginError loginError = validateInput(email, password);
//
//            //If there is an error
//            if(loginError != LoginError.None) {
//                //Show a snackbar with the error message
//                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.login_page), loginError.toString(), BaseTransientBottomBar.LENGTH_SHORT);
//                //Add close button
//                mySnackbar.setAction("CLOSE", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                    }
//                });
//                //Clear text when snackbar is closed
//                mySnackbar.addCallback(new Snackbar.Callback() {
//                    @Override
//                    public void onDismissed(Snackbar snackbar, int event) {
//                        switch(loginError) {
//                            case FieldsEmpty:
//                                break;
//                            case EmailInvalid:
//                                //Clears only the email entry
//                                login_emailEntry.getText().clear();
//                                break;
//                            case InvalidAdminLogin:
//                            case PasswordTooShort:
//                                //Clears only the password entry
//                                login_passwordEntry.getText().clear();
//                                break;
//                        }
//                    }
//                });
//                //Show snackbar
//                mySnackbar.show();
//            } else {
                //Sign in using UserController
                UserController.getInstance().signIn(email, password, getCurrentFocus(), new Function() {
                    @Override
                    public void f(Object... params) {
                        writeToSharedPrefs(params);

                        //Navigate to Main Activity when successful
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        //set the new task and clear flags, so that the user can't go back here
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
//            }
        }
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

    private void updateUI(FirebaseUser user) {

    }


}
