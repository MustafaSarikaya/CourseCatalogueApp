package com.example.coursecatalogueapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

//        TextView number = listViewItem.findViewById(R.id.number);
//        number.setText(position + 1+ ".");
        TextView name = listViewItem.findViewById(R.id.courseCode);

        User account = accounts.get(position);

        name.setText(account.getName());

        return listViewItem;
    }
}

