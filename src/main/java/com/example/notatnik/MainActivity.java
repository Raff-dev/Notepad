package com.example.notatnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private ListView myListView;
    private static View creationWindowView;
    private Button createNewDocumentButton, deleteAllButton, createButton, closeButton;
    private RadioButton cRadio, nRadio, orangeRadio;
    private RadioGroup radioGroupType, radioGroupColor;
    private EditText newDocumentEditText;
    private MyDBHandler dbHandler;
    private static int documentid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //DECLARATIONS
        setContentView(R.layout.activity_main);
        MainActivity.context = getApplicationContext();
        createNewDocumentButton = findViewById(R.id.createNewDocumentButton);
        deleteAllButton = findViewById(R.id.deleteAllButton);
        myListView = findViewById(R.id.myListView);
        myListView.setAdapter(documentAdapter);
        //-------------------------------------------------------------------------

        //INSTANTIATIONS
        dbHandler = new MyDBHandler(this, null, null, 1);
        documentList = new ArrayList<>();
        documentAdapter = new DocumentAdapter(this, documentList);
        //-------------------------------------------------------------------------
        //ACTUAL CODE

        displayList();
        //-------------------------------------------------------------------------

        //LISTENERS
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDocumentActivity(position);
            }
        });
        createNewDocumentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreationWindow(v);
            }
        });
        deleteAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllDocuments();
            }
        });
        //-------------------------------------------------------------------------
    }

    private void deleteAllDocuments() {
        dbHandler.deleteAll();

        int i = documentList.size();
        while (i != 0) {
            documentList.remove(4);
            i--;
        }
        documentAdapter.notifyDataSetChanged();
    }

    private void displayList() {
        Cursor data = dbHandler.getDocuments();
        if (data.moveToFirst()) {
            while (!data.isAfterLast()) {
                int docId = data.getInt(data.getColumnIndex("idd"));
                String name = data.getString(data.getColumnIndex("documentname"));
                String type = data.getString(data.getColumnIndex("documenttype"));
                Document document = new Document(docId,name, type);
                documentList.add(document);
                data.moveToNext();
            }
            documentAdapter.notifyDataSetChanged();
        }

    }

    private void openCreationWindow(View view) {
        //DECLARATIONS
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        creationWindowView = inflater.inflate(R.layout.activity_creation_window, null);
        final PopupWindow creationPopUp = new PopupWindow(
                creationWindowView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                true);
        newDocumentEditText = creationWindowView.findViewById(R.id.newDocumentName);
        createButton = creationWindowView.findViewById(R.id.createButton);
        closeButton = creationWindowView.findViewById(R.id.closeButton);
        radioGroupType = creationWindowView.findViewById(R.id.radioGroupType);
        nRadio = creationWindowView.findViewById(R.id.nRadio);
        cRadio = creationWindowView.findViewById(R.id.cRadio);
        orangeRadio = creationWindowView.findViewById(R.id.orangeRadio);
        radioGroupColor = creationWindowView.findViewById(R.id.radioGroupColor);
        //-------------------------------------------------------------------------
        nRadio.setChecked(true);
        orangeRadio.setChecked(true);
        //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.showSoftInput(newDocumentEditText,InputMethodManager.SHOW_IMPLICIT);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //LISTENERS

        closeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                creationPopUp.dismiss();
            }
        });
        createButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean create = createNewDocument();
                if (create) {
                    creationPopUp.dismiss();
                }
            }
        });
        //-------------------------------------------------------------------------


        creationPopUp.showAtLocation(view, Gravity.CENTER, 0, -200);

    }

    private void openDocumentActivity(int position) {
        String type = documentList.get(position).getType();
        int id= documentList.get(position).getId();
        switch (type) {
            case "CheckList":
                Intent i = new Intent(
                        getApplicationContext(),
                        TaskList.class);
                i.putExtra("POSITION", position);
                i.putExtra("DOCUMENT_ID",id);
                startActivity(i);
                break;
            case "Note":
                Intent a = new Intent(
                        getApplicationContext(),
                        NoteActivity.class);
                a.putExtra("POSITION", position);
                a.putExtra("DOCUMENT_ID",id);
                startActivity(a);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Cannot identify document's type", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean createNewDocument() {
        dbHandler = new MyDBHandler(this, null, null, 1);
        if (!newDocumentEditText.getText().toString().isEmpty()) {
            Document document = new Document(
                    dbHandler.getSavedID(),
                    newDocumentEditText.getText().toString(),
                    getDocumentType(),
                    getDocumentColor());

            boolean insert = dbHandler.addDocument(document);
            if (insert) {
                Toast.makeText(getApplicationContext(), "Added Succesfully", Toast.LENGTH_SHORT).show();
                documentList.add(document);
                documentAdapter.notifyDataSetChanged();
                return true;

            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong! :( ", Toast.LENGTH_SHORT);
                return false;
            }
        } else {
            Toast.makeText(this, "Document's name cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    //GETTERS
    public static ArrayList<Document> GetDocumentList() {
        return documentList;
    }

    public static DocumentAdapter GetDocumentAdapter() {
        return documentAdapter;
    }

    public static Context getAppContext() {
        return MainActivity.context;
    }

    private String getDocumentType() {
        String type;

        radioGroupType = creationWindowView.findViewById(R.id.radioGroupType);
        switch (radioGroupType.getCheckedRadioButtonId()) {
            case R.id.nRadio:
                type = "NoteActivity";
                break;
            case R.id.cRadio:
                type = "CheckList";
                break;
            default:
                type = "chuj, nie działa";
        }
        return (type);
    }

    private String getDocumentColor() {
        String color;

        radioGroupColor = creationWindowView.findViewById(R.id.radioGroupColor);
        switch (radioGroupColor.getCheckedRadioButtonId()) {
            case R.id.orangeRadio:
                color = "@color/newOrange";
                break;
            case R.id.greenRadio:
                color = "@color/newGreen";
                break;
            case R.id.lightBlueRadio:
                color = "@color/newLightBlue";
                break;
            case R.id.darkBlueRadio:
                color = "@color/newDarkBlue";
                break;
            case R.id.purpleRadio:
                color = "@color/newPurple";
                break;
            case R.id.redRadio:
                color = "@color/newRed";
                break;
            default:
                color = "chuj, nie działa";
        }
        return (color);

    }
}