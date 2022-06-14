package com.example.coursecatalogueapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coursecatalogueapp.modules.User;
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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        User account = UserController.getInstance().getUserAccount();

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
        switch(account.getRole()) {
            case "admin":
                intent = new Intent(MainActivity.this, AdminMainActivity.class);
                break;
            case "Instructor":
                intent = new Intent(MainActivity.this, InstructorsActivity.class);
                break;
            case "Student":
                intent = new Intent(MainActivity.this, StudentInterface.class);
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