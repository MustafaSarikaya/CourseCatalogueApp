package com.example.coursecatalogueapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class ListViewAdapter extends ArrayAdapter<String> {
    private static final String KEY_NAME = null ;
    ArrayList<String> list;
    Context context;
    public ListViewAdapter(Context context,ArrayList<String>names){
        super(context,R.layout.list_row, names);
        this.context=context;
        list=names;

    }

    @NonNull
    @Override
    public  View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row, null);

            TextView number = convertView.findViewById(R.id.number);
            number.setText(position + 1+ ".");
            TextView name = convertView.findViewById(R.id.name);
            name.setText(list.get(position));

            ImageView duplicate = convertView.findViewById(R.id.update);
            ImageView remove = convertView.findViewById(R.id.remove);

            duplicate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "Name Edited", Toast.LENGTH_SHORT).show();

                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InstructorsListActivity.removeName(position);
                    Toast.makeText(context, " Name Removed" , Toast.LENGTH_SHORT).show();

                }
            });


        }
        return convertView;
    }


}

