package com.example.simplepois;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.simplepois.data.PoisContract.PoiDetailsEntry;
import com.example.simplepois.data.PoisContract.PoiInfoEntry;
import com.example.simplepois.data.model.PoiInfo;
import com.example.simplepois.data.model.WithId;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {

    // Loader ID
    public static final int LOADER_POI_INFO_LIST = 1;

    public static final String KEY_FILTER_STRING = "filterString";

    @Bind(R.id.list_pois) RecyclerView vPoiList;

    public PoiTitlesAdapter poiTitlesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize the view references
        ButterKnife.bind(this);

        // Initialize the adapter
        poiTitlesAdapter = new PoiTitlesAdapter();
        poiTitlesAdapter.setClickListener(
                new PoiTitlesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClicked(WithId<PoiInfo> data) {
                        Uri uri = PoiDetailsEntry.uriWithId(data.data.remoteId);
                        DetailsActivity.start(HomeActivity.this, uri);
                    }
                });

        // Initialize the recycler view
        LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        vPoiList.setLayoutManager(layoutManager);
        vPoiList.setHasFixedSize(true);
        vPoiList.setAdapter(poiTitlesAdapter);

        initLoader();
    }

    private void initLoader() {
        getLoaderManager().initLoader(LOADER_POI_INFO_LIST, null, this);
    }

    private void restartLoader(String filterString) {
        Bundle args = new Bundle();
        args.putString(KEY_FILTER_STRING, filterString);
        getLoaderManager().restartLoader(LOADER_POI_INFO_LIST, args, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Loader<Cursor> loader;

        switch (id) {
            case LOADER_POI_INFO_LIST:

                String selection = null;
                String[] selectionArgs = null;
                String sortOrder = PoiInfoEntry.COL_TITLE + " ASC";

                if (args != null) {
                    String filterString = args.getString(KEY_FILTER_STRING);

                    selection = PoiInfoEntry.COL_TITLE + " LIKE ?";
                    selectionArgs = new String[]{"%" + filterString + "%"};
                }

                loader = new CursorLoader(this, PoiInfoEntry.CONTENT_URI,
                        null,
                        selection,
                        selectionArgs,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Invalid loader ID: " + id);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        DatabaseUtils.dumpCursor(data);
        List<WithId<PoiInfo>> poiInfoList = PoiInfo.listFromCursor(data);
        data.close();
        poiTitlesAdapter.swapList(poiInfoList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        poiTitlesAdapter.swapList(Collections.<WithId<PoiInfo>>emptyList());
    }

    // TODO: Don't filter on every keystroke (maybe use RxJava)
    @Override
    public boolean onQueryTextChange(String newText) {
        restartLoader(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }
}
