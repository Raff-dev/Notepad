package com.example.notatnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


/*  TO DO

-add color setting
-type images in list view
-create database
-swipe to delete




MAKE A REFRESH FUNCTION THAT PUTS ITEMS INTO LISTVIEW
INVOKE REFRESH WHILE ADDING NEW ELEMENT TO LIST
*/

public class MainActivity extends AppCompatActivity {

    private static ArrayList<Document> documentList;
    private static DocumentAdapter documentAdapter;
    private static Context context;
    ListView myListView;
    Button createButton,deleteAllButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context=getApplicationContext();
        final MyDBHandler dbHandler = new MyDBHandler(this,null,null,1);
        Document exampleDocument = new Document("Your very first note", "Note");

        documentList = new ArrayList<>();



        documentAdapter = new DocumentAdapter(this, documentList);
        myListView = findViewById(R.id.myListView);
        myListView.setAdapter(documentAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDocumentActivity(position);
            }
        });


        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreationActivity();
            }
        });
        deleteAllButton = findViewById(R.id.deleteAllButton);
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.deleteAll();

                int i = documentList.size();
                while(i!=0){
                    documentList.remove(1);
                    i--;
                }
                documentAdapter.notifyDataSetChanged();
            }
        });

        Cursor data = dbHandler.getListContents();
        if(data.moveToFirst()){
            while(!data.isAfterLast()){
                String name = data.getString(data.getColumnIndex("documentname"));
                String type = data.getString(data.getColumnIndex("documenttype"));
                Document document = new Document(name,type);
                documentList.add(document);
                documentAdapter.notifyDataSetChanged();
                data.moveToNext();
            }
        }
    }



    //PRIVATE
    private void openCreationActivity() {
        Intent i = new Intent(
                getApplicationContext(),
                CreationWindow.class);
        startActivity(i);
    }

    private void openDocumentActivity(int position) {
        String type = documentList.get(position).getType();
        switch (type) {
            case "CheckList":
                Intent i = new Intent(
                        getApplicationContext(),
                        DocumentsGutsChecklist.class);
                i.putExtra("POSITION", position);
                startActivity(i);
                break;
            case "Note":
                Intent a = new Intent(
                        getApplicationContext(),
                        DocumentsGutsNote.class);
                a.putExtra("POSITION", position);
                startActivity(a);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Cannot identify document's type", Toast.LENGTH_SHORT).show();
        }
    }

    //PUBLIC
    public static void displayWindow(
            WindowManager windowManager, Window window, double widthModifier, double heightModifier, EditText editText) {
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        window.setLayout((int) (width * widthModifier), (int) (height * heightModifier));

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -200;

        window.setAttributes(params);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static void displayList(){



    }
    //GETTERS
    public static ArrayList<Document> GetDocumentList() {
        return documentList;
    }
    public static DocumentAdapter GetDocumentAdapter(){
        return documentAdapter;
    }

    public static Context getAppContext(){
        return MainActivity.context;
    }
}
