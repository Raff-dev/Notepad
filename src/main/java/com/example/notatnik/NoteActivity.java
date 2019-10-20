package com.example.notatnik;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteActivity extends AppCompatActivity {


    Button saveButton,deleteButton;
    EditText noteEditText;
    MyDBHandler dbHandler;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note);

        deleteButton = findViewById(R.id.deleteButton);
        saveButton = findViewById(R.id.saveNoteButton);
        noteEditText = findViewById(R.id.noteEditText);
        //-------------------------------------------------------------------------
            Note note = getNote();
            noteEditText.setText(note.getText());

        //-------------------------------------------------------------------------

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDocument();
            }
        });
        //-------------------------------------------------------------------------

    }
    private boolean deleteDocument(){
        final int position = getIntent().getExtras().getInt("POSITION");
        int documentId = getIntent().getExtras().getInt("DOCUMENT_ID", -1);
        MyDBHandler dbHandler = new MyDBHandler(MainActivity.getAppContext(), null, null, 1);
        dbHandler.deleteDocument(documentId);
        MainActivity.GetDocumentList().remove(position);
        MainActivity.GetDocumentAdapter().notifyDataSetChanged();
        finish();
        return true;
    }
    private Note getNote(){
        Cursor data = dbHandler.getNote();
        data.moveToFirst();

        int documentId = data.getInt(data.getColumnIndex("idd"));
        String noteText = data.getString(data.getColumnIndex("notetext"));
        Note note = new Note(documentId,noteText);
        return note;
    }
    private boolean saveNote(){
        int documentId = getIntent().getExtras().getInt("DOCUMENT_ID", -1);
        dbHandler = new MyDBHandler(this,null,null,1);
        Note note = new Note(documentId,noteEditText.getText().toString());
        boolean insert = dbHandler.addNote(note);
        if(insert){
            Toast.makeText(this,"Saved succesfully! :D",Toast.LENGTH_SHORT);
            return true;
        }else{
            Toast.makeText(this,"Something went wrong! :(",Toast.LENGTH_SHORT);
            return false;
        }

    }
}
