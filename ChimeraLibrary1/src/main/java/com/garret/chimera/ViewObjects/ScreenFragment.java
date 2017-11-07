package com.garret.chimera.ViewObjects;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garret.chimera.ButtonClicks.ButtonOnClicks;
import com.garret.chimera.DataObjects.ButtonDataObject;
import com.garret.chimera.DataObjects.ConstraintDataObject;
import com.garret.chimera.DataObjects.IDataObject;
import com.garret.chimera.DataObjects.ImageDataObject;
import com.garret.chimera.DataObjects.TextfieldDataObject;
import com.garret.chimera.Database.ChimeraDatabase;
import com.garret.chimera.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Captain on 22/05/2017.
 * <p>
 * <p>
 * Copyright Greenr Republic Software Company - All Rights Reserved.
 */

public class ScreenFragment extends Fragment {

    ConstraintLayout constraintLayout;
    String uuid;
    String screen_name;
    Context context;
    String textOrImageOrButton;
    ArrayList<IDataObject> interfaceDataObjectListFromDb;
    Map<String, View> viewObjectMap = new HashMap<>();

    ChimeraDatabase db;
    Image img;
    TextView textView;
    Button button;

    public ScreenFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ChimeraDatabase(getActivity());
        uuid = getArguments().getString("uuid");
        screen_name = getArguments().getString("screen_name");

        interfaceDataObjectListFromDb = db.getScreenDataByUuid(uuid);

        Log.d("Frag OnCreate UUID: ", uuid);
        Log.d("interfaceDataobj: ", interfaceDataObjectListFromDb.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_object_screen_fragment, container, false);
        //layout = (LinearLayout) v.findViewById(R.id.screenLayout);
        constraintLayout = (ConstraintLayout) v.findViewById(R.id.screenLayout);
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
                textOrImageOrButton = "text";
                textView = new TextView(context);
                textView.setId(View.generateViewId());
                textView.setText(tdo.getContent());
                viewObjectMap.put(tdo.getUuid(), textView);
                //LinearLayout.LayoutParams params = SetHorizontalParameters(i, tdo, null);
                //textView.setLayoutParams(params);
                constraintLayout.addView(textView);

            } else if (interfaceDataObjectListFromDb.get(i).getClass() == ImageDataObject.class) {
                ImageDataObject ido = (ImageDataObject) interfaceDataObjectListFromDb.get(i);
                textOrImageOrButton = "image";
                //LinearLayout.LayoutParams params = SetHorizontalParameters(i, null, ido);
                img = new Image(context, ido.getUri());
                img.setId(View.generateViewId());
                viewObjectMap.put(ido.getUuid(), img);
                //img.imageHolder.setGravity(params.gravity);
                constraintLayout.addView(img);

            } else if (interfaceDataObjectListFromDb.get(i).getClass() == ButtonDataObject.class){
                ButtonDataObject bdo = (ButtonDataObject) interfaceDataObjectListFromDb.get(i);
                textOrImageOrButton = "button";
                button = new Button(context);
                button.setId(View.generateViewId());
                button.setText(bdo.get_label());

                button.setOnClickListener(ButtonOnClicks.getOnClickListener(bdo));

                viewObjectMap.put(bdo.get_uuid(), button);
                constraintLayout.addView(button);

            }
            else {
                // Not a text and not an image - what do?
                Log.i("Making ViewObj Map", "Not an image or a text or a button!");
                Log.d("interfaceObj:", interfaceDataObjectListFromDb.get(i).toString());
                textOrImageOrButton = "whoops";
            }

        }

        Log.i("The Map: ", viewObjectMap.toString());
        Log.i("The Map at problem: ", "Number of members: " + viewObjectMap.size());

        //Log.i("The Layout: ", constraintLayout.toString());

        GetConstraintsParameters(constraintLayout);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void GetConstraintsParameters(ConstraintLayout layout) {
        Log.i("GetConstraintParameters", "Started");
        ConstraintSet set = new ConstraintSet();

        ArrayList<ConstraintDataObject> cdo = new ArrayList<ConstraintDataObject>();
        ConstraintDataObject constraintDataObject = new ConstraintDataObject();

        int startSide = 0;
        int endSide = 0;

        set.clone(layout);
/*
* Idea - go through the view objects and apply constraints
* */
        for (String key: viewObjectMap.keySet()) {
          //  Log.i("KEY: ", key.toString());
            //todo: add null checks!
            cdo = db.GetConstraintsByViewObjectUuid(key);
            for (int i = 0; i < cdo.size(); i++) {
                constraintDataObject = cdo.get(i);

                switch (constraintDataObject.getStartSide()) {

                    case "BOTTOM":
                        startSide = ConstraintSet.BOTTOM;
                        break;

                    case "TOP":
                        startSide = ConstraintSet.TOP;
                        break;

                    case "START":
                        startSide = ConstraintSet.START;
                        break;

                    case "END":
                        startSide = ConstraintSet.END;
                        break;
                }

                switch (constraintDataObject.getEndSide()) {
                    case "BOTTOM":
                        endSide = ConstraintSet.BOTTOM;
                        break;

                    case "TOP":
                        endSide = ConstraintSet.TOP;
                        break;

                    case "START":
                        endSide = ConstraintSet.START;
                        break;

                    case "END":
                        endSide = ConstraintSet.END;
                        break;
                }

                //Log.i("constraint object endID", constraintDataObject.getEndId());
                try {
                    set.connect(viewObjectMap.get(key).getId(),
                            startSide,
                            viewObjectMap.get(constraintDataObject.getEndId()).getId(),
                            endSide,
                            constraintDataObject.getMargin());
                } catch (Exception e) {
                    Log.e("constraint error", "Error: " + e);
                }

            }

        }

        set.applyTo(layout);
    }


    public LinearLayout.LayoutParams SetHorizontalParameters(int i, TextfieldDataObject tdo, ImageDataObject ido) {
        LinearLayout.LayoutParams params;
        //image
        if (textOrImageOrButton.equalsIgnoreCase("image")) {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }
        //text
        else if (textOrImageOrButton.equalsIgnoreCase("text")) {
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        } else if (textOrImageOrButton.equalsIgnoreCase("button")){
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        else {
            Log.e(TAG, "This should never happen!");
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        }

        // Set Justification Gravity

        if (tdo != null) {
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
        } else if (ido != null) {
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
        } else {
            // Should never happen
            Log.e(TAG, "ido and tdo are null? no horizontal params.");
        }
        return params;
    }
}
