package com.garret.chimera.DataObjects;

/**
 * Created by Captain on 26/08/2016.
 * <p/>
 * <p/>
 * Copyright Garret Kielburger - All Rights Reserved.
 */
public class AppDataDataObject {

    public int _id;
    public String _app_name;
    public int _navigation_id;


    public AppDataDataObject(){}


    //constructor
    public AppDataDataObject(int id, String app_name, int navigation_id) {
        this._id = id;
        this._app_name = app_name;
        this._navigation_id = navigation_id;
    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting ID
    public void setID(int id){
        this._id = id;
    }

    // getting the number of the page the image exists on
    public String getAppName(){
        return this._app_name;
    }

    // setting the number of the page the image exists on
    public void setAppName(String app_name){
        this._app_name = app_name;
    }

    // getting boolean for if it is an image
    public int getNavigationId(){
        return this._navigation_id;
    }

    // setting boolean for if it is an image
    public void setNavigationId(int navigation_id){
        this._navigation_id =  navigation_id;
    }

}
