package com.garret.chimera.Database;

/**
 * Created by Captain on 11/08/2015.
 *
 * Copyright Garret Kielburger - All Rights Reserved.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.garret.chimera.DataObjects.AppDataDataObject;
import com.garret.chimera.DataObjects.IDataObject;
import com.garret.chimera.DataObjects.ImageDataObject;
import com.garret.chimera.DataObjects.TextfieldDataObject;
import com.garret.chimera.DataObjects.ScreenDataObject;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.provider.Contacts.SettingsColumns.KEY;

public class ChimeraDatabase extends SQLiteAssetHelper {

    private static String TAG = "Chimera Database:";

    //TODO: Create contstructor for new Tables (as pages/screens) - ie using data from push

    private static String DB_NAME = "Chimera.db";
    private static final int DB_VERSION = 1;

    // Tables

    public static final String TABLE_APP_DATA = "app_data";
    public static final String TABLE_SCREENS = "screens";
    public static final String TABLE_TEXTS = "texts";
    public static final String TABLE_IMAGES = "images";

    // Manticore-Chimera Screens Table Columns names
    //TODO: Create dynamic fields from push data - assume all fields and leave out nulls?

    //  Column names
    private static final String KEY_ID = "id";
    private static final String KEY_SCREEN_UUID = "screen_uuid";
    private static final String KEY_PURPOSE = "purpose";
    private static final String KEY_HORIZONTAL_ALIGN = "horizontal_align";
    private static final String KEY_VERTICAL_ALIGN = "vertical_align";


    // app_data Column names
    private static final String KEY_APP_NAME = "app_name";
    private static final String KEY_NAVIGATION_ID = "navigation_id";

    // screens Column names
    private static final String KEY_UUID = "uuid";
    private static final String KEY_SCREEN_NAME = "screen_name";
    private static final String KEY_SCREEN_ORDER = "screen_order_number";

    // texts Column names
    public static final String KEY_CONTENT = "content";

    // images Column names
    public static final String KEY_URI = "uri";

    private ArrayList<ImageDataObject> imageList = new ArrayList<ImageDataObject>();
    private ArrayList<TextfieldDataObject> textfieldList = new ArrayList<TextfieldDataObject>();
    private ArrayList<ScreenDataObject> screenList = new ArrayList<ScreenDataObject>();
    private ArrayList<IDataObject> interfaceDataObjectList = new ArrayList<IDataObject>();
    private ScreenDataObject current_screen = new ScreenDataObject();

    private int number_of_screens;
    private ArrayList<String> titles = new ArrayList<String>();

    public String screen_uuid;

    public ChimeraDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.i(TAG, "Constructor");
        // TODO Auto-generated constructor stub
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations for Screens!
     */


    /**
     *
     *      Save data (used from API calls)
     *
     */

    public void SaveAppData(AppDataDataObject addo) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        // todo: check for existing app name, and if so, update instead of insert

        values.put(KEY_APP_NAME, addo.getAppName());
        values.put(KEY_NAVIGATION_ID, addo.getNavigationId());

        db.insert(TABLE_APP_DATA, null, values);
        db.close();
        Log.i(TAG, "App Data Saved");

    }

    // todo: add timestamps to db as TEXT field in sqlite
    public void SaveScreenData(ScreenDataObject sdo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        // todo: check for existing screen name, and if so, update instead of insert

        values.put(KEY_UUID, sdo.getUuid());
        values.put(KEY_SCREEN_NAME, sdo.getName());
        values.put(KEY_SCREEN_ORDER, sdo.getOrder());

        db.insert(TABLE_SCREENS, null, values);
        db.close();
        Log.i(TAG, "Screen Data Saved");

    }

    public void SaveTextData(TextfieldDataObject tdo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_SCREEN_UUID, tdo.getScreenUuid());
        values.put(KEY_PURPOSE, tdo.getPurpose());
        values.put(KEY_VERTICAL_ALIGN, tdo.getVerticalAlign());
        values.put(KEY_HORIZONTAL_ALIGN, tdo.getHorizontalAlign());
        values.put(KEY_CONTENT, tdo.getContent());

        db.insert(TABLE_TEXTS, null, values);
        db.close();
        Log.i(TAG, "Text Data Saved");

    }

    public void SaveImageData(ImageDataObject ido) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_SCREEN_UUID, ido.getScreenUuid());
        values.put(KEY_PURPOSE, ido.getPurpose());
        values.put(KEY_VERTICAL_ALIGN, ido.getVerticalAlign());
        values.put(KEY_HORIZONTAL_ALIGN, ido.getHorizontalAlign());
        values.put(KEY_URI, ido.getUri());

        db.insert(TABLE_IMAGES, null, values);
        db.close();
        Log.i(TAG, "Image Data Saved");
    }

    /**
     *
     *      Get All methods (used from the UI callers)
     *
     */

    /**
     * Get A Screen by Screen Number (number comes from the count of screens and increments from 0)
     * @param screen_count_i
     * @return ScreenDataObject ArrayList
     *
     *  Goal is to produce an array of content items in the correct top to bottom order
     *
     *
     * Problem - need to call all records, sort and select, then choose uuid from proper screen from there.
     *
     *
     * Better way: store list of screens in shared prefs to pass the uuid/order # to the db call for the rest of the data.
     *
     * TODO: make list of current screens in SHaredPrefs to get screens by uuid first?
     *
     *
     */

    public ArrayList<IDataObject> Get_Screen_Content_By_Screen(int screen_count_i){

        interfaceDataObjectList.clear();

        // GET screen by screen_count integer ordered by screen order.

        try {
            // offsetter needed for regular screens functioning was screen_count_i - 1;
            //int offsetter = screen_count_i - 1;
            int offsetter = screen_count_i;

            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_SCREENS + " ORDER BY " + KEY_SCREEN_ORDER + " ASC LIMIT 1 OFFSET " + offsetter;
            Cursor cursor = db.rawQuery(query, null);

            Log.e("Screen offset cursor", DatabaseUtils.dumpCursorToString(cursor));

            if (cursor.moveToFirst()) {
                do {
                    // fields:
                    // id (0)           int
                    // uuid (1)         String
                    // screen_name (2)  String

                    ScreenDataObject sdo = new ScreenDataObject();
                    sdo.setUuid(cursor.getString(1));
                    sdo.setName(cursor.getString(2));
                    sdo.setOrder(cursor.getInt(3));
                    screen_uuid = sdo.getUuid();

                    // note: screen will always be the first element of the returned array (need it for name, etc)
                    //interfaceDataObjectList.add(sdo);

                    current_screen = sdo;

                } while (cursor.moveToNext());
            }

            cursor.close();

            // Get Text data

            String textQuery = "SELECT * FROM " + TABLE_TEXTS + " WHERE " + KEY_SCREEN_UUID + " = ?";
            Cursor textCursor = db.rawQuery(textQuery, new String[] {screen_uuid.toString()} );

            if (textCursor.moveToFirst()) {
                do {
                    // fields: id       (0)     int
                    // screen_uuid      (1)     String
                    // purpose          (2)     String
                    // vertical align   (3)     int
                    // horizontal align (4)     int
                    // content          (5)     String]
                    TextfieldDataObject tdo = new TextfieldDataObject();
                    //tdo.setScreenUuid(textCursor.getString(1));  ---> Screen UUID needed in each element?
                    tdo.setPurpose(textCursor.getString(2));
                    tdo.setVerticalAlign(textCursor.getInt(3));
                    //todo: remove old debug Logs
                    Log.i("Textfield getVerticalAlign()", tdo.getVerticalAlign().toString());

                    tdo.setHorizontalAlign(textCursor.getInt(4));
                    tdo.setContent(textCursor.getString(5));

                    //Log.i("<====== TEXT CURSOR ======> ", DatabaseUtils.dumpCursorToString(textCursor));


                    interfaceDataObjectList.add(tdo);

                } while (textCursor.moveToNext());
            }

            textCursor.close();

            // Get Image data

            String imageQuery = "SELECT * FROM " + TABLE_IMAGES + " WHERE " + KEY_SCREEN_UUID + " = ?";
            Cursor imageCursor = db.rawQuery(imageQuery, new String[] {screen_uuid.toString()} );

            if (imageCursor.moveToFirst()) {
                do {
                    // fields: id       (0)     int
                    // screen_uuid      (1)     String
                    // purpose          (2)     String
                    // vertical align   (3)     int
                    // horizontal align (4)     int
                    // content          (5)     String]
                    ImageDataObject ido = new ImageDataObject();
                    ido.setPurpose(imageCursor.getString(2));
                    ido.setVerticalAlign(imageCursor.getInt(3));
                    //todo: remove old debug Logs
                    Log.i("Image getVerticalAlign()", ido.getVerticalAlign().toString());
                    ido.setHorizontalAlign(imageCursor.getInt(4));
                    ido.setUri(imageCursor.getString(5));

                    //Log.i("<====== IMAGE CURSOR ======>", DatabaseUtils.dumpCursorToString(imageCursor));

                    interfaceDataObjectList.add(ido);

                } while (imageCursor.moveToNext());
            }

            imageCursor.close();
            db.close();

            // order top to bottom by vertical align before adding screen to ArrayList
            Collections.sort(interfaceDataObjectList, new Comparator<IDataObject>(){
                @Override
                public int compare(IDataObject lhs, IDataObject rhs) {
                    return Integer.valueOf(lhs.getVerticalAlign().compareTo(rhs.getVerticalAlign()));
                }
            });

            interfaceDataObjectList.add(current_screen);


            return interfaceDataObjectList;

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("Get_Screen_By_Screen():", "exception: " + e);
        }

        return interfaceDataObjectList;

    }

    public ArrayList<TextfieldDataObject> Get_All_Textfields() {

        try {
            textfieldList.clear();
            String textfieldQuery = "SELECT * FROM " + TABLE_TEXTS;
            SQLiteDatabase db = getReadableDatabase();
            Cursor textCursor = db.rawQuery(textfieldQuery, null);

            if (textCursor.moveToFirst()) {
                do {
                    TextfieldDataObject tdo = new TextfieldDataObject();
                    //tdo.setScreenUuid(textCursor.getString(1));  ---> Screen UUID needed in each element?
                    tdo.setPurpose(textCursor.getString(2));
                    tdo.setVerticalAlign(textCursor.getInt(3));
                    tdo.setHorizontalAlign(textCursor.getInt(4));
                    tdo.setContent(textCursor.getString(5));

                    textfieldList.add(tdo);

                } while (textCursor.moveToNext());
            } textCursor.close();
            db.close();
            return textfieldList;


        } catch (Exception e) {
            Log.e("All Textfields error: ", "" + e);
        }
        return textfieldList;
    }

    public ArrayList<String> GetScreenNames() {
        titles.clear();

        try {
            String titleQuery = "SELECT * FROM " + TABLE_TEXTS + " WHERE " + KEY_PURPOSE + " = 'title' ";
            SQLiteDatabase db = getReadableDatabase();
            Cursor titleCursor = db.rawQuery(titleQuery, null);

            if (titleCursor.moveToFirst()) {
                do {
                    titles.add(titleCursor.getString(5));
                } while (titleCursor.moveToNext());
            } titleCursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("Screen Titles error: ", "" + e);
        }



        return titles;
    }

    public ArrayList<ImageDataObject> Get_All_Images() {

        try {
            imageList.clear();
            String imageQuery = "SELECT * FROM " + TABLE_IMAGES;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(imageQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    ImageDataObject ido = new ImageDataObject();
                    //ido.setScreenUuid(cursor.getString(1));  ---> Screen UUID needed in each element?
                    ido.setPurpose(cursor.getString(2));
                    ido.setVerticalAlign(cursor.getInt(3));
                    ido.setHorizontalAlign(cursor.getInt(4));
                    ido.setUri(cursor.getString(5));

                    imageList.add(ido);

                } while (cursor.moveToNext());
            }
            return imageList;

        } catch (Exception e) {
            Log.e("All Images error: ", "" + e);
        }
        return imageList;
    }

    public ArrayList<ScreenDataObject> Get_All_Screens_Metadata() {

        try {
            screenList.clear();
            String screensQuery = "SELECT * FROM " + TABLE_SCREENS + " ORDER BY " + KEY_SCREEN_ORDER;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(screensQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    ScreenDataObject sdo = new ScreenDataObject();
                    sdo.setUuid(cursor.getString(1));
                    sdo.setName(cursor.getString(2));

                    screenList.add(sdo);

                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return screenList;

        } catch (Exception e) {
            Log.e("All Textfields error: ", "" + e);
        }
        return screenList;
    }


    //Omit Update method for now - this is a read-only application


    // Delete all from Image, Text and Screen Tables
    public void Delete_All() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMAGES, null, null);
        db.delete(TABLE_TEXTS, null, null);
        db.delete(TABLE_SCREENS, null, null);
        db.delete(TABLE_APP_DATA, null, null);
        db.close();
    }

    // Deleting single Image or Textfield - by ID found from loop through list of all objects
    public void Delete_Image_Textfield(int id, String text_or_image) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (text_or_image == "text"){
            db.delete(TABLE_IMAGES, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        } else if (text_or_image == "image") {
            db.delete(TABLE_TEXTS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        } else {
            // In future, add delete screen option, but needs to check for texts/images with constraints
        }
        db.close();
    }

    /**
     *
     * Get count of screens methods
     *
     */

    // Getting Total number of Online Images
    public int Get_Total_Count() {
        String countQuery = "SELECT * FROM " + TABLE_SCREENS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }


    //Method for getting a count of all the pages for the app.
    public int Get_Number_Of_Screens() {

        number_of_screens = 0;

        SQLiteDatabase db = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_SCREENS);
        db.close();

        number_of_screens = (int) count;

        return number_of_screens;
    }

    public int Get_Navigation_Type(){
        int nav_id = 0;

        try {
            //todo: add --> + " WHERE " + KEY_APP_NAME + "= "  --> after storing app name in shared prefs, etc
            String appQuery = "SELECT "+ KEY_NAVIGATION_ID +" FROM " + TABLE_APP_DATA;
            SQLiteDatabase db = getReadableDatabase();
            Cursor navIdCursor = db.rawQuery(appQuery, null);


            if (navIdCursor.moveToFirst()) {
                do {
                    // App Data Table: however, only selecting navigation_id, therefore need element 0
                    // fields: id       (0)     int
                    // app_name         (1)     String
                    // navigation_id    (2)     int
                    nav_id = navIdCursor.getInt(0);
                } while (navIdCursor.moveToNext());
            }
            navIdCursor.close();
            db.close();

            return nav_id;

        } catch (Exception e) {
            //todo: handle excepetion
            Log.e("Navigation Id error: ", "exception: " + e);

        }

        return nav_id;
    }


    public ArrayList<IDataObject> getScreenDataByUuid(String uuid) {
        interfaceDataObjectList.clear();

        // GET screen by screen_count integer ordered by screen order.

        try {

            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_SCREENS + " WHERE " + KEY_UUID + " =?";
            Cursor cursor  = db.rawQuery(query, new String[] {uuid} );

            Log.e("Screen offset cursor", DatabaseUtils.dumpCursorToString(cursor));

            if (cursor.moveToFirst()) {
                do {
                    // fields:
                    // id (0)           int
                    // uuid (1)         String
                    // screen_name (2)  String

                    ScreenDataObject sdo = new ScreenDataObject();
                    sdo.setUuid(cursor.getString(1));
                    sdo.setName(cursor.getString(2));
                    sdo.setOrder(cursor.getInt(3));
                    screen_uuid = sdo.getUuid();

                    // note: screen will always be the first element of the returned array (need it for name, etc)
                    //interfaceDataObjectList.add(sdo);

                    current_screen = sdo;

                } while (cursor.moveToNext());
            }

            cursor.close();

            // Get Text data

            String textQuery = "SELECT * FROM " + TABLE_TEXTS + " WHERE " + KEY_SCREEN_UUID + " = ?";
            Cursor textCursor = db.rawQuery(textQuery, new String[] {screen_uuid.toString()} );

            if (textCursor.moveToFirst()) {
                do {
                    // fields: id       (0)     int
                    // screen_uuid      (1)     String
                    // purpose          (2)     String
                    // vertical align   (3)     int
                    // horizontal align (4)     int
                    // content          (5)     String]
                    TextfieldDataObject tdo = new TextfieldDataObject();
                    //tdo.setScreenUuid(textCursor.getString(1));  ---> Screen UUID needed in each element?
                    tdo.setPurpose(textCursor.getString(2));
                    tdo.setVerticalAlign(textCursor.getInt(3));
                    //todo: remove old debug Logs
                    Log.i("Textfield getVerticalAlign()", tdo.getVerticalAlign().toString());

                    tdo.setHorizontalAlign(textCursor.getInt(4));
                    tdo.setContent(textCursor.getString(5));

                    //Log.i("<====== TEXT CURSOR ======> ", DatabaseUtils.dumpCursorToString(textCursor));


                    interfaceDataObjectList.add(tdo);

                } while (textCursor.moveToNext());
            }

            textCursor.close();

            // Get Image data

            String imageQuery = "SELECT * FROM " + TABLE_IMAGES + " WHERE " + KEY_SCREEN_UUID + " = ?";
            Cursor imageCursor = db.rawQuery(imageQuery, new String[] {screen_uuid.toString()} );

            if (imageCursor.moveToFirst()) {
                do {
                    // fields: id       (0)     int
                    // screen_uuid      (1)     String
                    // purpose          (2)     String
                    // vertical align   (3)     int
                    // horizontal align (4)     int
                    // content          (5)     String]
                    ImageDataObject ido = new ImageDataObject();
                    ido.setPurpose(imageCursor.getString(2));
                    ido.setVerticalAlign(imageCursor.getInt(3));
                    //todo: remove old debug Logs
                    Log.i("Image getVerticalAlign()", ido.getVerticalAlign().toString());
                    ido.setHorizontalAlign(imageCursor.getInt(4));
                    ido.setUri(imageCursor.getString(5));

                    //Log.i("<====== IMAGE CURSOR ======>", DatabaseUtils.dumpCursorToString(imageCursor));

                    interfaceDataObjectList.add(ido);

                } while (imageCursor.moveToNext());
            }

            imageCursor.close();
            db.close();

            // order top to bottom by vertical align before adding screen to ArrayList
            Collections.sort(interfaceDataObjectList, new Comparator<IDataObject>(){
                @Override
                public int compare(IDataObject lhs, IDataObject rhs) {
                    return Integer.valueOf(lhs.getVerticalAlign().compareTo(rhs.getVerticalAlign()));
                }
            });

            interfaceDataObjectList.add(current_screen);

            db.close();
            return interfaceDataObjectList;

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("getScreenDataByUuid():", "exception: " + e);
        }
        return interfaceDataObjectList;

    }
}
