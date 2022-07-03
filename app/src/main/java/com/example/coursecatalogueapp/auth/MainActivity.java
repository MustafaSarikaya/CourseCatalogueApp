package com.example.coursecatalogueapp.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.coursecatalogueapp.instructor.InstructorMainActivity;
import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.StudentMainActivity;
import com.example.coursecatalogueapp.admin.AdminMainActivity;
import com.example.coursecatalogueapp.instructor.Instructor_MyCourses;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends Activity {

//    User account;
    String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
        userRole = sharedPref.getString(getString(R.string.user_role_key), null);

        FirebaseMessaging.getInstance().getToken()
        .addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("Messaging", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                final String token = task.getResult();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(user.getUid())
                            .collection("tokens")
                            .document(token)
                            .set(new HashMap<String, Object>() {{ put("signedIn", new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(new Date())); }});
            }
        });

        Intent intent;
        //Sets intent based on what role the user is
        switch(userRole) {
            case "admin":
                intent = new Intent(MainActivity.this, AdminMainActivity.class);
                break;
            case "Instructor":
                intent = new Intent(MainActivity.this, InstructorMainActivity.class);
                break;
            case "Student":
                intent = new Intent(MainActivity.this, StudentMainActivity.class);
                break;
            default:
                intent = new Intent(MainActivity.this, LogInActivity.class);
                break;
        }
        //set the new task and clear flags, so that the user can't go back here
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}