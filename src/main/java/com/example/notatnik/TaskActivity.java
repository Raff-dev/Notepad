package com.example.notatnik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

//TO DO
//SWIPE TO SIDES TO CHANGE BETWEEN DOCUMENTS

public class TaskActivity extends AppCompatActivity {

    private static ArrayList<Task> taskArrayList;
    private RecyclerView taskRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter taskAdapter;

    private MyDBHandler dbHandler;
    private Document document;
    private Task task;
    private int position, documentId;

    private static View creationTaskWindow;
    private Button addTaskButton, deleteButton, applybutton;
    private EditText newTaskName;
    private CheckBox taskCheckBox;
    private TextView taskName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //SETTING UP
        //-------------------------------------------------------------------------
        setContentView(R.layout.activity_task);
        position = getIntent().getExtras().getInt("POSITION", -1);
        document = MainActivity.GetDocumentList().get(position);
        documentId = document.getId();
        dbHandler = new MyDBHandler(this, null, null, 1);
        //-------------------------------------------------------------------------
        taskRecyclerView = findViewById(R.id.taskRecyclerView);
        addTaskButton = findViewById(R.id.addTaskButton);
        deleteButton = findViewById(R.id.deleteButton);
        taskCheckBox = findViewById(R.id.taskCheckbox);
        taskName = findViewById(R.id.taskName);
        //-------------------------------------------------------------------------
        taskArrayList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        taskAdapter = new TaskAdapter(taskArrayList);
        taskRecyclerView.setLayoutManager(layoutManager);
        taskRecyclerView.setAdapter(taskAdapter);
        //-------------------------------------------------------------------------

        //CODE
        //-------------------------------------------------------------------------
        setDocumentName();
        setDocumentColor();
        displayTasks();
        itemTouchHelper();

        //LISTENERS
        //-------------------------------------------------------------------------
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

    private void itemTouchHelper() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteTask(viewHolder.getAdapterPosition());

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                dX /= 2;
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(TaskActivity.this, R.color.newRed))
                        .addActionIcon(R.drawable.ic_delete)
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper ith = new ItemTouchHelper(callback);
        ith.attachToRecyclerView(taskRecyclerView);
    }

    private void setDocumentName() {

        TextView documentName = findViewById(R.id.documentName);
        documentName.setText(MainActivity.GetDocumentList().get(position).getName());
    }

    private void setDocumentColor() {
        RelativeLayout rl = this.findViewById(R.id.taskActivityLayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rl.setBackground(getResources().getDrawable(MainActivity.getBackgroundColor(document)));
        }
    }

    private void deleteDocument() {
        final int position = getIntent().getExtras().getInt("POSITION");
        MyDBHandler dbHandler = new MyDBHandler(MainActivity.getAppContext(), null, null, 1);
        int id = MainActivity.GetDocumentList().get(position).getId();
        dbHandler.deleteTaskDocument(id);
        MainActivity.GetDocumentList().remove(position);
        MainActivity.GetDocumentAdapter().notifyDataSetChanged();
        finish();
    }

    public boolean createNewTask() {
        if (!newTaskName.getText().toString().isEmpty()) {
            Task task = new Task(
                    documentId,
                    getTaskList().size(),
                    newTaskName.getText().toString(),
                    false);
            boolean insert = dbHandler.addTask(task);
            if (insert) {
                Toast.makeText(this, "Task added succesfully", Toast.LENGTH_SHORT).show();
                taskArrayList.add(task);
                taskAdapter.notifyDataSetChanged();
                MainActivity.GetDocumentAdapter().notifyDataSetChanged();
                return true;
            } else {
                Toast.makeText(this, "Something went wrong! :(", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "Task name cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void deleteTask(int position) {
        int iddt = taskArrayList.get(position).getDocumentId();
        int idt = taskArrayList.get(position).getId();
        taskArrayList.remove(position);
        dbHandler.deleteTask(iddt, idt);
        taskAdapter.notifyDataSetChanged();
    }

    public void deleteTask(Task Task) {

    }

    public void displayTasks() {

        Cursor data = dbHandler.getTasks(documentId);
        if (data.moveToFirst()) {
            while (!data.isAfterLast()) {
                int docId = data.getInt(data.getColumnIndex("iddt"));
                int taskId = data.getInt(data.getColumnIndex("idt"));
                String taskName = data.getString(data.getColumnIndex("taskname"));
                int isCheckedInt = data.getInt(data.getColumnIndex("checked"));
                Boolean isChecked = (isCheckedInt == 1);
                Task task = new Task(docId, taskId, taskName, isChecked);
                data.moveToNext();
                taskArrayList.add(task);
            }

        }
        taskAdapter.notifyDataSetChanged();
    }

    private void openCreationTaskWindow(View view) {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        creationTaskWindow = inflater.inflate(R.layout.activity_creation_task_window, null);
        final PopupWindow creationTaskPopUp = new PopupWindow(
                creationTaskWindow,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                160,
                true);
        //-------------------------------------------------------------------------
        newTaskName = creationTaskWindow.findViewById(R.id.newTaskName);
        newTaskName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean bool = true;
                if (bool) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    bool = !bool;
                } else {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    bool = !bool;
                }
            }
        });
        newTaskName.requestFocus();
        //-------------------------------------------------------------------------
        applybutton = creationTaskWindow.findViewById(R.id.applyButton);
        applybutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean create = createNewTask();
                if (create) {
                    taskAdapter.notifyDataSetChanged();
                    //MainActivity.GetDocumentAdapter().notifyDataSetChanged();
                    creationTaskPopUp.dismiss();
                }
            }

        });
        creationTaskPopUp.showAtLocation(view, Gravity.CENTER, 0, 14);

    }

    //GETTERS
    public static ArrayList<Task> getTaskList() {
        return taskArrayList;
    }

}
