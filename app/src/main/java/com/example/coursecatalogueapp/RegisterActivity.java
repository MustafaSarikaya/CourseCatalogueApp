package com.example.coursecatalogueapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends Activity {

    private static final String TAG = "Register";

    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private String fullName;
    private String userRole;
    private DatabaseReference mDatabase;

    EditText inputEmail, inputPassword, inputFullname;
    Spinner spinnerRoles;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        inputEmail = findViewById(R.id.usernameInput);
        inputPassword = findViewById(R.id.passwordInput);
        inputFullname = findViewById(R.id.fullNameInput);
        spinnerRoles = findViewById(R.id.roleSpinnerInput);
        registerButton = findViewById(R.id.registerBtn);

        setRegisterClickListener();
    }

    private void setRegisterClickListener() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
                fullName = inputFullname.getText().toString();
                userRole = spinnerRoles.getSelectedItem().toString();
                createAccount(email,password);
                // TODO add Role check functionality when the admin, instructor, student classes created
                // TODO Create an intent to navigate to the appropriate page (student, instructor, admin)
            }
        });
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

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (userRole.equals("instructor")) {
                                mDatabase.child("instructors").child(user.getUid()).child("fullName").setValue(fullName);
                            } else if (userRole.equals("student")){
                                mDatabase.child("students").child(user.getUid()).child("fullName").setValue(fullName);
                            }
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            // TODO add error messages to the UI fields
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }
}
