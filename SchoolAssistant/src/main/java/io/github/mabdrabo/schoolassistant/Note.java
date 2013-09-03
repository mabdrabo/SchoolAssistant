package io.github.mabdrabo.schoolassistant;

/**
 * Created by mahmoud on 9/3/13.
 */
public class Note {

    private int _id;
    private int _courseId;
    private String _content;
    private long _date;


    public Note(String _content) {
        this._content = _content;
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

    public String get_content() {
        return _content;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    public long get_date() {
        return _date;
    }

    public void set_date(long _date) {
        this._date = _date;
    }
}
