package com.example.testtask.table.history;

/**
 * Created by Алексей on 06.02.2018.
 */

public class PropertyHistory {

    //property basics
    private String number;
    private String fio;

    //constructor
    public PropertyHistory(String fio, String number) {
        this.fio = fio;
        this.number = number;
    }

    //getters
    public String getFio() { return fio; }
    public String getNumber() { return number; }
}
