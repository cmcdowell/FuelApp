package com.work.group.myapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by vibs on 13/05/16.
 */
public class ProfileProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.work.group.myapplication.provider.Cars";
    static final String URL = "content://" + PROVIDER_NAME + "/profiles";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String NAME = "model";
    static final String EFFICIENCY = "mpg";

    private static HashMap<String, String> PROFILES_PROJECTION_MAP;

    static final int PROFILES = 1;
    static final int PROFILES_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "profiles", PROFILES);
        uriMatcher.addURI(PROVIDER_NAME, "profiles/#", PROFILES_ID);
    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Cars";
    static final String PROFILES_TABLE_NAME = "profiles";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + PROFILES_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " model TEXT NOT NULL, " +
                    " mpg REAL NOT NULL);";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + PROFILES_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new profile
         */
        long rowID = db.insert(	PROFILES_TABLE_NAME, "", values);

        /**
         * If profile is added successfully
         */

        if (rowID > 0)
        {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(PROFILES_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case PROFILES:
                qb.setProjectionMap(PROFILES_PROJECTION_MAP);
                break;

            case PROFILES_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on profile names
             */
            sortOrder = NAME;
        }
        Cursor c = qb.query(db,	projection,	selection, selectionArgs,null, null, sortOrder);

        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case PROFILES:
                count = db.delete(PROFILES_TABLE_NAME, selection, selectionArgs);
                break;

            case PROFILES_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( PROFILES_TABLE_NAME, _ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)){
            case PROFILES:
                count = db.update(PROFILES_TABLE_NAME, values, selection, selectionArgs);
                break;

            case PROFILES_ID:
                count = db.update(PROFILES_TABLE_NAME, values, _ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all profiles
             */
            case PROFILES:
                return "vnd.android.cursor.dir/vnd.com.work.group.myapplication.provider.profiles";

            /**
             * Get a particular profile
             */
            case PROFILES_ID:
                return "vnd.android.cursor.item/vnd.com.work.group.myapplication.provider.profiles";

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

}