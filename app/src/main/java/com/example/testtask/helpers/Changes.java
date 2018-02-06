package com.example.testtask.helpers;

import android.database.Cursor;

import com.example.testtask.db.DB;

import java.util.ArrayList;

/**
 * Created by Алексей on 05.02.2018.
 */

public class Changes {
    private ArrayList<Change> changes;

    public Changes() {
        changes = new ArrayList<>();
    }

    public Change getItemAt(int i) {
        return changes.get(i);
    }

    public int getCount() {
        return changes.size();
    }

    public void readFromDB() {
        Cursor cursor = DB.getInstance(null).getChanges();
        String date, ip, text;
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                date = cursor.getString(cursor.getColumnIndex(DB.CHANGES_DATE));
                ip = cursor.getString(cursor.getColumnIndex(DB.CHANGES_IP));
                text = cursor.getString(cursor.getColumnIndex(DB.CHANGES_TEXT));
                changes.add(new Change(date, ip, text));
                cursor.moveToNext();
            }
        }
    }
}
