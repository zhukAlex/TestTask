package com.example.testtask.table;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.testtask.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Алексей on 04.02.2018.
 */

public class PropertyArrayAdapter extends ArrayAdapter<Property> {

    private Context context;
    private List<Property> rentalProperties;
    private int id;
    private int i = 0;

    //constructor, call on creation
    public PropertyArrayAdapter(Context context, int resource, ArrayList<Property> objects, int id) {
        super(context, resource, objects);
        this.id = id;
        this.context = context;
        this.rentalProperties = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Property property = rentalProperties.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(id, null);
        TextView textViewFio = (TextView) view.findViewById(R.id.textViewFIO);
        TextView textViewNumber = (TextView) view.findViewById(R.id.textViewNumber);

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

        textViewFio.setBackground(ContextCompat.getDrawable(context,idDrawable));
        textViewNumber.setBackground(ContextCompat.getDrawable(context, idDrawable));
        textViewFio.setTextColor(color);
        textViewNumber.setTextColor(color);

        textViewFio.setText(property.getFio() == null ? "" : property.getFio());
        textViewNumber.setText(property.getNumber() == null ? "" : property.getNumber());

        return view;
    }
}