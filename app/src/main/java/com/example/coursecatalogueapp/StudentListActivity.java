package com.example.coursecatalogueapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class StudentListActivity extends AppCompatActivity {

    static ListView listView2;
    static ArrayList<String> studentnames;
    static ListViewAdapter2 adapter_students;
    EditText input;
    ImageView enter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        listView2 = (ListView)findViewById(R.id.listview);
        input= findViewById(R.id.input);
        enter=findViewById(R.id.add);

        studentnames = new ArrayList<>();
        studentnames.add("Mustafa Sarikaya");
        studentnames.add("Tao Zhong");
        studentnames.add("Talya Vuruskaner");
        studentnames.add("Kaiyuan Ye");
        studentnames.add("Selim Oztekin");

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String first = studentnames.get(position);
                makeToast(first);
            }
        });
        listView2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                makeToast("Removed: " + studentnames.get(position));
                removeName(position);
                return false;
            }
        });
        adapter_students = new ListViewAdapter2(getApplicationContext(),studentnames);
        listView2.setAdapter(adapter_students);

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
        loadContent();

    }
    // extract changes
    public void loadContent(){
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path,"list2.txt");
        byte[] content2 = new byte[(int) readFrom.length()];

        FileInputStream stream2 = null;
        try{
            stream2= new FileInputStream(readFrom);
            stream2.read(content2);

            String ss = new String(content2);

            ss= ss.substring(1, ss.length() -1 );
            String split[] = ss.split(", ");
            studentnames = new ArrayList<>(Arrays.asList(split));

            adapter_students= new ListViewAdapter2(this,studentnames);
            listView2.setAdapter(adapter_students);

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    // save changes
    @Override
    protected void onDestroy() {
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer2 = new FileOutputStream(new File(path, "list2.txt"));
            writer2.write(studentnames.toString().getBytes());
            writer2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    public static void removeName(int remove) {
        studentnames.remove(remove);
        listView2.setAdapter(adapter_students);
    }
    public static void addName(String first) {
        studentnames.add(first);
        listView2.setAdapter(adapter_students);
    }
    Toast t;
    private void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        t.show();
    }

}