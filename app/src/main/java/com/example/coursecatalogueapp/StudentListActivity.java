package com.example.coursecatalogueapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    static ListView listView2;
    static ArrayList<String> studentnames;
    static ListViewAdapter2 adapter2;
    EditText input;
    ImageView enter;
    ImageView HomeButton;
    ImageView AddStudent;

    private TextView isim2, numara2,email2;
    private Button ok_pop2;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        listView2=(ListView)findViewById(R.id.listView2);
        input = findViewById(R.id.input);
        enter = findViewById(R.id.add);
        HomeButton = findViewById(R.id.Home);
        AddStudent = findViewById(R.id.Add_btn);

        studentnames = new ArrayList<>();
        studentnames.add("Selim Oztkein");




        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String name = studentnames.get(position);

                viewCard();
                isim2.setText(name);
               // String name = studentnames.get(position);
               // makeToast(name);
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
        adapter2 = new ListViewAdapter2(getApplicationContext(),studentnames);
        listView2.setAdapter(adapter2);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();

                if (text==null || text.length() == 0 || text.contains("\n")) {
                    makeToast("Enter a Name");

                }else if(text.matches("([z0-9]+)||([$&+,:;=?@#|'<>.^*()%!-])")){
                    makeToast("Invalid Entry!");
                }
                else {
                    addName(text);
                    input.setText("");
                    makeToast("Added " + text);
                }
            }
        });
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });
        AddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(StudentListActivity.this,RegisterActivity.class);
                intent.putExtra("TAG","Student");
                startActivity(intent);
            }
        });


        loadcontent();

    }

    public void viewCard(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View popupWindow = getLayoutInflater().inflate(R.layout.popup,null);
        isim2 = (TextView) popupWindow.findViewById(R.id.isim);
        numara2 = (TextView) popupWindow.findViewById(R.id.numara);
        email2= (TextView) popupWindow.findViewById(R.id.email);
        ok_pop2 = (Button)popupWindow.findViewById(R.id.ok_pop);



        dialogBuilder.setView(popupWindow);
        dialog = dialogBuilder.create();
        dialog.show();

        ok_pop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }


    public void loadcontent() {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, "list2.txt");
        byte[] content = new byte[(int) readFrom.length()];

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(readFrom);
            stream.read(content);

            String s = new String(content);

            s = s.substring(1, s.length() - 1);
            String split[] = s.split(", ");
            studentnames = new ArrayList<>(Arrays.asList(split));

            adapter2 = new ListViewAdapter2(this, studentnames);
            listView2.setAdapter(adapter2);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void onDestroy() {
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, "list2.txt"));
            writer.write(studentnames.toString().getBytes());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

   // remove name
    public static void removeName(int remove) {
        studentnames.remove(remove);
        listView2.setAdapter(adapter2);
    }

    //add name
    public static void addName(String name){
        studentnames.add(name);
        listView2.setAdapter(adapter2);
    }
    Toast t;
    private void makeToast(String s){
        if(t!=null) t.cancel();
        t= Toast.makeText(getApplicationContext(),s , Toast.LENGTH_SHORT);
        t.show();
    }
}
