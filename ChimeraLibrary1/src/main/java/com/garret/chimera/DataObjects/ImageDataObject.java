package com.garret.chimera.DataObjects;

/**
 * Created by Captain on 13/08/2015.
 * <p/>
 * <p/>
 * Copyright Garret Kielburger - All Rights Reserved.
 */
public class ImageDataObject implements IDataObject {

    // private variables
    public int _id;

    public String _screen_uuid;
    public String _purpose;
    public String _uri;
    public Integer _horizontal_align;
    public Integer _vertical_align;

    // public String _name;

    public ImageDataObject() {
        // TODO Auto-generated constructor stub
    }

    //constructor
    public ImageDataObject(int id, String screen_uuid, String purpose, String uri, Integer horizontal_align, Integer vertical_align){
        this._id = id;
        this._screen_uuid = screen_uuid;
        this._purpose = purpose;
        //this._name = name;
        this._uri = uri;
        this._horizontal_align = horizontal_align;
        this._vertical_align = vertical_align;
    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting ID
    public void setID(int id){
        this._id = id;
    }

    // getting screen uuid
    public String getScreenUuid(){
        return this._screen_uuid;
    }

    // setting screen uuid
    public void setScreenUuid(String screen_uuid) {
        this._screen_uuid = screen_uuid;
    }

    // getting Purpose
    public String getPurpose(){
        return this._purpose;
    }

    // setting Purpose
    public void setPurpose(String purpose){
        this._purpose = purpose;
    }

    // getting URI
    public String getUri(){
        return this._uri;
    }

    // setting URI
    public void setUri(String uri){
        this._uri = uri;
    }

/*    // getting name/label of image
    public String getName(){
        return this._name;
    }

    // setting name/label of image
    public void setName(String name){
        this._name = name;
    }*/

    // getting justification
    public Integer getHorizontalAlign(){
        return this._horizontal_align;
    }

    // setting Justification
    public void setHorizontalAlign(Integer horizontal_align){ this._horizontal_align = horizontal_align; }

    // getting Vertical Alignment
    public Integer getVerticalAlign(){
        return this._vertical_align;
    }

    // setting Vertical Alignment
    public void setVerticalAlign(Integer vertical_align){
        this._vertical_align = vertical_align;
    }

/*
    // for interface
    // getting text
    public String getText(){
        return this._text;
    }

    // setting text
    public void setText(String text){
        this._text = text;
    }
*/
}