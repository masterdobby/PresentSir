package com.wait4it.presentsir;

/**
 * Created by Rahul Yadav on 9/23/2015.
 */
public class Attendance {

    private int _id;
    private String _name;
    private double _result;
    private int _attendedClasses;
    private int _totalClasses;

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_result(double _result) {
        this._result = _result;
    }

    public void set_attendedClasses(int _attendedClasses) {
        this._attendedClasses = _attendedClasses;
    }

    public void set_totalClasses(int _totalClasses) {
        this._totalClasses = _totalClasses;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public double get_result() {
        return _result;
    }

    public int get_attendedClasses() {
        return _attendedClasses;
    }

    public int get_totalClasses() {
        return _totalClasses;
    }
}
