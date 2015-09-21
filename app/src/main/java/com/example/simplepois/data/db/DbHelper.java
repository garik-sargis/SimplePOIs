package com.example.simplepois.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.simplepois.data.PoisContract.PoiDetailsEntry;
import com.example.simplepois.data.PoisContract.PoiInfoEntry;

public class DbHelper extends SQLiteOpenHelper {

    /**
     * Database name
     */
    private static final String DB_NAME = "pois.db";
    /**
     * Current version number of the database
     */
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create 2 tables
        db.execSQL(PoiInfoEntry.SQL_CREATE_TABLE);
        db.execSQL(PoiDetailsEntry.SQL_CREATE_TABLE);
    }

    /**
     * There is no upgrade policy. Just clear the old tables.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PoiInfoEntry.SQL_DROP_TABLE);
        db.execSQL(PoiDetailsEntry.SQL_DROP_TABLE);
    }
}
