package com.example.coursecatalogueapp.admin;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.coursecatalogueapp.R;
import com.example.coursecatalogueapp.modules.Course;
import com.example.coursecatalogueapp.modules.User;

import java.util.List;

class AdminUserListAdapter extends ArrayAdapter<User> {
    private Activity context;
    List<User> accounts;

    public AdminUserListAdapter(@NonNull Activity context, @NonNull List<User> accounts) {
        super(context, R.layout.list_row, accounts);
        this.context = context;
        this.accounts = accounts;
    }

    @NonNull
    @Override
    public  View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View listViewItem = layoutInflater.inflate(R.layout.list_row, null,true);

        TextView username = listViewItem.findViewById(R.id.username);
        TextView email = listViewItem.findViewById(R.id.email);

        User account = accounts.get(position);

        username.setText(account.getName());
        email.setText(account.getEmailAddress());

        //Set delete button functions
        ImageButton deleteButton = (ImageButton) listViewItem.findViewById(R.id.userDeleteBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminUserListActivity.deleteUser(account);
            }
        });

        return listViewItem;
    }
}

