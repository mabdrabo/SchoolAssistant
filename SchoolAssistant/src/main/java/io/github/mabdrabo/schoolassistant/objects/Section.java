package io.github.mabdrabo.schoolassistant.objects;

import io.github.mabdrabo.schoolassistant.activities.MainActivity;

/**
 * Created by mahmoud on 9/3/13.
 */
public class Section {

    private int _id;
    private int _courseId;
    private String _place;
    private int _day;
    private int _time;


    public Section(String _place) {
        this._place = _place;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_courseId() {
        return _courseId;
    }

    public void set_courseId(int _courseId) {
        this._courseId = _courseId;
    }

    public String get_place() {
        return _place;
    }

    public void set_place(String _place) {
        this._place = _place;
    }

    public int get_day() {
        return _day;
    }

    public String get_day_long() {
        return MainActivity.DAY_OF_WEEK_LONG[this._day];
    }

    public String get_day_short() {
        return MainActivity.DAY_OF_WEEK_SHORT[this._day];
    }

    public void set_day(int _day) {
        this._day = _day;
    }

    public int get_time() {
        return _time;
    }

    public void set_time(int _time) {
        this._time = _time;
    }

}
