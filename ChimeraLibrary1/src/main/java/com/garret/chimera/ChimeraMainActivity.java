package com.garret.chimera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.garret.chimera.Database.ChimeraDatabase;
import com.garret.chimera.ServerApi.ApiUtilities;
import com.garret.chimera.ViewObjects.Screen;

import static com.garret.chimera.Constants.UPDATE_UI;

public class ChimeraMainActivity extends AppCompatActivity {

    private static final String TAG = "ChimeraMainActivity";

    // Connection detector
    ConnectionDetector cd;
    Context context;

    String regid;

    // Declarations for populating UI
    Screen screen;
    LinearLayout layout;
    ChimeraDatabase db;
    int number_of_screens;

    //todo: Limit api checks to when it is called - on initial update and then only on sync calls or long periods of inactivity - NOT after every onDestroy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chimera_activity_main);

        // todo: check for Firebase regId - if blank, try again to reconnect until success

        db = new ChimeraDatabase(this);
        context = getApplicationContext();

        cd = new ConnectionDetector(this);

        // Check for internet cconnection

        if ((!cd.isConnectingToInternet())) {
            // no internet! Abort!
            return;
        } else {
            FirebaseConnect();
        }
        //todo: only update from API on sync or after a period of inactivity away
        // todo: implement first run check
        updateDatabaseFromApi();
        //PopulateUI();

        // todo: Perform check to see if app is registered with Firebase:
        //      - if no, register (AsyncTask)
        //          - and in onPostExecute(), update db and UI
        //      - if yes, update db and update UI

/*        LocalBroadcastManager.getInstance(this).registerReceiver(
                mSyncReceiver, new IntentFilter(UPDATE_UI));*/
    }

    private BroadcastReceiver mSyncReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //String action = intent.getAction();
            if (intent != null){
                Log.d("BroadcastReceive intent", intent.toString());
            }

            PopulateUI();
        }
    };

    private void PopulateUI() {
        Log.i(TAG, "PopulateUI() Constructor");
        SetupScreen();
        layout.removeAllViews();

        number_of_screens = db.Get_Number_Of_Screens();

        Log.d("number_of_screens = ", String.valueOf(number_of_screens));

        // index in db starts at 1, not 0, so use i
        for (int i = 0; i < number_of_screens + 1; i++) {
            Log.d("PopulateUI()", "number of i: " + i);
            if (i == 0){
                //No Screens to Add
            }
            if (i > 0){
                screen = new Screen(this, i);
                layout.addView(screen);
            }
        }
    }

    /**
     *
     *
     *
     *
     *
     *
     *
     *
     *      Update Database Code begins
     *
     *
     *
     *
     *
     *
     *
     *
     */

        //  AsyncTask for updating db via API

    private void updateDatabaseFromApi() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                ApiUtilities.fetchServerData(context);
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                PopulateUI();
            }
        }.execute(null, null, null);
    }

    /**
     *
     *
     *
     *
     *
     *
     *
     *
     *      Update Database Code ends
     *
     *
     *
     *
     *
     *
     *
     *
     */


    /**
     *
     *
     *
     *
     *
     *
     *
     *
     *      Push Notification Code begins
     *
     *
     *
     *
     *
     *
     *
     *
     */

    private void FirebaseConnect() {
    }

    /**
     *
     *
     *
     *
     *
     *
     *
     *
     *      Push Notification Code Ends
     *
     *
     *
     *
     *
     *
     *
     *
     */


    private void SetupScreen() {
        layout = (LinearLayout) findViewById(R.id.scrollContainer);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.unregisterReceiver(mSyncReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mSyncReceiver, new IntentFilter(UPDATE_UI));
    }
}