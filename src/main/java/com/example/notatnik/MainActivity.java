package com.example.notatnik;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


/*  TO DO
-swipe to delete
-edit name


*/

public class MainActivity extends AppCompatActivity implements DocumentAdapter.OnDocumentListener {

    private static ArrayList<Document> documentList;
    private RecyclerView documentRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView.Adapter documentAdapter;


    private static Context context;
    public MyDBHandler dbHandler;

    private static View creationWindowView;
    private Button createNewDocumentButton, deleteAllButton, createButton, closeButton;
    private RadioButton cRadio, nRadio, orangeRadio;
    private RadioGroup radioGroupType, radioGroupColor;
    private EditText newDocumentEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SETTING UP
        //-------------------------------------------------------------------------
        createNewDocumentButton = findViewById(R.id.createNewDocumentButton);
        deleteAllButton = findViewById(R.id.deleteAllButton);
        Button refreshButton = findViewById(R.id.refreshButton);
        documentRecyclerView = findViewById(R.id.DocumentRecyclerView);
        //-------------------------------------------------------------------------
        MainActivity.context = getApplicationContext();
        dbHandler = new MyDBHandler(this, null, null, 1);
        documentList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        documentAdapter = new DocumentAdapter(documentList, this);
        documentRecyclerView.setLayoutManager(layoutManager);
        documentRecyclerView.setAdapter(documentAdapter);

        //CODE
        //-------------------------------------------------------------------------
        displayList();
        touchHelper();

        //LISTENERS
        //-------------------------------------------------------------------------
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshList();
                Toast.makeText(getApplicationContext(), "reefereeeshing", Toast.LENGTH_SHORT).show();
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

    private void touchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
                    case ItemTouchHelper.RIGHT:
                        final Document document = documentList.get(viewHolder.getAdapterPosition());
                        deleteDocument(viewHolder.getAdapterPosition());
                        Snackbar.make(documentRecyclerView, String.valueOf(document.getName()) + " has been deleted", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                documentList.add(document);
                                dbHandler.addDocument(document);
                                refreshList();
                            }
                        }).show();
                        break;
                    case ItemTouchHelper.LEFT:
                        openWindowEditDocument(viewHolder.getAdapterPosition());
                        documentAdapter.notifyDataSetChanged();
                        break;

                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                dX /= 2;
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.newRed))
                        .addSwipeRightActionIcon(R.drawable.ic_delete)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.newBlue))
                        .addSwipeLeftActionIcon(R.drawable.ic_edit)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(documentRecyclerView);
    }

    @Override
    public void OnDocumentClick(int position) {

        switch (documentList.get(position).getType()) {
            case "CheckList":
                Intent i = new Intent(getApplicationContext(), TaskActivity.class);
                i.putExtra("POSITION", position);
                startActivity(i);
                break;
            case "Note":
                Intent a = new Intent(getApplicationContext(), NoteActivity.class);
                a.putExtra("POSITION", position);
                startActivity(a);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Cannot identify document's type", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAllDocuments() {
        dbHandler.deleteAll();

        int i = documentList.size();
        while (i != 0) {
            documentList.remove(0);
            i--;
        }

        documentAdapter.notifyDataSetChanged();


    }

    private void displayList() {
        MyDBHandler dbHandler = new MyDBHandler(getAppContext(), null, null, 1);
        Cursor data = dbHandler.getDocuments();
        if (data.moveToFirst()) {
            while (!data.isAfterLast()) {
                int docId = data.getInt(data.getColumnIndex("idd"));
                String name = data.getString(data.getColumnIndex("documentname"));
                String type = data.getString(data.getColumnIndex("documenttype"));
                String color = data.getString(data.getColumnIndex("documentcolor"));
                Document document = new Document(docId, name, type, color);
                GetDocumentList().add(document);
                data.moveToNext();
            }
            documentAdapter.notifyDataSetChanged();
            data.close();
        }

    }

    private void refreshList() {
        int i = documentList.size();
        while (i != 0) {
            documentList.remove(0);
            i--;
        }
        displayList();
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
        //-------------------------------------------------------------------------
        newDocumentEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        newDocumentEditText.requestFocus();
        //-------------------------------------------------------------------------
        closeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                creationPopUp.dismiss();
            }
        });
        createButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createNewDocument()) {
                    creationPopUp.dismiss();
                }
            }
        });
        //-------------------------------------------------------------------------


        creationPopUp.showAtLocation(view, Gravity.CENTER, 0, -200);

    }

    private void openWindowEditDocument(final int position) {
        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View editWindow = inflater.inflate(R.layout.window_edit_document, null);
        final PopupWindow editPopUp = new PopupWindow(
                editWindow,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                160,
                true);
        //-------------------------------------------------------------------------
        EditText newName = editWindow.findViewById(R.id.editDocumentName);
        newName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
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
        newName.requestFocus();
        //-------------------------------------------------------------------------
        final Button applyButton = editWindow.findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText newName = editWindow.findViewById(R.id.editDocumentName);
                String name = newName.getText().toString();
                if (!name.isEmpty()) {
                    int id = GetDocumentList().get(position).getId();
                    dbHandler.updateDocumentName(id, name);
                    refreshList();
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    editPopUp.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Name can not be empty", Toast.LENGTH_SHORT);
                }
            }


        });
        editPopUp.showAtLocation(editWindow, Gravity.CENTER, 0, 14);


    }

    private boolean createNewDocument() {
        dbHandler = new MyDBHandler(this, null, null, 1);
        if (!newDocumentEditText.getText().toString().isEmpty()) {
            Document document = new Document(
                    dbHandler.getSavedID(),
                    newDocumentEditText.getText().toString(),
                    getDocumentType(),
                    getDocumentColor());

            if (dbHandler.addDocument(document)) {
                Toast.makeText(getApplicationContext(), "Added Succesfully", Toast.LENGTH_SHORT).show();
                if (document.getType() == "Note") {
                    Note note = new Note(document.getId(), " ");
                    dbHandler.addNote(note);
                }
                documentList.add(document);
                dbHandler.incrementSavedID();
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

    private void deleteDocument(final int position) {
        switch (documentList.get(position).getType()) {
            case "Note":
                dbHandler.deleteNoteDocument(documentList.get(position).getId());

                break;
            case "CheckList":
                dbHandler.deleteTaskDocument(documentList.get(position).getId());
                break;
        }
        documentList.remove(position);
        documentAdapter.notifyDataSetChanged();

    }

    //GETTERS
    public static ArrayList<Document> GetDocumentList() {
        return documentList;
    }

    public static RecyclerView.Adapter GetDocumentAdapter() {
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
                type = "Note";
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
                color = "Orange";
                break;
            case R.id.greenRadio:
                color = "Green";
                break;
            case R.id.BlueRadio:
                color = "Blue";
                break;
            case R.id.redRadio:
                color = "Red";
                break;
            default:
                color = "chuj, nie działa";
        }
        return (color);

    }

    public static int getBackgroundColor(Document document) {
        switch (document.getColor()) {
            case "Orange":
                return R.drawable.color_dot_orange;
            case "Green":
                return R.drawable.color_dot_green;
            case "Blue":
                return R.drawable.color_dot_blue;
            case "Red":
                return R.drawable.color_dot_red;
            default:
                return R.drawable.color_dot_orange;
        }
    }


}