package io.github.mabdrabo.schoolassistant;

import java.util.ArrayList;

/**
 * Created by mahmoud on 9/3/13.
 */
public class Course {

    private int _id;
    private String _name;
    private ArrayList<Section> _sections;
    private ArrayList<Note> _notes;
    private ArrayList<Assignment> _assignments;
    private ArrayList<Quiz> _quizes;
    private ArrayList<Project> _projects;


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

    public ArrayList<Section> get_sections() {
        return _sections;
    }

    public void set_sections(ArrayList<Section> _sections) {
        this._sections = _sections;
    }

    public ArrayList<Assignment> get_assignments() {
        return _assignments;
    }

    public void set_assignments(ArrayList<Assignment> _assignments) {
        this._assignments = _assignments;
    }

    public ArrayList<Quiz> get_quizes() {
        return _quizes;
    }

    public void set_quizes(ArrayList<Quiz> _quizes) {
        this._quizes = _quizes;
    }

    public ArrayList<Project> get_projects() {
        return _projects;
    }

    public void set_projects(ArrayList<Project> _projects) {
        this._projects = _projects;
    }

    public ArrayList<Note> get_notes() {
        return _notes;
    }

    public void set_notes(ArrayList<Note> _notes) {
        this._notes = _notes;
    }
}
