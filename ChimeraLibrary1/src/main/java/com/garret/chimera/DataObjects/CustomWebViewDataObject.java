package com.garret.chimera.DataObjects;

/**
 * Created by Captain on 12/12/2017.
 * <p>
 * <p>
 * Copyright Greenr Republic Software Company - All Rights Reserved.
 */

public class CustomWebViewDataObject implements IDataObject {

    // private variables
    public int _id;
    public String _uuid;
    public String _screen_uuid;             // belongs to a screen
    public String _web_address;


    public CustomWebViewDataObject() {
    }

    public CustomWebViewDataObject(int _id, String _uuid, String _screen_uuid, String _web_address) {
        this._id = _id;
        this._uuid = _uuid;
        this._screen_uuid = _screen_uuid;
        this._web_address = _web_address;
    }


    // getting ID
    public int getID(){
        return this._id;
    }

    // setting ID
    public void setID(int id){
        this._id = id;
    }

    //getting uuid
    public String getUuid() { return this._uuid; }

    //setting uuid
    public void setUuid(String uuid) {
        this._uuid = uuid;
    }

    // getting screen uuid
    public String getScreenUuid(){
        return this._screen_uuid;
    }

    // setting screen uuid
    public void setScreenUuid(String screen_uuid) {
        this._screen_uuid = screen_uuid;
    }

    // getting screen uuid
    public String getWebAddress(){
        return this._web_address;
    }

    // setting screen uuid
    public void setWebAddress(String web_address) {
        this._web_address = web_address;
    }




}
