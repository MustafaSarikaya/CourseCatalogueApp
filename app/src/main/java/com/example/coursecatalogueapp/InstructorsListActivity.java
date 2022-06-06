package com.example.coursecatalogueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class InstructorsListActivity extends AppCompatActivity {

    static ListView listView;
    static ArrayList<String> names;
    static ListViewAdapter adapter;
    EditText input;
    ImageView enter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);

        listView = (ListView)findViewById(R.id.listview);
        input= findViewById(R.id.input);
        enter=findViewById(R.id.add);

        names = new ArrayList<>();
        names.add("Juan Gomez");
        names.add("Cris Ronaldo");
        names.add("Jitao Zhang");
        names.add("Paulo Dybala");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String name = names.get(position);
                makeToast(name);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                makeToast("Removed: " + names.get(position));
                removeName(position);
                return false;
            }
        });

        adapter = new ListViewAdapter(getApplicationContext(),names);
        listView.setAdapter(adapter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();
                if(text==null || text.length() == 0){
                    makeToast("Enter a Name");
                }else{
                    addName(text);
                    input.setText("");
                    makeToast("Added "+ text);
                }
            }
        });

    }
    public static void removeName(int remove){
        names.remove(remove);
        listView.setAdapter(adapter);

    }

    public static void addName(String name){
        names.add(name);
        listView.setAdapter(adapter);
    }
    Toast t;
    private void makeToast(String s){
        if(t!=null) t.cancel();
        t= Toast.makeText(getApplicationContext(),s , Toast.LENGTH_SHORT);
        t.show();
    }
}