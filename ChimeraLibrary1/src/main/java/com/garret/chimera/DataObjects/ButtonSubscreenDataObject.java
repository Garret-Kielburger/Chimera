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
    public String _screen_uuid;
    public String _owning_button_uuid;
    public String _button_uuid;
    public String _text_uuid;
    public String _image_uuid;

    public ButtonSubscreenDataObject() {
    }

    public ButtonSubscreenDataObject(int _id, String _uuid, String _screen_uuid, String _owning_button_uuid, String _button_uuid, String _text_uuid, String _image_uuid) {
        this._id = _id;
        this._uuid = _uuid;
        this._screen_uuid = _screen_uuid;
        this._owning_button_uuid = _owning_button_uuid;
        this._button_uuid = _button_uuid;
        this._text_uuid = _text_uuid;
        this._image_uuid = _image_uuid;
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

    public String get_button_uuid() {
        return _button_uuid;
    }

    public void set_button_uuid(String _button_uuid) {
        this._button_uuid = _button_uuid;
    }

    public String get_text_uuid() {
        return _text_uuid;
    }

    public void set_text_uuid(String _text_uuid) {
        this._text_uuid = _text_uuid;
    }

    public String get_image_uuid() {
        return _image_uuid;
    }

    public void set_image_uuid(String _image_uuid) {
        this._image_uuid = _image_uuid;
    }
}
