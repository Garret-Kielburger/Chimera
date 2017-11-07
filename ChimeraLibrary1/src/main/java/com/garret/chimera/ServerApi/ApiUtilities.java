package com.garret.chimera.ServerApi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.garret.chimera.Constants;
import com.garret.chimera.DataObjects.ButtonDataObject;
import com.garret.chimera.DataObjects.ButtonSubscreenDataObject;
import com.garret.chimera.DataObjects.ConstraintDataObject;
import com.garret.chimera.Database.ChimeraDatabase;
import com.garret.chimera.DataObjects.AppDataDataObject;
import com.garret.chimera.DataObjects.ImageDataObject;
import com.garret.chimera.DataObjects.ScreenDataObject;
import com.garret.chimera.DataObjects.TextfieldDataObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.garret.chimera.Constants.ACCESS_TOKEN;
import static com.garret.chimera.Constants.ACCESS_TOKEN_TIME;
import static com.garret.chimera.Constants.CLIENT_ID;
import static com.garret.chimera.Constants.CLIENT_SECRET;
import static com.garret.chimera.Constants.GRANT_TYPE;
import static com.garret.chimera.Constants.HEADER_TAG_CLIENT_ID;
import static com.garret.chimera.Constants.HEADER_TAG_CLIENT_SECRET;
import static com.garret.chimera.Constants.HEADER_TAG_GRANT_TYPE;
import static com.garret.chimera.Constants.MAX_ATTEMPTS;
import static com.garret.chimera.Constants.PROTECTED_GET_ALL_URL;
import static com.garret.chimera.Constants.SERVER_URL_REGISTER;
import static com.garret.chimera.Constants.TAG2;
import static com.garret.chimera.Constants.TAG;
import static com.garret.chimera.Constants.TOKEN_EXPIRY;
import static com.garret.chimera.Constants.TOKEN_TYPE;
import static com.garret.chimera.Constants.JSON_NODE_SUCCESS;
import static com.garret.chimera.Constants.JSON_NODE_APP_DATA;
import static com.garret.chimera.Constants.JSON_NODE_SCREENS;
import static com.garret.chimera.Constants.JSON_NODE_TEXTS;
import static com.garret.chimera.Constants.JSON_NODE_IMAGES;
import static com.garret.chimera.Constants.JSON_NODE_BUTTONS;
import static com.garret.chimera.Constants.JSON_NODE_BUTTONS_SUB_SCREENS;
import static com.garret.chimera.Constants.JSON_NODE_CONSTRAINTS;
import static com.garret.chimera.Constants.random;

/**
 * Created by Captain on 24/08/2016.
 * <p/>
 * <p/>
 * Copyright Garret Kielburger - All Rights Reserved.
 */
public class ApiUtilities {

    public static void fetchServerData(Context context) {

        boolean isUiThread = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? Looper.getMainLooper().isCurrentThread()
                : Thread.currentThread() == Looper.getMainLooper().getThread();

        Log.i("fetchServer Ui Thread? ", "bool: " + isUiThread);

        // Step 1 - check shared prefs for existing, valid token

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String token = prefs.getString(ACCESS_TOKEN, null);

        if (token == null) {
            getToken(context);
        } else if (tokenIsExpired(prefs)) {
            getToken(context);
        } else{
            try {
                getDataFromServer(PROTECTED_GET_ALL_URL, token, context);
            } catch (IOException e) {
                Log.e("fetchServerData()", "1 - IO exception: " + e);
                //todo: actually handle error
            }
        }

    }

    private static boolean tokenIsExpired(SharedPreferences prefs) {
        float currentTimeInMillis = new Date().getTime();
        float tokenExpiry = prefs.getFloat(TOKEN_EXPIRY, 0);

        float timeDifference = tokenExpiry - currentTimeInMillis;

        if ((timeDifference > 0) && (3600 > timeDifference)) {
            return false;
        } else {
            return true;
        }

    }

