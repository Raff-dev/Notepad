package com.example.notatnik;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.notatnik.DocumentsGutsChecklist.GetTaskList;
import static com.example.notatnik.DocumentsGutsChecklist.addTask;
import static com.example.notatnik.MainActivity.displayWindow;

public class CreationTaskWindow extends AppCompatActivity {

    EditText newTaskName;
    Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_task_window);

        displayWindow(getWindowManager(), getWindow(),1,0.1, newTaskName);

        newTaskName = findViewById(R.id.newTaskName);
        newTaskName.requestFocus();

        applyButton = findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrToast();
            }
        });


    }

    private void createOrToast() {

        //CREATE GET_CHECKBOXES METHOD

        final EditText newTaskName = findViewById(R.id.newTaskName);

        if (!newTaskName.getText().toString().isEmpty()) {
            addTask(
                    GetTaskList().size() + 1,
                    newTaskName.getText().toString(),
                    false);

            finish();
        } else {

            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    "Enter a Task name",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
