package com.example.coursecatalogueapp.auth;

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

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.UserController;
import com.example.coursecatalogueapp.Utils.Function;
import com.example.coursecatalogueapp.Utils.Utils;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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
    TextView loginLink, spinnerText, pageTitle;

    Intent i;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //This part is for testing purpose only

    RegisterActivity(String name, String email, String password){
        fullName = name;
        this.email = email;
        this.password = password;
    }

    public SignUpError validateInputForTesting(){
        return validateInput(fullName, email, password);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

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
        spinnerText = findViewById(R.id.roleSpinnerText);
        pageTitle = findViewById(R.id.InstructorAssign);
        loginLink = findViewById(R.id.loginLink);
        i = getIntent();
        if (i.getExtras() != null) {
            userRole = i.getStringExtra("TAG");
            spinnerRoles.setVisibility(View.INVISIBLE);
            loginLink.setVisibility(View.INVISIBLE);
            spinnerText.setVisibility(View.INVISIBLE);
            registerButton.setText(R.string.add);
            pageTitle.setText(userRole.equals("Instructor") ?
                    R.string.addInstructor :
                    R.string.addStudent);
        }

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
                userRole = userRole == null ? spinnerRoles.getSelectedItem().toString() : userRole;

                createAccount(fullName,email,userRole, password, getCurrentFocus());
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
        final SignUpError signUpError = validateInput(name, email, password);

        //If there is an error
        if (signUpError != SignUpError.None) {
            //Show a snackbar with the error message
            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.registerPage), signUpError.toString(), BaseTransientBottomBar.LENGTH_SHORT);
            //Add close button
            mySnackbar.setAction("CLOSE", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Close button does nothing really
                }
            });
            //Clear text when snackbar is closed
            mySnackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    switch (signUpError) {
                        case FieldsEmpty:
                            break;
                        case InvalidName:
                            //Clears only the name entry
                            inputFullname.getText().clear();
                            break;
                        case InvalidEmail:
                            //Clears only the email entry
                            inputEmail.getText().clear();
                            break;
                        case PasswordTooShort:
                    }
                }
            });
            //Show snackbar
            mySnackbar.show();
        } else {
            // Create user on Firebase auth
            UserController.getInstance().signUp(name, email, role, password, getCurrentFocus(), new Function() {
                @Override
                public void f(Object... params) {
                    writeToSharedPrefs(params);

                    Intent intent;
                    if (i.getExtras() == null) {
                        intent = new Intent(RegisterActivity.this, MainActivity.class);
                        //set the new task and clear flags, so that the user can't go back here
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return;
                    } else {
                        back(view);
                    }
                }
            });
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

    private SignUpError validateInput(String name, String email, String password) {
        //Checks if any field is empty
        if(name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return SignUpError.FieldsEmpty;
        }
        //Checks if name is only composed of whitespaces
        if(name.trim().length() == 0 || !Utils.isNameValid(name)){
            return SignUpError.InvalidName;
        }
        //Validates Email
        boolean validEmail = Utils.isEmailValid(email);
        if(!validEmail) {
            return SignUpError.InvalidEmail;
        }
        //Checks if password is long enough
        if(password.length() < 6) {
            return SignUpError.PasswordTooShort;
        }
        //Returns no error if all inputs are valid.
        return SignUpError.None;
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
