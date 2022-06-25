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

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.auth.RegisterActivity;
import com.example.coursecatalogueapp.modules.Instructor;
import com.example.coursecatalogueapp.modules.Student;
import com.example.coursecatalogueapp.modules.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminUserListActivity extends AppCompatActivity {

    static AdminUserListAdapter adapter;
    EditText input;
    ImageView enter, HomeButton, addUserButton;
    String userRole;
    Intent intent;

//
//    private AlertDialog.Builder dialogBuilder;
//    private AlertDialog dialog;

    List<User> users;
    static private ListView userList;
    private FirebaseFirestore firestore;
    private static CollectionReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_list);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set up firestore
        firestore = FirebaseFirestore.getInstance();
        usersReference = firestore.collection("users");
        //Set up the employee and customer lists
        users = new ArrayList<>();

        userList = findViewById(R.id.listview);
        input= findViewById(R.id.input);
        enter= findViewById(R.id.add);
        HomeButton = findViewById(R.id.Home);
        addUserButton =findViewById(R.id.addUserButton);

        intent = getIntent();
        if (intent.getExtras() != null) {
            userRole = intent.getStringExtra("TAG");
        }

        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminUserListActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminUserListActivity.this, RegisterActivity.class);
                intent.putExtra("TAG",userRole);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        usersReference.whereEqualTo("role", userRole).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("UsersActivity", "Listen failed.", error);
                    return;
                }
                //Clear the list to prepare for loading of new data
                users.clear();
//                userList.setAdapter(adapter);
                //Iterate through the documents read from firestore
                for(QueryDocumentSnapshot doc : value) {
                    //If the document exists
                    if(doc.exists()) {
                        //Get the basic user data from the document
                        String name = doc.getString("name");
                        String email = doc.getString("email");
                        String uid = doc.getId();
                        //Create an EmployeeAccount Object
                        User account;
                        if (userRole.equals("Instructor")) {
                            account = new Instructor(name, email, uid);
                        } else {
                            account = new Student(name, email, uid);
                        }

                        //Add the account to the list
                        users.add(account);
                    }
                }
                //Set up the list in the UI
                setUpList(users, userList);
            }
        });
    }

    private void setUpList(final List<User> accounts, ListView listView) {
        //Create a list adapter
        AdminUserListAdapter adapter = new AdminUserListAdapter(AdminUserListActivity.this, accounts);
        listView.setAdapter(adapter);
    }

    /**
     * Deletes the given user from the database
     * @param account the account to delete
     */
    public static void deleteUser(User account) {
        usersReference.document(account.getUid()).delete();
    }

    /**
     * Back button function
     * @param view the current view
     */
    public void back(View view) {
        //Go back to previous activity
        this.finish();
    }
}