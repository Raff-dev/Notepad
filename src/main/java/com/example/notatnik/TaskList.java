package com.example.notatnik;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//TO DO
//SWIPE TO SIDES TO CHANGE BETWEEN DOCUMENTS

public class TaskList extends AppCompatActivity {

    private static ArrayList<Task> taskList;
    private static TaskAdapter taskAdapter;
    private ListView taskListView;
    private Button addTaskButton,deleteButton,applybutton;
    private EditText newTaskName;
    private MyDBHandler dbHandler;
    private int position, documentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //DECLARATIONS
        setContentView(R.layout.activity_tasklist);
        setDocumentName();
        position = getIntent().getExtras().getInt("POSITION", -1);
        documentId = getIntent().getExtras().getInt("DOCUMENT_ID", -1);
        taskListView = findViewById(R.id.taskListView);
        addTaskButton = findViewById(R.id.addTaskButton);
        deleteButton = findViewById(R.id.deleteButton);
        taskListView.setAdapter(taskAdapter);
        //-------------------------------------------------------------------------

        //INSTANTIATIONS
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList);
        //-------------------------------------------------------------------------

        //ACTUAL CODE
        displayTasks();
        //-------------------------------------------------------------------------

        //LISTENERS
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreationTaskWindow(v);
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
    private void setDocumentName() {

        TextView documentName = findViewById(R.id.documentName);
        documentName.setText(MainActivity.GetDocumentList().get(position).getName());
    }
    private void deleteDocument(){
        final int position = getIntent().getExtras().getInt("POSITION");
        MyDBHandler dbHandler = new MyDBHandler(MainActivity.getAppContext(),null,null,1);
        int id =MainActivity.GetDocumentList().get(position).getId();
        dbHandler.deleteDocument(id);
        MainActivity.GetDocumentList().remove(position);
        MainActivity.GetDocumentAdapter().notifyDataSetChanged();
        finish();
    }

    public boolean createNewTask() {
        dbHandler=new MyDBHandler(this,null,null,1);
        if(!newTaskName.getText().toString().isEmpty()) {
            Task task = new Task(
                    documentId,
                    getTaskList().size(),
                    newTaskName.getText().toString(),
                    false);
            boolean insert = dbHandler.addTask(task);
            if(insert){
                Toast.makeText(this,"Task added succesfully", Toast.LENGTH_SHORT).show();
                taskList.add(task);
                taskAdapter.notifyDataSetChanged();
                return true;
            }else{
                Toast.makeText(this,"Something went wrong! :(", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(this, "Task name cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void displayTasks(){
        Cursor data = dbHandler.getTasks();
        if(data.moveToFirst()){
            while(!data.isAfterLast()){
                int docId = data.getInt(data.getColumnIndex("idd"));
                int taskId = data.getInt(data.getColumnIndex("idt"));
                String taskName = data.getString(data.getColumnIndex("taskname"));
                int isCheckedInt = data.getInt(data.getColumnIndex("checked"));
                Boolean isChecked;
                if(isCheckedInt!=0){
                    isChecked=true;
                }else{
                    isChecked=false;
                }
                Task task = new Task(docId,taskId,taskName,isChecked);
                taskList.add(task);
            }

        }
        taskAdapter.notifyDataSetChanged();
    }

    private void openCreationTaskWindow(View view) {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View creationTaskWindow = inflater.inflate(R.layout.activity_creation_task_window,null);
        final PopupWindow creationTaskPopUp = new PopupWindow(
                creationTaskWindow,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                true);
        newTaskName = findViewById(R.id.newTaskName);
        applybutton = findViewById(R.id.addTaskButton);
        //-------------------------------------------------------------------------
        applybutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean create = createNewTask();
                if(create){
                    creationTaskPopUp.dismiss();
                }
            }

        });
        creationTaskPopUp.showAtLocation(view, Gravity.CENTER,0,0);

    }

    //GETTERS
    public static ArrayList<Task> getTaskList() {
        return taskList;
    }
    public static TaskAdapter getTaskAdapter(){
        return taskAdapter;
    }
}
