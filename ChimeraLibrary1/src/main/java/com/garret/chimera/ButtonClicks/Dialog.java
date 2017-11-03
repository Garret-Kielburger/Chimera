package com.garret.chimera.ButtonClicks;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.garret.chimera.R;

import java.util.List;

/**
 * Created by Captain on 02/11/2017.
 * <p>
 * <p>
 * Copyright Greenr Republic Software Company - All Rights Reserved.
 */

public class Dialog extends DialogFragment {

    static public Dialog newInstance() {
        Dialog dialog = new Dialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putString("number", number);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        email = getArguments().getString("email");
        number = getArguments().getString("number");
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Contact Gardener");
        View v = inflater.inflate(R.layout.contact_dialog, container, false);

        Button emailButton = (Button)v.findViewById(R.id.email_button);
        emailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("contact dialog:", "Clicking the Email button");
                // When button is clicked, call up to owning activity.
                //((FragmentDialog)getActivity()).showDialog();
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email}); // recipients
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sandy Hill Community Garden");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi!");

                PackageManager packageManager = getActivity().getPackageManager();
                List activities = packageManager.queryIntentActivities(emailIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;

                if (isIntentSafe) {
                    startActivity(Intent.createChooser(emailIntent, "Send Email"));
                }
            }
        });

        Button phoneButton = (Button)v.findViewById(R.id.phone_button);
        phoneButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("contact dialog:", "Clicking the Phone button");
                // When button is clicked, call up to owning activity.
                //((FragmentDialog)getActivity()).showDialog();
                Uri phone = Uri.parse(number);
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, phone);
                Log.d("Phone Button: number = ", number);
                Log.d("Phone Button: phone = ", phone.toString());

                PackageManager packageManager = getActivity().getPackageManager();
                List activities = packageManager.queryIntentActivities(phoneIntent,
                        PackageManager.MATCH_DEFAULT_ONLY);
                boolean isIntentSafe = activities.size() > 0;

                if (isIntentSafe) {
                    startActivity(Intent.createChooser(phoneIntent, "Make Phone Call"));
                }
            }
        });

        return v;

    }
}