    static void getToken(Context context) {
        boolean isUiThread = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? Looper.getMainLooper().isCurrentThread()
                : Thread.currentThread() == Looper.getMainLooper().getThread();
        Log.i("getToken() Ui Thread? ", "boolean: " + isUiThread);
        Map<String, String> map = new HashMap<String, String>();

        map.put(HEADER_TAG_GRANT_TYPE, GRANT_TYPE);
        map.put(HEADER_TAG_CLIENT_ID, CLIENT_ID);
        map.put(HEADER_TAG_CLIENT_SECRET, CLIENT_SECRET);

        String serverURL = SERVER_URL_REGISTER;


/*
        try {
            post(serverURL, map, context);
        } catch (IOException e) {
            //todo: try again with exp backoff
            Log.e("getToken", "2 - IO exception: " + e);
        }

*/

        long backoff = Constants.BACKOFF_MILLI_SECONDS + random.nextInt(1000);

        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG2, "Attempt #" + i + " to send regid to server");
            try {
                post(serverURL, map, context);
                String message = "Request post sent.";
                Log.i(TAG2, message);
                return;
            } catch (IOException e) {
                // Here we are simplifying and retrying on any error;
                // todo: in a real application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(TAG2, " getToken() - 2 - IO exception - attempt: " + i + ":" + e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(TAG2, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(TAG2, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }

        //fetchServerData(context);
    }


    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params   request parameters.
     * @throws IOException propagated from POST.
     */
    private static void post(String endpoint, Map<String, String> params, Context context)
            throws IOException {

        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(Constants.TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();

            if (status != 200) {
                throw new IOException("Post failed with error code " + status);
            } else if (status == 200) {

                InputStream response = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response));
                String line = "";

                StringBuilder responseBuilder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                response.close();

                try {
                    JSONObject result = new JSONObject(responseBuilder.toString());
                    Log.i(Constants.TAG, result.toString());

                    if (result.getString("access_token") != null) {
                        String token = result.getString("access_token");
                        Log.d("access token ====>", token);

                        // todo: save token to shared preferences, add token from shared prefs to get request for other data

                        float tokenExpiryTime = new Date().getTime() + 3600;

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor;

                        editor = prefs.edit();
                        editor.putString(ACCESS_TOKEN, token);
                        editor.putLong(ACCESS_TOKEN_TIME, System.currentTimeMillis());
                        editor.putString(TOKEN_TYPE, result.getString("token_type"));
                        editor.putFloat(TOKEN_EXPIRY, tokenExpiryTime);

                        editor.apply();
                    }
                } catch (JSONException e) {
                    Log.e(Constants.TAG, "JSON Exception: " + e);
                }
            }

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }


    protected static void getDataFromServer(String endpoint, String token, Context context) throws IOException {

        URL url;
        try {
            url = new URL(endpoint + token);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }

        Log.v(Constants.TAG, "GETting from " + url);

        HttpURLConnection conn = null;

        try {
            Log.e("URL", "> " + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            // send the request

            int status = conn.getResponseCode();

            if (status != 200) {
                //          Log.i(TAG, resp);
                throw new IOException("Post failed with error code " + status);
            } else if (status == 200) {

                InputStream response = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response));
                String line = "";

                StringBuilder responseBuilder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                response.close();

                try {
                    JSONObject result = new JSONObject(responseBuilder.toString());
                    Log.i(TAG, result.toString());

                    if (result.getString(JSON_NODE_APP_DATA) != null) {
                        String app_data_string = result.getString(JSON_NODE_APP_DATA);
                        Log.d("JSON data ====>", app_data_string);

                        ChimeraDatabase db = new ChimeraDatabase(context);
                        //Empty database to make room for new updates
                        db.Delete_All();

                        JSONArray json_app_data = result.getJSONArray(JSON_NODE_APP_DATA);

                        for (int i = 0; i < json_app_data.length(); i++) {
                            JSONObject j = json_app_data.getJSONObject(i);
                            AppDataDataObject addo = new AppDataDataObject();
                            addo.setAppName(j.getString("app_name"));
                            Log.i("app name: ", j.getString("app_name"));
                            addo.setNavigationId(Integer.parseInt(j.getString("navigation_id")));
                            Log.i("nav id: ", j.getString("navigation_id"));

                            db.SaveAppData(addo);
                        }

                        boolean isUiThread = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? Looper.getMainLooper().isCurrentThread()
                                : Thread.currentThread() == Looper.getMainLooper().getThread();
                        Log.i("GetDataFrom Ui Thread? ", "boolean: " + isUiThread);

                        //todo: change from "everything" to "screens" on chimera and manticore both

                        JSONArray json_array_screens = result.getJSONArray(JSON_NODE_SCREENS);
                        Log.d("Screens Array: ", json_array_screens.toString() );

                        if (json_array_screens != null) {

                            for (int i = 0; i < json_array_screens.length(); i++) {

                                JSONObject screens_obj = json_array_screens.getJSONObject(i);
                                if (screens_obj != null){
                                    ScreenDataObject sdo = new ScreenDataObject();
                                    sdo.setUuid(screens_obj.getString("uuid"));
                                    Log.i("uuid: ", screens_obj.getString("uuid"));

                                    sdo.setName(screens_obj.getString("screen_name"));
                                    Log.i("screen name: ", screens_obj.getString("screen_name"));

                                    sdo.setOrder(Integer.parseInt(screens_obj.getString("screen_order_number")));
                                    Log.i("screen order number: ", screens_obj.getString("screen_order_number"));

                                    db.SaveScreenData(sdo);

                                    JSONArray text_array = screens_obj.getJSONArray(JSON_NODE_TEXTS);
                                    if (text_array != null) {
                                        for (int j = 0; j < text_array.length() ; j++) {
                                            JSONObject text_obj = text_array.getJSONObject(j);
                                            if (text_obj != null) {
                                                TextfieldDataObject tdo = new TextfieldDataObject();
                                                tdo.setUuid(text_obj.getString("uuid"));
                                                tdo.setScreenUuid(text_obj.getString("screen_uuid"));
                                                tdo.setButtonSubscreenUuid(text_obj.getString("button_sub_screen_uuid"));
                                                //todo: null checks
                                                tdo.setPurpose(text_obj.getString("purpose"));
                                                String va = text_obj.getString("vertical_align");
                                                //int verta = Integer.parseInt(va);
                                                //tdo.setVerticalAlign(verta);

                                                //Log.d("SET VERTALIGN JSON ==>", text_obj.toString() );
                                                //tdo.setVerticalAlign(Integer.parseInt(text_obj.getString("vertical_align")));
                                                //tdo.setHorizontalAlign(Integer.parseInt(text_obj.getString("horizontal_align")));
                                                tdo.setContent(text_obj.getString("content"));

                                                db.SaveTextData(tdo);
                                            }
                                        }
                                    }

                                    JSONArray images_array = screens_obj.getJSONArray(JSON_NODE_IMAGES);
                                    if (images_array != null) {
                                        Log.i("IMG JSON ARRAY ====>  ", images_array.toString());
                                        for (int k = 0; k < images_array.length(); k++) {
                                            JSONObject image_obj = images_array.getJSONObject(k);
                                            if (image_obj != null) {
                                                ImageDataObject ido = new ImageDataObject();
                                                // todo: null checks
                                                ido.setUuid(image_obj.getString("uuid"));
                                                ido.setScreenUuid(image_obj.getString("screen_uuid"));
                                                ido.setButtonSubscreenUuid(image_obj.getString("button_sub_screen_uuid"));
                                                ido.setPurpose(image_obj.getString("purpose"));
                                                //ido.setVerticalAlign(Integer.parseInt(image_obj.getString("vertical_align")));
                                                //ido.setHorizontalAlign(Integer.parseInt(image_obj.getString("horizontal_align")));
                                                ido.setUri(image_obj.getString("uri"));

                                                db.SaveImageData(ido);
                                            }
                                        }
                                    }

                                    JSONArray buttons_array = screens_obj.getJSONArray(JSON_NODE_BUTTONS);
                                    if (buttons_array != null){
                                        Log.i("Button JSON array: ", buttons_array.toString());
                                        for (int l = 0; l < buttons_array.length(); l++) {
                                            JSONObject button_obj = buttons_array.getJSONObject(l);
                                            if (button_obj != null) {
                                                ButtonDataObject bdo = new ButtonDataObject();
                                                //todo: null checks
                                                bdo.set_uuid(button_obj.getString("uuid"));
                                                bdo.set_screen_uuid(button_obj.getString("screen_uuid"));
                                                bdo.set_button_sub_screen_uuid(button_obj.getString("button_sub_screen_uuid"));
                                                boolean yesOrNo = button_obj.getInt("with_sub_screen") > 0;
                                                bdo.set_with_sub_screen(yesOrNo);
                                                bdo.set_sub_screen_uuid(button_obj.getString("sub_screen_uuid"));
                                                bdo.set_label(button_obj.getString("label"));
                                                bdo.set_purpose(button_obj.getString("purpose"));
                                                bdo.set_content(button_obj.getString("content"));
                                                db.SaveButtonData(bdo);
                                            }
                                        }
                                    }

                                    JSONArray buttons_sub_screen_array = screens_obj.getJSONArray(JSON_NODE_BUTTONS_SUB_SCREENS);
                                    if (buttons_sub_screen_array != null){
                                        Log.i("Button JSON array: ", buttons_sub_screen_array.toString());
                                        for (int l = 0; l < buttons_sub_screen_array.length(); l++) {
                                            JSONObject sub_screen_obj = buttons_sub_screen_array.getJSONObject(l);
                                            if (sub_screen_obj != null) {
                                                ButtonSubscreenDataObject bsdo = new ButtonSubscreenDataObject();
                                                //todo: null checks
                                                bsdo.set_uuid(sub_screen_obj.getString("uuid"));
                                                bsdo.set_screen_uuid(sub_screen_obj.getString("screen_uuid"));
                                                bsdo.set_owning_button_uuid(sub_screen_obj.getString("owning_button_uuid"));
                                                bsdo.set_title(sub_screen_obj.getString("title"));
                                                bsdo.set_purpose(sub_screen_obj.getString("purpose"));

                                                db.SaveButtonSubscreenData(bsdo);
                                            }
                                        }
                                    }

                                    Log.d("Constraints JSON", "just started");

                                    JSONArray constraints_array = screens_obj.getJSONArray(JSON_NODE_CONSTRAINTS);
                                    if (constraints_array != null) {
                                        Log.i("Constrait JSON ARRAY>  ", constraints_array.toString());
                                        for (int j = 0; j < constraints_array.length() ; j++) {
                                            JSONObject constraint_obj = constraints_array.getJSONObject(j);
                                            if (constraint_obj != null) {
                                                Log.d("Constraint JSON OBJ ==>", constraint_obj.toString());
                                                ConstraintDataObject cdo = new ConstraintDataObject();

                                                cdo.setScreenUuid(constraint_obj.getString("screen_uuid"));
                                                //todo: null checks
                                                Log.d("Start_id:", constraint_obj.getString("start_id"));
                                                cdo.setStartId(constraint_obj.getString("start_id"));
                                                cdo.setStartSide(constraint_obj.getString("start_side"));
                                                cdo.setEndId(constraint_obj.getString("end_id"));
                                                cdo.setEndSide(constraint_obj.getString("end_side"));
                                                //cdo.setMargin(constraint_obj.getInt("margin"));
                                                String marginString = constraint_obj.getString("margin");
                                                int margin = Integer.parseInt(marginString);
                                                cdo.setMargin(margin);

                                                //Log.d("SET VERTALIGN JSON ==>", constraint_obj.toString() );
                                                //tdo.setVerticalAlign(Integer.parseInt(text_obj.getString("vertical_align")));

                                                db.SaveConstraintData(cdo);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception: " + e.getMessage());
                }
            }

        } finally {

            /**
             * Just made for testing to see thaat the database has been properly updated.
             */

            Log.d(TAG, "so far so good");

            // todo: only do this if the app window is open, otherwise wait
         /*   Intent intent = new Intent(Constants.UPDATE_UI);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
*/
        }
    }

/*    private static void SendUIUpdateBroadcast(Context context) {
        Intent intent = new Intent(Constants.UPDATE_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }*/


}

/**
 *
 *  Other methods of reading input stream:
 *
 *                 for (String line ; (line = reader.readLine()) != null;)
 *                 {
 *                    System.out.println(line);
 *                  }
 *                   reader.close();
 */


