package io.github.mabdrabo.schoolassistant;

/**
 * Created by mahmoud on 9/3/13.
 */
public class Quiz {

    private int _id;
    private int _courseId;
    private String _place;
    private long _date;


    public Quiz(String _place) {
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

    public long get_date() {
        return _date;
    }

    public void set_date(long _date) {
        this._date = _date;
    }
}
