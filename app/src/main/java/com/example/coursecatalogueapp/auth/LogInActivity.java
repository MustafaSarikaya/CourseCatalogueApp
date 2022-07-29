package com.example.coursecatalogueapp.auth;

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

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.UserController;
import com.example.coursecatalogueapp.Utils.Function;
import com.example.coursecatalogueapp.Utils.Utils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LogInActivity extends Activity {

    private static final String TAG = "Login";

    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private FirebaseFirestore db;

    EditText inputEmail, inputPassword;
    TextView registerLink;
    Button loginButton;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //This part is for testing purpose only

//    LogInActivity(String email, String password){
//        this.email = email;
//        this.password = password;
//    }
//
//    public LoginError validateInputForTesting(){
//        return validateInput(email, password);
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputUsername);
        inputPassword = findViewById(R.id.inputPassword);
        loginButton = findViewById(R.id.login);
        registerLink = findViewById(R.id.registerLink);

        setLoginClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }


    private void setLoginClickListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                signIn(email,password);
            }
        });
    }

    //registerLink click
    public void onLinkClick(View view) {
        Intent i = new Intent(LogInActivity.this, RegisterActivity.class);
        startActivity(i);
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
            final LoginError loginError = validateInput(email, password);

            //If there is an error
            if(loginError != LoginError.None) {
                //Show a snackbar with the error message
                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.loginPage), loginError.toString(), BaseTransientBottomBar.LENGTH_SHORT);
                //Add close button
                mySnackbar.setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
                //Clear text when snackbar is closed
                mySnackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        switch(loginError) {
                            case FieldsEmpty:
                                break;
                            case EmailInvalid:
                                //Clears only the email entry
                                inputEmail.getText().clear();
                                break;
                            case InvalidAdminLogin:
                            case PasswordTooShort:
                                //Clears only the password entry
                                inputPassword.getText().clear();
                                break;
                        }
                    }
                });
                //Show snackbar
                mySnackbar.show();
            } else {
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
            }
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

    /**
     * Validates the inputs of login page
     * @param email the email to validate
     * @param password the password to validate
     * @return the LoginError value for the given inputs.
     */
    private LoginError validateInput(String email, String password) {
        //Checks if any field is empty
        if(email.isEmpty() || password.isEmpty()) {
            return LoginError.FieldsEmpty;
        }
        if(email.compareTo("admin") == 0) {
            //This should only happen when the admin password is wrong.
            return LoginError.InvalidAdminLogin;
        }
        //Validates Email
        boolean validEmail = Utils.isEmailValid(email);
        if(!validEmail) {
            return LoginError.EmailInvalid;
        }
        //Checks if password is long enough
        if(password.length() < 6) {
            return LoginError.PasswordTooShort;
        }
        //Returns no error message if inputs are valid.
        return LoginError.None;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
