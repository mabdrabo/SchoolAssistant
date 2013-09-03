package io.github.mabdrabo.schoolassistant.objects;

import java.util.ArrayList;

/**
 * Created by mahmoud on 9/3/13.
 */
public class Course {

    private int _id;
    private String _name;


    public Course(String name) {
        this._name = name;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

}
