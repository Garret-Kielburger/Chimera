package com.garret.chimera.ViewObjects;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.garret.chimera.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Captain on 14/08/2015.
 * <p/>
 * <p/>
 * Copyright Garret Kielburger - All Rights Reserved.
 */

/**
 * Clas no longer needed? Just like Textfield class?
 */

public class Image extends LinearLayout {

    private static final String TAG = "Image View Object: ";
    ImageView imageView;
    LinearLayout imageHolder;

   // private AQuery aq;

    public Image(Context context, String uri) {
        super(context);
     //   Log.d(TAG, "Constructor, before inflation. uri: " + uri);

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li;
        li = (LayoutInflater) context.getSystemService(infService);
        li.inflate(R.layout.view_object_image, this);

        imageHolder = (LinearLayout) findViewById(R.id.imageHolder);
        imageView = (ImageView) findViewById(R.id.imageView);


/*        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.RIGHT;*/


/*
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params1.gravity = Gravity.RIGHT;
*/
   //     imageView.setLayoutParams(params);
      //  imageHolder.setLayoutParams(params);


       // imageHolder.setGravity(Gravity.CENTER);


        getImage(uri);
    }

    private void getImage(String uri) {
     //   Log.d(TAG, "getImage(), before logic - uri: " + uri);

        Picasso.with(getContext()).load(uri).into(imageView);

        // aq = new AQuery(getContext());
       // aq.id(imageView).progress(pb).image(uri, true, true, 500, 0);
    }
}
