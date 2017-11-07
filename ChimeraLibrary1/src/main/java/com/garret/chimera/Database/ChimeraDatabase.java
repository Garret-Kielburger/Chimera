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
import com.garret.chimera.DataObjects.ButtonDataObject;
import com.garret.chimera.DataObjects.ButtonSubscreenDataObject;
import com.garret.chimera.DataObjects.ConstraintDataObject;
import com.garret.chimera.DataObjects.IDataObject;
import com.garret.chimera.DataObjects.ImageDataObject;
import com.garret.chimera.DataObjects.TextfieldDataObject;
import com.garret.chimera.DataObjects.ScreenDataObject;
import com.garret.chimera.ViewObjects.Screen;
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
    public static final String TABLE_BUTTONS = "buttons";
    public static final String TABLE_BUTTONS_SUBSCREENS = "buttons_sub_screens";
    public static final String TABLE_CONSTRAINTS = "constraints";

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

    // buttons Column names
    public static final String KEY_BUTTON_SUB_SCREEN_UUID = "button_sub_screen_uuid";
    public static final String KEY_WITH_SUB_SCREEN = "with_sub_screen";
    public static final String KEY_SUB_SCREEN_UUID = "sub_screen_uuid";
    public static final String KEY_LABEL = "label";

    // buttons_subscreens Column names
    public static final String KEY_OWNING_BUTTON_UUID = "owning_button_uuid";
    public static final String KEY_TITLE = "title";

    // constraint Column names
    public static final String KEY_START_ID = "start_id";
    public static final String KEY_START_SIDE = "start_side";
    public static final String KEY_END_ID = "end_id";
    public static final String KEY_END_SIDE = "end_side";
    public static final String KEY_MARGIN = "margin";

    private ArrayList<ImageDataObject> imageList = new ArrayList<ImageDataObject>();
    private ArrayList<TextfieldDataObject> textfieldList = new ArrayList<TextfieldDataObject>();
    private ArrayList<ScreenDataObject> screenList = new ArrayList<ScreenDataObject>();
    private ArrayList<IDataObject> interfaceDataObjectList = new ArrayList<IDataObject>();
    private ArrayList<IDataObject> subscreenInterfaceDataObjectList = new ArrayList<IDataObject>();
    private ScreenDataObject current_screen = new ScreenDataObject();
    private ArrayList<ConstraintDataObject> constraints = new ArrayList<ConstraintDataObject>();

    private int number_of_screens;
    private ArrayList<String> titles = new ArrayList<String>();

    public String screen_uuid;
    public String sub_screen_uuid;

    public ChimeraDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.i(TAG, "Constructor");
        // TODO Auto-generated constructor stub
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations for Screens!
     */


    /**
     * Save data (used from API calls)
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
        Log.i(TAG, "Screen Data Saved:");
        Log.i(TAG, values.toString());


    }

    public void SaveTextData(TextfieldDataObject tdo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UUID, tdo.getUuid());
        values.put(KEY_SCREEN_UUID, tdo.getScreenUuid());
        values.put(KEY_BUTTON_SUB_SCREEN_UUID, tdo.getButtonSubscreenUuid());
        values.put(KEY_PURPOSE, tdo.getPurpose());
        //values.put(KEY_VERTICAL_ALIGN, tdo.getVerticalAlign());
        //values.put(KEY_HORIZONTAL_ALIGN, tdo.getHorizontalAlign());
        values.put(KEY_CONTENT, tdo.getContent());

        db.insert(TABLE_TEXTS, null, values);
        db.close();
        Log.i(TAG, "Text Data Saved:");
        Log.i(TAG, values.toString());

    }

    public void SaveImageData(ImageDataObject ido) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UUID, ido.getUuid());
        values.put(KEY_SCREEN_UUID, ido.getScreenUuid());
        values.put(KEY_BUTTON_SUB_SCREEN_UUID, ido.getButtonSubscreenUuid());
        values.put(KEY_PURPOSE, ido.getPurpose());
        //values.put(KEY_VERTICAL_ALIGN, ido.getVerticalAlign());
        // values.put(KEY_HORIZONTAL_ALIGN, ido.getHorizontalAlign());
        values.put(KEY_URI, ido.getUri());

        db.insert(TABLE_IMAGES, null, values);
        db.close();
        Log.i(TAG, "Image Data Saved:");
        Log.i(TAG, values.toString());

    }

    public void SaveButtonData(ButtonDataObject bdo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UUID, bdo.get_uuid());
        values.put(KEY_SCREEN_UUID, bdo.get_screen_uuid());
        values.put(KEY_BUTTON_SUB_SCREEN_UUID, bdo.get_button_sub_screen_uuid());
        values.put(KEY_WITH_SUB_SCREEN, bdo.get_with_sub_screen());
        values.put(KEY_SUB_SCREEN_UUID, bdo.get_sub_screen_uuid());
        values.put(KEY_LABEL, bdo.get_label());
        values.put(KEY_PURPOSE, bdo.get_purpose());
        values.put(KEY_CONTENT, bdo.get_content());

        db.insert(TABLE_BUTTONS, null, values);
        db.close();
        Log.i(TAG, "Button Data Saved:");
        Log.i(TAG, values.toString());

    }

    public void SaveButtonSubscreenData(ButtonSubscreenDataObject bsdo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UUID, bsdo.get_uuid());
        values.put(KEY_SCREEN_UUID, bsdo.get_screen_uuid());
        values.put(KEY_OWNING_BUTTON_UUID, bsdo.get_owning_button_uuid());
        values.put(KEY_TITLE, bsdo.get_title());
        values.put(KEY_PURPOSE, bsdo.get_purpose());

        db.insert(TABLE_BUTTONS_SUBSCREENS, null, values);
        db.close();
        Log.i(TAG, "Button Subscreen Data Saved:");
        Log.i(TAG, values.toString());

    }

    public void SaveConstraintData(ConstraintDataObject cdo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SCREEN_UUID, cdo.getScreenUuid());
        values.put(KEY_START_ID, cdo.getStartId());
        values.put(KEY_START_SIDE, cdo.getStartSide());
        values.put(KEY_END_ID, cdo.getEndId());
        values.put(KEY_END_SIDE, cdo.getEndSide());
        values.put(KEY_MARGIN, cdo.getMargin());

        db.insert(TABLE_CONSTRAINTS, null, values);
        db.close();
        Log.i(TAG, "Constraint Data Saved:");
        Log.i(TAG, values.toString());

    }

    /**
     *
     *      Get All methods (used from the UI callers)
     *
     */

    /**
     * Get A Screen by Screen Number (number comes from the count of screens and increments from 0)
     *
     * @param screen_count_i
     * @return ScreenDataObject ArrayList
     * <p>
     * Goal is to produce an array of content items in the correct top to bottom order
     * <p>
     * <p>
     * Problem - need to call all records, sort and select, then choose uuid from proper screen from there.
     * <p>
     * <p>
     * Better way: store list of screens in shared prefs to pass the uuid/order # to the db call for the rest of the data.
     * <p>
     * TODO: make list of current screens in SHaredPrefs to get screens by uuid first?
     */

    public ArrayList<IDataObject> Get_Screen_Content_By_Screen(int screen_count_i) {

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
            Cursor textCursor = db.rawQuery(textQuery, new String[]{screen_uuid.toString()});
            Log.e("Text cursor", DatabaseUtils.dumpCursorToString(textCursor));


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
            Cursor imageCursor = db.rawQuery(imageQuery, new String[]{screen_uuid.toString()});

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
/*            Collections.sort(interfaceDataObjectList, new Comparator<IDataObject>(){
                @Override
                public int compare(IDataObject lhs, IDataObject rhs) {
                    return Integer.valueOf(lhs.getVerticalAlign().compareTo(rhs.getVerticalAlign()));
                }
            });*/

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
            }
            textCursor.close();
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
            }
            titleCursor.close();
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


    // todo:  reimplement! Delete all from Image, Text and Screen Tables
    public void Delete_All() {
        Log.e("DELETE_ALL", "DB Deleted");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMAGES, null, null);
        db.delete(TABLE_TEXTS, null, null);
        db.delete(TABLE_SCREENS, null, null);
        db.delete(TABLE_BUTTONS, null, null);
        db.delete(TABLE_BUTTONS_SUBSCREENS, null, null);
        db.delete(TABLE_APP_DATA, null, null);
        db.delete(TABLE_CONSTRAINTS, null, null);
        db.close();
    }

    // Deleting single Image or Textfield - by ID found from loop through list of all objects
    public void Delete_Image_Textfield(int id, String text_or_image) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (text_or_image == "text") {
            db.delete(TABLE_IMAGES, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        } else if (text_or_image == "image") {
            db.delete(TABLE_TEXTS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        } else {
            // In future, add delete screen option, but needs to check for texts/images with constraints
        }
        db.close();
    }

    /**
     * Get count of screens methods
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

    public int Get_Navigation_Type() {
        int nav_id = 0;

        try {
            //todo: add --> + " WHERE " + KEY_APP_NAME + "= "  --> after storing app name in shared prefs, etc
            String appQuery = "SELECT " + KEY_NAVIGATION_ID + " FROM " + TABLE_APP_DATA;
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
            Cursor cursor = db.rawQuery(query, new String[]{uuid});

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
                    screen_uuid = uuid;

                    // note: screen will always be the first element of the returned array (need it for name, etc)
                    interfaceDataObjectList.add(sdo);

                    current_screen = sdo;
                    Log.d("current_screen: ", sdo.toString());

                } while (cursor.moveToNext());
            }

            cursor.close();

            // Get Text data

            String textQuery = "SELECT * FROM " + TABLE_TEXTS + " WHERE " + KEY_SCREEN_UUID + " = ? " + " AND " + KEY_BUTTON_SUB_SCREEN_UUID + " = ?";
            //String textQuery = "SELECT * FROM texts WHERE screen_uuid = ? AND button_sub_screen_uuid IS NULL";
            Cursor textCursor = db.rawQuery(textQuery, new String[]{screen_uuid, "null"});

            //Cursor textCursor = db.query(TABLE_TEXTS, null, KEY_SCREEN_UUID + "=?", new String[]{screen_uuid}, null, null, null);

            Log.e("Text cursor", DatabaseUtils.dumpCursorToString(textCursor));

            if (textCursor.moveToFirst()) {
                do {
                    Log.e("textCursor at 3:", textCursor.getString(3).toString());
                    Log.e("boolean of textCursor:", Boolean.toString(textCursor.getString(3).equals("null")));
                    // WHERE KEY_SUB_SCREEN_UUID IS NULL is not working for some reason? returns nothing at all.
                        // fields: id       (0)     int
                        // uuid             (1)     String
                        // screen_uuid      (2)     String
                        // button_sub_screen_uuid (3) String
                        // purpose          (4)     String
                        // vertical align   (5)     int
                        // horizontal align (6)     int
                        // content          (7)     String]
                        TextfieldDataObject tdo = new TextfieldDataObject();
                        tdo.setUuid(textCursor.getString(1));
                        tdo.setScreenUuid(screen_uuid);  //---> Screen UUID needed in each element?
                        Log.e("TEXT BUTTON_SUB_SCREEN_UUID", textCursor.getString(3));
                        tdo.setButtonSubscreenUuid(textCursor.getString(3));
                        tdo.setPurpose(textCursor.getString(4));
                        //tdo.setVerticalAlign(textCursor.getInt(4));
                        //todo: remove old debug Logs
                        //Log.i("Textfield getVerticalAlign()", tdo.getVerticalAlign().toString());

                        //tdo.setHorizontalAlign(textCursor.getInt(5));
                        tdo.setContent(textCursor.getString(7));

                        Log.i("<====== TEXT CURSOR ======> ", DatabaseUtils.dumpCursorToString(textCursor));


                        interfaceDataObjectList.add(tdo);

                } while (textCursor.moveToNext());
            }

            textCursor.close();

            // Get Image data

            String imageQuery = "SELECT * FROM " + TABLE_IMAGES + " WHERE " + KEY_SCREEN_UUID + " = ? " + " AND " + KEY_BUTTON_SUB_SCREEN_UUID + " = ?";
            Cursor imageCursor = db.rawQuery(imageQuery, new String[]{screen_uuid, "null"});
            Log.e("Image cursor", DatabaseUtils.dumpCursorToString(imageCursor));

            if (imageCursor.moveToFirst()) {
                do {
                    // fields: id       (0)     int
                    // uuid             (1)     String
                    // screen_uuid      (2)     String
                    // button_sub_screen_uuid (3) String
                    // purpose          (4)     String
                    // vertical align   (5)     int
                    // horizontal align (6)     int
                    // content          (7)     String
                    ImageDataObject ido = new ImageDataObject();
                    ido.setUuid(imageCursor.getString(1));
                    ido.setScreenUuid(imageCursor.getString(2));
                    ido.setButtonSubscreenUuid(imageCursor.getString(3));
                    ido.setPurpose(imageCursor.getString(4));
                    //ido.setVerticalAlign(imageCursor.getInt(4));
                    //todo: remove old debug Logs
                    //Log.i("Image getVerticalAlign()", ido.getVerticalAlign().toString());
                    //ido.setHorizontalAlign(imageCursor.getInt(5));
                    ido.setUri(imageCursor.getString(7));

                    Log.i("<====== IMAGE CURSOR ======>", DatabaseUtils.dumpCursorToString(imageCursor));

                    interfaceDataObjectList.add(ido);

                } while (imageCursor.moveToNext());
            }

            imageCursor.close();


            // Get Button data

            String buttonQuery = "SELECT * FROM " + TABLE_BUTTONS + " WHERE " + KEY_SCREEN_UUID + " = ? " + " AND " + KEY_BUTTON_SUB_SCREEN_UUID + " = ?";
            Cursor buttonCursor = db.rawQuery(buttonQuery, new String[]{screen_uuid, "null"});
            Log.e("Button cursor", DatabaseUtils.dumpCursorToString(buttonCursor));

            if (buttonCursor.moveToFirst()) {
                do {
                    // fields:
                    // id               (0)     int
                    // uuid             (1)     String
                    // screen_uuid      (2)     String
                    // button_sub_screen_uuid (3) String
                    // with_sub_screen  (4)     Boolean
                    // sub_screen_uuid  (5)     String
                    // label            (6)     String
                    // purpose          (7)     String
                    // content          (8)     String

                    ButtonDataObject bdo = new ButtonDataObject();
                    bdo.set_uuid(buttonCursor.getString(1));
                    bdo.set_screen_uuid(buttonCursor.getString(2));
                    bdo.set_button_sub_screen_uuid(buttonCursor.getString(3));
                    boolean subscreen = buttonCursor.getInt(4) > 0;
                    bdo.set_with_sub_screen(subscreen);
                    bdo.set_sub_screen_uuid(buttonCursor.getString(5));
                    bdo.set_label(buttonCursor.getString(6));
                    bdo.set_purpose(buttonCursor.getString(7));
                    bdo.set_content(buttonCursor.getString(8));

                    Log.i("<====== BUTTON CURSOR ======>", DatabaseUtils.dumpCursorToString(buttonCursor));

                    interfaceDataObjectList.add(bdo);

                } while (buttonCursor.moveToNext());
            }

            buttonCursor.close();

            // Get Button Subscreen data

            // Is this a good idea to pull it out now? Or use another method? Trying alternate method first.


            db.close();

            // order top to bottom by vertical align before adding screen to ArrayList
/*
            Collections.sort(interfaceDataObjectList, new Comparator<IDataObject>(){
                @Override
                public int compare(IDataObject lhs, IDataObject rhs) {
                    return Integer.valueOf(lhs.getVerticalAlign().compareTo(rhs.getVerticalAlign()));
                }
            });
*/

            interfaceDataObjectList.add(current_screen);

            //db.close();
            return interfaceDataObjectList;

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("getScreenDataByUuid():", "exception: " + e);
        }
        return interfaceDataObjectList;

    }

    public ArrayList<IDataObject> getSubScreenDataByUuid(String sub_screen_uuid) {
        subscreenInterfaceDataObjectList.clear();

        // GET sub_screen by sub_screen_count integer.

        try {

            SQLiteDatabase db = getReadableDatabase();


//            String query = "SELECT * FROM " + TABLE_BUTTONS_SUBSCREENS + " WHERE " + KEY_OWNING_BUTTON_UUID + " =?";
//            Cursor cursor = db.rawQuery(query, new String[]{uuid});
//
//            Log.e("Subscreen Cursor: ", DatabaseUtils.dumpCursorToString(cursor));
//
//            if (cursor.moveToFirst()) {
//                do {
//                    // fields:
//                    // id                 (0)   int
//                    // uuid               (1)   String
//                    // screen_uuid        (2)   String
//                    // owning_button_uuid (3)   String
//                    // title              (4)   String
//                    // purpose            (5)   String
//
//                    ButtonSubscreenDataObject bsdo = new ButtonSubscreenDataObject();
//                    sub_screen_uuid = cursor.getString(1);
//                    bsdo.set_uuid(sub_screen_uuid);
//                    bsdo.set_screen_uuid(cursor.getString(2));
//                    bsdo.set_owning_button_uuid(uuid);
//                    bsdo.set_title(cursor.getString(4));
//                    bsdo.set_purpose(cursor.getString(5));
//
//                    // note: screen will always be the first element of the returned array (need it for name, etc)
//                    //subscreenInterfaceDataObjectList.add(sdo);
//
//                } while (cursor.moveToNext());
//            }
//
//            cursor.close();

            // Get Text data

            String textQuery = "SELECT * FROM " + TABLE_TEXTS + " WHERE " + KEY_BUTTON_SUB_SCREEN_UUID + " =?";
            //Cursor textCursor = db.rawQuery(textQuery, null);
            Cursor textCursor = db.rawQuery(textQuery, new String[]{sub_screen_uuid});
            Log.e("Subscreen Text cursor", DatabaseUtils.dumpCursorToString(textCursor));

            if (textCursor.moveToFirst()) {
                do {
                    // fields: id       (0)     int
                    // uuid             (1)     String
                    // screen_uuid      (2)     String
                    // button_sub_screen_uuid (3) String
                    // purpose          (4)     String
                    // vertical align   (5)     int
                    // horizontal align (6)     int
                    // content          (7)     String]
                    TextfieldDataObject tdo = new TextfieldDataObject();
                    tdo.setUuid(textCursor.getString(1));
                    tdo.setScreenUuid(textCursor.getString(2));  //---> Screen UUID needed in each element?
                    tdo.setButtonSubscreenUuid(textCursor.getString(3));
                    tdo.setPurpose(textCursor.getString(4));
                    //tdo.setVerticalAlign(textCursor.getInt(4));
                    //todo: remove old debug Logs
                    //Log.i("Textfield getVerticalAlign()", tdo.getVerticalAlign().toString());
                    //tdo.setHorizontalAlign(textCursor.getInt(5));
                    tdo.setContent(textCursor.getString(7));

                    Log.i("<====== SUBSCREEN TEXT CURSOR ======> ", DatabaseUtils.dumpCursorToString(textCursor));


                    subscreenInterfaceDataObjectList.add(tdo);

                } while (textCursor.moveToNext());
            }

            textCursor.close();

            // Get Image data

            String imageQuery = "SELECT * FROM " + TABLE_IMAGES + " WHERE " + KEY_BUTTON_SUB_SCREEN_UUID + " =?";
            Cursor imageCursor = db.rawQuery(imageQuery, new String[]{sub_screen_uuid});
            Log.e("Subscreen Image cursor", DatabaseUtils.dumpCursorToString(imageCursor));

            if (imageCursor.moveToFirst()) {
                do {
                    // fields: id       (0)     int
                    // uuid             (1)     String
                    // screen_uuid      (2)     String
                    // button_sub_screen_uuid (3) String
                    // purpose          (4)     String
                    // vertical align   (5)     int
                    // horizontal align (6)     int
                    // content          (7)     String
                    ImageDataObject ido = new ImageDataObject();
                    ido.setUuid(imageCursor.getString(1));
                    ido.setScreenUuid(imageCursor.getString(2));
                    ido.setButtonSubscreenUuid(imageCursor.getString(3));
                    ido.setPurpose(imageCursor.getString(4));
                    //ido.setVerticalAlign(imageCursor.getInt(4));
                    //todo: remove old debug Logs
                    //Log.i("Image getVerticalAlign()", ido.getVerticalAlign().toString());
                    //ido.setHorizontalAlign(imageCursor.getInt(5));
                    ido.setUri(imageCursor.getString(7));

                    Log.i("<====== SUBSCREEN IMAGE CURSOR ======>", DatabaseUtils.dumpCursorToString(imageCursor));

                    subscreenInterfaceDataObjectList.add(ido);

                } while (imageCursor.moveToNext());
            }

            imageCursor.close();


            // Get Button data

            String buttonQuery = "SELECT * FROM " + TABLE_BUTTONS + " WHERE " + KEY_BUTTON_SUB_SCREEN_UUID + " =?";
            Cursor buttonCursor = db.rawQuery(buttonQuery, new String[]{sub_screen_uuid});
            Log.e("Subscreen Button cursor", DatabaseUtils.dumpCursorToString(buttonCursor));

            if (buttonCursor.moveToFirst()) {
                do {
                    // fields:
                    // id               (0)     int
                    // uuid             (1)     String
                    // screen_uuid      (2)     String
                    // button_sub_screen_uuid (3) String
                    // with_sub_screen  (4)     Boolean
                    // sub_screen_uuid  (5)     String
                    // label            (6)     String
                    // purpose          (7)     String
                    // content          (8)     String

                    ButtonDataObject bdo = new ButtonDataObject();
                    bdo.set_uuid(buttonCursor.getString(1));
                    bdo.set_screen_uuid(buttonCursor.getString(2));
                    bdo.set_button_sub_screen_uuid(buttonCursor.getString(3));
                    boolean subscreen = buttonCursor.getInt(4) > 0;
                    bdo.set_with_sub_screen(subscreen);
                    bdo.set_sub_screen_uuid(buttonCursor.getString(5));
                    bdo.set_label(buttonCursor.getString(6));
                    bdo.set_purpose(buttonCursor.getString(7));
                    bdo.set_content(buttonCursor.getString(8));

                    Log.i("<====== BUTTON CURSOR ======>", DatabaseUtils.dumpCursorToString(buttonCursor));

                    subscreenInterfaceDataObjectList.add(bdo);

                } while (buttonCursor.moveToNext());
            }

            buttonCursor.close();

            // Get Button Subscreen data

            // Is this a good idea to pull it out now? Or use another method? Trying alternate method first.


            db.close();

            // order top to bottom by vertical align before adding screen to ArrayList
/*
            Collections.sort(interfaceDataObjectList, new Comparator<IDataObject>(){
                @Override
                public int compare(IDataObject lhs, IDataObject rhs) {
                    return Integer.valueOf(lhs.getVerticalAlign().compareTo(rhs.getVerticalAlign()));
                }
            });
*/

            //db.close();
            return subscreenInterfaceDataObjectList;

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("getSubscreenDataByUuid():", "exception: " + e);
        }
        return subscreenInterfaceDataObjectList;

    }

    public ArrayList<ConstraintDataObject> GetConstraintsByScreenUuid(String uuid) {
        constraints.clear();

        try {
            SQLiteDatabase db = getReadableDatabase();
            //String query = "SELECT * FROM " + TABLE_CONSTRAINTS + " WHERE " + KEY_SCREEN_UUID + " = ?";
            String query = "SELECT * FROM " + TABLE_CONSTRAINTS + " WHERE " + KEY_SCREEN_UUID + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{uuid});
            Log.i("GetConstraintsByUUID cursor:", DatabaseUtils.dumpCursorToString(cursor));
            if (cursor.moveToFirst()) {
                do {
                    //fields:
                    // id (0)
                    // screen_uuid (1)
                    // start_id (2)
                    // start_side (3)
                    // end_id (4)
                    // end_side (5)
                    // margin (6)
                    ConstraintDataObject cdo = new ConstraintDataObject();
                    cdo.setScreenUuid(uuid);
                    cdo.setStartId(cursor.getString(2));
                    cdo.setStartSide(cursor.getString(3));
                    cdo.setEndId(cursor.getString(4));
                    cdo.setEndSide(cursor.getString(5));
                    cdo.setMargin(cursor.getInt(6));

                    constraints.add(cdo);

                } while (cursor.moveToNext());
            }
            cursor.close();
            return constraints;

        } catch (Exception e) {
            Log.e("GetConstraintByUuid():", "Exception:" + e);
        }

        return constraints;
    }

    public ButtonSubscreenDataObject GetButtonSubscreenByUuid(String given_owning_button_uuid) {

        ButtonSubscreenDataObject bsdo = new ButtonSubscreenDataObject();

        try {
            SQLiteDatabase db = getReadableDatabase();
            String button_sub_screen_query = "SELECT * FROM " + TABLE_BUTTONS_SUBSCREENS + " WHERE " + KEY_OWNING_BUTTON_UUID + " =?";
            Cursor button_sub_screen_cursor = db.rawQuery(button_sub_screen_query, new String[]{given_owning_button_uuid});

            Log.e("Button Subscreen Cursor:", DatabaseUtils.dumpCursorToString(button_sub_screen_cursor));

            if (button_sub_screen_cursor.moveToFirst()) {
                do {
                    // fields:
                    // id               (0)     int
                    // uuid             (1)     String
                    // screen_uuid      (2)     String
                    // owning_button_uuid (3)   String
                    // title            (4)     String
                    // purpose          (5)     String

                    bsdo.set_uuid(button_sub_screen_cursor.getString(1));
                    bsdo.set_screen_uuid(button_sub_screen_cursor.getString(2));
                    bsdo.set_owning_button_uuid(button_sub_screen_cursor.getString(3));
                    bsdo.set_title(button_sub_screen_cursor.getString(4));
                    bsdo.set_purpose(button_sub_screen_cursor.getString(5));

                } while (button_sub_screen_cursor.moveToNext());
            }

            button_sub_screen_cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("GetButtonSubscreenByUuid", "Exception: " + e);
        }

        return bsdo;
    }


    public ArrayList<ConstraintDataObject> GetConstraintsByViewObjectUuid(String uuid) {
        constraints.clear();

        try {
            SQLiteDatabase db = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_CONSTRAINTS + " WHERE " + KEY_START_ID + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{uuid});
            //Log.i("GetConstraintsByUUID cursor:", DatabaseUtils.dumpCursorToString(cursor));
            if (cursor.moveToFirst()) {
                do {
                    //fields:
                    // screen_uuid (0)
                    // start_id (1)
                    // start_side (2)
                    // end_id (3)
                    // end_side (4)
                    // margin (5)
                    ConstraintDataObject cdo = new ConstraintDataObject();
                    cdo.setScreenUuid(uuid);
                    cdo.setStartId(cursor.getString(1));
                    cdo.setStartSide(cursor.getString(2));
                    cdo.setEndId(cursor.getString(3));
                    cdo.setEndSide(cursor.getString(4));
                    cdo.setMargin(cursor.getInt(5));

//                    cdo.setScreenUuid(uuid);
//                    cdo.setStartId(cursor.getString(2));
//                    cdo.setStartSide(cursor.getString(3));
//                    cdo.setEndId(cursor.getString(4));
//                    cdo.setEndSide(cursor.getString(5));
//                    cdo.setMargin(cursor.getInt(6));


                    constraints.add(cdo);

                } while (cursor.moveToNext());
            }
            cursor.close();
            return constraints;

        } catch (Exception e) {
            Log.e("GetConstraintByUuid():", "Exception:" + e);
        }

        return constraints;
    }
}
