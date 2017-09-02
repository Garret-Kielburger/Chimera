package com.garret.chimera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.garret.chimera.Database.ChimeraDatabase;
import com.garret.chimera.ServerApi.ApiService;

import static com.garret.chimera.Constants.ACCESS_TOKEN_TIME;
import static com.garret.chimera.Constants.RUN_ONCE;
import static com.garret.chimera.Constants.UPDATE_UI;

public class SplashActivity extends AppCompatActivity {
    // todo: set this as the main and launcher in the manifest

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor prefsEditor;

    private boolean runOnce, isOpen;

    private long token_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
            Original broadcast manager implementation that works - but might be limited scope until it's in the application subclass

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mSyncReceiver, new IntentFilter(UPDATE_UI));*/

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.registerReceiver(mSyncReceiver, new IntentFilter(UPDATE_UI));

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //isOpen = sharedPreferences.getBoolean(IS_OPEN, true);
        runOnce = sharedPreferences.getBoolean(RUN_ONCE, false);

        context = getApplicationContext();

//todo: only get update from first run - after that, only when told to sync? or once per month?
        //todo: check for internet connection before trying this and catch if none

        if (TokenIsFresh()) {
            //Get server data
            ApiService.startFetchServerDataForForeground(this);
            ApiService.BroadcastUI(this);
        } else {
            //get Token, then get server data
            ApiService.startFetchTokenForForeground(this);
            ApiService.startFetchServerDataForForeground(this);
            ApiService.BroadcastUI(this);

        }

        //updateDatabaseFromApi();
    }


    private boolean TokenIsFresh() {
        token_time = sharedPreferences.getLong(ACCESS_TOKEN_TIME, 0);
        long now = System.currentTimeMillis();

        // check for 55 minutes, so that it doesn't expire in the middle of transaction
        if ((token_time == 0) || ((now - token_time) > 3300000)) {
            return false;
        } else {
            return true;
        }
    }

    private BroadcastReceiver mSyncReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //String action = intent.getAction();
            if (intent != null){
                Log.d("BroadcastReceive intent", intent.toString());
            }

            BuildApp();

        }
    };

    private void BuildApp(){
        int nav_id = 0;
        ChimeraDatabase db = new ChimeraDatabase(this);

        nav_id = db.Get_Navigation_Type();

        switch (nav_id) {
            case (1):
                //tabs
                // actually bottom navigation
                Intent i = new Intent(context, BottomNavigationActivity.class);
                startActivity(i);

                finish();

                break;
            case (2):
                //swipe
                // actually tabs
                Intent i2 = new Intent(context, TabNavigationActivity.class );
                startActivity(i2);

                finish();
                break;
            case (3):
                //drawer
                Intent i3 = new Intent(context, NavigationDrawerActivity.class);
                startActivity(i3);

                finish();
                break;
            default:

                Log.i("Nav picked: ", "Default case selected line 57");
                break;
        }
    }



}
