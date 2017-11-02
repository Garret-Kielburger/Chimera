package com.garret.chimera.DataObjects;

/**
 * Created by Captain on 30/10/2017.
 * <p>
 * <p>
 * Copyright Greenr Republic Software Company - All Rights Reserved.
 */

public class ButtonDataObject implements IDataObject {

    // private variables
    public int _id;
    public String _uuid;
    public String _screen_uuid;
    public boolean _with_sub_screen;
    public String _sub_screen_uuid;
    public String _label;
    public String _purpose;
    public String _content; // When wilth_sub_screen = true, content wil be UUID for ButtonSubscreenDataObject?


    public ButtonDataObject() {
    }

    public ButtonDataObject(int _id, String _uuid, String _screen_uuid, boolean _with_sub_screen, String _sub_screen_uuid, String _label, String _purpose, String _content) {
        this._id = _id;
        this._uuid = _uuid;
        this._screen_uuid = _screen_uuid;
        this._with_sub_screen = _with_sub_screen;
        this._sub_screen_uuid = _sub_screen_uuid;
        this._label = _label;
        this._purpose = _purpose;
        this._content = _content;
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

    public boolean get_with_sub_screen() {
        return _with_sub_screen;
    }

    public void set_with_sub_screen(boolean _with_sub_screen) {
        this._with_sub_screen = _with_sub_screen;
    }

    public String get_sub_screen_uuid() {
        return _sub_screen_uuid;
    }

    public void set_sub_screen_uuid(String _sub_screen_uuid) {
        this._sub_screen_uuid = _sub_screen_uuid;
    }

    public String get_label() {
        return _label;
    }

    public void set_label(String _label) {
        this._label = _label;
    }

    public String get_purpose() {
        return _purpose;
    }

    public void set_purpose(String _purpose) {
        this._purpose = _purpose;
    }

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }
}
