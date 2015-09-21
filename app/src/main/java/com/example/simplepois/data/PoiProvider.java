package com.example.simplepois.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.simplepois.data.PoisContract.PoiDetailsEntry;
import com.example.simplepois.data.PoisContract.PoiInfoEntry;
import com.example.simplepois.data.api.PoiRetrofitService;
import com.example.simplepois.data.db.DbHelper;
import com.example.simplepois.data.model.PoiDetails;
import com.example.simplepois.data.model.PoiInfo;
import com.example.simplepois.data.model.PoiInfoList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Converter;
import retrofit.JacksonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class PoiProvider extends ContentProvider {

    public static final String LOG_TAG = PoiProvider.class.getSimpleName();

    // URI Types
    private static final int TYPE_POI_INFO_DIR = 0;
    private static final int TYPE_POI_DETAILS_ITEM = 1;

    private UriMatcher uriMatcher;

    private DbHelper dbHelper;

    private PoiRetrofitService retrofitService;

    private UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = PoisContract.AUTHORITY;

        matcher.addURI(authority, PoisContract.PATH_POI_INFO, TYPE_POI_INFO_DIR);
        matcher.addURI(authority, PoisContract.PATH_POI_DETAILS + "/#", TYPE_POI_DETAILS_ITEM);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        uriMatcher = buildUriMatcher();

        dbHelper = new DbHelper(getContext());

        retrofitService = buildRetrofitService();

        return true;
    }

    private PoiRetrofitService buildRetrofitService() {
        // Jackson object mapper construction
        final ObjectMapper mapper = new ObjectMapper()
                .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);

        // Retrofit JSON converter factory construction
        Converter.Factory converterFactory = JacksonConverterFactory.create(mapper);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://t21services.herokuapp.com")
                .addConverterFactory(converterFactory)
                .build();

        return retrofit.create(PoiRetrofitService.class);
    }

    @Override
    public String getType(Uri uri) {
        final int typeId = uriMatcher.match(uri);

        switch (typeId) {
            case TYPE_POI_INFO_DIR:
                return PoiInfoEntry.CONTENT_DIR_TYPE;
            case TYPE_POI_DETAILS_ITEM:
                return PoiDetailsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unsupported URI type: " + uri);
        }
    }

    // TODO: Refactor
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        int typeId = uriMatcher.match(uri);

        switch (typeId) {
            case TYPE_POI_INFO_DIR:
                cursor = queryPoiInfo(projection, selection, selectionArgs, sortOrder);
                break;
            case TYPE_POI_DETAILS_ITEM:
                long remoteId = ContentUris.parseId(uri);
                cursor = queryPoiDetails(remoteId, projection);
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor queryPoiInfo(String[] projection, String selection,
                                String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Try to load from the server
        if (!poiInfoListLoaded()) {
            Log.d(LOG_TAG, "Poi info not loaded: loading from the server");

            Call<PoiInfoList> call = retrofitService.poiInfoList();

            try {
                Response<PoiInfoList> response = call.execute();
                if (response.isSuccess()) {
                    List<PoiInfo> poiInfoList = response.body().list;
                    db.beginTransaction();
                    for (PoiInfo poiInfo : poiInfoList) {
                        ContentValues values = PoiInfo.toValues(poiInfo);
                        db.insert(PoiInfoEntry.TABLE_NAME, null, values);
                    }
                    db.setTransactionSuccessful();
                    db.endTransaction();
                }

            } catch (IOException e) {
                // TODO: Handle exception
                e.printStackTrace();
            }
        } else {
            Log.d(LOG_TAG, "Poi info already loaded");
        }

        return db.query(PoiInfoEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    private Cursor queryPoiDetails(long remoteId, String[] projection) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Try to load from the server
        if (!poiDetailsLoaded(remoteId)) {
            Log.d(LOG_TAG, "Poi details not loaded: loading from the server");

            Call<PoiDetails> call = retrofitService.poiDetails(remoteId);

            try {
                Response<PoiDetails> response = call.execute();
                if (response.isSuccess()) {
                    PoiDetails poiDetails = response.body();

                    ContentValues values = PoiDetails.toValues(poiDetails);
                    db.insert(PoiDetailsEntry.TABLE_NAME, null, values);
                }

            } catch (IOException e) {
                // TODO: Handle exception
                e.printStackTrace();
            }
        } else {
            Log.d(LOG_TAG, "Poi details already loaded");
        }

        String selection = PoiDetailsEntry.COL_REMOTE_ID + " = ?";
        String[] selectionArgs = {Long.toString(remoteId)};

        return db.query(PoiDetailsEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
    }

    private boolean poiInfoListLoaded() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        long numEntries = DatabaseUtils.queryNumEntries(db, PoiInfoEntry.TABLE_NAME);

        return numEntries > 0;
    }

    private boolean poiDetailsLoaded(long remoteId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = PoiDetailsEntry.COL_REMOTE_ID + " = ?";
        String[] selectionArgs = {Long.toString(remoteId)};
        long numEntries = DatabaseUtils.queryNumEntries(db, PoiDetailsEntry.TABLE_NAME,
                selection, selectionArgs);

        return numEntries > 0;
    }

    /**
     * Not supported
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Not supported
     */

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Not supported
     */

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
