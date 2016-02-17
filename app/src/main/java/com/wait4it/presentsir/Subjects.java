package com.wait4it.presentsir;

/**
 * Created by Rahul Yadav on 9/23/2015.
 */
public class Subjects {

    private int _id;
    private String _name;
    private String _start;
    private String _end;
    private String _location;

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_start(String _start) {
        this._start = _start;
    }

    public void set_end(String _end) {
        this._end = _end;
    }

    public void set_location(String _location) {
        this._location = _location;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_start() {
        return _start;
    }

    public String get_end() {
        return _end;
    }

    public String get_location() {
        return _location;
    }
}

