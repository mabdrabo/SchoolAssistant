package io.github.mabdrabo.schoolassistant.objects;

/**
 * Created by mahmoud on 9/3/13.
 */
public class Assignment {

    private int _id;
    private int _courseId;
    private String _description;
    private long _dueDate;
    private String _notes;


    public Assignment(String _description) {
        this._description = _description;
        this._notes = "";
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int id) {
        this._id = id;
    }

    public int get_courseId() {
        return _courseId;
    }

    public void set_courseId(int _courseId) {
        this._courseId = _courseId;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public long get_dueDate() {
        return _dueDate;
    }

    public void set_dueDate(long _dueDate) {
        this._dueDate = _dueDate;
    }

    public String get_notes() {
        return _notes;
    }

    public void set_notes(String _notes) {
        this._notes = _notes;
    }
}
