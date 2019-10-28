package com.example.notatnik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {


    private Button saveButton, deleteButton;
    private EditText noteEditText;
    private TextView documentNameTextView;
    private MyDBHandler dbHandler;
    private static Context noteContext;
    Document document;
    int documentId, position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //DECLARATIONS
        setContentView(R.layout.activity_note);
        noteContext = this.getApplicationContext();
        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveNoteButton);
        noteEditText = findViewById(R.id.noteEditText);
        documentNameTextView=findViewById(R.id.documentName);
        position = getIntent().getExtras().getInt("POSITION");
        document = MainActivity.GetDocumentList().get(position);
        documentId=document.getId();
        //-------------------------------------------------------------------------
        //INSTANTIATIONS
        dbHandler = new MyDBHandler(this, null, null, 1);
        Note note = new Note(dbHandler.getNote(documentId));

        //-------------------------------------------------------------------------
        //ACTUAL CODE
        setDocumentColor();
        documentNameTextView.setText(document.getName());
        noteEditText.setText(note.getText());
        //-------------------------------------------------------------------------
        //LISTENERS
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDocument();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.updateNote(new Note(documentId, noteEditText.getText().toString()));
                finish();
            }
        });
        //-------------------------------------------------------------------------

    }

    private boolean deleteDocument() {

        dbHandler.deleteNoteDocument(documentId);
        MainActivity.GetDocumentList().remove(position);
        MainActivity.GetDocumentAdapter().notifyDataSetChanged();
        finish();
        return true;
    }

    private void setDocumentColor() {
        RelativeLayout rl = this.findViewById(R.id.noteActivityLayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rl.setBackground(getResources().getDrawable(MainActivity.getBackgroundColor(document)));
        }
    }

    public static Context getNoteContext() {
        return noteContext;
    }
}
