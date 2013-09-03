package io.github.mabdrabo.schoolassistant;

/**
 * Created by mahmoud on 9/3/13.
 */
public class Assignment {

    private int id;
    private int _courseId;
    private String _description;
    private long _dueDate;
    private String _notes;


    public Assignment(String _description) {
        this._description = _description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
