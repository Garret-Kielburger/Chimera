package com.garret.chimera.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.garret.chimera.ChimeraMainActivity;
import com.garret.chimera.R;
import com.garret.chimera.ServerApi.ApiService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static com.garret.chimera.Constants.ACCESS_TOKEN_TIME;

public class FirebasePushMessenger extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";
    private long token_time;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefsEditor;

    public FirebasePushMessenger() {
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        Log.d(TAG, "Collapse Key: " + remoteMessage.getCollapseKey());
        Log.d(TAG, "Notification: " + remoteMessage.getNotification());


        // Check if message contains a data payload.
        if (!remoteMessage.getData().isEmpty()) {
            Map<String, String> data = remoteMessage.getData();

            Log.d(TAG, "Unpack Data: " + data.get("sync"));

            //Sync the database

            if (data.get("sync").equals("sync")){
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                if (TokenIsFresh()) {
                    //Get server data
                    ApiService.startFetchServerDataForForeground(this);
                    ApiService.BroadcastUI(this);

                } else {
                    //get Token, then get server data
                    //todo: optimize intentservice class to call these three sequentially instead of requiring differnt instances
                    ApiService.startFetchTokenForForeground(this);
                    ApiService.startFetchServerDataForForeground(this);
                    ApiService.BroadcastUI(this);
                }

            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            // if data has notification field
            sendNotification(remoteMessage.getNotification());
        }
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

    private void sendNotification(RemoteMessage.Notification message) {
        // Create notification

        Intent intent = new Intent(this, ChimeraMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //todo: Make and if then statement to check size of incoming message and use BigTextStyle if over a certain length

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message.getTitle())
                .setContentText(message.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                /*.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(message.getBody()))*/
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}
