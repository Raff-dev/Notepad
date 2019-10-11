package com.example.notatnik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import static com.example.notatnik.MainActivity.*;

public class CreationWindow extends AppCompatActivity {

    Button createButton, closeButton;
    RadioButton cRadio, lRadio, gRadio;
    RadioGroup radioGroup;
    EditText focus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_window);

        MainActivity.displayWindow(getWindowManager(),getWindow(),0.8,0.35,focus);

        lRadio = findViewById(R.id.nRadio);
        lRadio.setChecked(true);
        gRadio = findViewById(R.id.gRadio);
        cRadio = findViewById(R.id.cRadio);


        focus = findViewById(R.id.newDocumentName);
        focus.requestFocus();


        closeButton = findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //APPLY DOCUMENT'S ITEM_COUNT HERE
        //APPLY DOCUMENTS TYPE
        //APPLY COLOR
        createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrToast();
            }
        });
    }




    private void createOrToast() {

        //CREATE GET_CHECKBOXES METHOD

        final EditText newDocumentNameEditText = findViewById(R.id.newDocumentName);


        if (!newDocumentNameEditText.getText().toString().isEmpty()) {
            createNewDocument(
                    GetDocumentList().size() + 1,
                    newDocumentNameEditText.getText().toString(),
                    getDocumentType()
            );

            finish();
        } else {

            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    "Document name is required",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private String getDocumentType() {
        String type;

        radioGroup = findViewById(R.id.radioGroup);
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.nRadio:
                type = "Note";
                break;
            case R.id.gRadio:
                type = "Graphic";
                break;
            case R.id.cRadio:
                type = "CheckList";
                break;
            default:
                type = "chuj, nie działa";
        }
        return (type);
    }
}
