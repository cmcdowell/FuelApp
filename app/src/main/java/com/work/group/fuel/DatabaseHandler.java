package com.work.group.fuel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vibs on 10/05/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "ProfileManager";

    // Contacts table name
    private static final String TABLE_PROFILES = "Profiles";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_MODEL = "model";
    private static final String KEY_MPG = "mpg";
    private static final String KEY_PHOTO = "photo";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MODEL + " TEXT,"
                + KEY_MPG + " INTEGER," + KEY_PHOTO + " TEXT" + ")";
        db.execSQL(CREATE_PROFILE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILES);

        // Create tables again
        onCreate(db);
    }

    // Adding new profile
    void addProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MODEL, profile.getModel());
        values.put(KEY_MPG, profile.getMpg());
        values.put(KEY_PHOTO, profile.getPhotoURI());

        // Inserting Row
        db.insert(TABLE_PROFILES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single profile
    Profile getProfile(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILES, new String[] { KEY_ID,
                        KEY_MODEL, KEY_MPG }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Profile profile = new Profile(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Double.parseDouble(cursor.getString(2)), cursor.getString(3));
        // return profile
        return profile;
    }

    // Deleting single profile
    public void deleteProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROFILES, KEY_ID + " = ?",
                new String[] { String.valueOf(profile.getId()) });
        db.close();
    }
}