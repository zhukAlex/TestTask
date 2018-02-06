package com.example.testtask.helpers;

import android.database.Cursor;

import com.example.testtask.db.DB;

/**
 * Created by Алексей on 05.02.2018.
 */

public class Change {
    private String date;
    private String ip;
    private String text;

    public Change(String date, String ip, String text) {
        this.date = date;
        this.ip = ip;
        this.text = text;
    }

    public Change() {

    }

    public void writeToDB() {
        DB.getInstance(null).addToChanges(date, ip, text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
