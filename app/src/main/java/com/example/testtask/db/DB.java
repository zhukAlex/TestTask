package com.example.testtask.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Алексей on 05.02.2018.
 */


public class DB extends SQLiteOpenHelper {
    // Tables
    public static final String TABLE_CHANGES = "Changes";
    public static final String CHANGES_ID = "_id";
    public static final String CHANGES_TEXT = "text";
    public static final String CHANGES_IP = "ip";
    public static final String CHANGES_DATE = "date";

    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "DB.db";
    private static final int SCHEMA = 1; // версия базы данных
    private Context myContext;
    private static DB dataBase;
    private Cursor cursor;
    SQLiteDatabase db;

    private DB(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getPath() + DB_NAME;
        create_db();
        db = open();
    }

    public static DB getInstance(Context context) {
        if (dataBase == null) {
            dataBase = new DB(context);
        }
        return dataBase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void dispose() {
        if (cursor != null)
            cursor.close();
        db.close();

    }

    public void create_db(){
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                this.getReadableDatabase();
                //получаем локальную бд как поток
                myInput = myContext.getAssets().open(DB_NAME);
                // Путь к новой бд
                String outFileName = DB_PATH;

                // Открываем пустую бд
                myOutput = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        }
        catch(IOException ex){
            Log.d("DatabaseHelper", ex.getMessage());
        }
    }

    public SQLiteDatabase open()throws SQLException {
        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public Cursor getChanges() {
        cursor = db.query(TABLE_CHANGES, new String[]{CHANGES_IP, CHANGES_DATE, CHANGES_TEXT}, null,
                null, null, null, null);
        if (cursor.getCount() > 0)
            cursor.moveToFirst();
        else
            cursor = null;

        return cursor;
    }

    public boolean addToChanges(String date, String ip, String text) {
        ContentValues cv = new ContentValues();
        cv.put(CHANGES_DATE, date);
        cv.put(CHANGES_IP, ip);
        cv.put(CHANGES_TEXT, text);
        return db.insert(TABLE_CHANGES, null, cv) > -1;
    }
}

