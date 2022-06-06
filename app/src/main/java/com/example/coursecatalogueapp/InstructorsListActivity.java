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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

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
        loadcontent();

    }
    // extract changes
    public void loadcontent(){
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path,"list.txt");
        byte[] content = new byte[(int) readFrom.length()];

        FileInputStream stream = null;
        try{
            stream= new FileInputStream(readFrom);
            stream.read(content);

            String s = new String(content);

            s= s.substring(1, s.length() -1 );
            String split[] = s.split(", ");
            names = new ArrayList<>(Arrays.asList(split));

            adapter= new ListViewAdapter(this,names);
            listView.setAdapter(adapter);

        }catch(Exception e){
            e.printStackTrace();
        }


    }
       // save changes
    @Override
    protected void onDestroy() {
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, "list.txt"));
            writer.write(names.toString().getBytes());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    // remove name
    public static void removeName(int remove){
        names.remove(remove);
        listView.setAdapter(adapter);

    }
    //add name
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