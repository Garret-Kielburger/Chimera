package com.garret.chimera.DataObjects;

/**
 * Created by Captain on 30/10/2017.
 * <p>
 * <p>
 * Copyright Greenr Republic Software Company - All Rights Reserved.
 */

public class ButtonSubscreenDataObject implements IDataObject {

    // private variables
    public int _id;
    public String _uuid;
    public String _screen_uuid;         // Screen this is associated with
    public String _owning_button_uuid;  // Button that clicked to this screen
    public String _title;
    public String _purpose;

    public ButtonSubscreenDataObject() {
    }

    public ButtonSubscreenDataObject(int _id, String _uuid, String _screen_uuid, String _owning_button_uuid, String _title, String _purpose) {
        this._id = _id;
        this._uuid = _uuid;
        this._screen_uuid = _screen_uuid;
        this._owning_button_uuid = _owning_button_uuid;
        this._title = _title;
        this._purpose = _purpose;
    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting ID
    public void setID(int id){
        this._id = id;
    }

    public String get_uuid() {
        return _uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public String get_screen_uuid() {
        return _screen_uuid;
    }

    public void set_screen_uuid(String _screen_uuid) {
        this._screen_uuid = _screen_uuid;
    }

    public String get_owning_button_uuid() {
        return _owning_button_uuid;
    }

    public void set_owning_button_uuid(String _owning_button_uuid) {
        this._owning_button_uuid = _owning_button_uuid;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_purpose() {
        return _purpose;
    }

    public void set_purpose(String _purpose) {
        this._purpose = _purpose;
    }
}
