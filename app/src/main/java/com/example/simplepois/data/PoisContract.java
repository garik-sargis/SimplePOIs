package com.example.simplepois.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Content provider contract class. Also serves as a SQLite database contract class.
 * Each nested class describes a separate database/provider table.
 * All tables use "_id" as an integer primary key.
 */
public abstract class PoisContract {

    public static final String AUTHORITY = "com.example.simplepois.provider";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_POI_INFO = PoiInfoEntry.TABLE_NAME;
    public static final String PATH_POI_DETAILS = PoiDetailsEntry.TABLE_NAME;

    public static abstract class PoiInfoEntry {
        // Table Name
        public static final String TABLE_NAME = "poiInfo";
        // Column Names
        public static final String COL_ID = BaseColumns._ID;
        public static final String COL_REMOTE_ID = "remoteId";
        public static final String COL_TITLE = "title";
        public static final String COL_GEOCOORDINATES = "geoCoordinates";
        // SQL Statements
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_REMOTE_ID + " INTEGER  NOT NULL, " +
                        COL_TITLE + " TEXT NOT NULL, " +
                        COL_GEOCOORDINATES + " TEXT NOT NULL" +
                        ");";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        // URIs
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_POI_INFO)
                .build();

        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                AUTHORITY + "/" + PATH_POI_INFO;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                AUTHORITY + "/" + PATH_POI_INFO;

        public static Uri uriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static abstract class PoiDetailsEntry {
        // Table Name
        public static final String TABLE_NAME = "poiDetails";
        // Column Names
        public static final String COL_ID = BaseColumns._ID;
        public static final String COL_REMOTE_ID = "remoteId";
        public static final String COL_TITLE = "title";
        public static final String COL_ADDRESS = "address";
        public static final String COL_TRANSPORT = "transport";
        public static final String COL_EMAIL = "email";
        public static final String COL_GEOCOORDINATES = "geoCoordinates";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_PHONE = "phone";
        // SQL Statements
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_REMOTE_ID + " INTEGER NOT NULL, " +
                        COL_TITLE + " TEXT NOT NULL, " +
                        COL_ADDRESS + " TEXT NOT NULL, " +
                        COL_TRANSPORT + " TEXT NOT NULL, " +
                        COL_EMAIL + " TEXT NOT NULL, " +
                        COL_GEOCOORDINATES + " TEXT NOT NULL, " +
                        COL_DESCRIPTION + " TEXT NOT NULL, " +
                        COL_PHONE + " TEXT NOT NULL" +
                        ");";
        // URIs
        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH_POI_DETAILS)
                .build();

        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                AUTHORITY + "/" + PATH_POI_DETAILS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                AUTHORITY + "/" + PATH_POI_DETAILS;

        public static Uri uriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }


}

//{
//    "id":"2",
//    "title":"Fundació Antoni Tàpies",
//    "address":"C/ ARAGÓ, 255, 08007 Barcelona",
//    "transport":"Underground:Passeig de Gràcia -L3",
//    "email":"http://www.fundaciotapies.org/",
//    "geocoordinates":"41.39154,2.163835",
//    "description":"The Fundació opened its doors in June 1990 in the building of the former Editorial Montaner i Simon publishing house, the work of the Modernist architect Lluís Domènech i MontanerRestored and refurbished by the architects Roser Amadó and Lluís Domènech Girbau. Constructed between 1880 and 1885, at an early stage of the evolution of Catalan Modernism, the building was the first in the Eixample district to integrate industrial typology and technology, combining exposed brick and iron, into the fabric of the city centre. ",
//    "phone":"undefined"
// }
