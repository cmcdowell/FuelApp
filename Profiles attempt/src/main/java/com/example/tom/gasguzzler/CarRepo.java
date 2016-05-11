package com.example.tom.gasguzzler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.HashMap;

public class CarRepo {
    private DBHelper dbHelper;

    public CarRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public int insert(Car car) {

        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Car.KEY_mpg, car.mpg);
        values.put(Car.KEY_name, car.name);

        // Inserting Row
        long car_Id = db.insert(Car.TABLE, null, values);
        db.close(); // Closing database connection
        return (int) car_Id;
    }

    public void delete(int car_Id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Using param ? instead of concatenation
        db.delete(Car.TABLE, Car.KEY_ID + "= ?", new String[] { String.valueOf(car_Id) });
        db.close(); // Closing database connection
    }

    public void update(Car car) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Car.KEY_mpg,car.mpg);
        values.put(Car.KEY_name, car.name);

        // It's a good practice to use parameter ?, instead of concatenate string
        db.update(Car.TABLE, values, Car.KEY_ID + "= ?", new String[] { String.valueOf(car.car_ID) });
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>>  getCarList() {
        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Car.KEY_ID + "," +
                Car.KEY_name + "," +
                Car.KEY_mpg +
                " FROM " + Car.TABLE;

        ArrayList<HashMap<String, String>> carList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> car = new HashMap<String, String>();
                car.put("id", cursor.getString(cursor.getColumnIndex(Car.KEY_ID)));
                car.put("name", cursor.getString(cursor.getColumnIndex(Car.KEY_name)));
                carList.add(car);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return carList;

    }

    public Car getCarById(int Id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                Car.KEY_ID + "," +
                Car.KEY_name + "," +
                Car.KEY_mpg+
                " FROM " + Car.TABLE
                + " WHERE " +
                Car.KEY_ID + "=?";//Using param ? instead of concatenation

        int iCount =0;
        Car car = new Car();

        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(Id) } );

        if (cursor.moveToFirst()) {
            do {
                car.car_ID =cursor.getInt(cursor.getColumnIndex(Car.KEY_ID));
                car.name =cursor.getString(cursor.getColumnIndex(Car.KEY_name));
                car.mpg  =cursor.getString(cursor.getColumnIndex(Car.KEY_mpg));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return car;
    }

}