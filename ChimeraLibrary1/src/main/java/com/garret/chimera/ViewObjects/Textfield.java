package com.garret.chimera.ViewObjects;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.garret.chimera.R;


/**
 * Created by Captain on 14/08/2015.
 *
 * //TODO: IS this class needed? It's just a TextView
 * Even Needed this class?
 *
 * Copyright Garret Kielburger - All Rights Reserved.
 */
public class Textfield extends FrameLayout {

    private static final String TAG = "Textfield View Object: ";
    TextView textField;


    public Textfield(Context context) {
        super(context);

        Log.d(TAG, "Constructor, before inflation");

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li;
        li = (LayoutInflater) context.getSystemService(infService);
        li.inflate(R.layout.view_object_textfield, this);

        textField = (TextView) findViewById(R.id.textField);



        Log.d(TAG, "Constructor, after inflation");

    }
}
