package com.garret.chimera.DataObjects;

/**
 * Created by Captain on 13/08/2015.
 * <p/>
 * <p/>
 * Copyright Garret Kielburger - All Rights Reserved.
 */

import java.util.ArrayList;

public class ScreenDataObject implements IDataObject {
    // private variables
    public int _id;
    public String _name;
    public String _uuid;
    public int _order;
    public Integer _vertical_align;

    public ArrayList<ImageDataObject> _imageList;
    public ArrayList<TextfieldDataObject> _textfieldList;
    public ArrayList<IDataObject> _elementList;


    public ScreenDataObject() {
        // TODO Auto-generated constructor stub
    }

    //constructor
    public ScreenDataObject(int id, String name, String uuid, int order, ArrayList<TextfieldDataObject> textfieldList, ArrayList<ImageDataObject> imageList, ArrayList<IDataObject> elementList){
        this._id = id;
        this._name = name;
        this._uuid = uuid;
        this._order = order;
        this._imageList = imageList;
        this._textfieldList = textfieldList;
        this._elementList = elementList;
    }

    public ScreenDataObject(int id, String name, String uuid, int order, ArrayList<IDataObject> elementList){
        this._id = id;
        this._name = name;
        this._uuid = uuid;
        this._order = order;
        this._elementList = elementList;
    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting ID
    public void setID(int id){
        this._id = id;
    }

    // getting name/label of page
    public String getName(){
        return this._name;
    }

    // setting name/label of page
    public void setName(String name){
        this._name = name;
    }

    // getting uuid
    public String getUuid() {return this._uuid;}

    // setting uuid
    public void setUuid(String uuid) { this._uuid = uuid;}

    // getting order
    public int getOrder() {
        return _order;
    }

    // setting order
    public void setOrder(int _order) {
        this._order = _order;
    }

    // getting list of textfields on page
    public ArrayList<TextfieldDataObject> getTextfieldList(){
        return this._textfieldList;
    }

    // setting list of textfields on page
    public void setTextfieldList(ArrayList<TextfieldDataObject> textfieldList){
        this._textfieldList = textfieldList;
    }

    // getting list of images on page
    public ArrayList<ImageDataObject> getImageList(){
        return this._imageList;
    }

    // setting list of images on page
    public void setImageList(ArrayList<ImageDataObject> imageList){
        this._imageList = imageList;
    }

    // getting list of all images/texts via interface ArrayList
    public ArrayList<IDataObject> getElementList() {
        return this._elementList;
    }

    // setting list of all images/texts via interface ArrayList
    public void setElementList(ArrayList<IDataObject> elementList) {
        this._elementList = elementList;
    }


    // for IDataObject contract (needed by images and texts) --> might be useful in future for multiple fragments on one screen
    // getting Vertical Alignment
    public Integer getVerticalAlign(){
        return this._vertical_align;
    }

    // setting Vertical Alignment
    public void setVerticalAlign(Integer vertical_align){
        this._vertical_align = vertical_align;
    }




    //TODO: Find way to use relative numbers to assign more accurate placement of objects on each screen.

}