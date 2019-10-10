package com.example.notatnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

//TO DO
//SWIPE TO SIDES TO CHANGE BETWEEN DOCUMENTS

public class DocumentsGutsChecklist extends AppCompatActivity {

    public static ArrayList<Task> taskList;
    public static TaskAdapter taskAdapter;
    ListView taskListView;
    Button addTaskButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_guts_checklist);
        setDocumentName();


        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList);
        taskListView = findViewById(R.id.taskListView);
        taskListView.setAdapter(taskAdapter);

        taskList.add(new Task(1, "asd", false));

        addTaskButton = findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreationTaskActivity();
            }
        });


    }

    private void setDocumentName() {
        int position = getIntent().getExtras().getInt("POSITION", -1);
        TextView documentName = findViewById(R.id.documentName);
        documentName.setText(MainActivity.GetDocumentList().get(position).getName());
    }

    public static void addTask(int id, String name, boolean ischecked) {
        taskList.add(new Task(id, name, ischecked));
        taskAdapter.notifyDataSetChanged();
    }

    private void openCreationTaskActivity() {
        Intent i = new Intent(
                getApplicationContext(),
                CreationTaskWindow.class);
        startActivity(i);
    }

    public static ArrayList<Task> GetTaskList() {
        return taskList;
    }
}
