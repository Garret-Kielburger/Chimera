package com.garret.chimera.ViewObjects;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garret.chimera.DataObjects.IDataObject;
import com.garret.chimera.DataObjects.ImageDataObject;
import com.garret.chimera.DataObjects.TextfieldDataObject;
import com.garret.chimera.Database.ChimeraDatabase;
import com.garret.chimera.R;

import java.util.ArrayList;

/**
 * Created by Captain on 14/08/2015.
 * <p/>
 * <p/>
 * Copyright Garret Kielburger - All Rights Reserved.
 */
public class Screen extends LinearLayout {

    private static final String TAG = "Screen View Object: ";
    LinearLayout layout;
    ArrayList<ImageDataObject> imageList = new ArrayList<ImageDataObject>();
    ArrayList<TextfieldDataObject> textfieldList = new ArrayList<TextfieldDataObject>();
    ArrayList<ImageDataObject> imagesFromDb;
    ArrayList<TextfieldDataObject> textfieldsFromDb;

    ArrayList<IDataObject> interfaceDataObjectList = new ArrayList<IDataObject>();
    ArrayList<IDataObject> interfaceDataObjectListFromDb;
    ArrayList<IDataObject> unOrderedArrayList;


    ChimeraDatabase db;
    Image img;
    TextView textfield;

    String textOrImage;


    public Screen(Context context, int screen_number) {
        super(context);

        Log.d(TAG, "Constructor, before inflation");
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li;
        li = (LayoutInflater) context.getSystemService(infService);
        li.inflate(R.layout.view_object_screen, this);

        layout = (LinearLayout) findViewById(R.id.screenLayout);
        layout.removeAllViews();
        imageList.clear();
        textfieldList.clear();
        db = new ChimeraDatabase(context);

        // TODO: Use this method to prepare for possible future need of Adapter for List/GridView
        // TODO: Also, clear the lists to ensure up to date images/data.


        // TODO: create fragment for each screen, and insert imgs/txts into it
        Log.d("screen_number: Screen: ", String.valueOf(screen_number));
        //   Log.d("getallImages(1): ", db.Get_All_Images_By_Screen(1).toString());


        // Return list of all screen elements and screen data object (last element)
        interfaceDataObjectListFromDb = db.Get_Screen_Content_By_Screen(screen_number);

     //   ScreenDataObject this_screen = new ScreenDataObject();

     //   IDataObject screen_from_db;
     //   screen_from_db = interfaceDataObjectListFromDb.get(interfaceDataObjectListFromDb.size());

       // this_screen = (ScreenDataObject) screen_from_db;

        for (int i = 0; i < interfaceDataObjectListFromDb.size(); i++) {

            /**
             * If performance issues occur, can revert to using broader Interface to use only interfaceDataObjectListFromDb without class casting
             */

            if (interfaceDataObjectListFromDb.get(i).getClass() == TextfieldDataObject.class) {
                TextfieldDataObject tdo = (TextfieldDataObject) interfaceDataObjectListFromDb.get(i);
                textOrImage = "text";
                textfield = new TextView(context);
                textfield.setText(tdo.getContent());
                LayoutParams params = SetHorizontalParameters(i, tdo, null);
                textfield.setLayoutParams(params);
                layout.addView(textfield);


            } else if (interfaceDataObjectListFromDb.get(i).getClass() == ImageDataObject.class) {
                ImageDataObject ido = (ImageDataObject) interfaceDataObjectListFromDb.get(i);
                textOrImage = "image";
                LayoutParams params = SetHorizontalParameters(i, null, ido);
                img = new Image(context, ido.getUri());
                img.imageHolder.setGravity(params.gravity);
                layout.addView(img);

            } else {
                // Not a text and not an image - what do?
                Log.i(TAG, "Not an image or a text!");
                textOrImage = "whoops";
            }

        }
        db.close();
    }

    public LayoutParams SetHorizontalParameters(int i, TextfieldDataObject tdo, ImageDataObject ido) {
        LayoutParams params;
        //image
        if (textOrImage.equalsIgnoreCase("image")) {
            params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }
        //text
        else if (textOrImage.equalsIgnoreCase("text")) {
            params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        } else {
            Log.e(TAG, "This should never happen!");
            params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        }

        // Set Justification Gravity

        if (tdo != null){
            switch (tdo.getHorizontalAlign()) {
                case 0:
                case 1:
                case 2:
                case 3:
                    params.gravity = Gravity.LEFT;
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                    params.gravity = Gravity.RIGHT;
                    break;

                default:
                    params.gravity = Gravity.NO_GRAVITY;
                    break;
            }
        } else if (ido != null){
            switch (ido.getHorizontalAlign()) {
                case 0:
                case 1:
                case 2:
                case 3:
                    params.gravity = Gravity.LEFT;
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                    params.gravity = Gravity.RIGHT;
                    break;

                default:
                    params.gravity = Gravity.NO_GRAVITY;
                    break;
            }
        } else{
            // Should never happen
            Log.e(TAG, "ido and tdo are null? no horizontal params.");
        }
        return params;
    }


}