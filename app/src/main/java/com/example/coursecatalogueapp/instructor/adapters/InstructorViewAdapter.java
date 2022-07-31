package com.example.coursecatalogueapp.instructor.adapters;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.admin.AdminUserListActivity;
import com.example.coursecatalogueapp.modules.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class InstructorViewAdapter extends ArrayAdapter<User> {
    private Activity context;
    List<User> accounts;
    private FirebaseFirestore firestore;
    private static CollectionReference userReference;

    public InstructorViewAdapter(@NonNull Activity context, @NonNull List<User> accounts) {
        super(context, R.layout.list_row, accounts);
        this.context = context;
        this.accounts = accounts;
        firestore = FirebaseFirestore.getInstance();
        userReference = firestore.collection("users");
    }

    @NonNull
    @Override
    public  View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listViewItem = layoutInflater.inflate(R.layout.list_row, null,true);

        TextView username = listViewItem.findViewById(R.id.username);
        TextView email = listViewItem.findViewById(R.id.email);

        User account = accounts.get(position);

        DocumentReference docRef = userReference.document(account.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String userName = (String) document.get("name");
                        username.setText(userName);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        //Set delete button functions
        ImageButton deleteButton = (ImageButton) listViewItem.findViewById(R.id.userDeleteBtn);
        deleteButton.setVisibility(View.INVISIBLE);

        return listViewItem;
    }
}

