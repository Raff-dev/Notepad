package com.example.notatnik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME= "Notepad_Training_Documents.db";
    public static final String TABLE_DOCUMENTS = "documments";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DOCUMENTNAME= "documentname";
    public static final String COLUMN_DOCUMENTTYPE= "documenttype";


    public MyDBHandler(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_DOCUMENTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DOCUMENTNAME + " TEXT, " +
                COLUMN_DOCUMENTTYPE + " TEXT " + " );" ;
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCUMENTS);
        onCreate(db);
    }

    public void addDocument(Document document){
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCUMENTNAME, document.getName());
        values.put(COLUMN_DOCUMENTTYPE, document.getType());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_DOCUMENTS,null,values);
        db.close();
    }
    public void deleteDocument(String documentName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_DOCUMENTS + " WHERE " + COLUMN_DOCUMENTNAME + "=\"" + documentName + "\";" );
    }
    public String databaseToString(){
        String dbString= "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DOCUMENTS + " WHERE 1";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast()){
            if(cursor.getString(cursor.getColumnIndex("documentname"))!=null){
                dbString+= cursor.getString(cursor.getColumnIndex("documentname"));
                dbString += ", ";
                dbString += cursor.getString(cursor.getColumnIndex("documenttype"));
                dbString += "\n";
            }
            db.close();
        }
        return dbString;
    }
}
