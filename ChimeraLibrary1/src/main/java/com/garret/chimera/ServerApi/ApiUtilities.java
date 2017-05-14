package com.garret.chimera.ServerApi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.garret.chimera.Constants;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.garret.chimera.Constants.ACCESS_TOKEN;
import static com.garret.chimera.Constants.CLIENT_ID;
import static com.garret.chimera.Constants.CLIENT_SECRET;
import static com.garret.chimera.Constants.EXPIRY;
import static com.garret.chimera.Constants.GRANT_TYPE;
import static com.garret.chimera.Constants.HEADER_TAG_CLIENT_ID;
import static com.garret.chimera.Constants.HEADER_TAG_CLIENT_SECRET;
import static com.garret.chimera.Constants.HEADER_TAG_GRANT_TYPE;
import static com.garret.chimera.Constants.MAX_ATTEMPTS;
import static com.garret.chimera.Constants.PROTECTED_GET_ALL_URL;
import static com.garret.chimera.Constants.SERVER_URL_REGISTER;
import static com.garret.chimera.Constants.TAG2;
import static com.garret.chimera.Constants.TAG;
import static com.garret.chimera.Constants.TOKEN_TYPE;
import static com.garret.chimera.Constants.JSON_NODE_SUCCESS;
import static com.garret.chimera.Constants.JSON_NODE_APP_DATA;
import static com.garret.chimera.Constants.JSON_NODE_SCREENS;
import static com.garret.chimera.Constants.JSON_NODE_TEXTS;
import static com.garret.chimera.Constants.JSON_NODE_IMAGES;
import static com.garret.chimera.Constants.random;

/**
 * Created by Captain on 24/08/2016.
 * <p/>
 * <p/>
 * Copyright Garret Kielburger - All Rights Reserved.
 */
public class ApiUtilities {

    //todo: replace possibility of infinite loop between fetchServerData() and getToken()
    public static void fetchServerData(Context context) {


        // Step 1 - check shared prefs for existing, valid token

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String token = prefs.getString(ACCESS_TOKEN, null);

        if (token != null) {

            // step 2 - todo: Check that token is not expired - if expired, get new token. Currently only looking for not null
            // Implement in future - via timestamp insert in shared prefs to compare against

            // step 3 -  use token to send request to server for data
            try {
                getDataFromServer(PROTECTED_GET_ALL_URL, token, context);
            } catch (IOException e) {
                Log.e("fetchServerData()", "1 - IO exception: " + e);
                //todo: just refetch token if exception 401; will need to avoid infinite loop possibility
                getToken(context);
            }

        } else {
            getToken(context);
        }

    }

    static void getToken(Context context) {

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

        fetchServerData(context);
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

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor;

                        editor = prefs.edit();
                        editor.putString(ACCESS_TOKEN, token);
                        editor.putString(TOKEN_TYPE, result.getString("token_type"));
                        editor.putInt(EXPIRY, result.getInt("expires_in"));

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


    private static void getDataFromServer(String endpoint, String token, Context context) throws IOException {

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

                        //todo: change from "everything" to "screens" on chimera and manticore both

                        JSONArray json_array_screens = result.getJSONArray(JSON_NODE_SCREENS);

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
                                                tdo.setScreenUuid(text_obj.getString("screen_uuid"));
                                                //todo: null checks
                                                tdo.setPurpose(text_obj.getString("purpose"));
                                                String va = text_obj.getString("vertical_align");
                                                int verta = Integer.parseInt(va);
                                                tdo.setVerticalAlign(verta);

                                                Log.d("SET VERTALIGN JSON ==>", text_obj.toString() );
                                                //tdo.setVerticalAlign(Integer.parseInt(text_obj.getString("vertical_align")));
                                                tdo.setHorizontalAlign(Integer.parseInt(text_obj.getString("horizontal_align")));
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
                                                ido.setScreenUuid(image_obj.getString("screen_uuid"));
                                                ido.setPurpose(image_obj.getString("purpose"));
                                                ido.setVerticalAlign(Integer.parseInt(image_obj.getString("vertical_align")));
                                                ido.setHorizontalAlign(Integer.parseInt(image_obj.getString("horizontal_align")));
                                                ido.setUri(image_obj.getString("uri"));

                                                db.SaveImageData(ido);
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

            //SendUIUpdateBroadcast(context);
            // todo: only do this if the window is open, otherwise wait
            Intent intent = new Intent(Constants.UPDATE_UI);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        }
    }

    private static void SendUIUpdateBroadcast(Context context) {
        Intent intent = new Intent(Constants.UPDATE_UI);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }


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


