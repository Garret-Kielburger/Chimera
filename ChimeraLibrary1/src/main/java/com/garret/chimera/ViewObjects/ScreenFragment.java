package com.garret.chimera.ViewObjects;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garret.chimera.DataObjects.IDataObject;
import com.garret.chimera.DataObjects.ImageDataObject;
import com.garret.chimera.DataObjects.TextfieldDataObject;
import com.garret.chimera.Database.ChimeraDatabase;
import com.garret.chimera.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Captain on 22/05/2017.
 * <p>
 * <p>
 * Copyright Greenr Republic Software Company - All Rights Reserved.
 */

public class ScreenFragment extends Fragment {

    LinearLayout layout;
    String uuid;
    String screen_name;
    Context context;
    String textOrImage;
    ArrayList<IDataObject> interfaceDataObjectListFromDb;

    ChimeraDatabase db;
    Image img;
    TextView textfield;

    public ScreenFragment(){
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ChimeraDatabase(getActivity());
        uuid = getArguments().getString("uuid");
        screen_name = getArguments().getString("screen_name");

        interfaceDataObjectListFromDb = db.getScreenDataByUuid(uuid);

        Log.d("Frag OnCreate UUID: ", uuid);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_object_screen_fragment, container, false);
        layout = (LinearLayout) v.findViewById(R.id.screenLayout);
        context = getActivity();

/*        TextView tv = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(params);
        tv.setText(uuid);
        layout.addView(tv);

        TextView tv2 = new TextView(context);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params1.gravity = Gravity.CENTER;
        tv2.setLayoutParams(params1);
        tv2.setText(screen_name);
        layout.addView(tv2);*/

        for (int i = 0; i < interfaceDataObjectListFromDb.size(); i++) {

            /**
             * If performance issues occur, can revert to using broader Interface to use only interfaceDataObjectListFromDb without class casting
             */

            if (interfaceDataObjectListFromDb.get(i).getClass() == TextfieldDataObject.class) {
                TextfieldDataObject tdo = (TextfieldDataObject) interfaceDataObjectListFromDb.get(i);
                textOrImage = "text";
                textfield = new TextView(context);
                textfield.setText(tdo.getContent());
                LinearLayout.LayoutParams params = SetHorizontalParameters(i, tdo, null);
                textfield.setLayoutParams(params);
                layout.addView(textfield);


            } else if (interfaceDataObjectListFromDb.get(i).getClass() == ImageDataObject.class) {
                ImageDataObject ido = (ImageDataObject) interfaceDataObjectListFromDb.get(i);
                textOrImage = "image";
                LinearLayout.LayoutParams params = SetHorizontalParameters(i, null, ido);
                img = new Image(context, ido.getUri());
                img.imageHolder.setGravity(params.gravity);
                layout.addView(img);

            } else {
                // Not a text and not an image - what do?
                Log.i(TAG, "Not an image or a text!");
                textOrImage = "whoops";
            }

        }




        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }


    public LinearLayout.LayoutParams SetHorizontalParameters(int i, TextfieldDataObject tdo, ImageDataObject ido) {
        LinearLayout.LayoutParams params;
        //image
        if (textOrImage.equalsIgnoreCase("image")) {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        //text
        else if (textOrImage.equalsIgnoreCase("text")) {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        } else {
            Log.e(TAG, "This should never happen!");
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
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
