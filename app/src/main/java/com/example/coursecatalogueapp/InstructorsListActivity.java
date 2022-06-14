package com.example.coursecatalogueapp;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coursecatalogueapp.modules.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InstructorsListActivity extends AppCompatActivity {

    static ListView listView;
    static ArrayList<String> names;
    static ListViewAdapter adapter;
    EditText input;
    ImageView enter;
    ImageView HomeButton;
    ImageView PlusButton;
    ImageView Update;

    private TextView isim2, numara2,email2;
    private Button ok_pop2;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    List<User> instructor;
    LinearLayout employeesList;
    private FirebaseFirestore firestore;
    private CollectionReference usersReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listView = (ListView)findViewById(R.id.listview);
        input= findViewById(R.id.input);
        enter= findViewById(R.id.add);
        HomeButton = findViewById(R.id.Home);
        PlusButton=findViewById(R.id.Add_btn);
        Update = findViewById(R.id.update);



        // String surnames = getIntent().getStringExtra("keyname");
        //names.add(surnames);
        //names.add(new instructor("juan","gomez","gomez@gmail.com","1234"));





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String name = names.get(position);


                viewCard();
                isim2.setText(name);
               // String name = names.get(position);
                //makeToast(name);
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

                if (text==null || text.length() == 0 || text.contains("\n")) {
                    makeToast("Enter a Name");

                }else if(text.matches("([z0-9]+)||([$&+,:;=?@#|'<>.^*()%!-])")){
                    makeToast("Invalid Entry!");
                }
                else {
                    //addInstructor(text);
                    input.setText("");
                    makeToast("Added " + text);
                }
            }
        });
        HomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstructorsListActivity.this, AdminMainActivity.class);
                startActivity(intent);
            }
        });
        PlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstructorsListActivity.this,RegisterActivity.class);
                intent.putExtra("TAG","Instructor");
                startActivity(intent);
            }
        });






        loadcontent();

       // File path = getApplicationContext().getFilesDir();
       // File readFrom = new File(path,"list.txt");
        //byte[] content = new byte[(int) readFrom.length()];

     //   Bundle ReceiveName = getIntent().getExtras();

     //   if(ReceiveName != null){
         //   String nam= ReceiveName.getString("name");
           // names.add(nam);
        }

        // popup
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

    //  FileInputStream stream = null;



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


//            names = new ArrayList<>(Arrays.asList(split));
//            adapter= new ListViewAdapter(this,names);
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