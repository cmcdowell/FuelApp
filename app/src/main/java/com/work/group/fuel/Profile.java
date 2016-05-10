package com.work.group.fuel;

/**
 * Created by vibs on 10/05/16.
 */
public class Profile {
    int _id;
    String _model;
    double _mpg;
    String _photoURI;

    //constructor
    public Profile(int id, String model, double mpg, String photoURI) {
        this._model = model;
        this._mpg = mpg;
        this._photoURI = photoURI;
        this._id = id;
    }

    //constructor
    public Profile(String model, double mpg, String photoURI) {
        this._model = model;
        this._mpg = mpg;
        this._photoURI = photoURI;
    }

    public String getModel() {
        return this._model;
    }

    public void setModel(String model) {
        this._model = model;
    }

    public double getMpg() {
        return this._mpg;
    }

    public void setMpg(double mpg) {
        this._mpg = mpg;
    }

    public String getPhotoURI() {
        return this._photoURI;
    }

    public int getId() {
        return this.getId();
    }

    public void setId(int id) {
        this._id = id;
    }
}
