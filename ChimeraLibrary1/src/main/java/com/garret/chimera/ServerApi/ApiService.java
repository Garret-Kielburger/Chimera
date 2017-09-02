package com.garret.chimera.ServerApi;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.garret.chimera.Constants;

import java.io.IOException;

import static com.garret.chimera.Constants.ACCESS_TOKEN;
import static com.garret.chimera.Constants.PROTECTED_GET_ALL_URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ApiService extends IntentService {
    // Tasks/ Actions that this IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String FETCH_TOKEN_FOR_FOREGROUND = "com.garret.chimera.ServerApi.TokenFetchForeground";
    private static final String FETCH_TOKEN_FOR_BACKGROUND = "com.garret.chimera.ServerApi.TokenFetchBackground";
    private static final String FETCH_SERVER_DATA_FOR_FOREGROUND = "com.garret.chimera.ServerApi.ServerFetchForeground";
    private static final String FETCH_SERVER_DATA_FOR_BACKGROUND = "com.garret.chimera.ServerApi.ServerFetchBackground";
    private static final String UPDATE_UI_FROM_SERVICE = "com.garret.chimera.ServerApi.UpdateUIFromService";

    // parameters
    private static final String EXTRA_PARAM1 = "com.garret.chimera.ServerApi.extra.PARAM1";

    public ApiService() {
        super("ApiService");
        setIntentRedelivery(true);
    }

    /**
     * Starts this service to update token when started from splash activity.
     *
     */
    public static void startFetchTokenForForeground(Context context) {
        Intent intent = new Intent(context, ApiService.class);
        intent.setAction(FETCH_TOKEN_FOR_FOREGROUND);
        //intent.putExtra(EXTRA_PARAM1, param1);
        context.startService(intent);
    }

    /**
     * Starts this service to update token when started from firebase sync message.
     *
     */
    public static void startFetchTokenForBackground(Context context) {
        Intent intent = new Intent(context, ApiService.class);
        intent.setAction(FETCH_TOKEN_FOR_BACKGROUND);
        //intent.putExtra(EXTRA_PARAM1, param1);
        context.startService(intent);
    }

    public static void startFetchServerDataForForeground(Context context) {
        Intent intent = new Intent(context, ApiService.class);
        intent.setAction(FETCH_SERVER_DATA_FOR_FOREGROUND);
        context.startService(intent);
    }

    public static void startFetchServerDataForBackground(Context context) {
        Intent intent = new Intent(context, ApiService.class);
        intent.setAction(FETCH_SERVER_DATA_FOR_BACKGROUND);
        context.startService(intent);
    }

    public static void BroadcastUI (Context context){
        Intent intent = new Intent(context, ApiService.class);
        intent.setAction(UPDATE_UI_FROM_SERVICE);
        context.startService(intent);
    }

// todo: need to handle null content from server  to avoid FormatExceptions

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (FETCH_TOKEN_FOR_FOREGROUND.equals(action)) {
                //final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                handleTokenFetchForeground();
            }
            if (FETCH_TOKEN_FOR_BACKGROUND.equals(action)) {
                //final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                handleTokenFetchBackground();
            }
            if (FETCH_SERVER_DATA_FOR_FOREGROUND.equals(action)) {
                handleServerFetchForeground();
            }
            if (FETCH_SERVER_DATA_FOR_BACKGROUND.equals(action)) {
                handleServerFetchBackground();
            }
            if (UPDATE_UI_FROM_SERVICE.equals(action)) {
                handleUpdateUIFromService();
            }

        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleTokenFetchForeground() {
        ApiUtilities.getToken(this);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleTokenFetchBackground() {
        ApiUtilities.getToken(this);
    }

    private void handleServerFetchForeground() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String token = prefs.getString(ACCESS_TOKEN, null);

        try {
            ApiUtilities.getDataFromServer(PROTECTED_GET_ALL_URL, token, this);
        } catch (IOException e) {
            Log.e("fetchServerData()", "1 - IO exception: " + e);
            //todo: just refetch token if exception 401; will need to avoid infinite loop possibility
        }
    }

    private void handleServerFetchBackground() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String token = prefs.getString(ACCESS_TOKEN, null);
        try {
            ApiUtilities.getDataFromServer(PROTECTED_GET_ALL_URL, token, this);
        } catch (IOException e) {
            Log.e("fetchServerData()", "1 - IO exception: " + e);
            //todo: just refetch token if exception 401; will need to avoid infinite loop possibility

        }
    }

    public  void handleUpdateUIFromService() {
        Intent intent = new Intent(Constants.UPDATE_UI);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
