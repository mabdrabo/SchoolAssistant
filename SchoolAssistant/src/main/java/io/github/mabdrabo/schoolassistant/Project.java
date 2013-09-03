package io.github.mabdrabo.schoolassistant;

/**
 * Created by mahmoud on 9/3/13.
 */
public class Project {

    private int _id;
    private int _courseId;
    private String _title;
    private String _notes;
    private long _dueDate;


    public Project(String _title) {
        this._title = _title;
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

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_notes() {
        return _notes;
    }

    public void set_notes(String _notes) {
        this._notes = _notes;
    }

    public long get_dueDate() {
        return _dueDate;
    }

    public void set_dueDate(long _dueDate) {
        this._dueDate = _dueDate;
    }
}
