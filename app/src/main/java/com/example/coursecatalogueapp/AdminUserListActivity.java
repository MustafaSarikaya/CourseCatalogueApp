package com.example.coursecatalogueapp;

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
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.coursecatalogueapp.modules.Instructor;
import com.example.coursecatalogueapp.modules.Student;
import com.example.coursecatalogueapp.modules.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AdminUserListActivity extends AppCompatActivity {

    static AdminUserListAdapter adapter;
    EditText input;
    ImageView enter, HomeButton, addUserButton, Update;
    String userRole;
    Intent intent;

//    private TextView isim2, numara2,email2;
//    private Button ok_pop2;
//
//    private AlertDialog.Builder dialogBuilder;
//    private AlertDialog dialog;

    List<User> users;
    static private ListView userList;
    private FirebaseFirestore firestore;
    private CollectionReference usersReference;

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
//        Update = findViewById(R.id.update);

        intent = getIntent();
        if (intent.getExtras() != null) {
            userRole = intent.getStringExtra("TAG");
        }

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

        userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteUser(users.get(position));
                return false;
            }
        });

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
                Intent intent = new Intent(AdminUserListActivity.this,RegisterActivity.class);
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
        for(int i = 0; i < adapter.getCount(); i++) {
            //Get final version of index
            final int finalI = i;
            //Get the list item from the adapter at the index
            View view = adapter.getView(i, null, listView);
            //Open the user info dialog when the list item is clicked
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userInfoDialog(accounts.get(finalI));
                }
            });
            //Set delete button functions
            ImageButton deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteUserDialog(accounts.get(finalI));
                }
            });
            //Add the list item to the list view

        }
        listView.setAdapter(adapter);
    }

    /**
     * Opens a dialog displaying the text format of the given user account
     * @param account the user account to display the info for
     */
    private void userInfoDialog(User account) {
        //Create new AlertDialog
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(AdminUserListActivity.this);
        alertDialogBuilder
                .setTitle(account.getName()) //Set the title of the dialog to the account name
                .setMessage(account.toString()) //Set the message of the dialog to the account text
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
     * @param account the account to delete
     */
    private void deleteUserDialog(final User account) {
        //Create new AlertDialog
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(AdminUserListActivity.this);
        alertDialogBuilder
                .setTitle("Delete user account for " + account.getName() + "?")
                .setMessage("Are you sure you want to delete this user account? Any data associated with this user will be permanently deleted.")
                .setCancelable(true)
                .setPositiveButton(
                        "YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete the account
                                deleteUser(account);
                                dialog.cancel();
                            }
                        }
                )
                .setNegativeButton(
                        "NO",
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
     * Deletes the given user from the database
     * @param account the account to delete
     */
    private void deleteUser(User account) {
        usersReference.document(account.getUid()).delete();
    }

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
     * @param view the current view
     */
    public void back(View view) {
        //Go back to previous activity
        this.finish();
    }
}