package com.garret.chimera.ButtonClicks;

/**
 * Created by Captain on 02/11/2017.
 * <p>
 * <p>
 * Copyright Greenr Republic Software Company - All Rights Reserved.
 */

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

                Context context = v.getContext();

                if (bdo.get_with_sub_screen()){
                    // has sub screen

                    Dialog.newInstance(bdo.get_uuid());


                } else {
                    // no sub screen

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

                            Uri phone = Uri.parse("tel:" + Integer.parseInt(bdo.get_content())); // recipient
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
