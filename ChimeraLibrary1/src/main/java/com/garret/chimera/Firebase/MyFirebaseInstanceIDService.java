package com.garret.chimera.Firebase;

import android.provider.Settings;
import android.util.Log;
import android.provider.Settings;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Captain on 03/09/2016.
 * <p/>
 * <p/>
 * Copyright Garret Kielburger - All Rights Reserved.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project

        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.i(TAG, "SENDING REGISTRATION TO BACK END: deviceID: " + deviceId + " AND regID: " + token);
        //todo: if this fails, need to do it gain elsewhere in the app - ie splash screen startup? Sharedprefs boolean isRegistered?
        RegisterUtilities.register(this, deviceId, token);


    }

}
