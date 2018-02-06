package com.example.testtask.table.contact;

import android.graphics.drawable.Drawable;

/**
 * Created by Алексей on 04.02.2018.
 */

public class PropertyContacts {

    //property basics
    private String number;
    private String fio;

    //constructor
    public PropertyContacts(String fio, String number) {
        this.fio = fio;
        this.number = number;
    }

    //getters
    public String getFio() { return fio; }
    public String getNumber() { return number; }
}

