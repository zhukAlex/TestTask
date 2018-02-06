package com.example.testtask.table.history;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.testtask.R;
import com.example.testtask.helpers.Change;
import com.example.testtask.table.contact.PropertyContacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алексей on 06.02.2018.
 */

public class PropertyAdapterHistory extends ArrayAdapter<Change> {

    private Context context;
    private List<Change> rentalProperties;
    private int id;
    private int i = 0;

    //constructor, call on creation
    public PropertyAdapterHistory(Context context, int resource, ArrayList<Change> objects, int id) {
        super(context, resource, objects);
        this.id = id;
        this.context = context;
        this.rentalProperties = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Change property = rentalProperties.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(id, null);

        TextView textViewActivity = (TextView) view.findViewById(R.id.textViewActivity);
        TextView textViewTime = (TextView) view.findViewById(R.id.textViewTime);
        TextView textViewIP = (TextView) view.findViewById(R.id.textViewIP);

        i ++;
        int idDrawable;
        int color;
        if (i % 2 != 0) {
            idDrawable = R.drawable.white;
            color = Color.BLACK;
        }
        else {
            idDrawable = R.drawable.back;
            color = Color.WHITE;
        }

        textViewActivity.setBackground(ContextCompat.getDrawable(context,idDrawable));
        textViewIP.setBackground(ContextCompat.getDrawable(context, idDrawable));
        textViewTime.setBackground(ContextCompat.getDrawable(context, idDrawable));

        textViewActivity.setTextColor(color);
        textViewTime.setTextColor(color);
        textViewIP.setTextColor(color);

        textViewTime.setText(property.getDate() == null ? "" : property.getDate());
        textViewActivity.setText(property.getText() == null ? "" : property.getText());
        textViewIP.setText(property.getIp() == null ? "" : property.getIp());

        return view;
    }
}
