package com.example.notatnik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;


public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Notepad_Training_Documents.db";
    //---------------------------------------------------------------------------------------------
    public static final String TABLE_SAVED_ID = "tablesavedid";
    public static final String SAVED_ID = "savedid";
    //---------------------------------------------------------------------------------------------
    public static final String TABLE_DOCUMENTS = "documments";
    public static final String ID_D = "idd";
    public static final String COLUMN_DOCUMENTNAME = "documentname";
    public static final String COLUMN_DOCUMENTTYPE = "documenttype";
    public static final String COLUMN_DOCUMENTCOLOR = "documentcolor";
    //---------------------------------------------------------------------------------------------
    public static final String TABLE_TASKS = "tasks";
    public static final String ID_DT = "iddt";
    public static final String ID_T = "idt";
    public static final String COLUMN_TASKNAME = "taskname";
    public static final String COLUMN_TASKCHECKED = "checked";
    //---------------------------------------------------------------------------------------------
    public static final String TABLE_NOTES = "notes";
    public static final String ID_DN = "iddn";
    public static final String COLUMN_NOTETEXT = "notetext";
    //---------------------------------------------------------------------------------------------


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //------------------------------------------------------
        String query1 = "CREATE TABLE " + TABLE_SAVED_ID + "(" +
                SAVED_ID + " INTEGER PRIMARY KEY" + " );";
        db.execSQL(query1);
        ContentValues values = new ContentValues();
        values.put(SAVED_ID, 1);
        db.insert(TABLE_SAVED_ID, null, values);
        //------------------------------------------------------
        String query2 = "CREATE TABLE " + TABLE_DOCUMENTS + "(" +
                ID_D + " INTEGER PRIMARY KEY, " +
                COLUMN_DOCUMENTNAME + " TEXT, " +
                COLUMN_DOCUMENTTYPE + " TEXT, " +
                COLUMN_DOCUMENTCOLOR + " TEXT " + " );";
        db.execSQL(query2);
        //------------------------------------------------------
        String query3 = "CREATE TABLE " + TABLE_TASKS + "(" +
                ID_DT + " INTEGER, " +
                ID_T + " INTEGER, " +
                COLUMN_TASKNAME + " TEXT, " +
                COLUMN_TASKCHECKED + " INT, "
                + " PRIMARY KEY (" + ID_DT + ", " + ID_T + "));";
        db.execSQL(query3);
        //------------------------------------------------------
        String query4 = "CREATE TABLE " + TABLE_NOTES + "(" +
                ID_DN + " INTEGER PRIMARY KEY, " +
                COLUMN_NOTETEXT + " TEXT " + " );";
        db.execSQL(query4);
        //------------------------------------------------------
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVED_ID);
        onCreate(db);
    }

    public int getSavedID() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_SAVED_ID, null);
        data.moveToFirst();
        int id = data.getInt(data.getColumnIndex("savedid"));
        return id;
    }

    public void incrementSavedID() {
        int id = this.getSavedID();
        id++;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_SAVED_ID + " SET " + SAVED_ID + "=" + id);
    }
    //DOCUMENT

    public boolean addDocument(Document document) {
        ContentValues values = new ContentValues();
        values.put(ID_D, document.getId());
        values.put(COLUMN_DOCUMENTNAME, document.getName());
        values.put(COLUMN_DOCUMENTTYPE, document.getType());
        values.put(COLUMN_DOCUMENTCOLOR, document.getColor());
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_DOCUMENTS, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Document getDocument(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_DOCUMENTS +
                " WHERE " + ID_D + "=" + id + ";", null);
        data.moveToFirst();
        String name = data.getString(data.getColumnIndex(COLUMN_DOCUMENTNAME));
        String type = data.getString(data.getColumnIndex(COLUMN_DOCUMENTTYPE));
        String color = data.getString(data.getColumnIndex(COLUMN_DOCUMENTCOLOR));

        Document document=new Document(id,name,type,color);
        return document;
    }

    public Cursor getDocuments() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_DOCUMENTS, null);
        return data;
    }

    public void updateDocumentName(int docId, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_DOCUMENTS +
                " SET " + COLUMN_DOCUMENTNAME + "='" + newName +
                "' WHERE " + ID_D + "=" + docId + ";");
    }

    public void deleteNoteDocument(int docId) {
        SQLiteDatabase db = getWritableDatabase();
        String query1 = "DELETE FROM " + TABLE_DOCUMENTS + " WHERE " + ID_D + " = " + docId + ";";
        String query2 = "DELETE FROM " + TABLE_NOTES + " WHERE " + ID_DN + " = " + docId + ";";

        db.execSQL(query1);
        db.execSQL(query2);
    }

    public void deleteTaskDocument(int docId) {
        SQLiteDatabase db = getWritableDatabase();
        String query1 = "DELETE FROM " + TABLE_DOCUMENTS + " WHERE " + ID_D + " = " + docId + ";";
        String query2 = "DELETE FROM " + TABLE_TASKS + " WHERE " + ID_DT + " = " + docId + ";";

        db.execSQL(query1);
        db.execSQL(query2);
    }
    //TASK

    public void deleteDocumentTasks(int docId) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TASKS + " WHERE " + ID_DT + " = " + docId + ";";
        db.execSQL(query);
    }

    public boolean addTask(Task task) {
        int isChecked = task.isChecked() ? 1 : 0;
        ContentValues values = new ContentValues();
        values.put(ID_DT, task.getDocumentId());
        values.put(ID_T, task.getId());
        values.put(COLUMN_TASKNAME, task.getTaskName());
        values.put(COLUMN_TASKCHECKED, isChecked);
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_TASKS, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getTasks(int docId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(
                "SELECT * FROM " + TABLE_TASKS + " WHERE " +
                        ID_DT + "=" + docId, null);
        return data;
    }

    public boolean getTaskCheckValue(int iddt, int idt) {
        SQLiteDatabase db = getWritableDatabase();
        boolean isChecked;
        int isCheckedInt;
        Cursor data = db.rawQuery("SELECT " + COLUMN_TASKCHECKED + " FROM " + TABLE_TASKS + " WHERE " +
                ID_DT + "=" + iddt + " AND " + ID_T + "=" + idt + ";", null);
        data.moveToFirst();
        isCheckedInt = data.getInt(data.getColumnIndex("checked"));
        isChecked = (isCheckedInt == 1);
        return isChecked;
    }

    public void updateTaskChecked(int iddt, int idt, boolean isChecked) {
        int isCheckedInt = isChecked ? 1 : 0;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_TASKS +
                " SET " + COLUMN_TASKCHECKED + "='" + isCheckedInt +
                "' WHERE " + ID_DT + "=" + iddt + " AND " + ID_T + "=" + idt + ";");


    }

    public void updateTaskName(int iddt, int idt, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_TASKS +
                " SET " + COLUMN_TASKNAME + "='" + newName +
                "' WHERE " + ID_DT + "=" + iddt + " AND " + ID_T + "=" + idt + ";");
    }

    public void deleteTask(int iddt, int idt) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TASKS +
                " WHERE " + ID_T + " = " + idt +
                " AND " + ID_DT + " = " + iddt + ";";
        db.execSQL(query);
    }

    public int getTaskCount(int iddt) {
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(
                "SELECT * FROM " + TABLE_TASKS + " WHERE " +
                        ID_DT + "=" + iddt, null);
        if (data.moveToFirst()) {
            while (!data.isAfterLast()) {
                count++;
                data.moveToNext();
            }
        }
        return count;
    }

    //NOTE
    public boolean addNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(ID_DN, note.getDocumentId());
        values.put(COLUMN_NOTETEXT, note.getText());
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_NOTES, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Note getNote(int docId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(
                "SELECT * FROM " + TABLE_NOTES + " WHERE " +
                        ID_DN + "=" + docId, null);
        data.moveToFirst();
        String text = data.getString(data.getColumnIndex("notetext"));
        Note note = new Note(docId,text);
        return note;

    }

    public void updateNote(Note note) {
        String iddn = String.valueOf(note.getDocumentId());
        String text = note.getText();
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NOTES +
                " SET " + COLUMN_NOTETEXT + "=" + "'" + text + "'" +
                " WHERE " + ID_DN + " = " + iddn);
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DOCUMENTS + " WHERE 1");
        db.execSQL("DELETE FROM " + TABLE_TASKS + " WHERE 1");
        db.execSQL("DELETE FROM " + TABLE_NOTES + " WHERE 1");
        db.execSQL("DELETE FROM " + TABLE_SAVED_ID + " WHERE 1");
        ContentValues values = new ContentValues();
        values.put(SAVED_ID, 1);
        db.insert(TABLE_SAVED_ID, null, values);

        db.close();
    }

}
