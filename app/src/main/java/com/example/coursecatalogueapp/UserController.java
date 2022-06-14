package com.example.coursecatalogueapp;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.coursecatalogueapp.Utils.Function;
import com.example.coursecatalogueapp.Utils.Utils;
import com.example.coursecatalogueapp.modules.Administrator;
import com.example.coursecatalogueapp.modules.Instructor;
import com.example.coursecatalogueapp.modules.Student;
import com.example.coursecatalogueapp.modules.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserController {
    private static UserController uInstance = null;

    private User userAccount;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    protected UserController() {
        this(FirebaseFirestore.getInstance(), FirebaseAuth.getInstance());
    }

    protected UserController(FirebaseFirestore db, FirebaseAuth auth) {
        this.auth = auth;
        this.firestore = db;
    }

    public void signInAsAdmin(final View view, final Function onSuccess) {
        //Sign into firebase as an anonymous user, to meet firestore rules
        auth.signInWithEmailAndPassword("admin@gmail.com", "admin123")
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    setUserAccount(new Administrator(auth.getCurrentUser().getUid()));
                    onSuccess.f();
                }
        })
        .addOnFailureListener(new OnFailureListener() {
            //Failed to get data from database
            @Override
            public void onFailure(@NonNull Exception e) {
                //Show failed error
//                Utils.showSnackbar("Failed to sign in as admin.", view);
            }
        });
    }

    /**
     * Signs user in with given email and password. Input validation is not handled here.
     * Shows error message in a snackbar on the view if any.
     * @param email the email of the user to sign in.
     * @param password the password of the user to sign in.
     * @param view the current view, required to show the error message.
     * @param onSuccess the function to call after everything has succeeded.
     */
    public void signIn(
        final String email,
        String password,
        final View view,
        final Function onSuccess
    ) {
        //Sign into firebase
        auth.signInWithEmailAndPassword(email, password)
        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            //Successful login attempt
            @Override
            public void onSuccess(AuthResult authResult) {
                //Retrieve user info from firestore
                firestore.collection("users")
                .document(auth.getCurrentUser().getUid()) //Gets the document with the user UID, where the data should be stored.
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //Successful data getting
                        //Gets the name and role from the document snapshot
                        String n = (String) documentSnapshot.getData().get("name");
                        String r = (String) documentSnapshot.getData().get("role");
                        switch(r) {
                            case "instructor":
                                setUserAccount(new Instructor(n, email, auth.getCurrentUser().getUid()));
                                break;
                            case "student":
                                setUserAccount(new Student(n, email, auth.getCurrentUser().getUid()));
                                break;
                            default:
//                                Utils.showSnackbar("Invalid data from database!", view);
                                break;
                        }
                        //Call the function to call after everything has succeeded.
                        //We pass through the login data to write to shared preferences.
                        onSuccess.f(n, email, r, auth.getCurrentUser().getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    //Failed to get data from database
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Show failed error
//                        Utils.showSnackbar("Failed to get user details from database!", view);
                    }
                });
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Failed login attempt
                try {
                    //Try to get firebase auth exception message
                    String errorMessage = "Log In Error: " + ((FirebaseAuthException) e).getErrorCode().replace("ERROR_", "");
                    //Show failed error
//                    Utils.showSnackbar(errorMessage, view);
                } catch (Exception ex) {
                    //Unexpected error
//                    Utils.showSnackbar("An unexpected error occurred.", view);
                }
            }
        });
    }

    /**
     * Signs user up with the given name, email, role, and password. Input validation is not handled here.
     * @param name the name of the user to sign up with
     * @param email the email of the user to sign up with
     * @param role the role of the user to sign up with
     * @param password the password of the user to sign up with
     * @param view the current view, required to show the error message.
     * @param onSuccess the function to call after everything has succeeded.
     */
    public void signUp(
        final String name,
        final String email,
        final String role,
        final String password,
        final View view,
        final Function onSuccess
    ) {
        // Create user on Firebase auth
        auth.createUserWithEmailAndPassword(email, password)
        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //Get the date and time that the account is created
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                //Create a map with the data to write to cloud firestore
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("name", name);
                userInfo.put("email", email);
                userInfo.put("role", role);
                userInfo.put("timeCreated", currentDateandTime);

                //Writes the data to firestore
                firestore.collection("users")
                .document(auth.getCurrentUser().getUid())
                .set(userInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    //Only when firestore succeeds in writing user data to database does the app log the user in.
                    @Override
                    public void onSuccess(Void aVoid) {
                        switch(role) {
                            case "Instructor":
                                setUserAccount(new Instructor(name, email, auth.getCurrentUser().getUid()));
                                break;
                            case "Student":
                                setUserAccount(new Student(name, email, auth.getCurrentUser().getUid()));
                                break;
                            default:
                                //This really shouldn't happen
                                Utils.showSnackbar("Invalid role! This should never happen.", view);
                                break;
                        }
                        onSuccess.f(name, email, role, auth.getCurrentUser().getUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Show failed error
                        Utils.showSnackbar("Failed to add user to database.", view);
                        //Tries to delete current user so that user can try to create new account again.
                        auth.getCurrentUser().delete();
                    }
                });
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Show failed error
//                Utils.showSnackbar("Failed to create user!", view);
            }
        });
    }

    public void signOut() {
        final String uid = userAccount.getUid();
        if(userAccount instanceof Administrator) {
            //Dispose of anonymous account
            auth.getCurrentUser().delete();
            auth.signOut();
        } else {
            FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.w("Messaging", "Fetching FCM registration token failed", task.getException());
                        auth.signOut();
                        return;
                    }

                    // Get new FCM registration token
                    final String token = task.getResult();

                    FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .collection("tokens")
                    .document(token)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            auth.signOut();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            auth.signOut();
                        }
                    });
                }
            });
        }
        userAccount = null;
    }

    public User getUserAccount() {
        return userAccount;
    }

    private void setUserAccount(User account) {
        this.userAccount = account;
    }

    public static synchronized void initialize(User account) {
        uInstance = new UserController();
        uInstance.setUserAccount(account);
    }

    public static synchronized void initialize(User account, FirebaseFirestore db, FirebaseAuth auth) {
        uInstance = new UserController(db, auth);
        uInstance.setUserAccount(account);
    }

    public static synchronized UserController getInstance() {
        if (uInstance == null) {
            uInstance = new UserController();
        }
        return uInstance;
    }
}
