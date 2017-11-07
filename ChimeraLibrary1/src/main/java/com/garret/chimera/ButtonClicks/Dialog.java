package com.garret.chimera.ButtonClicks;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.garret.chimera.DataObjects.ButtonDataObject;
import com.garret.chimera.DataObjects.ButtonSubscreenDataObject;
import com.garret.chimera.DataObjects.ConstraintDataObject;
import com.garret.chimera.DataObjects.IDataObject;
import com.garret.chimera.DataObjects.ImageDataObject;
import com.garret.chimera.DataObjects.TextfieldDataObject;
import com.garret.chimera.Database.ChimeraDatabase;
import com.garret.chimera.R;
import com.garret.chimera.ViewObjects.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Captain on 02/11/2017.
 * <p>
 * <p>
 * Copyright Greenr Republic Software Company - All Rights Reserved.
 */


public class Dialog extends DialogFragment {

    String button_data_object_uuid;
    ButtonSubscreenDataObject bsdo;
    ChimeraDatabase db;

    ArrayList<IDataObject> subscreenInterfaceDataObjectListFromDb;
    Map<String, View> viewObjectMap = new HashMap<>();
    ConstraintLayout constraintLayout;
    Context context;

    static public Dialog newInstance(String button_data_object_uuid) {
        Log.i("Dialog instance", "Newly made");
        Dialog dialog = new Dialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("button_data_object_uuid", button_data_object_uuid);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ChimeraDatabase(getActivity());
        button_data_object_uuid = getArguments().getString("button_data_object_uuid");
        bsdo = db.GetButtonSubscreenByUuid(button_data_object_uuid);

        subscreenInterfaceDataObjectListFromDb = db.getSubScreenDataByUuid(bsdo.get_uuid());

        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(bsdo.get_title());

        View v = inflater.inflate(R.layout.dialog, container, false);
        constraintLayout = (ConstraintLayout) v.findViewById(R.id.dialog_layout);
        context = getActivity();

        for (int i = 0; i < subscreenInterfaceDataObjectListFromDb.size(); i++) {

            if (subscreenInterfaceDataObjectListFromDb.get(i).getClass() == TextfieldDataObject.class) {
                TextfieldDataObject tdo = (TextfieldDataObject) subscreenInterfaceDataObjectListFromDb.get(i);
                TextView textView = new TextView(context);
                textView.setId(View.generateViewId());
                textView.setText(tdo.getContent());
                viewObjectMap.put(tdo.getUuid(), textView);
                constraintLayout.addView(textView);

            } else if (subscreenInterfaceDataObjectListFromDb.get(i).getClass() == ImageDataObject.class) {
                ImageDataObject ido = (ImageDataObject) subscreenInterfaceDataObjectListFromDb.get(i);
                Image imgView = new Image(context, ido.getUri());
                imgView.setId(View.generateViewId());
                viewObjectMap.put(ido.getUuid(), imgView);
                constraintLayout.addView(imgView);

            } else if (subscreenInterfaceDataObjectListFromDb.get(i).getClass() == ButtonDataObject.class){
                ButtonDataObject bdo = (ButtonDataObject) subscreenInterfaceDataObjectListFromDb.get(i);
                Button button = new Button(context);
                button.setId(View.generateViewId());
                button.setText(bdo.get_label());

                button.setOnClickListener(ButtonOnClicks.getOnClickListener(bdo));

                viewObjectMap.put(bdo.get_uuid(), button);
                constraintLayout.addView(button);

            } else {
                Log.i("Making ViewObj Map", "Not an image or a text or a button!");
            }
        }

        GetconstraintsParameters(constraintLayout);
        
        return v;

    }

    private void GetconstraintsParameters(ConstraintLayout layout) {
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
            Log.i("KEY: ", key.toString());
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

                Log.i("constraint object endID", constraintDataObject.getEndId());
                set.connect(viewObjectMap.get(key).getId(),
                        startSide,
                        viewObjectMap.get(constraintDataObject.getEndId()).getId(),
                        endSide,
                        constraintDataObject.getMargin());
            }

        }

        set.applyTo(layout);
    }


}
