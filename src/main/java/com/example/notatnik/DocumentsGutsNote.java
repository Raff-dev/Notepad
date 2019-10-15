package com.example.notatnik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DocumentsGutsNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int position = getIntent().getExtras().getInt("POSITION");
        setContentView(R.layout.activity_documents_guts_note);

        //
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDBHandler dbHandler = new MyDBHandler(MainActivity.getAppContext(), null, null, 1);
                int id = MainActivity.GetDocumentList().get(position).getId();
                dbHandler.deleteDocument(id);
                MainActivity.GetDocumentList().remove(position);
                MainActivity.GetDocumentAdapter().notifyDataSetChanged();
                finish();
            }
        });
        //


    }
}
