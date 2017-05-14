package com.garret.chimera;

import java.util.Random;

/**
 * Created by Captain on 24/08/2016.
 * <p/>
 * <p/>
 * Copyright Garret Kielburger - All Rights Reserved.
 */
public interface Constants {

    /**
     * Tag name for logging.
     */

    public static final String TAG = "Internet Test App";
    public static final String TAG2 = "Registering API: ";

    /**
     * Stuff to change for each app
     * todo: For each app:
     *       APP_UUID
     *       CLIENT_SECRET   // OAUTH API
     *       CLIENT_ID       // OAUTH API
     *       PROJECT_ID      // Push Notification stuff
     *
     *       ?? PROJECT_NUMBER
     *
     */

    // static final String APP_UUID = "aa70153d-f201-40f7-89ac-67593aeedc90";  // manticoretest.tk
    static final String APP_UUID = "11a6b9b5-c215-49c0-96cf-0a750e30297a";

 /**
     *   Constants Used for the API connections and calls
     */

    String WEBSITE = "http://greenrrepublic.com/";

    //todo: break into individual strings to concatenate app uuid separately
    static final String API_SERVER_URL = WEBSITE + APP_UUID + "/mobile";

    static final String SERVER_URL_REGISTER = WEBSITE +"api/" + APP_UUID + "/gain_permission";

    static final String SERVER_URL_PROTECTED = WEBSITE +"api/" + APP_UUID + "/mobile_oauth";


    static final String HEADER_TAG_GRANT_TYPE = "grant_type";

    static final String HEADER_TAG_CLIENT_ID = "client_id";

    static final String HEADER_TAG_CLIENT_SECRET = "client_secret";

    static final String GRANT_TYPE = "client_credentials";

    // todo: Change these for every single application using Chimera library

    static final String CLIENT_SECRET = "27165cd9-5cb9-47c8-bf1d-f9b71644bc8a";

    static final String CLIENT_ID = "20d3de22-631f-11e6-acc6-08002777c33d";

    /**
     * Server fetch all images URL here
     */

    static final String PROTECTED_GET_ALL_URL = WEBSITE +"api/" + APP_UUID + "/mobile_oauth?access_token=";

    static final String OPEN_GET_ALL_URL = WEBSITE +"api/" + APP_UUID + "/mobile";

    static final String ACCESS_TOKEN = "access_token";

    static final String TOKEN_TYPE = "token_type";

    static final String EXPIRY = "expires_in";

    /**
     *
     *
     *
     *      Constants used by the Push Notification provider
     *
     *
     *
     */

    /**
     * Set Project ID of your Google APIs Console Project.
     */
    //// TODO: change this for each app using Chimera library
    public static final String PROJECT_ID = "gcm-app-1";

    /**
     * Set Project Number of your Google APIs Console Project.
     */
    public static final String PROJECT_NUMBER = "358057768286";

    /**
     * Set your Web Client ID for authentication at backend.
     */
    public static final String WEB_CLIENT_ID = "*** ENTER YOUR WEB CLIENT ID ***";

    /**
     * Set default user authentication enabled or disabled.
     */
    public static final boolean IS_AUTH_ENABLED = false;

    /**
     * Auth audience for authentication on backend.
     */
    public static final String AUTH_AUDIENCE = "server:client_id:" + WEB_CLIENT_ID;

    /**
     *  Preferences Key for device RegId
     */
    public static final String PROPERTY_REG_ID = "registration_id";

    /**
     * 	Preferences Key for app version
     */
    public static final String PROPERTY_APP_VERSION = "app_version";

    /**
     * Endpoint root URL
     */
    public static final String ENDPOINT_ROOT_URL = WEBSITE;
    /**
     * A flag to switch if the app should be run with local dev server or
     * production (cloud).
     */
    public static final boolean LOCAL_ANDROID_RUN = false;

    /**
     * SharedPreferences keys for CloudBackend.
     */
    public static final String PREF_KEY_CLOUD_BACKEND = "PREF_KEY_CLOUD_BACKEND";
    public static final String PREF_KEY_ACCOUNT_NAME = "PREF_KEY_ACCOUNT_NAME";

    /**
     *   Constants for exponential backoff
     */
    public static final int MAX_ATTEMPTS = 5;
    public static final int BACKOFF_MILLI_SECONDS = 2000;
    public static final Random random = new Random();

    /**
     * Server registration URL here
     */
//    static final String SERVER_URL = "http://www.indieappstore.com/gcm_server_php/register.php";
//    static final String SERVER_URL = "http://www.randomprojecttest.tk/push/register";

    static final String REGISTER_PUSH_SERVER_URL = WEBSITE + "push/" + APP_UUID;

    /**
     *   JSON Node names
     */
    public static final String JSON_NODE_SUCCESS = "success";
    public static final String JSON_NODE_APP_DATA = "app_data";
    public static final String JSON_NODE_SCREENS = "screens";
    public static final String JSON_NODE_TEXTS = "texts";
    public static final String JSON_NODE_IMAGES = "images";


   /**
    * Tag for Intent Label
    */

   public static final String UPDATE_UI = "UPDATE_UI";

}
