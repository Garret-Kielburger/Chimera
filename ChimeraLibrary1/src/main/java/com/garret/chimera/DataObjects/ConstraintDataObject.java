package com.garret.chimera.DataObjects;

/**
 * Created by Captain on 02/09/2017.
 * <p>
 * <p>
 * Copyright Greenr Republic Software Company - All Rights Reserved.
 */

public class ConstraintDataObject {

    String _screen_uuid;
    String _start_id;
    String _start_side;
    String _end_id;
    String _end_side;
    int margin;

    public ConstraintDataObject() {
    }

    public ConstraintDataObject(String _screen_uuid, String _start_id, String _start_side, String _end_id, String _end_side, int margin) {
        this._screen_uuid = _screen_uuid;
        this._start_id = _start_id;
        this._start_side = _start_side;
        this._end_id = _end_id;
        this._end_side = _end_side;
        this.margin = margin;
    }

    public String getScreenUuid() {
        return _screen_uuid;
    }

    public void setScreenUuid(String _screen_uuid) {
        this._screen_uuid = _screen_uuid;
    }

    public String getStartId() {
        return _start_id;
    }

    public void setStartId(String _start_id) {
        this._start_id = _start_id;
    }

    public String getStartSide() {
        return _start_side;
    }

    public void setStartSide(String _start_side) {
        this._start_side = _start_side;
    }

    public String getEndId() {
        return _end_id;
    }

    public void setEndId(String _end_id) {
        this._end_id = _end_id;
    }

    public String getEndSide() {
        return _end_side;
    }

    public void setEndSide(String _end_side) {
        this._end_side = _end_side;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }
}
