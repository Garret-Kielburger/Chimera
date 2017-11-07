package com.garret.chimera.ButtonClicks;

/**
 * Created by Captain on 02/11/2017.
 * <p>
 * <p>
 * Copyright Greenr Republic Software Company - All Rights Reserved.
 */

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import com.garret.chimera.DataObjects.ButtonDataObject;

import java.util.List;


public class ButtonOnClicks {


    public static View.OnClickListener getOnClickListener(final ButtonDataObject bdo)  {
        return new View.OnClickListener() {
            public void onClick(View v) {

                Log.d("getOnCLickListener()", "Started");
                Context context = v.getContext();
                Log.d("bdo uuid:", bdo.get_uuid().toString());
                Log.d("bdo screen uuid:", bdo.get_screen_uuid().toString());
                Log.d("bdo button sub screen:", bdo.get_button_sub_screen_uuid().toString());
                Log.d("bdo boolean:", Boolean.toString(bdo.get_with_sub_screen()));
                Log.d("bdo label:", bdo.get_label().toString());
                Log.d("bdo purpose:", bdo.get_purpose().toString());
                Log.d("bdo content:", bdo.get_content().toString());


                if (bdo.get_with_sub_screen()){
                    // has sub screen

                   /* FragmentTransaction fm = context.getFragmentManager().beginTransaction();
                    Fragment prev = context.getFragmentManager().findFragmentByTag("Contact Fragment");

                    Dialog dialog = Dialog.newInstance(bdo.get_uuid());
                    dialog.show(fm, "");
*/

                    Activity activity = (Activity) v.getContext();
                    FragmentManager manager = activity.getFragmentManager();

                    FragmentTransaction fm = manager.beginTransaction();

                    Dialog newDialog = Dialog.newInstance(bdo.get_uuid());
                    newDialog.show(fm, "dialog");

                } else {
                    // no sub screen

                    Log.d("getOnCLickListener()", "Switch Case");
                    Log.d("bdo getpurpose:", bdo.get_purpose().toString());
                    switch (bdo.get_purpose()) {

                        case "email":

                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("message/rfc822");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {bdo.get_content()}); // recipient
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getApplicationInfo().name);

                            PackageManager packageManager = context.getPackageManager();
                            List activities = packageManager.queryIntentActivities(emailIntent, packageManager.MATCH_DEFAULT_ONLY);
                            boolean isIntentSafe = activities.size() > 0;

                            if (isIntentSafe) {
                                context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
                            }
                            break;

                        case "phone":
                            //Integer i = Integer.parseInt(bdo.get_content());
                            Uri phone = Uri.parse("tel:" + bdo.get_content());

                           // Uri phone = Uri.parse("tel:" + Integer.parseInt(bdo.get_content())); // recipient
                            Intent phoneIntent = new Intent(Intent.ACTION_DIAL, phone);

                            PackageManager packageManager1 = context.getPackageManager();
                            List activities1 = packageManager1.queryIntentActivities(phoneIntent, PackageManager.MATCH_DEFAULT_ONLY);
                            boolean isIntentSafe1 = activities1.size() > 0;

                            if (isIntentSafe1) {
                                context.startActivity(Intent.createChooser(phoneIntent, "Make Phone Call"));
                            }

                            break;

                        default:
                            break;
                    }


                }




            }
        };
    }

}
