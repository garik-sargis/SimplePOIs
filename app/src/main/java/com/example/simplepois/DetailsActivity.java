package com.example.simplepois;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.simplepois.data.model.PoiDetails;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Loader ID
    public static final int LOADER_POI_DETAILS = 1;

    public static void start(Context context, Uri poiDetailsUri) {
        Intent intent = new Intent(context, DetailsActivity.class)
                .setData(poiDetailsUri);
        context.startActivity(intent);
    }

    // URI of the currently displayed POI details
    private Uri poiDetailsUri;

    @Bind(R.id.text_title) TextView vTitleText;
    @Bind(R.id.text_address) TextView vAddressText;
    @Bind(R.id.text_transport) TextView vTransportText;
    @Bind(R.id.text_email) TextView vEmailText;
    @Bind(R.id.text_phone) TextView vPhoneText;
    @Bind(R.id.text_description) TextView vDescriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        // Initialize the view references
        ButterKnife.bind(this);

        // Extract the URI from the starting intent
        poiDetailsUri = getIntent().getData();

        getLoaderManager().initLoader(LOADER_POI_DETAILS, null, this);
    }

    private void bindData(PoiDetails poiDetails) {
        vTitleText.setText(poiDetails.title);
        vAddressText.setText(poiDetails.address);
        vTransportText.setText(poiDetails.transport);
        vEmailText.setText(poiDetails.email);
        vPhoneText.setText(poiDetails.phone);
        vDescriptionText.setText(poiDetails.description);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader;

        switch (id) {
            case LOADER_POI_DETAILS:
                loader = new CursorLoader(this, poiDetailsUri,
                        null,
                        null,
                        null,
                        null);
                break;
            default:
                throw new IllegalArgumentException("Invalid loader ID: " + id);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            PoiDetails poiDetails = PoiDetails.fromCursor(data);
            bindData(poiDetails);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Do nothing
    }
}
