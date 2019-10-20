package com.example.notatnik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Notepad_Training_Documents.db";
    //---------------------------------------------------------------------------------------------
    public static final String TABLE_SAVED_ID="tablesavedid";
    public static final String SAVED_ID="savedid";
    //---------------------------------------------------------------------------------------------
    public static final String TABLE_DOCUMENTS = "documments";
    public static final String ID_D = "idd";
    public static final String COLUMN_DOCUMENTNAME = "documentname";
    public static final String COLUMN_DOCUMENTTYPE = "documenttype";
    //---------------------------------------------------------------------------------------------
    public static final String TABLE_TASKS = "tasks";
    //                         ID_D
    public static final String ID_T = "idt";
    public static final String COLUMN_TASKNAME = "taskname";
    public static final String COLUMN_TASKCHECKED = "checked";
    //---------------------------------------------------------------------------------------------
    public static final String TABLE_NOTES = "notes";
    //                         ID_D
    public static final String COLUMN_NOTETEXT = "notetext";
    //---------------------------------------------------------------------------------------------


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //------------------------------------------------------
        String query1 = "CREATE TABLE " + TABLE_SAVED_ID + "(" +
                SAVED_ID + " INTEGER PRIMARY KEY" +" );";
        db.execSQL(query1);
        ContentValues values = new ContentValues();
        values.put(SAVED_ID,1);
        db.insert(TABLE_SAVED_ID,null,values);
        //------------------------------------------------------
        String query2 = "CREATE TABLE " + TABLE_DOCUMENTS + "(" +
                ID_D + " INTEGER PRIMARY KEY, " +
                COLUMN_DOCUMENTNAME + " TEXT, " +
                COLUMN_DOCUMENTTYPE + " TEXT " + " );";
        db.execSQL(query2);
        //------------------------------------------------------
        String query3 = "CREATE TABLE " + TABLE_TASKS + "(" +
                ID_D + " INTEGER, " +
                ID_T + " INTEGER PRIMARY KEY, " +
                COLUMN_TASKNAME + " TEXT, " +
                COLUMN_TASKCHECKED + " INT " + " );";
        db.execSQL(query3);
        //------------------------------------------------------
        String query4 = "CREATE TABLE " + TABLE_NOTES + "(" +
                ID_D + " INTEGER PRIMARY KEY, " +
                COLUMN_NOTETEXT + " TEXT " + " );";
        db.execSQL(query4);
        //------------------------------------------------------
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }
    public int getSavedID(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_SAVED_ID,null);
        data.moveToFirst();
        int id = data.getInt(data.getColumnIndex("savedid"));
        incrementSavedID();
        return id;
    }
    public void incrementSavedID(){
        int id = this.getSavedID();
        id++;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_SAVED_ID+ " SET " + SAVED_ID + "=" + id);
    }
    public boolean addDocument(Document document) {
        ContentValues values = new ContentValues();
        values.put(ID_D, document.getId());
        values.put(COLUMN_DOCUMENTNAME, document.getName());
        values.put(COLUMN_DOCUMENTTYPE, document.getType());
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_DOCUMENTS, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getDocuments() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_DOCUMENTS, null);
        return data;
    }
    public void deleteDocument(int docId) {
        SQLiteDatabase db = getWritableDatabase();
        String query1 = "DELETE FROM " + TABLE_DOCUMENTS + " WHERE " + ID_D + " = " + docId + ";";
        String query2 = "DELETE FROM " + TABLE_TASKS + " WHERE " + ID_D + " = " + docId + ";";
        String query3 = "DELETE FROM " + TABLE_NOTES + " WHERE " + ID_D + " = " + docId + ";";

        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
    }
    public void deleteDocumentTaskS(int docId) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TASKS + " WHERE " + ID_D + " = " + docId + ";";
        db.execSQL(query);
    }

    public boolean addTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(ID_D, task.getDocumentId());
        values.put(ID_T, task.getId());
        values.put(COLUMN_TASKNAME, task.getTaskText());
        values.put(COLUMN_TASKCHECKED, Boolean.valueOf(task.isChecked()));
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_TASKS, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(
                "SELECT * FROM " + TABLE_DOCUMENTS + " d " +
                        " JOIN " + TABLE_TASKS + " t " +
                        " ON d." + ID_D + "= t." + ID_D, null);
        return data;
    }
    public void deleteTask(int idt) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_TASKS + " WHERE " + ID_T + " = " + idt + ";";
        db.execSQL(query);
    }

    public boolean addNote(Note note) {
        ContentValues values = new ContentValues();
        values.put(ID_D, note.getDocumentId());
        values.put(COLUMN_TASKNAME, note.getText());
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_NOTES, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getNote(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery(
                "SELECT * FROM " + TABLE_DOCUMENTS + " d " +
                        " JOIN " + TABLE_NOTES + " n " +
                        " ON d." + ID_D + "= n." + ID_D, null);
        return data;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DOCUMENTS + " WHERE 1");
        db.execSQL("DELETE FROM " + TABLE_TASKS + " WHERE 1");
        db.execSQL("DELETE FROM " + TABLE_NOTES + " WHERE 1");
        db.close();
    }

    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DOCUMENTS + " WHERE 1";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex("documentname")) != null) {
                dbString += cursor.getString(cursor.getColumnIndex("documentname"));
                dbString += ", ";
            }
            if (cursor.getString(cursor.getColumnIndex("documenttype")) != null) {
                dbString += cursor.getString(cursor.getColumnIndex("documenttype"));
                dbString += "\n";
            }
            db.close();
        }
        return dbString;
    }




}
