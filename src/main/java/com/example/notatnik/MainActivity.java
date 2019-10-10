package com.example.notatnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
-create note text view
-create database
-swipe to delete

*/

public class MainActivity extends AppCompatActivity {

    private static ArrayList<Document> documentList;
    private DocumentAdapter documentAdapter;

    ListView myListView;
    Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        documentList = new ArrayList<>();
        documentList.add(new Document(1, "Your very first note", "Note", 0));

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

    }

    //PRIVATE
    //OPENERS
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
    public static void displayWindow(WindowManager windowManager, Window window, double widthModifier, double heightModifier, EditText editText) {
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

    //CREATORS
    public static void createNewDocument(int id, String name, String type, int itemsCount) {

        documentList.add(new Document(id, name, type, itemsCount));
    }

    //GETTERS
    public static ArrayList<Document> GetDocumentList() {
        return documentList;
    }


}
